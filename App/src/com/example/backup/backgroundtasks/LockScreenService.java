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
import android.os.IBinder;
import android.util.Log;

public class LockScreenService extends Service implements Constants{
	 BroadcastReceiver mReceiver;
	 KeyguardManager.KeyguardLock k1;

	 @Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void onCreate() {
		Log.d(TAG,"Lock Service Oncreate");
		
	    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	    filter.addAction(Intent.ACTION_SCREEN_OFF);
	    
	    mReceiver = new LockScreenReceiver();
	    registerReceiver(mReceiver, filter);
	    
	    KeyguardManager km =(KeyguardManager) getSystemService(KEYGUARD_SERVICE);
	    k1= km.newKeyguardLock("IN");
	    k1.disableKeyguard();
	    
	    /*
	    Notification note=new Notification(R.drawable.on,
                "Can you hear the music?",
                System.currentTimeMillis());
		Intent i=new Intent(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
		Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi=PendingIntent.getActivity(this, 0,
		                        i, 0);
		note.setLatestEventInfo(this, "Fake Player",
		    "Now Playing: \"Ummmm, Nothing\"",
		    pi);
		note.flags|=Notification.FLAG_NO_CLEAR;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(13,note);
		*/
		startForeground(22, new Notification());
	
	    super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
	
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_REDELIVER_INTENT | START_FLAG_REDELIVERY;
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		//stopForeground(true);
		Log.d(TAG,"LockScreen OnDestroy");
		super.onDestroy();
	}
}