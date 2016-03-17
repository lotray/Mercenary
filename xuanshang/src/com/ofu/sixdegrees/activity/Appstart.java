package com.ofu.sixdegrees.activity;

import com.example.xuanshang.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.WindowManager;

public class Appstart extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.appstart);
		
		
	new Handler().postDelayed(new Runnable(){
		@Override
		public void run(){
			Intent intent = new Intent (Appstart.this,login.class);			
			startActivity(intent);			
			Appstart.this.finish();
		}
	}, 1000);
   }
}