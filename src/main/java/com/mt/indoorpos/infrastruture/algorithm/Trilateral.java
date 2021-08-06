package com.mt.indoorpos.infrastruture.algorithm;


import Jama.Matrix;
import com.mt.indoorpos.infrastruture.entity.BleBase;
import com.mt.indoorpos.infrastruture.entity.EnvFactors;
import com.mt.indoorpos.infrastruture.entity.Location;
import com.mt.indoorpos.service.Server;

import java.util.ArrayList;
import java.util.List;


/**
  * @description: 三边定位算法
  * @author: ChenYihao
  * @date: 2021-08-04
  * @vision: 1.0
  */
public class Trilateral implements Dealer{
	
	/*定位结果*/
	private Location location;
	
	@Override
	public Location getLocation(List<BleBase> bleBases, EnvFactors envFactors){
		
		/*实例化定位结果*/
		location = new Location();
		
		/*分组*/
		DoGroup doGrouper = new DoGroup();
		List<BleBase> uniqueBases = doGrouper.doGroup(bleBases);
		
		/*如果收到的基站个数小于3，不能定位，直接返回*/
		if(uniqueBases==null){
			return null;
		}
		return calculate(uniqueBases,envFactors);
	}
	
	/**
	 * 计算定位坐标
	 * 
	 * @param  bases 接收到的一组基站对象列表(此处列表中的基站应当是id各异的)
	 * @return  返回定位坐标
	 */
	public Location calculate(List<BleBase> bases, EnvFactors envFactors){
		
		int baseNum = bases.size();
		
		/*距离数组*/
		double[] distanceArray = new double[baseNum];
		
		String[] ids = new String[baseNum];
		
		int j = 0;
		
		/*获得基站id*/
		for (BleBase base : bases) {
			ids[j] = base.getId();
			distanceArray[j] = base.getDistance(envFactors.getHeight(), envFactors.getN(), envFactors.getP0());
			j++;
		}
		
		int disArrayLength = distanceArray.length;
		
		double[][] a = new double[baseNum-1][2];
		
		double[][] b = new double[baseNum-1][1];
		
		/*数组a初始化*/
		for(int i = 0; i < 2; i ++ ) {
 			a[i][0] = 2*(Server.baseStationLocs.get(ids[i])[0]-Server.baseStationLocs.get(ids[baseNum-1])[0]);
			a[i][1] = 2*(Server.baseStationLocs.get(ids[i])[1]-Server.baseStationLocs.get(ids[baseNum-1])[1]);
		}
		
		/*数组b初始化*/
		for(int i = 0; i < 2; i ++ ) {
			b[i][0] = Math.pow(Server.baseStationLocs.get(ids[i])[0], 2) 
					- Math.pow(Server.baseStationLocs.get(ids[baseNum-1])[0], 2)
					+ Math.pow(Server.baseStationLocs.get(ids[i])[1], 2)
					- Math.pow(Server.baseStationLocs.get(ids[baseNum-1])[1], 2)
					+ Math.pow(distanceArray[disArrayLength-1], 2)
					- Math.pow(distanceArray[i],2);
		}
		
		/*将数组封装成矩阵*/
		Matrix b1 = new Matrix(b);
		Matrix a1 = new Matrix(a);
		
		/*求矩阵的转置*/
		Matrix a2  = a1.transpose();
		
		/*求矩阵a1与矩阵a1转置矩阵a2的乘积*/
		Matrix tmpMatrix1 = a2.times(a1);
		Matrix reTmpMatrix1 = tmpMatrix1.inverse();
		Matrix tmpMatrix2 = reTmpMatrix1.times(a2);
		
		/*中间结果乘以最后的b1矩阵*/
		Matrix resultMatrix = tmpMatrix2.times(b1);
		double[][] resultArray = resultMatrix.getArray();
		
		location.setXAxis(resultArray[0][0]);
		location.setYAxis(resultArray[1][0]);
		
		return location;
	}
}
