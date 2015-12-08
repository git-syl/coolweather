package com.coolweather.app.activity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R.id;
import com.coolweather.app.R.layout;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	
	private List<String> dataList = new ArrayList<String>();
	
//ʡ,�У��أ��б�
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	
	//ѡ�е� ʡ������
	private Province selectedProvince;
	private City selectedCity;
	
	//��ǰѡ��ļ���
	private int currentLevel;
	
	//�Ƿ���WeatherActivity����ת����
	private boolean isFromWeatherActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//�Ѿ�ѡ���˳��Ҳ��Ǵ�WeatherActivity��ת�������Ż�ֱ����ת��WeatherActivity
		if(prefs.getBoolean("city_selected", false)&&!isFromWeatherActivity){
			Intent intent =new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
			
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(layout.choose_area);
		
	
		listView = (ListView) findViewById(id.list_view);
		titleText = (TextView) findViewById(id.title_text);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				// TODO �Զ����ɵķ������
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince = provinceList.get(index);
					queryCities();
					
				}else if(currentLevel==LEVEL_CITY){
					selectedCity = cityList.get(index);
					Log.e("selectedCity", selectedCity+"CCA");
					queryCounties();
				}else if(currentLevel==LEVEL_COUNTY){
					String countyCode = countyList.get(index).getCountyCode();
					Intent intent =new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("county_code", countyCode);
					startActivity(intent);
					finish();
				}
			}
			
		});
		queryProvince();
		//����ʡ������
		
	}
	
	//��ѯȫ������ʡ���ȴ����ݿ�飬û�о�ȥ��������ѯ
	private void queryProvince(){
		provinceList= coolWeatherDB.loadProvinces();
		if(provinceList.size()>0){
			Log.e("provinceList.size()>0", "");
			dataList.clear();
			for(Province province:provinceList){
				   dataList.add(province.getProvinceName());
			}
			//ˢ��List
			adapter.notifyDataSetChanged();
		//	��ʾ���б��ƶ���ָ����Position����
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;	
		}else{
			Log.e("queryProvince _else", "");
			queryFromServer(null,"province");
		
		}
	}
	
	private void 	queryCities(){
	
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		Log.d("queryCities",cityList.size()+"AID"+selectedProvince.getId()+"AA");
		if(cityList.size()>0){
			Log.d("cityList", cityList.size()+"");
			dataList.clear();
			for(City city :cityList){
				dataList.add(city.getCityName());		
				Log.e("cityList", city.getCityName()+"BB");
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}else {
			Log.d("queryFromServerqueryCities"," queryCities"+"");
			queryFromServer(selectedProvince.getProvinceCode(),"city");
		}
		
		
	}
	private void queryCounties(){
		countyList = coolWeatherDB.loadCounties(selectedCity.getId());
		Log.e("countyList", selectedCity.getId()+"CCi");
		Log.e("countyList", countyList.size()+"CC");
		if(countyList.size()>0){
			dataList.clear();
			for(County county: countyList){
				dataList.add(county.getCountyName());
				
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
			
			
		}else{
			Log.e("selectedCity", selectedCity.getCityCode()+"C1");
			queryFromServer(selectedCity.getCityCode(),"county");
		}
		
	}
	
	//���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ���ؼ�����
	private void queryFromServer(final String code,final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
		}
		else{
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
	            boolean result = false;
	            if("province".equals(type)){
	            	result = Utility.handleProvinceResponse(coolWeatherDB, response);
	            }else if("city".equals(type)){
	            	
	            	result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());
	            	Log.e("boolean resul", result+"");
	            	
	            }else if("county".equals(type)){
	            	result = Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
	            	Log.e("Main", result+"C2");
	           
	            }
	            
	            if(result){
	            	//ͨ��runOnUiThread�ص����̴߳����߼�
	            	runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO �Զ����ɵķ������
							closeProgressDialog();
							if("province".equals(type)){
								queryProvince();
							}else if("city".equals(type)){
								queryCities();
								Log.e("city.equals(type", "������");
							}else if("county".equals(type)){
								queryCounties();
								
							}
						}
	            		
	            	});
	            }
				
			}

			@Override
			public void onError(Exception e) {
			     
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO �Զ����ɵķ������
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��",Toast.LENGTH_LONG).show();
						
					}
					
				});
				
			}
			
		});
	}
	
	//��ʾ �رնԻ���
	private void  showProgressDialog(){
		if(progressDialog==null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���......");
			//�Ի�������������Ƿ��öԻ�����ʧ
			progressDialog.setCanceledOnTouchOutside(false);
			
		}
		progressDialog.show();
	}
	
	private void closeProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
	}
	
	@Override
	public void onBackPressed(){
		
		if(currentLevel==LEVEL_COUNTY){
			queryCities();			
		}else if(currentLevel==LEVEL_CITY){
			queryProvince();
		}else{
			if(isFromWeatherActivity){
				Intent intent = new Intent(this,WeatherActivity.class);
				startActivity(intent);
				
			}
			finish();
		}
		
	}
	

}
