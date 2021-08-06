package com.mt.indoorpos.infrastruture.algorithm;



import com.mt.indoorpos.infrastruture.entity.BleBase;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
  * @description: 根据基站id进行分组，并分别求每个基站rssi的去除极端值的均值
  * @author: ChenYihao
  * @date: 2021-08-04
  * @vision: 1.0
  */
public class DoGroup {
	
	/*用来求组合数的数组*/
	private Integer[] a; 
	
	/**
	 * 根据传进来的基站列表，将基站进行分组，得到每个基站rssi的去除极端值的均值所组成的列表
	 *
	 * @param  bleBases  接收到的一组基站集
	 * @return  返回每个基站rssi的去除极端值的均值所组成的列表
	 */
	public List<BleBase> doGroup(List<BleBase> bleBases) {

		/*如果接收到的不同基站信号小于3个，不能定位，直接返回*/
		if(bleBases.size()<3){
			return null;
		}
		//将相同id基站汇总
		Map<String, List<BleBase>> groupedBases = new HashMap<>();
		bleBases.forEach(e ->{
			if (groupedBases.containsKey(e.getId())) {
				groupedBases.get(e.getId()).add(e);
			}
			groupedBases.put(e.getId(), Arrays.asList(e));
		});
		//处理重复ID的基站是信号
		List<BleBase> uniqueBases = dealByGroup(groupedBases);
		
		/*如果接收到的基站信号个数大于3个，那么就取RSSI值最大的3个用来定位*/
		int len = uniqueBases.size();
		if(len>3){
			Collections.sort(uniqueBases);
			return new ArrayList<BleBase>(uniqueBases.subList(len-3, len));
		}
		return uniqueBases;
	}
	
	/**
	 * 
	 * 把根据id分组后的Map进行处理，得到每个id组中去掉首尾极端值的rssi均值，若某组的rssi值个数小于4，
	 * 				  则得到其中值，最后返回List对象，其中List存放的元素为最终参与计算的rssi值构造的基站对象
	 *
	 * @param  groups  根据id分组后的Map
	 * @return  返回List对象
	 */
	public List<BleBase> dealByGroup(Map<String, List<BleBase>> groups){

		Double r;
		
		List<BleBase> bases = new ArrayList<BleBase>();
		
		/*一共收到了几个基站的值*/
		int baseNum = groups.size();
		
		/*new一个对应大小的数组，用来求组合数*/
		a = new Integer[baseNum];
		
		int k = 0;

		for (String id : groups.keySet()) {
			BleBase bleBase = groups.get(id).get(0);
			List<Double> rssis = groups.get(id).stream()
					.map(e -> e.getRssi()).collect(Collectors.toList());

			int len = rssis.size();

			int len2 = len/4;

			/*如果收到的数值个数大于4，则取中间的一部分然后求均值*/
			if(len>=4){
				Collections.sort(rssis);
				Double count = new Double(0);
				for(int i=len2;i<len-len2;i++){
					count+=rssis.get(i);
				}
				r = count/(len-2*len2);
			}else if(len==1){
				/*如果收到的数值个数等于1，就用该数*/
				r = rssis.get(0);
			}else{
				/*如果收到的数值个数小于4，则求中位数*/
				r = getMedian(rssis);
			}

			BleBase base = new BleBase(id,bleBase.getX(), bleBase.getY(), r);
			bases.add(base);

			/*a[k]代表bases的第k个元素*/
			this.a[k] = k;
			k++;

	        /*
			用每组的最大值
	        Collections.sort(rssis);
	        r=rssis.get(len-1);
	        BleBase base = new BleBase(id, r);
	        bases.add(base);
	        a[k] = k;
			k++;
			*/
		}
		
		return (ArrayList<BleBase>) bases;	
	}
	
	/**
	 * 得到列表的中位数
	 *
	 * @param  ls  一个整型数列表
	 * @return  返回该List的中值
	 */
	public static Double getMedian(List<Double> ls){
		Double m;
		
		/*对列表进行排序*/
        Collections.sort(ls);
        
		if(ls.size()%2==0){
        	m = (ls.get((ls.size()/2)-1)+ls.get(ls.size()/2))/2;
        }else{
        	m=(ls.get(ls.size()/2));
        }
		return m;
	}

	public Integer[] getA() {
		return a;
	}

	public void setA(Integer[] a) {
		this.a = a;
	}
}
