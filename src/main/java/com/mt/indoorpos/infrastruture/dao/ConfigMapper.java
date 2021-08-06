package com.mt.indoorpos.infrastruture.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: chenyh08
 * @date: 2021-08-05
 * @vision: 1.0
 */
@Repository
public interface ConfigMapper {

    @Select({"select ifnull((select `value` from config where name = #{name} limit 1), '');"})
    String selectValue(@Param("name") String name);
}
