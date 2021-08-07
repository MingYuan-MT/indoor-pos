package com.mt.indoorpos.interfaces.dto;

import com.mt.indoorpos.interfaces.vo.ApInfoVo;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: chenyh08
 * @date: 2021-08-07
 * @vision: 1.0
 */
@Data
public class GetLocationDto {

    private List<ApInfoVo> apList;
}
