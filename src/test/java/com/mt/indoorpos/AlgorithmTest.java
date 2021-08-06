package com.mt.indoorpos;


import com.mt.indoorpos.infrastruture.algorithm.Dealer;
import com.mt.indoorpos.infrastruture.algorithm.Trilateral;
import com.mt.indoorpos.infrastruture.algorithm.WeightTrilateral;
import com.mt.indoorpos.infrastruture.entity.BleBase;
import com.mt.indoorpos.infrastruture.entity.EnvFactors;
import com.mt.indoorpos.infrastruture.entity.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 定位算法测试类
 * @author: chenyh08
 * @date: 2021-08-05
 * @vision: 1.0
 */
public class AlgorithmTest {

    /**
     * 基站集合
     */
    private List<BleBase> bleBases;
    /**
     *
     * 环境因子
     */
    private EnvFactors envFactors;


    @Before
    public void init() {
        bleBases = Arrays.asList(
                new BleBase("001", 100.0,100.0, -69.0),
                new BleBase("002", 500.0,150.0, -82.0),
                new BleBase("003", 330.0,440.0, -85.0)
                );
        envFactors = new EnvFactors(2.0,3.0,4.0);

    }



    @Test
    public void weightTrilateralTest() {
        Dealer dealer = new WeightTrilateral();
        Location location = dealer.getLocation(bleBases, envFactors);
        System.out.println(location);
    }


    @Test
    public void trilateralTest() {
        Dealer dealer = new Trilateral();
        Location location = dealer.getLocation(bleBases, envFactors);
        System.out.println(location);
    }




}
