package com.example.backup;

import java.util.List;

import com.example.backup.ads.AdLogic;
import com.example.backup.ads.AdScreen;
import com.example.backup.backgroundtasks.LockScreenService;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.Constants;
import com.fima.glowpadview.GlowPadView;
import com.fima.glowpadview.GlowPadView.OnTriggerListener;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class LockScreenActivity extends Activity implements Constants, OnTriggerListener {
	
	private GestureDetectorCompat mDetector; 
    private Bitmap bitmap;
    private GlowPadView mGlowPadView;
    private Intent myService;
	KeyguardManager.KeyguardLock k1;
	Intent myIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
   	    setContentView(R.layout.lock);
   	    KeyguardManager km =(KeyguardManager) getSystemService(KEYGUARD_SERVICE);
	    k1= km.newKeyguardLock("IN");
	    k1.disableKeyguard();
	   	
	    myIntent = getIntent();
	    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
   	     
   	     AdLogic adL = new AdLogic(this);
	     bitmap = adL.getImageUri(this);
	     
	     myService = new Intent(getBaseContext(),MyService.class);
	     stopService(myService);
	     
	     StateListener phoneStateListener = new StateListener();
         TelephonyManager telephonyManager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
         telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
         
         mGlowPadView = (GlowPadView) findViewById(R.id.glow_pad_view);
         mGlowPadView.setBackgroundDrawable(new BitmapDrawable(bitmap));
         
 		 mGlowPadView.setOnTriggerListener(this);
 		
 		// uncomment this to make sure the glowpad doesn't vibrate on touch
 		// mGlowPadView.setVibrateEnabled(false);
 		
 		// uncomment this to hide targets
 		 mGlowPadView.setShowTargetsOnIdle(true);
	}
	
	@Override
	public void onGrabbed(View v, int handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReleased(View v, int handle) {
		mGlowPadView.ping();

	}

	@Override
	public void onTrigger(View v, int target) {
		final int resId = mGlowPadView.getResourceIdForTarget(target);
		switch (resId) {
		case R.drawable.ic_item_camera:
			//Toast.makeText(this, "Camera selected", Toast.LENGTH_SHORT).show();
			break;

		case R.drawable.ic_item_google:
        	finish();
			//Toast.makeText(this, "Google selected", Toast.LENGTH_SHORT).show();

			break;
		default:
			// Code should never reach here.
		}

	}
	
	@Override
	protected void onPause() {
		myService.putExtra("wait","1");
        startService(myService);
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onGrabbedStateChange(View v, int handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinishFinalAnimation() {
		// TODO Auto-generated method stub

	}
	
	class StateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                	//finish();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                	finish();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    };
	
	
	 @Override
	    public void onBackPressed() {
	        return;
	    }
	 
	 @Override
	  public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

	    	if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)||(keyCode == KeyEvent.KEYCODE_VOLUME_UP)||(keyCode == KeyEvent.KEYCODE_CAMERA)) {
	    	    Log.d(TAG,"Power");
	    		return true; 
	    	}
	    	else if ((keyCode == KeyEvent.KEYCODE_POWER)) {
	    		return false;
	    	}
	       if((keyCode == KeyEvent.KEYCODE_HOME)){
	    	   finish();
	    	   return false;
	        }
	       
		return false;
	    }
	 
	 public boolean dispatchKeyEvent(KeyEvent event) {
		 	if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
		 		return false;
		 	}
		 	else if ((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)) {
	    	    return false;
	    	}
	    	 if((event.getKeyCode() == KeyEvent.KEYCODE_HOME)){
	      	   	 finish();
	    		 return false;
	         }
	    return false;
	    }
	 /*
	 private boolean isAdDisplayed() {
			 boolean isAdRunning  = false;
			 ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
			 List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
			 String Main = getApplication().getPackageName() + ".ads.AdScreen";
			 for(ActivityManager.RunningTaskInfo r : taskList) {
				 if(r.topActivity.getClassName().equals(Main)) {
					 isAdRunning = true;
					 break;
				 }
			 }
			 return isAdRunning;
	 }
	 */
}
