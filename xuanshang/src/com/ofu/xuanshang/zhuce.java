package com.ofu.xuanshang;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class zhuce extends Activity{
	private EditText zhuce_name;
	private EditText zhuce_tel;
	private EditText zhuce_psw;
	private EditText zhuce_spsw;
	private static String processURL = "http://172.16.146.94:8080/Mercenary/appadduser?";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		SysApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.zhuce);
		
		 zhuce_name =(EditText) findViewById(R.id.zhuce_name);
		 zhuce_tel =(EditText) findViewById(R.id.zhuce_tel);
		 zhuce_psw =(EditText) findViewById(R.id.zhuce_psw);
		 zhuce_spsw =(EditText) findViewById(R.id.zhuce_spsw);
		 
		
}
	public void zhuce(View v) throws UnsupportedEncodingException {
		 String name = zhuce_name.getText().toString();
		 //name = new String(name.getBytes("UTF-8"),"ISO8859-1");
		 String tel = zhuce_tel.getText().toString();
		 String psw = zhuce_psw.getText().toString();
		 String spsw = zhuce_spsw.getText().toString();
		 if (psw.equals(spsw)) {
			 zhuceService(name, tel,psw);
		}
		
	}
	public void zhuceService(String name, String tel, String psw) {
		String result = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String processURL1 = processURL + "user.name=" +name+ "&user.password=" + psw+ "&user.tel="+tel;
			Log.d("Զ��URL", processURL1);
			HttpGet request = new HttpGet(processURL1);
			request.addHeader("Accept", "text/json");
			
			HttpResponse response = httpclient.execute(request);
			
			HttpEntity entity = response.getEntity();
			
			String json = EntityUtils.toString(entity, "UTF-8");
		
			if (json != null) {
				JSONObject jsonObject = new JSONObject(json);
				result = jsonObject.get("message").toString();
				if (!result.equals(null)) {
					Intent intent = new Intent();
					intent.setClass(zhuce.this, login.class);
//					Bundle bundle =new Bundle();
//					System.out.println(id);
//					intent.putExtras(bundle);
					startActivity(intent);
					Toast.makeText(getApplicationContext(), "欢迎你"+result+"号佣兵",Toast.LENGTH_SHORT).show();
				}
			}

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
	public void login_backmain(View v) {
		Intent intent = new Intent();
		intent.setClass(zhuce.this, login.class);
		startActivity(intent);
		
}
}
