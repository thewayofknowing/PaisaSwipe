package com.example.backup.backgroundtasks;


import com.example.backup.MainActivity;
import com.example.backup.R;
import com.example.backup.constants.Constants;
import com.example.backup.listeners.LockScreenReceiver;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LockScreenService extends Service implements Constants{
	 BroadcastReceiver mReceiver;
	 KeyguardManager.KeyguardLock k1;
	 IntentFilter filter;
	 
	 @Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	 /**
	     * Class for clients to access.  Because we know this service always
	     * runs in the same process as its clients, we don't need to deal with
	     * IPC.
	     */
	    public class LocalBinder extends Binder {
	        public LockScreenService getService() {
	            return LockScreenService.this;
	        }
	    }

	@Override
	public void onCreate() {
		Log.d(TAG,"Lock Service Oncreate");
		
	    filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	    filter.addAction(Intent.ACTION_SCREEN_OFF);
	    
	    mReceiver = new LockScreenReceiver();
	    registerReceiver(mReceiver, filter);
	    
	    KeyguardManager km =(KeyguardManager) getSystemService(KEYGUARD_SERVICE);
	    k1= km.newKeyguardLock("IN");
	    k1.disableKeyguard();
	    
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
	
	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		stopForeground(true);
		Log.d(TAG,"LockScreen OnDestroy");
		super.onDestroy();
	}
}