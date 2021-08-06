package com.mt.indoorpos.infrastruture.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * created on 2016年8月25日
 *
 * 定位结果bean
 *
 * @author  megagao
 * @version  0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/*x轴坐标*/
	private Double xAxis;

	/*y轴坐标*/
	private Double yAxis;
	
	/*时间戳*/
	private Timestamp timeStamp;

	public static Location build(Coordinate coordinate) {
		Location location = new Location();
		location.setXAxis(coordinate.getXAxis());
		location.setYAxis(coordinate.getYAxis());
		return location;
	}

}
