package com.example.backup.backgroundtasks;

import android.content.BroadcastReceiver;

import com.example.backup.constants.*;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver implements Constants {

    public static boolean wasScreenOn = true;
	
	@Override
	public void onReceive(Context context, Intent intent) {
					
	            if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		            {
		     
	        			  context.startService(new Intent(context,FakeLockScreenService.class));
	            	
		            	  Log.d(TAG,"Broadcast Received");
						  Intent startServiceIntent = new Intent(context, MyService.class);
				          context.startService(startServiceIntent);
				          
		            }

			
	}

}
