package com.mt.indoorpos.infrastruture.algorithm;

import com.mt.indoorpos.infrastruture.entity.Location;

import java.util.List;

/**
 * @description: 计算坐标点是否在矩阵内
 * @author: chenyh08
 * @date: 2021-08-05
 * @vision: 1.0
 */
public class InnerRectangle {

    public static Boolean calculate(Location location, List<Location> rectangle){
        int n = rectangle.size(); //n=4
        double x = location.getXAxis();
        double y = location.getYAxis();
        double[][] loc = new double[][]{};
        for (int i=0; i< rectangle.size(); i++) {
            Location l = rectangle.get(i);
            loc[i][0] = l.getXAxis();
            loc[i][1] = l.getYAxis();
        }
        double sum = 0;
        for(int i = 0; i < n-1; i++)
            sum += area(loc[i][0],loc[i][1],loc[i+1][0],loc[i+1][1],x,y);
        sum += area(loc[n-1][0],loc[n-1][1],loc[0][0],loc[0][1],x,y);
        double polyArea = 2 * area(loc[0][0],loc[0][1],loc[1][0],loc[1][1],loc[2][0],loc[2][1]);
        if(sum == polyArea) return true;
        else return false;
    }

    //已知坐标，求三角形面积
    private static double area(double x1, double y1, double x2, double y2, double x3, double y3) {
        return 0.5 * Math.abs((x2-x1)*(y3-y1)-(x3-x1)*(y2-y1));
    }

}
