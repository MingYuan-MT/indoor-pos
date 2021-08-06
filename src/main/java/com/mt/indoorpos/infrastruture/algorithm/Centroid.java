package com.mt.indoorpos.infrastruture.algorithm;


import com.mt.indoorpos.infrastruture.entity.*;
import com.mt.indoorpos.service.Server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
  * @description: 三角形加权质心定位算法
  * @author: ChenYihao
  * @date: 2021-08-04
  * @vision: 1.0
  */
public class Centroid implements Dealer{
	
	/*所有组合的总权值*/
	private double totalWeight;
	
	/*定位结果*/
	private Location location;
	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	
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
		
		String maxRssiBaseId = uniqueBases.get(0).getId();

		/*求组合数*/
		Integer[] a = doGrouper.getA();
		CombineAlgorithm ca = null;
		
		try {
			ca = new CombineAlgorithm(a,3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Object[][] c = ca.getResult();
		
		double[] tempLocation = new double[2];
		
		for(int i = 0; i<c.length; i++){
			
			/*创建一个列表，用来对每个组合进行计算*/
			List<BleBase> triBases = new ArrayList<BleBase>();
			
			for(int j = 0; j<3; j++){
				BleBase bb = uniqueBases.get((int) c[i][j]);
				triBases.add(bb);
			}
			
			/*三个基站为一组通过距离加权后求出的坐标*/
			double[] weightLocation = calculate(triBases, envFactors);
			
			tempLocation[0]+=weightLocation[0];
			tempLocation[1]+=weightLocation[1];
		}
		
		location.setXAxis(tempLocation[0]/totalWeight);
		location.setXAxis(tempLocation[1]/totalWeight);
		
		/*设置定位结果的时间戳*/
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		location.setTimeStamp(ts);
		
		return location;
	}
	
	/**
	 * 求出通过该组基站距离加权后的坐标
	 * 
	 * @param  bases 接收到的一组基站对象列表(此处列表中的基站应当是id各异的)
	 * @return  返回通过该组基站距离加权后的坐标
	 */
	public double[] calculate(List<BleBase> bases, EnvFactors envFactors){
		
		/*基站的id与坐标*/
		final Map<String, double[]> basesLocation =new HashMap<String, double[]>();
		
		/*距离数组*/
		double[] distanceArray = new double[3];
		
		String[] ids = new String[3];
		
		double[] rawLocation;
		
		double[] loc;
		
		int j = 0;
		
		/*获得基站id*/
		for (BleBase base : bases) {
			ids[j] = base.getId();
			distanceArray[j] = base.getDistance(envFactors.getHeight(), envFactors.getN(), envFactors.getP0());
			j++;
		}
		
		Round r[] = new Round[3];
		/*数组a初始化*/
		for(int i = 0; i < 3; i ++ ) {
			r[i] = new Round(basesLocation.get(ids[i])[0], basesLocation.get(ids[i])[1], distanceArray[i]);
		}
		
		Coordinate rawCoor = triCentroid(r[0],r[1],r[2]);
		
		/*给未加权的结果数组赋值*/
		rawLocation = new double[]{rawCoor.getXAxis(),rawCoor.getYAxis()};

		/*对应的权值*/
		double weight = 0;

		for(int i = 0; i<3; i++){
			weight+=(1.0/distanceArray[i]);
		}

		totalWeight+=weight;

		/*实例化坐标数组*/
		loc = new double[2];

		/*计算加权过后的坐标*/
		for(int i = 0; i < 2; i++) {
			loc[i] = rawLocation[i]*weight;
		}

		return loc;
	}

	/**
	 * 求三角形质心算法
	 *
	 * @param  r1 Round对象
	 * @param  r2
	 * @param  r3
	 * @return  Coordinate对象
	 */
	public static Coordinate triCentroid(Round r1, Round r2, Round r3) {

		/*有效交叉点1*/
		Coordinate p1 = null;
		/*有效交叉点2*/
		Coordinate p2 = null;
		/*有效交叉点3*/
		Coordinate p3 = null;

		/*三点质心坐标*/
		Coordinate centroid = new Coordinate();

		/*r1,r2交点*/
		List<Coordinate> intersections1 = intersection(r1.getX(), r1.getY(), r1.getR(),
											r2.getX(), r2.getY(), r2.getR());

		if (intersections1 != null && !intersections1.isEmpty()) {
			for (Coordinate intersection :intersections1) {
				if (p1==null&&Math.pow(intersection.getXAxis()-r3.getX(),2)
						+ Math.pow(intersection.getYAxis()-r3.getY(),2) <= Math.pow(r3.getR(),2)) {
					p1 = intersection;
				}else if(p1!=null){
					if(Math.pow(intersection.getXAxis()-r3.getX(),2) + Math.pow(intersection.getYAxis()
							-r3.getY(),2)<= Math.pow(r3.getR(),2)){
						if(Math.sqrt(Math.pow(intersection.getXAxis()-r3.getX(),2)
								+ Math.pow(intersection.getYAxis()-r3.getY(),2))>Math.sqrt(Math.pow(p1.getXAxis()
										-r3.getX(),2) + Math.pow(p1.getYAxis()-r3.getY(),2))){
							p1 = intersection;
						}
					}
				}
			}
		} else {//没有交点定位错误
			return null;
		}

		/*r1,r3交点*/
		List<Coordinate> intersections2 = intersection(r1.getX(), r1.getY(), r1.getR(),
											r3.getX(), r3.getY(), r3.getR());

		if (intersections2 != null && !intersections2.isEmpty()) {
			for (Coordinate intersection : intersections2) {//有交点
				if (p2==null&&Math.pow(intersection.getXAxis()-r2.getX(),2)
						+ Math.pow(intersection.getYAxis()-r2.getY(),2) <= Math.pow(r2.getR(),2)) {
					p2 = intersection;

				}else if(p2!=null){
					if(Math.pow(intersection.getXAxis()-r2.getX(),2) + Math.pow(intersection.getYAxis()
							-r2.getY(),2) <= Math.pow(r2.getR(),2)){
						if(Math.pow(intersection.getXAxis()-r2.getX(),2) + Math.pow(intersection.getYAxis()
								-r2.getY(),2)>Math.sqrt(Math.pow(p2.getXAxis()-r2.getX(),2)
										+ Math.pow(p2.getYAxis()-r2.getY(),2))){
							p1 = intersection;
						}
					}
				}
			}
		} else {//没有交点定位错误
			return null;
		}

		/*r1,r2交点*/
		List<Coordinate> intersections3 = intersection(r2.getX(), r2.getY(), r2.getR(),
											r3.getX(), r3.getY(), r3.getR());

		if (intersections3 != null && !intersections3.isEmpty()) {
			for (Coordinate intersection : intersections3) {//有交点
				if (Math.pow(intersection.getXAxis()-r1.getX(),2)
						+ Math.pow(intersection.getYAxis()-r1.getY(),2) <= Math.pow(r1.getR(),2)) {
					p3 = intersection;
				}else if(p3!=null){
					if(Math.pow(intersection.getXAxis()-r1.getX(),2) + Math.pow(intersection.getYAxis()
							-r1.getY(),2) <= Math.pow(r1.getR(),2)){
						if(Math.pow(intersection.getXAxis()-r1.getX(),2) + Math.pow(intersection.getYAxis()
								-r1.getY(),2)>Math.sqrt(Math.pow(p3.getXAxis()-r1.getX(),2)
										+ Math.pow(p3.getYAxis()-r1.getY(),2))){
							p3 = intersection;
						}
					}
				}
			}
		} else {//没有交点定位错误
			return null;
		}

		/*质心*/
		centroid.setXAxis((p1.getXAxis()+p2.getXAxis()+p3.getXAxis())/3);
		centroid.setYAxis((p1.getYAxis()+p2.getYAxis()+p3.getYAxis())/3);

		return centroid;
	}

	/**
	 * 求两个圆的交点
	 *
	 * @param  x1  圆心1横坐标
	 * @param  y1  圆心1纵坐标
	 * @param  r1  圆心1半径
	 * @param  x2  圆心2横坐标
	 * @param  y2  圆心2纵坐标
	 * @param  r2 圆心2半径
	 * @return 返回两个圆的交点坐标对象列表
	 */
	public static List<Coordinate> intersection(double x1, double y1, double r1,
												double x2, double y2, double r2) {

		double d = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));// 两圆心距离

