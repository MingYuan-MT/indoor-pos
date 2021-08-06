package com.mt.indoorpos.interfaces.controller;

import com.mt.indoorpos.infrastruture.entity.Location;
import com.mt.indoorpos.interfaces.vo.ApInfoVo;
import com.mt.indoorpos.service.PosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 定位Controler
 * @author: chenyh08
 * @date: 2021-08-04
 * @vision: 1.0
 */
@Controller
@RequestMapping("/pos")
public class PosController {
    @Autowired
    private PosService posService;

    @PostMapping(path = "/get-location")
    public Location getLocation(@NonNull @RequestParam("apList") List<ApInfoVo> apList){
        return posService.getLocation(apList);
    }

    @PostMapping(path = "/isInner")
    public Boolean isInner(@NonNull @RequestParam("apList") List<ApInfoVo> apList, @RequestParam("roomId") Integer roomId){
        return posService.isInner(apList, roomId);
    }

}
