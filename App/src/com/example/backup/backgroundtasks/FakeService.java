package com.example.backup.backgroundtasks;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FakeService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		startForeground(22, new Notification());
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		stopForeground(true);
		super.onDestroy();
	}

}
