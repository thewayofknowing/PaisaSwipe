package com.example.backup.backgroundtasks;

import com.example.backup.constants.Constants;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FakeLockScreenService extends IntentService implements Constants {
	
	public FakeLockScreenService() {
	    super("FakeService");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG,"Fake OnCreate");
		Notification notification = new Notification();
		startForeground(13, notification);
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startService(new Intent(getBaseContext(),LockScreenService.class));
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		stopForeground(true);
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
