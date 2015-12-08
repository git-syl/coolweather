package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * 数据库帮助类,对数据库进行创建与升级
 * */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
     /*
      * Province建表语句 
      */
	public static final String CREATE_PROVINCE = "create table Province("
			+"id integer primary key autoincrement,"
			+"province_name text,"
			+"province_code text)";

	/*	City 表建表语句
 * */
	public static final String CREATE_CITY = "create table City("
			+"id integer primary key autoincrement,"
			+"city_name text,"
			+"city_code text,"
			+"province_id integer)";
	/*
	 * County表建表语句
	 * */
	public static final String CREATE_COUNTY = "create table County("
			+"id integer primary key autoincrement,"
			+"county_name text,"
			+"county_code text,"
			+"city_id integer)";
	
 public CoolWeatherOpenHelper(Context context,String name,CursorFactory factory,int version){
	     super(context ,name,factory,version);
	     //context,数据库名,返回自定义cursor,版本号
 }
 
 //在调用getWritableDatabse()执行
  @Override 
  public void onCreate(SQLiteDatabase db){
	     db.execSQL(CREATE_PROVINCE);
	     db.execSQL(CREATE_CITY);
	     db.execSQL(CREATE_COUNTY);
  }

@Override
public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	// TODO 自动生成的方法存根
	
}
	 
}
