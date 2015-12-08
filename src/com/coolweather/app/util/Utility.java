package com.coolweather.app.util;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.text.TextUtils;
import android.util.Log;

public class Utility {
	/*
	 * �����ʹ�����������ص���������
	 * */
	
	//�߳� �� ����
	public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolweatherDB,String response){
		       
		if(!TextUtils.isEmpty(response)){
		        	String[] allProvince = response.split(",");
		        	if(allProvince!= null&&allProvince.length>0){
		        		for(String p : allProvince){
		        			String[] array = p.split("\\|");
		        			Province province = new Province();
		        			province.setProvinceCode(array[0]);
		        			province.setProvinceName(array[1]);
		        			//���������������ݴ洢��Province��
		        			coolweatherDB.saveProvince(province);
		        		}
		        		return true;
		        	}
		        	
		        }
		
		       return false;
	}
	
	/*
	 * �����ʹ�������������е�����
	 * */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
				Log.e("handleCitiesResponse", response);
			String [] allCities = response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String c :allCities){
					 String[] array = c.split("\\|");
					 City city = new City();
					 city.setCityCode(array[0]);
					 city.setCityName(array[1]);
					 city.setProivnceId(provinceId);
					 //���������������ݴ洢��City��
					 coolWeatherDB.saveCity(city);
					 
				}
				return true;
			}
		}
		return false;
	}
	
	/*
	 * �������ʹ�����������ص��ؼ�����
	 * */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties!=null&&allCounties.length>0){
				 for(String c: allCounties){
					 String [] array = c.split("\\|");
					 County  county = new County();
					 county.setCountyCode(array[0]);
					 county.setCountyName(array[1]);
					 county.setCityId(cityId);
					 //���������������ݴ洢��County��
					 coolWeatherDB.saveCounty(county);
				 }
				 return true;
			}
		}
		return false;
	}
	

}
