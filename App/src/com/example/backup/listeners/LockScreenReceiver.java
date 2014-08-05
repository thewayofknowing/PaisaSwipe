package com.example.backup.listeners;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.backup.LockScreenActivity;
import com.example.backup.constants.Constants;

public class LockScreenReceiver extends BroadcastReceiver implements Constants  {
	 
	static String lastActiveApp,lastActiveActivity;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(!MyPhoneStateReceiver.s_isOnCall) {
	        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
	        	Intent intent11 = new Intent(context,LockScreenActivity.class);
	        	ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
	            List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1); 

	            Log.d("topActivity", "CURRENT Activity ::"
	                    + taskInfo.get(0).topActivity.getClassName());

	            ComponentName componentInfo = taskInfo.get(0).topActivity;
	            lastActiveApp = componentInfo.getPackageName();
	            lastActiveActivity = componentInfo.getClassName();
	    	 	intent11.putExtra("lastActiveApp", lastActiveApp);
	    	 	intent11.putExtra("lastActiveActivity", lastActiveActivity);
	        	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION );
	        	context.startActivity(intent11);
	        }
	        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
		    	Intent intent11 = new Intent(context,LockScreenActivity.class);
		    	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION );
		    	intent11.putExtra("lastActiveApp", lastActiveApp);
	    	 	intent11.putExtra("lastActiveActivity", lastActiveActivity);
		    	context.startActivity(intent11);
	        }
		}
    }

}
