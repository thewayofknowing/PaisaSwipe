package com.example.backup.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.backup.LockScreenActivity;
import com.example.backup.constants.Constants;

public class LockScreenReceiver extends BroadcastReceiver implements Constants  {
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(!MyPhoneStateReceiver.s_isOnCall) {
	        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
	        	Intent intent11 = new Intent(context,LockScreenActivity.class);
	        	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP );
	        	context.startActivity(intent11);
	        }
	        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
		    	Intent intent11 = new Intent(context,LockScreenActivity.class);
		    	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP );
		    	context.startActivity(intent11);
	        }
		}
    }

}
