package com.mt.indoorpos.infrastruture.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
  * @description: 坐标点
  * @author: ChenYihao
  * @date: 2021-08-04
  * @vision: 1.0
  */
@Data
@TableName("coordinate")
public class Coordinate {

	/**
	 * 坐标唯一码
	 */
	private String bssid;
	/**
	 * 坐标名称
	 */
	private String name;
	/**
	 * 会议室ID
	 */
	private Integer roomId;
	/**
	 * 会议室楼层
	 */
	private Integer floor;
	/**
	 * 类型：1-会议室坐标;2-信号基站坐标
	 */
	private Integer type;
	/**
	 * X轴坐标值
	 */
	private Double xAxis;
	/**
	 * Y轴坐标值
	 */
	private Double yAxis;

}