		if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) < (r1 + r2)) {// 两圆相交

		}

		List<Coordinate> points  =new ArrayList<Coordinate>();//交点坐标列表

		Coordinate coor;

		if (d > r1 + r2 || d < Math.abs(r1 - r2)) {//相离或内含
			return null;
		} else if (x1 == x2 && y1 == y2) {//同心圆
			return null;
		} else if (y1 == y2 && x1 != x2) {
			double a = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2)) / (2 * x2 - 2 * x1);
			if (d == Math.abs(r1 - r2) || d == r1 + r2) {// 只有一个交点时
				coor=new Coordinate();
				coor.setXAxis(a);
				coor.setYAxis(y1);
				points.add(coor);
			} else{// 两个交点
				double t = r1 * r1 - (a - x1) * (a - x1);
				coor=new Coordinate();
				coor.setXAxis(a);
				coor.setYAxis(y1 + Math.sqrt(t));
				points.add(coor);
				coor = new Coordinate();
				coor.setXAxis(a);
				coor.setYAxis(y1 - Math.sqrt(t));
				points.add(coor);
			}
		} else if (y1 != y2) {
			double k, disp;
			k = (2 * x1 - 2 * x2) / (2 * y2 - 2 * y1);
			disp = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2) - (y1 * y1 - y2 * y2))
					/ (2 * y2 - 2 * y1);// 直线偏移量
			double a, b, c;
			a = (k * k + 1);
			b = (2 * (disp - y1) * k - 2 * x1);
			c = (disp - y1) * (disp - y1) - r1 * r1 + x1 * x1;
			double disc;
			disc = b * b - 4 * a * c;// 一元二次方程判别式
			if (d == Math.abs(r1 - r2) || d == r1 + r2) {
				coor=new Coordinate();
				coor.setXAxis((-b) / (2 * a));
				coor.setYAxis(k * coor.getXAxis() + disp);
				points.add(coor);
			} else {
				coor=new Coordinate();
				coor.setXAxis(((-b) + Math.sqrt(disc)) / (2 * a));
				coor.setYAxis(k * coor.getXAxis() + disp);
				points.add(coor);
				coor=new Coordinate();
				coor.setXAxis(((-b) - Math.sqrt(disc)) / (2 * a));
				coor.setYAxis(k * coor.getXAxis() + disp);
				points.add(coor);
			}
		}
		return points;
	}
}
