package com.example.backup.listeners;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.backup.LockScreenActivity;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.Constants;

public class LockScreenReceiver extends BroadcastReceiver implements Constants  {
	 MyService s_myService;

	 ServiceConnection mConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
		        s_myService = ((MyService.LocalBinder)service).getService();
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
		        s_myService = null;
			}
		};
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(!MyPhoneStateReceiver.s_isOnCall) {
	        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
	        	
	        	Intent intent11 = new Intent(context,LockScreenActivity.class);
	        	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
	        	context.startActivity(intent11);
	        }
	        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
	        	 
		    	Intent intent11 = new Intent(context,LockScreenActivity.class);
		    	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
		    	context.startActivity(intent11);
	        }
		}
    }

}
