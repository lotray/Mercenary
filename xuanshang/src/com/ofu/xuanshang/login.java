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

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class login extends Activity {
	private EditText mUser; 
	private EditText mPassword; 
	private static String processURL = "http://172.16.146.94:8080/Mercenary/applogin?";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		SysApplication.getInstance().addActivity(this);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // or
																		// .detectAll()
																		// for
																		// all
																		// detectable
																		// problems
				.penaltyLog().build());
	
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				// .detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		SysApplication.getInstance().addActivity(this);
		mUser = (EditText) findViewById(R.id.login_user_edit);
		mPassword = (EditText) findViewById(R.id.login_passwd_edit);
	}

	public void login_mainxuanshang(View v) {
		String userName = mUser.getText().toString();
		String password = mPassword.getText().toString();
		loginRemoteService(userName, password);
	
	}

	public void loginRemoteService(String userName, String password) {
		String result = null;
		try {

			// HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// URL

			String processURL1 = processURL + "Uid=" + userName
					+ "&password=" + password;
			Log.d("Զ��URL", processURL1);
			// HttpGet
			HttpGet request = new HttpGet(processURL1);
			
			request.addHeader("Accept", "text/json");
			
			HttpResponse response = httpclient.execute(request);
		
			HttpEntity entity = response.getEntity();
			
			String json = EntityUtils.toString(entity, "UTF-8");
		
			if (json != null) {
				JSONObject jsonObject = new JSONObject(json);
				result = jsonObject.get("message").toString();
				if (result.equals("1")) {
					Intent intent = new Intent();
					intent.setClass(login.this, mainXuanshang.class);
					Bundle bundle =new Bundle();
					bundle.putInt("id", Integer.parseInt(userName));
//					System.out.println(id);
					intent.putExtras(bundle);
					startActivity(intent);
					Toast.makeText(getApplicationContext(), "欢迎你"+userName+"号佣兵",
							Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getApplicationContext(), "登陆失败！",
							Toast.LENGTH_SHORT).show();
				}
			}
			if (result == null) {
				json = "��¼ʧ�������µ�¼";
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

	public void login_zhuce(View v) {
		Intent intent = new Intent();
		intent.setClass(login.this, zhuce.class);
		startActivity(intent);
	}
	public void login_wangji(View v) {
		Intent intent = new Intent();
		intent.setClass(login.this, mainXuanshang.class);
		Bundle bundle =new Bundle();
		bundle.putInt("id", 1);
//		System.out.println(id);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
