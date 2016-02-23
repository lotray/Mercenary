package com.example.xuanshang;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class fabu extends Activity{
	private EditText fabu_name;
	private EditText fabu_money;
	private EditText fabu_text;
	private EditText shijian;
	private String category;
	private static int Uid;
	private static String processURL = "http://172.16.146.94:8080/Mercenary/addreward?";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.fabu);
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		Uid = data.getInt("Uid");
		SysApplication.getInstance().addActivity(this);
		Spinner spinner=(Spinner)findViewById(R.id.xuanze2);
		//spinner.getSelectedItem();
//		分类任务监听事件
		spinner.setOnItemSelectedListener(null);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onNothingSelected(AdapterView<?>arg0){
				
			}
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		final EditText date =(EditText) findViewById(R.id.shijian);
		  final Calendar cd = Calendar.getInstance();
		  Date date1 = new Date();
		  cd.setTime(date1);
		  date.setOnClickListener(new OnClickListener(){
		   public void onClick(View v) {
		     new DatePickerDialog(fabu.this, new OnDateSetListener(){
		      public void onDateSet(DatePicker view, int year,
		        int monthOfYear, int dayOfMonth) {
		    	  date.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
		      }
		      }, 
		      cd.get(Calendar.YEAR), 
		      cd.get(Calendar.MONTH),
		      cd.get(Calendar.DAY_OF_MONTH)).show();
		   }
		  });
		  date.setOnFocusChangeListener(new OnFocusChangeListener(){
		   public void onFocusChange(View v, boolean hasFocus) {
		     new DatePickerDialog(fabu.this, new OnDateSetListener(){
		      public void onDateSet(DatePicker view, int year,
		        int monthOfYear, int dayOfMonth) {
		    	  date.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
		      }
		      }, 
		      cd.get(Calendar.YEAR), 
		      cd.get(Calendar.MONTH),
		      cd.get(Calendar.DAY_OF_MONTH)).show();
		   }
		  });
		  
		  //添加到后台
		  category = spinner.getSelectedItem().toString();
		  fabu_name = (EditText) findViewById(R.id.fabu_name);
		  fabu_money = (EditText) findViewById(R.id.fabu_money);
		  fabu_text = (EditText) findViewById(R.id.fabu_text);
		  shijian = (EditText) findViewById(R.id.shijian);
		  
	}
	public void fabu(View v) throws UnsupportedEncodingException {
		 String title = fabu_name.getText().toString();
		 //name = new String(name.getBytes("UTF-8"),"ISO8859-1");
		 String money = fabu_money.getText().toString();
		 String content = fabu_text.getText().toString();
		 String time = shijian.getText().toString();
		 fabuService(title, money, content, time, category,Uid);
		
	}
	public void fabuService(String title, String money, String content,String time,String category,int Uid) {
		String result = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String processURL1 = processURL + "reward.title=" +title+ "&reward.money=" + money+ "&reward.content="+content+ "&reward.endtime="+time+"&category="+category+"&Uid="+Uid;
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
					intent.setClass(fabu.this, mainXuanshang.class);
					Bundle bundle =new Bundle();
					bundle.putInt("id", Uid);
//					System.out.println(id);
					intent.putExtras(bundle);
					startActivity(intent);
					Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
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
	
	
	public void login_back3(View v) {
		Intent intent = new Intent();
		intent.setClass(fabu.this, mainXuanshang.class);
		Bundle bundle =new Bundle();
		bundle.putInt("id", Uid);
//		System.out.println(id);
		intent.putExtras(bundle);
		startActivity(intent);
		// ��ϸ���񷵻�������
}
}