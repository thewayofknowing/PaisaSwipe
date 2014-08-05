package com.example.backup.backgroundtasks;


import com.example.backup.ads.AdLogic;
import com.example.backup.constants.Constants;
import com.example.backup.listeners.LockScreenReceiver;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class LockScreenService extends Service implements Constants{
	 static BroadcastReceiver mReceiver;
	 static KeyguardManager.KeyguardLock k1;
	 static IntentFilter filter;
	 private static Context context;
	 
	 @Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG,"Lock Service Oncreate");
		context = getBaseContext();
		
	    filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	    filter.addAction(Intent.ACTION_SCREEN_OFF);
	    
	    mReceiver = new LockScreenReceiver();
	    KeyguardManager km =(KeyguardManager) getSystemService(KEYGUARD_SERVICE);
	    k1= km.newKeyguardLock("IN");
	    
	    if(getSharedPreferences(myPreferences, Context.MODE_PRIVATE).getBoolean(SCREENLOCK_ACTIVATED, true)) {
		    enableLock();
	    }
		AdLogic.getInstance(getApplicationContext());
		startForeground(22, new Notification());
	
	    super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_REDELIVER_INTENT | START_FLAG_REDELIVERY;
	}
	
	public static void disableLock() {
		context.unregisterReceiver(mReceiver);
		k1.reenableKeyguard();
	}
	
	public static void enableLock() {
		context.registerReceiver(mReceiver, filter);
		k1.disableKeyguard();
	}
	
	@Override
	public void onDestroy() {
	    if(getSharedPreferences(myPreferences, Context.MODE_PRIVATE).getBoolean(SCREENLOCK_ACTIVATED, true)) {
	    	disableLock();
	    }
	    stopForeground(true);
		Log.d(TAG,"LockScreen OnDestroy");
		super.onDestroy();
	}
}