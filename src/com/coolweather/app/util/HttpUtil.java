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
					//此抽象类是表示字节输入流的所有类的超类。
					InputStream in = connection.getInputStream();
					// InputStreamReader是字节流通向字符流的桥梁
					//BufferedReader 从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取。 
                  BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                  //，用在字符串缓冲区被单个线程使用的时候（这种情况很普遍）。如果可能，
                  //建议优先采用该类，因为在大多数实现中，它比 StringBuffer 要快。 
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
