package com.example.backup.backgroundtasks;


import java.util.HashSet;
import java.util.List;

import com.example.backup.ads.AdScreen;
import com.example.backup.constants.*;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/* 
 * RUN BACKGROUND SERVICE
 * TO CHECK RUNNING APPLICATIONS
 * IF ANY IS IN THE LIST OF AD APPS
 * POPUP A FULLSCREEN ACTIVITY (AD)
 */

public class MyService extends Service implements Constants{

	private static Handler myHandler;
	private static Runnable runnable;
	private static Runnable waitRunnable;
	private ActivityManager am;
	private PackageManager pm;
	private List<RunningTaskInfo> tasks;
	private static HashSet<String> app_ad_lock;
	public static String activeAppPackageName;
	private static SharedPreferences sharedPreferences;
	static int s_appLock;
	String app;
	String oldApp="";
	/*
	 * THIS IS SO THAT AD DOESN'T POPUP AGAIN FOR SPLASH SCREEN APS
	 */
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		am = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
		myHandler = new Handler();
		pm = getPackageManager();
		
		/*
		 * TO CHECK FROM BOOT UP, BECAUSE MAIN ACTIVITY WON'T BE LOADED
		 * SO, IF THE TOGGLE WAS OFF, THEN DON'T START SERVICE
		 */
		sharedPreferences = getSharedPreferences(myPreferences,	MODE_PRIVATE);   
		
		//DataBaseHelper db = new DataBaseHelper(this);
		//advertisements = db.getAllAds();
		
		initVariables();
	
		startForeground(22, new Notification());
		Log.d(TAG, "onCreate");
		//return Service.START_STICKY;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "onStart");	
	}
   
	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) { 
		initRunnables();	
		if (getSharedPreferences(myPreferences, Context.MODE_PRIVATE).getBoolean(APPLOCK_ACTIVATED, true)) {
			startAds();
		} 
		return START_FLAG_REDELIVERY;
	}
	
	private void initRunnables() {
		/*
		 * GET FOREGROUND APP, IF IT IS PRESENT IN
		 * APP_LOCK LIST, THEN DISPLAY AD. TURN OFF ADS TILL THE APP IS NOT 
		 * SENT TO BACKGROUND
		 */
		runnable = new Runnable() {
			 @Override
			 public void run() {
				 
				 	List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1); 

		            Log.d("topActivity", "CURRENT Activity ::"
		                    + taskInfo.get(0).topActivity.getClassName());

		            ComponentName componentInfo = taskInfo.get(0).topActivity;
		            app = componentInfo.getPackageName();
				 	 //Log.d(TAG,app);
				 	 
					//app is running and is not in background
					if (app.equals(PACKAGE_NAME)==false && app.equals(oldApp)==false) {
						if (app_ad_lock.contains(app)) {
							//To prevent ad popping up until app opened again
							//Log.d(TAG,app + ":" + tasks.get(i).numRunning);
							oldApp = "" + app;
							Intent intent = new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION );
							if(s_appLock==1 && app_ad_lock.contains(app)) {
								intent.putExtra("app_lock", true);
							}
							else {
								intent.putExtra("app_lock", false);
							}
							intent.putExtra("name",app);
							intent.setClass(MyService.this, AdScreen.class);
							startActivity(intent);
						}
						else if (app.equals(PACKAGE_NAME) == false){
							oldApp = "";
						}
					}
					
				 	myHandler.postDelayed(runnable, 225);
			 }
		 };
	}
	
	/*
	 * LOAD THE LIST FROM FILE IF CALLED FROM BOOT BROADCAST LISTENER
	 */
	public static void initVariables() {
		s_appLock  = sharedPreferences.getInt(APP_LOCK_TYPE, 0);
		app_ad_lock = new HashSet<String>();
	    app_ad_lock.addAll( sharedPreferences.getStringSet(LOCKED_LIST, new HashSet<String>()) );
	    app_ad_lock.add("com.android.packageinstaller");
	}
	
	public static void delayAds() {
		 myHandler.post(waitRunnable);
	}
	
	public static void stopAds() {
		 if(myHandler!=null)
			 myHandler.removeCallbacks(runnable);
	}
	
	public static void startAds() {
		if(myHandler!=null)
			myHandler.post(runnable);
	}
	
	public static void enableLock() {
		s_appLock = 1;
	}
	
	public static void disableLock() {
		s_appLock = 0;
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "MyService onDestroy");
		myHandler.removeCallbacks(runnable);
		//myHandler.removeCallbacks(waitRunnable);
		stopForeground(true);
		super.onDestroy();
	}

	//Your inner thread class is here to getting response from Activity and processing them
	
}

