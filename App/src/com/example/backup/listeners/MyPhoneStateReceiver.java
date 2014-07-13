package com.example.backup.listeners;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


public class MyPhoneStateReceiver extends BroadcastReceiver {
	
	public static Boolean s_isOnCall = false;
	TelephonyManager s_Tmanager;
	static CallStateListener listener = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (listener == null) {
			listener = new CallStateListener();
			s_Tmanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			s_Tmanager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		}
		
	}
	
	public void onDestroy() {
		s_Tmanager.listen(null,PhoneStateListener.LISTEN_NONE);
	}
	
	public class CallStateListener extends PhoneStateListener {

		Boolean outGoing = false;
		
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if (outGoing || s_isOnCall) {
					Log.d("TAG","IDLE");
					s_isOnCall = false;
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				if (!outGoing) {
					Log.d("TAG","OFFHOOK");
					outGoing = true;
					s_isOnCall = true;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				if(!s_isOnCall) {
					Log.d("TAG","RINGING");
					s_isOnCall = true;
				}
				break;
			}
		}
	}

}