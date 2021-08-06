package com.mt.indoorpos.infrastruture.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
  * @description: 信号基站
  * @author: ChenYihao
  * @date: 2021-08-04
  * @vision: 1.0
  */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BleBase implements Comparable<BleBase>,Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 基站id
	 * */
	private String id;
	/**
	 * X轴坐标的值
	 */
	private Double x;
	/**
	 * Y轴坐标的值
	 */
	private Double y;
	/**
	 * 接收到的信号强度
	 * */
	private Double rssi;

	public BleBase(String id, Double rssi) {
		this.id = id;
		this.rssi = rssi;
	}


	public double getDistance(double height, double n, double p0){

		/*基站到定位终端的直线距离*/
		double rawDistance;

		rawDistance =Math.pow(10, (p0-rssi.doubleValue())/(10*n));

		/*基站到定位终端的水平距离*/
		return Math.sqrt(Math.pow(rawDistance, 2)-Math.pow(height, 2));
	}

	@Override
	public int compareTo(BleBase base) {
		// TODO Auto-generated method stub
		if(rssi.compareTo(base.rssi) == 1){
			return 1;
		}else{
			return -1;
		}
	}

	public static BleBase build(Coordinate coordinate) {
		BleBase bleBase = new BleBase();
		bleBase.setId(coordinate.getBssid());
		bleBase.setX(coordinate.getXAxis());
		bleBase.setY(coordinate.getYAxis());
		return bleBase;
	}

}


