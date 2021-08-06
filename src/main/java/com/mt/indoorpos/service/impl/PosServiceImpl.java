package com.mt.indoorpos.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mt.indoorpos.infrastruture.algorithm.Dealer;
import com.mt.indoorpos.infrastruture.algorithm.InnerRectangle;
import com.mt.indoorpos.infrastruture.algorithm.WeightTrilateral;
import com.mt.indoorpos.infrastruture.dao.ConfigMapper;
import com.mt.indoorpos.infrastruture.entity.BleBase;
import com.mt.indoorpos.infrastruture.entity.Coordinate;
import com.mt.indoorpos.infrastruture.entity.EnvFactors;
import com.mt.indoorpos.infrastruture.entity.Location;
import com.mt.indoorpos.interfaces.vo.ApInfoVo;
import com.mt.indoorpos.service.CoordinateService;
import com.mt.indoorpos.service.PosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 定位服务实现类
 * @author: chenyh08
 * @date: 2021-08-04
 * @vision: 1.0
 */
@Service
public class PosServiceImpl implements PosService {
    @Autowired
    private CoordinateService coordinateService;
    @Autowired
    private ConfigMapper configMapper;

    private Dealer dealer = new WeightTrilateral();


    @Override
    public Location getLocation(List<ApInfoVo> apList) {
        if (apList.size()<3) {
            throw new RuntimeException("有效信号基站数量少于3个，无法定位");
        }
        //获取数据库已有的信号基站
        List<String> bssids = apList.stream()
                .map(e -> e.getBssid()).collect(Collectors.toList());
        List<Coordinate> coordinates = coordinateService.list(Wrappers.<Coordinate>lambdaQuery()
                .in(Coordinate::getBssid, bssids));
        //小于三个无法定位
        if (coordinates.size()<3) {
            throw new RuntimeException("有效信号基站数量少于3个，无法定位");
        }

        //获取环境因子
        EnvFactors envFactors = EnvFactors.build(configMapper.selectValue("env_factors"));

        //转换信号源
        Map<String, Double> rssiMap = apList.stream().collect(Collectors.toMap(e -> e.getBssid(), e -> e.getRssi()));
        List<BleBase> bleBaseList = coordinates.stream().map(BleBase::build)
                .peek(e -> e.setRssi(rssiMap.get(e.getId())))
                .collect(Collectors.toList());

        //定位算法
        return dealer.getLocation(bleBaseList, envFactors);
    }

    @Override
    public Boolean isInner(List<ApInfoVo> apList, Integer roomId) {
        Location location = this.getLocation(apList);
        List<Location> rectangle = coordinateService.list(Wrappers.<Coordinate>lambdaQuery()
                    .eq(Coordinate::getRoomId, roomId)
                    .eq(Coordinate::getType, 1))
                .stream().map(Location::build)
                .collect(Collectors.toList());
        return InnerRectangle.calculate(location, rectangle);
    }


}
