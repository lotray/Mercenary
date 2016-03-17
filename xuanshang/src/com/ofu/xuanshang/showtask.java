package com.ofu.xuanshang;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.xuanshang.R;
import com.ofu.xuanshang.welcome.Click;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class showtask extends Activity{
	private static  String processURL="http://172.16.146.94:8080/Mercenary/findtaskbyUid?task.Uid=";
	private static int Uid;
@Override
public void onCreate(Bundle savedInstanceState) {
	SysApplication.getInstance().addActivity(this);
	 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()   
     .detectDiskReads()   
     .detectDiskWrites()   
     .detectNetwork()   // or .detectAll() for all detectable problems   
     .penaltyLog()   
     .build());   
	
	  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()   
	         .detectLeakedSqlLiteObjects()   
	         //.detectLeakedClosableObjects()   
	         .penaltyLog()   
	         .penaltyDeath()   
	         .build());
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);	
	setContentView(R.layout.welcome);
	TextView textView1=(TextView)findViewById(R.id.textView1);
	Intent intent = getIntent();
	Bundle data = intent.getExtras();
	Uid = data.getInt("Uid");
	//System.out.println(data.getInt("id"));
	//textView1.setText(data.getInt("id")+"");
	final LinearLayout lin1 = (LinearLayout)findViewById(R.id.category); 
	LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	RelativeLayout newSingleRL1=new RelativeLayout(this);
	HttpClient httpclient1 = new DefaultHttpClient();
	HttpGet request1=new HttpGet(processURL+data.getInt("Uid"));
	request1.addHeader("Accept","text/json");
    //获取响应的结果
	HttpResponse response1;
	try {
		response1 = httpclient1.execute(request1);
		HttpEntity entity1=response1.getEntity();
		String json1 =EntityUtils.toString(entity1,"UTF-8");
//		System.out.println(json);
//		JSONObject jsonObject1=new JSONObject(json);
		JSONArray jsonArray1 =new JSONArray(json1);
		for (int i = 0; i < jsonArray1.length(); i++) {
			JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
			newSingleRL1=generateSingleLayout(jsonObject1.getString("name"),jsonObject1.getString("state"));
			newSingleRL1.setOnClickListener(new Click(jsonObject1.getInt("rid"),jsonObject1.getInt("tid")));
			lin1.addView(newSingleRL1,LP_FW);//全锟斤拷锟矫革拷锟斤拷锟侥诧拷锟街诧拷锟斤拷
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
private RelativeLayout generateSingleLayout(String str1,String str2)
{
	 RelativeLayout layout_root_relative=new RelativeLayout(this);
     
//		RelativeLayout内嵌LineatLayout
		LinearLayout layout_sub_Lin=new LinearLayout(this);
//		layout_sub_Lin.setBackgroundColor(Color.argb(0xff, 0x00, 0xff, 0x00));
		layout_sub_Lin.setOrientation(LinearLayout.VERTICAL);
		layout_sub_Lin.setPadding(5, 5, 5, 5);
		
//	定义TextView	
		TextView tv = new TextView(this);
		LinearLayout.LayoutParams LP_WW = new LinearLayout.LayoutParams(
		LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		tv.setText("悬赏名："+str1+"\n"+"任务状态： "+getstate(str2));
		tv.setTextColor(Color.argb(0xff, 0x00, 0x00, 0x00));
		tv.setTextSize(20);
		tv.setBackgroundResource(R.drawable.preference_single_item);
		tv.setLayoutParams(LP_WW);
		layout_sub_Lin.addView(tv);
//	LinearLayout结束	
		RelativeLayout.LayoutParams RL_MW = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);//锟斤拷锟斤拷注锟斤拷锟斤拷锟轿伙拷茫锟斤拷玫锟斤拷歉锟斤拷锟斤拷锟斤拷牟锟斤拷植锟斤拷锟 
		RL_MW.setMargins(5, 5, 10, 5);
		
		
		layout_root_relative.addView(layout_sub_Lin,RL_MW);
		
		

		  
	     return layout_root_relative;
	
	}
	public String getstate(String str){
			System.out.println(str);
			String state=null;
			if (str.equals("0")) {
				state = "任务接取成功，执行中";
			}else if (str.equals("1")) {
				state = "佣兵确认任务完成，等待悬赏人确认";
			}
			else if (str.equals("2")) {
				state = "悬赏人确认成功，任务完成！";
			}
			return state;
		}
public class Click implements View.OnClickListener{
	int id;
	int tid;
	public Click(int i,int i1){
		id=i;
		tid=i1;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent (showtask.this,renwu.class);
		Bundle bundle =new Bundle();
		bundle.putInt("id",id);
		bundle.putInt("Uid",Uid);
		bundle.putInt("Tid",tid);
		System.out.println(id);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
}
}

