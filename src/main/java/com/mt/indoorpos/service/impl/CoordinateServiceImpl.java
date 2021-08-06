package com.mt.indoorpos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mt.indoorpos.infrastruture.dao.CoordinateMapper;
import com.mt.indoorpos.infrastruture.entity.Coordinate;
import com.mt.indoorpos.service.CoordinateService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: chenyh08
 * @date: 2021-08-04
 * @vision: 1.0
 */
@Service
public class CoordinateServiceImpl extends ServiceImpl<CoordinateMapper, Coordinate> implements CoordinateService {
}
