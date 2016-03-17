package com.ofu.xuanshang;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.xuanshang.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class renwu extends Activity{
	private static int Uid;
	private static  String processURL="http://172.16.146.94:8080/Mercenary/findrewardbyid?reward.rid=";
	private static  String processURL1="http://172.16.146.94:8080/Mercenary/giveuptask?task.Tid=";
	private static  String processURL2="http://172.16.146.94:8080/Mercenary/uptask?task.Tid=";
	private static int Tid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()   
         .detectDiskReads()   
         .detectDiskWrites()   
         .detectNetwork()   // or .detectAll() for all detectable problems   
         .penaltyLog()   
         .build());   
		//���������Ĳ���
		  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()   
		         .detectLeakedSqlLiteObjects()   
		         //.detectLeakedClosableObjects()   
		         .penaltyLog()   
		         .penaltyDeath()   
		         .build());
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.jieshou);
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		Uid = data.getInt("Uid");
		SysApplication.getInstance().addActivity(this);
		
		HttpClient httpclient1 = new DefaultHttpClient();
		HttpGet request1=new HttpGet(processURL+data.getInt("id"));
		request1.addHeader("Accept","text/json");
		HttpResponse response1;
		try {
			response1 = httpclient1.execute(request1);
			HttpEntity entity1=response1.getEntity();
			String json1 =EntityUtils.toString(entity1,"UTF-8");
			JSONObject jsonObject=new JSONObject(json1);
			Tid = data.getInt("Tid");
			
			TextView xuanshang_name1 = (TextView) findViewById(R.id.xuanshang_name1);
			TextView xuanshang_level1 = (TextView) findViewById(R.id.xuanshang_level1);
			TextView xuanshang_money1 = (TextView) findViewById(R.id.xuanshang_money1);
			TextView xuanshang_time1 = (TextView) findViewById(R.id.xuanshang_time1);
			TextView xuanshang_time2 = (TextView) findViewById(R.id.xuanshang_time2);
			TextView xuanshang_text1 = (TextView) findViewById(R.id.xuanshang_text1);
			
			xuanshang_name1.setText(jsonObject.getString("title"));
			xuanshang_level1.setText(jsonObject.getString("level")+"级任务");
			xuanshang_money1.setText(jsonObject.getString("money"));
			xuanshang_time1.setText(jsonObject.getString("starttime"));
			xuanshang_time2.setText(jsonObject.getString("endtime"));
			xuanshang_text1.setText(jsonObject.getString("content"));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
	public void giveup(View v) throws ClientProtocolException, IOException, JSONException{
		System.out.println(processURL1+Tid);
		fabuService();
	}
	public void tijiao(View v) throws ClientProtocolException, IOException, JSONException{
		System.out.println(processURL1+Tid);
		fabuService1();
	}
	public void fabuService() throws ClientProtocolException, IOException, JSONException {
		String result1 = null;
		HttpClient httpclient1 = new DefaultHttpClient();
		String URL = processURL1+Tid;
		HttpGet request1=new HttpGet(URL);
		request1.addHeader("Accept", "text/json");
		HttpResponse response = httpclient1.execute(request1);
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(entity, "UTF-8");
		if (json != null) {
			JSONObject jsonObject = new JSONObject(json);
			result1 = jsonObject.get("message").toString();
			if (!result1.equals(null)) {
				Toast.makeText(getApplicationContext(),result1,Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(renwu.this, showtask.class);
				Bundle bundle =new Bundle();
				bundle.putInt("Uid", Uid);
//				System.out.println(id);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	}
	public void fabuService1() throws ClientProtocolException, IOException, JSONException {
		String result1 = null;
		HttpClient httpclient1 = new DefaultHttpClient();
		String URL = processURL2+Tid;
		HttpGet request1=new HttpGet(URL);
		request1.addHeader("Accept", "text/json");
		HttpResponse response = httpclient1.execute(request1);
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(entity, "UTF-8");
		if (json != null) {
			JSONObject jsonObject = new JSONObject(json);
			result1 = jsonObject.get("message").toString();
			if (!result1.equals(null)) {
				Toast.makeText(getApplicationContext(),result1,Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(renwu.this, showtask.class);
				Bundle bundle =new Bundle();
				bundle.putInt("Uid", Uid);
//				System.out.println(id);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	}
	public void login_back1(View v) {
		Intent intent = new Intent();
		intent.setClass(renwu.this, mainXuanshang.class);
		Bundle bundle =new Bundle();
		bundle.putInt("id", Uid);
//		System.out.println(id);
		intent.putExtras(bundle);
		startActivity(intent);
		// ��ϸ���񷵻�������
}
}
