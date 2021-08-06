package com.mt.indoorpos.service;

import com.mt.indoorpos.infrastruture.entity.Location;
import com.mt.indoorpos.interfaces.vo.ApInfoVo;

import java.util.List;

/**
 * @description: 定位服务
 * @author: ChenYihao
 * @date: 2021-08-04
 * @vision: 1.0
 */
public interface PosService {
    /**
     * 获取位置坐标
     * @param apList
     * @return
     */
    Location getLocation(List<ApInfoVo> apList);

    /**
     * 是都在会议室内
     * @param apList
     * @param roomId
     * @return
     */
    Boolean isInner(List<ApInfoVo> apList, Integer roomId);
}
