package com.ofu.xuanshang;

import java.io.IOException;
import java.util.ArrayList;

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


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.StaticLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class mainXuanshang extends Activity {
	boolean isExit;
	public static mainXuanshang instance = null;

	private ViewPager mTabPager;
	private ImageView mTabImg;// 动画图片

	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one;// 单个水平动画位移
	private int two;
	private int three;
	private LinearLayout mClose;
	private LinearLayout mCloseBtn;
	private View layout;
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	private TextView mTab1, mTab2, mTab3;
//	动态遍历
	private static int id = 100;
	private static  String processURL="http://172.16.146.94:8080/Mercenary/findallcategory";
	private static  String processURL1="http://172.16.146.94:8080/Mercenary/findreward";
	private static  String processURL2="http://172.16.146.94:8080/Mercenary/finduserbyid?Uid=";

	private static int Uid;
	// private Button mRightBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		Uid = data.getInt("id");
		//System.out.println(data.getInt("id"));
		setContentView(R.layout.main_xuanshang);
		SysApplication.getInstance().addActivity(this);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		instance = this;
		/*
		 * mRightBtn = (Button) findViewById(R.id.right_btn);
		 * mRightBtn.setOnClickListener(new Button.OnClickListener() { @Override
		 * public void onClick(View v) { showPopupWindow
		 * (MainWeixin.this,mRightBtn); } });
		 */
		

		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mTab1 = (TextView) findViewById(R.id.textView1);
		mTab2 = (TextView) findViewById(R.id.textView2);
		mTab3 = (TextView) findViewById(R.id.textView3);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new MyOnClickListener(2));

		// mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		//
		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();
		one = displayWidth / 4; // 设置水平动画平移大小
		two = one * 2;
		three = one * 3;
		// Log.i("info", "获取的屏幕分辨率为" + one + two + three + "X" + displayHeight);

		// InitImageView();//使用动画
		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.main_tab_xuanshang, null);
		View view2 = mLi.inflate(R.layout.main_tab_leibie, null);
		View view3 = mLi.inflate(R.layout.main_tab_message, null);

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			// @Override
			// public CharSequence getPageTitle(int position) {
			// return titles.get(position);
			// }

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mTabPager.setAdapter(mPagerAdapter);
		
		
		//--分类任务--动态遍历-------------------------
		final LinearLayout lin = (LinearLayout) view2.findViewById(R.id.list_Lin); 
		LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout newSingleRL=new RelativeLayout(this);
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request=new HttpGet(processURL);
		request.addHeader("Accept","text/json");
        //获取响应的结果
		HttpResponse response;
		try {
			response = httpclient.execute(request);
			HttpEntity entity=response.getEntity();
			String json =EntityUtils.toString(entity,"UTF-8");
			System.out.println(json);
			JSONObject jsonObject1=new JSONObject(json);
			String js =jsonObject1.get("categories").toString();
			System.out.println(js);
			JSONArray jsonArray =new JSONArray(js);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				newSingleRL=generateSingleLayout(jsonObject.getInt("cid"),jsonObject.getString("name"));
				newSingleRL.setOnClickListener(new Click(jsonObject.getInt("cid")));
				lin.addView(newSingleRL,LP_FW);
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
		
		//--任务大厅--动态遍历-------------------------
		final LinearLayout lin1 = (LinearLayout) view1.findViewById(R.id.dongtai); 
		LinearLayout.LayoutParams LP_FW1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout newSingleRL1=new RelativeLayout(this);
		HttpClient httpclient1 = new DefaultHttpClient();
		HttpGet request1=new HttpGet(processURL1);
		request1.addHeader("Accept","text/json");
        //获取响应的结果
		HttpResponse response1;
		try {
			response1 = httpclient1.execute(request1);
			HttpEntity entity1=response1.getEntity();
			String json1 =EntityUtils.toString(entity1,"UTF-8");
			JSONArray jsonArray1 =new JSONArray(json1);
			for (int i = 0; i < jsonArray1.length(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				newSingleRL1=mainLayout(jsonObject1.getString("level"),jsonObject1.getString("title"),jsonObject1.getString("money"));
				newSingleRL1.setOnClickListener(new Click1(jsonObject1.getInt("rid")));
				lin1.addView(newSingleRL1,LP_FW);
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
		
		
		//--任务大厅--佣兵信息-------------------------
		HttpClient httpclient3 = new DefaultHttpClient();
		HttpGet request3=new HttpGet(processURL2+data.getInt("id"));
		request3.addHeader("Accept","text/json");
		HttpResponse response3;
		try {
			response3 = httpclient3.execute(request3);
			HttpEntity entity3=response3.getEntity();
			String json3 =EntityUtils.toString(entity3,"UTF-8");
			JSONObject jsonObject3=new JSONObject(json3);
			
			TextView name = (TextView) view3.findViewById(R.id.name);
			TextView level = (TextView) view3.findViewById(R.id.level);
			TextView money = (TextView) view3.findViewById(R.id.money);
			
			name.setText(jsonObject3.getString("name"));
			level.setText(jsonObject3.getString("level"));
			money.setText(jsonObject3.getString("money"));

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
	
	
	private RelativeLayout mainLayout(String str1,String str2,String str3)
	{
		 RelativeLayout layout_root_relative=new RelativeLayout(this);
		                   
//				RelativeLayout内嵌LineatLayout
				LinearLayout layout_sub_Lin=new LinearLayout(this);
//				layout_sub_Lin.setBackgroundColor(Color.argb(0xff, 0x00, 0xff, 0x00));
				layout_sub_Lin.setOrientation(LinearLayout.VERTICAL);
				layout_sub_Lin.setPadding(5, 5, 5, 5);
				
//			定义TextView	
				TextView tv = new TextView(this);
				LinearLayout.LayoutParams LP_WW = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				tv.setText("等级："+str1+"\n"+"任务名： "+str2+"\n"+"报酬："+str3);
				tv.setTextColor(Color.argb(0xff, 0x00, 0x00, 0x00));
				tv.setTextSize(20);
				tv.setBackgroundResource(R.drawable.preference_single_item);
				tv.setLayoutParams(LP_WW);
				layout_sub_Lin.addView(tv);
//			LinearLayout结束	
				RelativeLayout.LayoutParams RL_MW = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				RL_MW.setMargins(5, 5, 10, 5);
				
				
				layout_root_relative.addView(layout_sub_Lin,RL_MW);
				
				

				  
			     return layout_root_relative;
	}
//	下面是layout
	private RelativeLayout generateSingleLayout(int imageID,String str)
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
		tv.setText(str);
		tv.setTextColor(Color.argb(0xff, 0x00, 0x00, 0x00));
		tv.setTextSize(25);
		tv.setBackgroundResource(R.drawable.preference_single_item);
		tv.setLayoutParams(LP_WW);
		layout_sub_Lin.addView(tv);
//	LinearLayout结束	
		RelativeLayout.LayoutParams RL_MW = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RL_MW.setMargins(5, 5, 10, 5);
		RL_MW.addRule(RelativeLayout.RIGHT_OF,imageID);
		layout_root_relative.addView(layout_sub_Lin,RL_MW);

		  
	     return layout_root_relative;
	}
	
	public class Click implements View.OnClickListener{
		int id;
		public Click(int i){
			id=i;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent (mainXuanshang.this,welcome.class);
			Bundle bundle =new Bundle();
			bundle.putInt("id",id);
			bundle.putInt("Uid", Uid);
			System.out.println(id);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	}
	public class Click1 implements View.OnClickListener{
		int id;
		public Click1(int i){
			id=i;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent (mainXuanshang.this,reward.class);
			Bundle bundle =new Bundle();
			bundle.putInt("id",id);
			bundle.putInt("Uid", Uid);
			System.out.println(id);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	}
//	以上是动态遍历
	
	
	
	
	
	
	

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};

	/*
	 * 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setTextColor(Color.parseColor("#1ba526"));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setTextColor(Color.parseColor("#ffffff"));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setTextColor(Color.parseColor("#ffffff"));
				}
				// else if (currIndex == 3) {
				// animation = new TranslateAnimation(three, 0, 0, 0);
				//
				// }
				break;
			case 1:
				mTab2.setTextColor(Color.parseColor("#1ba526"));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setTextColor(Color.parseColor("#ffffff"));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setTextColor(Color.parseColor("#ffffff"));
				}
				// else if (currIndex == 3) {
				// animation = new TranslateAnimation(three, one, 0, 0);
				//
				// }
				break;
			case 2:
				mTab3.setTextColor(Color.parseColor("#1ba526"));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setTextColor(Color.parseColor("#ffffff"));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setTextColor(Color.parseColor("#ffffff"));
				}
				// else if (currIndex == 3) {
				// animation = new TranslateAnimation(three, two, 0, 0);
				//
				// }
				break;
			}
			currIndex = arg0;
			// animation.setFillAfter(true);// True:图片停在动画结束位置
			// animation.setDuration(150);
			// mTabImg.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	public void exit(){  

        if (!isExit) {  

            isExit = true;  

            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();  

            mHandler.sendEmptyMessageDelayed(0, 2000);  

        } else {  

            Intent intent = new Intent(Intent.ACTION_MAIN);  

            intent.addCategory(Intent.CATEGORY_HOME);  

            startActivity(intent);  

            System.exit(0);  

        }  

    } 
	Handler mHandler = new Handler() {  

		  

        @Override  

        public void handleMessage(Message msg) {  

            // TODO Auto-generated method stub   

            super.handleMessage(msg);  

            isExit = false;  

        }  

  

    };  
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 获取
																				// back键

			if (menu_display) { // 如果 Menu已经打开 ，先关闭Menu
				menuWindow.dismiss();
				menu_display = false;
			} else {
				exit();
				return false;
			}
		}

		else if (keyCode == KeyEvent.KEYCODE_MENU) { // 获取 Menu键
			if (!menu_display) {
				// 获取LayoutInflater实例
				inflater = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				// 该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.main_menu, null);

				// layout加入到PopupWindow中
				menuWindow = new PopupWindow(layout, LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT); // 后两个参数是width和height
				// menuWindow.showAsDropDown(layout); //设置弹出效果
				// menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(
						this.findViewById(R.id.mainXuanshang), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				// 获取main中的控件
				mClose = (LinearLayout) layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout) layout
						.findViewById(R.id.menu_close_btn);

				
				mCloseBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Toast.makeText(mainXuanshang.this, "退出",
								Toast.LENGTH_LONG).show();
						SysApplication.getInstance().exit();
						menuWindow.dismiss(); // 响应点击事件之后关闭Menu

					}

				});
				menu_display = true;
			} else {
				// 如果当前已经为显示状态，则隐藏起来
				menuWindow.dismiss();
				menu_display = false;
			}

			return false;
		}
		return false;
	}

	public void btnmainright(View v) {
		Intent intent = new Intent(mainXuanshang.this, MainTopRightDialog.class);
		Bundle bundle =new Bundle();
		bundle.putInt("Uid", Uid);
//		System.out.println(id);
		intent.putExtras(bundle);
		startActivity(intent);

		// Toast.makeText(getApplicationContext(), "点击了功能按钮",
		// Toast.LENGTH_LONG).show();
	}

	
	public void login_renwu(View v) {
		Intent intent = new Intent();
		Bundle bundle =new Bundle();
		bundle.putInt("Uid", Uid);
//		System.out.println(id);
		intent.putExtras(bundle);
		intent.setClass(mainXuanshang.this, showtask.class);
		startActivity(intent);
}
	public void wancheng(View v) {
		Intent intent = new Intent();
		intent.setClass(mainXuanshang.this,showreward.class);
		Bundle bundle =new Bundle();
		bundle.putInt("Uid", Uid);
		intent.putExtras(bundle);
		startActivity(intent);
}
}