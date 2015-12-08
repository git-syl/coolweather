package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
		
		new Thread(new Runnable(){
		
			@Override
			public void run(){
				HttpURLConnection connection = null;
				try{
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection .setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection .setReadTimeout(8000);
					//�˳������Ǳ�ʾ�ֽ���������������ĳ��ࡣ
					InputStream in = connection.getInputStream();
					// InputStreamReader���ֽ���ͨ���ַ���������
					//BufferedReader ���ַ��������ж�ȡ�ı�����������ַ����Ӷ�ʵ���ַ���������еĸ�Ч��ȡ�� 
                  BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                  //�������ַ����������������߳�ʹ�õ�ʱ������������ձ飩��������ܣ�
                  //�������Ȳ��ø��࣬��Ϊ�ڴ����ʵ���У����� StringBuffer Ҫ�졣 
                  StringBuilder response = new StringBuilder();
                  String line;
                  while((line = reader.readLine())!=null){
                	  response.append(line); 	  
                  }
                  if(listener!=null){
                	  listener.onFinish(response.toString());
                  }
				}catch(Exception e){
					if(listener!=null){
						listener.onError(e);
					}
					
				}finally{
					if(connection!=null){
						connection.disconnect();
					}
					
				}
			}
			
		}).start();
		
	}


}
