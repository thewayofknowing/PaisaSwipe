package com.example.backup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.example.backup.Views.TitleBar;
import com.example.backup.backgroundtasks.FakeLockScreenService;
import com.example.backup.backgroundtasks.LockScreenService;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.*;
import com.example.backup.postinfo.*;

public class MainActivity extends Activity implements Constants {
	
	
	/*
	 * @param: app_ad- set of activated ad strings (If an app is activated, it is present in
	 * 													this set, absent otherwise)
	 * @param: app_ad_off - set of deactivated ad strings (so that you don't keep 
	 * 							displaying an ad after an app opens once
	 * @param: app_ad_list- list of user activated ads (mostly the same as app_ad, 
	 * 													 a few exceptional occasions)
	 * @param:toggleservice- toggle button to activate ads on or off
	 * @param: intent- Intent for the background service
	 */
	
	public static SharedPreferences sharedPreferences;
	private ListView list;
	private CustomListAdapter adapter;
	private List<Drawable> icons;
	private List<String> appLabels;
	private PackageManager pm;
	
	public static HashSet<String> app_ad;
	public static HashSet<String> app_ad_off;
	public static HashSet<String> app_ad_list;
	
	private static Intent intent;
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	

	private ImageView leftNavButton = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences(myPreferences,	MODE_PRIVATE);   
				
		app_ad_list = new HashSet<String>();
		app_ad = new HashSet<String>();
		
		initAppLockList();	
		
		setContentView(R.layout.activity_main);
		initTitle();	
		sharedPreferences.edit().putBoolean(STATUS, true).commit();
		initDrawer();
		
		//Start LockScreen Service
		stopService(new Intent(getBaseContext(),LockScreenService.class));
		startService(new Intent(getBaseContext(),FakeLockScreenService.class));

		
		//INITIALIZE VARIABLES
		pm = getPackageManager();
		List<PackageInfo> apps = pm.getInstalledPackages(0);
	    icons = new ArrayList<Drawable>();
	    appLabels = new ArrayList<String>();
	    intent = new Intent(getBaseContext(),MyService.class);
	    stopService(intent);
	    
	   //POST INFO
	    postInfo(MainActivity.this);
	    
	    /*
	    * GET A LIST OF INSTALLED APPS
	    */
	    if (apps != null) {
			for (PackageInfo info: apps) {
				if((info.applicationInfo.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) == 0) {
					if(!(info.applicationInfo.packageName.equals(getPackageName())) && pm.getApplicationLabel(info.applicationInfo).toString().indexOf("com.")<0) {	
						appLabels.add("" + pm.getApplicationLabel(info.applicationInfo));
						icons.add(pm.getApplicationIcon(info.applicationInfo));
					}
				}
			}
		}
		else {
			appLabels.add("No Apps Installed");
		}
		
	    //PREPARE THE LIST OF APPS (CUSTOM ADAPTER)
		prepareList();
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		
		//stopService(intent);
		/*
		 * LOADING THE LIST OF ACTIVATED ADS
		 */
		if(sharedPreferences.contains(ACTIVATED_LIST)){
			 app_ad_list.addAll( sharedPreferences.getStringSet(ACTIVATED_LIST, new HashSet<String>() ));	
			 app_ad.addAll(app_ad_list);
	    }
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		
		
		//startService(intent);
		/*
		 * SAVE THE LIST OF ACTIVATED ADS 
		 */
		sharedPreferences.edit().putStringSet(ACTIVATED_LIST, app_ad_list).commit();
		finish();
		super.onPause();
	}
	
	
	/*
	 * READING THE LIST OF ACTIVATED APPS AT THE START
	 */
	private void initAppLockList() {
		if(sharedPreferences.contains(ACTIVATED_LIST)){
			app_ad_list.addAll( sharedPreferences.getStringSet(ACTIVATED_LIST, new HashSet<String>() ));	
		    app_ad.addAll(app_ad_list);
	    }
	}
	
	private void initTitle() {
		RelativeLayout title = (RelativeLayout) findViewById(R.id.titleBar);
		TitleBar tb = new TitleBar(this,title);
		leftNavButton = tb.getLeftOptionsImgBtn();
	}
	
	private void initDrawer() {
		 mDrawerList = (ListView) findViewById(R.id.drawer_list);
		
		 /*
		 mList = new ArrayList<HashMap<String,String>>();
		 for(int i=0;i<4;i++){
			 HashMap<String, String> hm = new HashMap<String,String>();
			 hm.put(CONTENT, mContents[i]);
			 hm.put(ICONZ, mIcon[i] + "" );
			 mList.add(hm);
		 }
		 
		 String[] from = { ICONZ, CONTENT};
		 int[] to = { R.id.icon, R.id.content};
		 mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from, to);
		 */
	     NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(this);

		 mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		 mDrawerList.setAdapter(l_leftNavBarListAdapter);
		 
		 mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Closing the drawer
				 mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
		});
		 leftNavButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mDrawerLayout.openDrawer(Gravity.LEFT);
					mDrawerList.bringToFront();
					mDrawerList.requestLayout();
				}
			});
	}
	
	
	/*
	 * PREPARE THE LIST FROM CUSTOM ADAPTER
	 */
	private void prepareList() {
		adapter = new CustomListAdapter(this, icons, appLabels);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);	
	}
	
	/*
	 * ACTIVATED BUTTON TOGGLE CHANGE HANDLER
	 *
	private void setToggle() {
		  // toggleservice = (ToggleButton) findViewById(R.id.toggleButton1);
		   if(sharedPreferences.contains(STATUS)) {
			   if(sharedPreferences.getBoolean(STATUS, false)) {
				   toggleservice.setChecked(true);
				   restartService();
			   }
		   
		   else {
			   sharedPreferences.edit().putBoolean(STATUS, false).commit();
		   }
		   
		   //TOGGLE ON/OFF APP ADS
		   toggleservice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		   
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					sharedPreferences.edit().putBoolean(STATUS, true).commit();
					startService(intent);
					Toast.makeText(getBaseContext(), "App Ads Activated", Toast.LENGTH_SHORT).show();
					//Intent i = new Intent(getBaseContext(), HomeActivity.class);
					//startActivity(i);
				}
				else {
					sharedPreferences.edit().putBoolean(STATUS, false).commit();
					stopService(intent);
					Toast.makeText(getBaseContext(), "App Ads DeActivated", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	*/
	/*
	 * Check connectivity of Internet
	 */
	public static boolean isConnented(Activity context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info!=null) {
			if(info.isConnected()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		//restartService();
		super.onDestroy();
	}
	
	private void postInfo(Activity context) {
		//if(!(sharedPreferences.contains("DataPosted") && sharedPreferences.getBoolean("DataPosted", false))) {
			if(isConnented(context)) {
				PostData.post(context);
			}
		//}
	}

}
