package com.example.backup.backgroundtasks;


import java.util.HashSet;
import java.util.List;

import com.example.backup.ads.AdScreen;
import com.example.backup.constants.*;
import com.example.backup.data.Advertisement;
import com.example.backup.db.DataBaseHelper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
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
	private static HashSet<String> reset;
	private static HashSet<String> app_ad;
	private static HashSet<String> app_ad_off;
	private static SharedPreferences sharedPreferences;
	private List<Advertisement> advertisements;
	static MyService s_myService;
	/*
	 * THIS IS SO THAT AD DOESN'T POPUP AGAIN FOR SPLASH SCREEN APS
	 */

	private static boolean wait_exec;
	
	/**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }
	
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
		if (!(getSharedPreferences(myPreferences, MODE_PRIVATE).contains(STATUS) && getSharedPreferences(myPreferences, MODE_PRIVATE).getBoolean(STATUS, false))){
			stopSelf();
		}
		
		DataBaseHelper db = new DataBaseHelper(this);
		advertisements = db.getAllAds();
		
		s_myService = MyService.this;
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
	myHandler.post(runnable);
	 /*
	  * LET THE SPLASH SCREEN APS BOOT UP, AVOID DISPLAYING AD AGAIN	
	  */
	

	// We need to return if we want to handle this service explicitly. 
	return START_FLAG_REDELIVERY;
}
	
	private void initRunnables() {
		/*
		 * CHECK THE LIST OF ACTIVATED ADS IN LIST OF RUNNING APPS
		 * IF PRESENT, START THE "AdScreen" ACTIVITY TO DISPLAY THE AD
		 * ADD THIS NAME TO app_ad_off, REMOVE IT FROM app_ad
		 * TILL THIS APP IS CLOSED, THE NAME REMAINS IN app_ad_off
		 * WHEN IT IS CLOSED, NAME IS ADDED BACK TO app_ad_on
		 */
		//Log.d(TAG,"off:" + app_ad_off.toString());
		//Log.d(TAG,"on:" + app_ad.toString());
		 runnable = new Runnable() {
			 @Override
			 public void run() {
				 
				 	 android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
				 	 
				 	 String app = "";
				 	 tasks = am.getRunningTasks(Integer.MAX_VALUE);
				 	 reset.addAll(app_ad_off);
				 	 
				 	 for (int i=0;i<tasks.size();i++) { 
				 		 try {
				 			app = "" +  pm.getApplicationLabel(pm.getApplicationInfo(tasks.get(i).baseActivity.getPackageName(),0));
				 			
				 			//app is stopped or pushed to background
				 			if(reset.contains(app) && tasks.get(i).numRunning>0) {
				 				Log.d(TAG,app + "-" + tasks.get(i).describeContents());
				 				reset.remove(app);
				 			}
				 			
				 			//app is running and is not in background
				 			if (app_ad.contains(app) && tasks.get(i).numRunning>0) {
				 				//To prevent ad popping up until app opened again
				 				//Log.d(TAG,app + ":" + tasks.get(i).numRunning);
				 				app_ad_off.add(app);
				 				app_ad.remove(app);
		
				 				Intent intent = new Intent();
				 				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				 				intent.putExtra("name",tasks.get(i).baseActivity.getPackageName() );
				 				intent.setClass(MyService.this, AdScreen.class);
				 				startActivity(intent);
				 			}
				 		 } catch (NameNotFoundException e) {
							e.printStackTrace();
						}
				 	 }
				 	 
				 	 //APP CLOSED: PUT IT BACK ON DISPLAY LIST
				 	 
				 	 for(String application: reset) {
				 		 Log.d(TAG,"Reset out:" + application);
				 		 app_ad_off.remove(application);
				 		 app_ad.add(application);
				 	 } 	 
				 	 reset.clear();
				 	 
				 	myHandler.postDelayed(runnable, 529);
			 }
		 };
		 
		 waitRunnable = new Runnable() {
			 	
				@Override
				public void run() {
						if(!wait_exec) {
							wait_exec = true;
							myHandler.removeCallbacks(runnable);
							myHandler.postDelayed(waitRunnable, interval);
						}
						else {
							 app_ad_off.addAll(app_ad);
							 app_ad.removeAll(app_ad_off);
					    	 myHandler.post(runnable);						
						}
				}
			};
	}
	

	public static void initVariables() {
		app_ad = new HashSet<String>();	
		app_ad_off = new HashSet<String>();
		
		/*
		 * LOAD THE LIST FROM FILE IF CALLED FROM BOOT BROADCAST LISTENER
		 */
		
		app_ad.addAll( sharedPreferences.getStringSet(ACTIVATED_LIST, new HashSet<String>()) );	
	
		Log.d(TAG,"OnStart:"+ app_ad.toString() + "");
		//Log.d(TAG,"app_ad(OnCreate): " + app_ad.toString());
		reset = new HashSet<String>();
	}
	
	public static void delayAds() {
		 wait_exec = false;	
		 myHandler.post(waitRunnable);
	}
	
	public static void stopAds() {
		 myHandler.removeCallbacks(runnable);
	}
	
	public static void startAds() {
		 myHandler.post(runnable);
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "MyService onDestroy");
		myHandler.removeCallbacks(runnable);
		myHandler.removeCallbacks(waitRunnable);
		stopForeground(true);
		super.onDestroy();
	}

	//Your inner thread class is here to getting response from Activity and processing them
	
}

