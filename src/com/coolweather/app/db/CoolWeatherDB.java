package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
/*
 * 数据库名
 * */
	public static final String DB_NAME = "cool_weather";
	
	/*
	 * 数据库版本
	 * */
	public static final int VERSION = 1;
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	//   db = dbHelper.getWritableDatabase();
	//返回一个SQLiteDatabase对象，借助它可对数据库CRUD操作
	
	/*
	 * 构造方法私有化
	 * */
	
	private CoolWeatherDB(Context context){
		//调用CoolWeatherOpenHelper(context,数据库名,cursor,版本)，创建数据库
		   CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
		   //调用方法 获取可写入数据库,
		   db = dbHelper.getWritableDatabase();
	}
	/*
	 *获取CoolWeatherDB的实例
	 * */
	public synchronized static CoolWeatherDB getInstance(Context context){
		
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
		
	}
	
	/*
	 * 将Province 实例存储到数据库
	 * */
	public void saveProvince(Province province){
		 if(province !=null){
			 ContentValues values = new ContentValues();
			 values.put("province_name", province.getProvinceName());
			 values.put("province_code", province.getProvinceCode());
			 //insert(表名,自动赋值参数,Content对象);
			 db.insert("Province",null,values);
		 }
	}
	/*
	 * 从数据库读取全国所有省份信息
	 * */
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor =
				//调用query后会返回cursor对象
				//db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
				db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			 do{
				 //遍历Cursor对象
				 Province province  = new Province();
				 province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				 province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				 province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				 list.add(province);
			 }while(cursor.moveToNext());
			
		
		}
		 if(cursor!=null){
			 cursor.close();
		 }
		return list;
	}
/*
 *  将City实体存储到数据库
 * */
	public void saveCity(City city){
		 if(city!=null){
			 ContentValues values = new ContentValues();
			 values.put("city_name", city.getCityName());
			 values.put("city_code", city.getCityCode());
			 values.put("province_id", city.getProivnceId());
			 db.insert("City",null,values);
		 }
		
	}
	/*
	 *  从数据库读取 某省下 所有的城市信息
	 * */
	public List<City> loadCities(int provinceId){
		   List<City> list = new ArrayList<City>();
		   Cursor cursor = db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		   if(cursor.moveToFirst()){
			   do{
				   City city =new City();
				   city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				   city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				   city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				   city.setProivnceId(provinceId);
				    list.add(city);
			   }while(cursor.moveToNext());
			   
		   }
		   if(cursor!=null){
			   cursor.close();
		   }
		   return list;
	}
	
	/*
	 * 将County 实例存储到数据库
	 * */
	public void saveCounty(County county){
		   if(county!=null){
			   ContentValues values = new ContentValues();
			   values.put("county_name", county.getCountyName());
			   values.put("county_code", county.getCountyCode());
			   values.put("city_id", county.getCityId());
			   db.insert("County",null,values);
		   }
	}
	/*
	 * 从数据库读取某个城市下所有县的信息
	 * */
	public List<County> loadCounties(int cityId){
		 List<County> list = new ArrayList<County>();
		 Cursor cursor =  db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		 if(cursor.moveToFirst()){
			 do{
				 County county = new County();
				 county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				 county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				 county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				 county.setCityId(cityId);
				 list.add(county);
			 }while(cursor.moveToNext());
		 }
		 if(cursor!=null){
			 cursor.close();
		 }
		 return list;
	}
	
}
