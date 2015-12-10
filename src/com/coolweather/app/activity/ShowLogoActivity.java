package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.R.id;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class ShowLogoActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_logo);
		Handler x = new Handler();  
	    x.postDelayed(new splashhandler(), 1000);  

		
	}
	class splashhandler implements Runnable{  
		 
        public void run() {  
       	 startActivity(new Intent(ShowLogoActivity.this,ChooseAreaActivity.class));
       	 ShowLogoActivity.this.finish();  
      }  
        
  }  

}

