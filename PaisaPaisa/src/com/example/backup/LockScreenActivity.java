package com.example.backup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import org.apache.http.NameValuePair;

import com.example.backup.ads.AdLogic;
import com.example.backup.ads.AdScreen;
import com.example.backup.backgroundtasks.LockScreenService;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.backgroundtasks.PostStatsAsyncTask;
import com.example.backup.constants.Constants;
import com.example.backup.data.Stats;
import com.fima.glowpadview.GlowPadView;
import com.fima.glowpadview.GlowPadView.OnTriggerListener;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
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
	MyService s_myService;
	Calendar calendar;
	String s_appearedAt,s_swipedAt;
	List<NameValuePair> s_postElements;
	Stack<String> s_activityStack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setType(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		//this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG|WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		setContentView(R.layout.lock);
	   	
	    myIntent = getIntent();
	    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
   	     
   	     AdLogic adL = new AdLogic(this);
	     bitmap = adL.getImageUri(this);
	     
	     calendar = Calendar.getInstance();
	     s_appearedAt =  new SimpleDateFormat("HH:mm:ss-dd-MM-yyyy").format(calendar.getTime());
	     //myService = new Intent(getBaseContext(),MyService.class);
	     //stopService(myService);
	     
	     Intent mIntent = new Intent(this, MyService.class);
	     bindService(mIntent, mConnection, BIND_AUTO_CREATE);
	     s_myService.stopAds();
	     
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
 		 
 		 s_postElements = new ArrayList<NameValuePair>();
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
			
			s_swipedAt = new SimpleDateFormat("HH:mm:ss-dd-MM-yyyy").format(calendar.getTime());
			/*
			Stats stats = new Stats();
			stats.setAdId(id);
			stats.setUserId(userId);
			stats.setCompanyId(companyId);
			stats.setAppearedAtTime(s_appearedAt);
			stats.setSwipedAtTime(s_swipedAt);
			PostStatsAsyncTask pTask = new PostStatsAsyncTask(stats, getBaseContext());
			pTask.execute();
			*/
        	finish();
			//Toast.makeText(this, "Google selected", Toast.LENGTH_SHORT).show();

			break;
		default:
			// Code should never reach here.
		}

	}
	
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
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		s_myService.delayAds();
		unbindService(mConnection);
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
	    	   //Toast.makeText(getBaseContext(), "Home", Toast.LENGTH_SHORT).show();
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
	    		 //Toast.makeText(getBaseContext(), "Home", Toast.LENGTH_SHORT).show();
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
