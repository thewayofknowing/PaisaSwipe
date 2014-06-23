package com.example.backup.backgroundtasks;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.backup.LockScreenActivity;
import com.example.backup.constants.Constants;

public class LockScreenReceiver extends BroadcastReceiver implements Constants  {
	 KeyguardManager.KeyguardLock k1;
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
        	Intent myService = new Intent(context,MyService.class);
        	context.stopService(myService);
        	//Log.d(TAG,"Screen OFF");
  
        	Intent intent11 = new Intent(context,LockScreenActivity.class);
        	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        	context.startActivity(intent11);
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        	//Log.d(TAG,"Screen ON");
        	Intent intent11 = new Intent(context,LockScreenActivity.class);
        	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        	context.startActivity(intent11);
        }

    }

}
