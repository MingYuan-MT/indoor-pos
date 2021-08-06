package com.mt.indoorpos.infrastruture.algorithm;


import com.mt.indoorpos.infrastruture.entity.BleBase;
import com.mt.indoorpos.infrastruture.entity.EnvFactors;
import com.mt.indoorpos.infrastruture.entity.Location;

import java.util.List;


/**
  * @description: 定位算法父接口
  * @author: ChenYihao
  * @date: 2021-08-04
  * @vision: 1.0
  */
public interface Dealer {
	/**
	 * 求定位终端坐标
	 *
	 * @param  bleBases  接收到的一组基站集
	 * @return  返回定位结果对象
	 */
	public Location getLocation(List<BleBase> bleBases, EnvFactors envFactors);
}
