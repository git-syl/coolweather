package com.coolweather.app.activity;

import com.coolweather.app.R.id;
import com.coolweather.app.R.layout;
import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class WeatherActivity extends Activity implements OnClickListener {
	
	
	private LinearLayout weatherInfoLayout;
//������ʾ������
	private TextView cityNameText;
	//������ʾ����������Ϣ
	private TextView publishText;
	//��ʾ����������Ϣ
	private TextView weatherDespText;
	//������ʾ����1
	private TextView temp1Text;
	//������ʾ����2
	private TextView temp2Text;
	//������ʾ��ǰ������
	private TextView currentDateText;
	
	//�����л���ť
	private Button switchCity;
	
	private Button refreshWeather;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(id.weather_info_layout);
		cityNameText = (TextView) findViewById(id.city_name);
		publishText = (TextView) findViewById(id.publish_text);
		weatherDespText = (TextView) findViewById(id.weather_desp);
		temp1Text = (TextView) findViewById(id.temp1);
		temp2Text = (TextView) findViewById(id.temp2);
		currentDateText = (TextView) findViewById(id.current_data);
		String countyCode = getIntent().getStringExtra("county_code");
		
		switchCity = (Button) findViewById(id.switch_city);
		refreshWeather = (Button) findViewById(id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		if(!TextUtils.isEmpty(countyCode)){
			//���ؼ�����ʱ��ȥ��ѯ����
			publishText.setText("ͬ����");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			Log.e("WeatherActivity", countyCode+"Code");
			queryWeatherCode(countyCode);
		}else{
			
			Log.e("WeatherActivity", countyCode+"showWeather");
			showWeather();
		
		}
		
		AdView adview = new AdView(this,AdSize.FIT_SCREEN);
		LinearLayout adLayout = (LinearLayout) findViewById(id.adLayout);
		adLayout.addView(adview);
		
	}

	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp2Text.setText(prefs.getString("temp1", ""));
		temp1Text.setText(prefs.getString("temp2", ""));
		Log.e("WeatherActivity", prefs.getString("temp1", "")+"ISN");
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����"+prefs.getString("publish_time", "")+"����");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
		Intent intent = new Intent(this,AutoUpdateService.class);
		startService(intent);
		
	}
//��ѯ�ؼ����Ŷ�Ӧ����������
	private void queryWeatherCode(String countyCode) {
		  String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		  queryFromServer(address,"countyCode");
		
	}
	//��ѯ������Ӧ������
	private void queryWeatherInfo(String weatherCode) {
	//String address = "http://www.weather.com.cn/data/cityinfo"+weatherCode+".html";
	//	queryFromServer(address,"weatherCode");
		
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address, "weatherCode");
		
	}
	private void queryFromServer(String address, final String type) {
		 HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO �Զ����ɵķ������
			      if("countyCode".equals(type)){
			  		String [] array = response.split("\\|");
					if(array!=null&&array.length==2){
						String weatherCode = array[1];
						Log.e("WeatherActivity", weatherCode);
						queryWeatherInfo(weatherCode);
						
					}
	
			      }else if("weatherCode".equals(type)){
			    	  Log.e("WeatherActivity","weatherCodeequals");
			    	  Utility.handleWeatherResponse(WeatherActivity.this, response);
			    	  
			    	  runOnUiThread(new Runnable(){

						@Override
						public void run() {
							showWeather();
							
						}});
			    	  
			      }
			
			
			}
		

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						publishText.setText("ͬ��ʧ��");
						
					}});
				
			}});
		
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		switch(v.getId()){
		case id.switch_city:
				Intent intent = new Intent(this,ChooseAreaActivity.class);
				intent.putExtra("from_weather_activity", true);
				startActivity(intent);
				finish();
				break;
		case id.refresh_weather:
			publishText.setText("ͬ����......");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if(!TextUtils.isEmpty(weatherCode)){
				queryWeatherInfo(weatherCode);
				
			}
			break;
			default:
				break;
		}
		
	}
	
}
