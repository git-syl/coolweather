package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
	
	/*
	 * �������������ص�JSON���ݣ������������������ݴ洢������
	 * */
	public static void handleWeatherResponse(Context context,String response){
		     try{
		    	 JSONObject jsonObject = new JSONObject(response);
		    	 JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
		    	 String cityName = weatherInfo.getString("city");
		    	 String weatherCode = weatherInfo.getString("cityid");
		    	 String temp1 = weatherInfo.getString("temp1");
		    	 String temp2 = weatherInfo.getString("temp2");
		    	 String weatherDesp = weatherInfo.getString("weather");
		    	 String publishTime = weatherInfo.getString("ptime");
		    	  Log.e("Utility", temp2+"U");
		    	 saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
		    	 
		     }catch(JSONException e){
		    	   e.printStackTrace();
		     }
	}

	private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1,
			String temp2, String weatherDesp, String publishTime) {
	     
		SimpleDateFormat  sdf= new SimpleDateFormat("yyyy��M��d��",Locale.CHINA);
		
		//getDefaultSharedPreferences(Context),��ȡ SharedPreferences ����
		//�Զ�ʹ��Ӧ�ó��������Ϊǰ׺
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_data", sdf.format(new Date()));
		Log.e("Utility", editor.putString("temp2", temp2)+"T");
		editor.commit();
		
				
		
	}
	

}
