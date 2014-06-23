package com.example.backup.backgroundtasks;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;

import com.example.backup.MainActivity;
import com.example.backup.R;
import com.example.backup.ads.AdScreen;
import com.example.backup.constants.*;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
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

	private Handler myHandler;
	private Runnable runnable,waitRunnable;
	private ActivityManager am;
	private PackageManager pm;
	private List<RunningTaskInfo> tasks;
	private HashSet<String> reset,app_ad,app_ad_off;
	private SharedPreferences sharedPreferences;
	
	/*
	 * THIS IS SO THAT AD DOESN'T POPUP AGAIN FOR SPLASH SCREEN APS
	 */

	private boolean wait_exec;
	
	
	/*
	 * TO CHECK IF APP IS RUNNING, OR SERVICE STARTED FROM BOOT BROADCASE LISTENER
	 */
	private boolean isMainRunning;
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

		initVariables();
		
		 int icon = R.drawable.ic_launcher;
		 long when = System.currentTimeMillis();
		 NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		 Intent intent=new Intent(getBaseContext(),MainActivity.class);
	     PendingIntent  pending=PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
	     Notification notification;
	        if (Build.VERSION.SDK_INT < 11) {
	            notification = new Notification(icon, "Title", when);
	            notification.setLatestEventInfo(
	                    getBaseContext(),
	                    "Title",
	                    "Text",
	                    pending);
	        } else {
	            notification = new Notification.Builder(getBaseContext())
	                    .setContentTitle("Title")
	                    .setContentText(
	                            "Text").setSmallIcon(R.drawable.ic_launcher)
	                    .setContentIntent(pending).setWhen(when).setAutoCancel(true)
	                    .build();
	        }
	     notification.flags |= Notification.FLAG_AUTO_CANCEL;
	     notification.defaults |= Notification.DEFAULT_SOUND;
	
		startForeground(22, notification);
		//Toast.makeText(this, "OnCreate", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
		//return Service.START_STICKY;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "onStart");	
	}
   
	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
	 
	initBackgroundAppCheck();	
		
	 /*
	  * LET THE SPLASH SCREEN APS BOOT UP, AVOID DISPLAYING AD AGAIN	
	  */
     wait_exec = false;
	 waitRunnable = new Runnable() {
	 	
		@Override
		public void run() {
			Bundle bundle  = intent.getExtras();
			if(bundle!=null) {
				if(!wait_exec) {
					wait_exec = true;
					if(bundle.containsKey("wait")) {
						myHandler.removeCallbacks(runnable);
					}
					myHandler.postDelayed(waitRunnable, interval);
				}
				else {
					if(bundle.containsKey("wait")) {
						 app_ad_off.addAll(app_ad);
						 app_ad.removeAll(app_ad_off);
				    	 myHandler.post(runnable);
					}
				}
			}
			else {
				myHandler.post(runnable);
			}
		}
	};	
	 myHandler.post(waitRunnable);
	
	 

	// We need to return if we want to handle this service explicitly. 
	return START_FLAG_REDELIVERY;
}
	
	private void initBackgroundAppCheck() {
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
	}
	

	
	private void initVariables() {
		app_ad = new HashSet<String>();	
		app_ad_off = new HashSet<String>();
		
		/*
		 * LOAD THE LIST FROM FILE IF CALLED FROM BOOT BROADCAST LISTENER
		 */
		
		app_ad.addAll( sharedPreferences.getStringSet(ACTIVATED_LIST, new HashSet<String>()) );	
		 
		if(app_ad.isEmpty()) {
			stopSelf();
		}
		Log.d(TAG,"OnStart:"+ app_ad.toString() + "");
		//Log.d(TAG,"app_ad(OnCreate): " + app_ad.toString());
		reset = new HashSet<String>();
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

