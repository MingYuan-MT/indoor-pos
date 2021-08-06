package com.mt.indoorpos.infrastruture.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mt.indoorpos.infrastruture.entity.Coordinate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: chenyh08
 * @date: 2021-08-04
 * @vision: 1.0
 */
@Repository
public interface CoordinateMapper extends BaseMapper<Coordinate> {
}
