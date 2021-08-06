package com.mt.indoorpos.infrastruture.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 环境因子
 * @author: chenyh08
 * @date: 2021-08-05
 * @vision: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvFactors {
    private Double height;
    private Double n ;
    private Double p0;


    public static EnvFactors build(String s){
        if (StringUtils.isBlank(s)) {
            throw new RuntimeException("环境因子配置错误");
        }
        String[] arr = s.split(",");
        if (arr.length <3) {
            throw new RuntimeException("环境因子配置错误");
        }

        return new EnvFactors(Double.valueOf(arr[0]), Double.valueOf(arr[1]), Double.valueOf(arr[2]));
    }
}
