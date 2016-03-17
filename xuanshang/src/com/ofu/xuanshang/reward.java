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

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class reward extends Activity{
	private static  String processURL="http://172.16.146.94:8080/Mercenary/findrewardbyid?reward.rid=";
	private static  String processURL1="http://172.16.146.94:8080/Mercenary/appaddtask?task.Rid=";
	private static int Uid;
	private static int Rid;
	private static String rname;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		SysApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.renwu);
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		Uid = data.getInt("Uid");
		Rid = data.getInt("id");
		System.out.println(Uid+"----"+Rid);
		//System.out.println(data.getInt("id"));
		
		HttpClient httpclient1 = new DefaultHttpClient();
		HttpGet request1=new HttpGet(processURL+data.getInt("id"));
		request1.addHeader("Accept","text/json");
		HttpResponse response1;
		try {
			response1 = httpclient1.execute(request1);
			HttpEntity entity1=response1.getEntity();
			String json1 =EntityUtils.toString(entity1,"UTF-8");
			JSONObject jsonObject=new JSONObject(json1);
			
			TextView xuanshang_name = (TextView) findViewById(R.id.xuanshang_name1);
			TextView xuanshang_lavel = (TextView) findViewById(R.id.xuanshang_lavel);
			TextView xuanshang_money = (TextView) findViewById(R.id.xuanshang_money1);
			TextView xuanshang_starttime = (TextView) findViewById(R.id.xuanshang_starttime);
			TextView xuanshang_time = (TextView) findViewById(R.id.xuanshang_time);
			TextView xuanshang_text = (TextView) findViewById(R.id.xuanshang_text);
			
			rname = jsonObject.getString("title");
			xuanshang_name.setText(jsonObject.getString("title"));
			xuanshang_lavel.setText(jsonObject.getString("level"));
			xuanshang_money.setText(jsonObject.getString("money"));
			xuanshang_starttime.setText(jsonObject.getString("starttime"));
			xuanshang_time.setText(jsonObject.getString("endtime"));
			xuanshang_text.setText(jsonObject.getString("content"));
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
	public void jieshou(View v) throws ClientProtocolException, IOException, JSONException{
		System.out.println(processURL1+Rid+"&task.Uid"+Uid+"&task.name="+rname);
		fabuService();
	}
	public void fabuService() throws ClientProtocolException, IOException, JSONException {
		String result1 = null;
		HttpClient httpclient1 = new DefaultHttpClient();
		String URL = processURL1+Rid+"&task.Uid="+Uid+"&task.name="+rname;
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
				intent.setClass(reward.this, mainXuanshang.class);
				Bundle bundle =new Bundle();
				bundle.putInt("id", Uid);
//				System.out.println(id);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	}
	public void login_back(View v) {
		Intent intent = new Intent();
		intent.setClass(reward.this, mainXuanshang.class);
		Bundle bundle =new Bundle();
		bundle.putInt("id", Uid);
//		System.out.println(id);
		intent.putExtras(bundle);
		startActivity(intent);
		// ��ϸ���񷵻�������
}
}
