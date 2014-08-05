package com.example.backup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import org.apache.http.NameValuePair;

import com.example.backup.ads.AdLogic;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.backgroundtasks.PostStatsAsyncTask;
import com.example.backup.constants.Constants;
import com.example.backup.data.Advertisement;
import com.example.backup.data.Stats;
import com.fima.glowpadview.GlowPadView;
import com.fima.glowpadview.GlowPadView.OnTriggerListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

public class LockScreenActivity extends Activity implements Constants, OnTriggerListener {
	
    private Bitmap bitmap;
    private Advertisement advertisement;
    private GlowPadView mGlowPadView;
	Intent myIntent;
	String s_appearedAt,s_swipedAt;
	List<NameValuePair> s_postElements;
	AdLogic adL;
	String lastActiveApp;
	PackageManager pm;
	Intent activityIntent = null;
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	    super.onWindowFocusChanged(hasFocus);
	    Log.d("Focus debug", "Focus changed !");
		if(!hasFocus) {
		    Log.d("Focus debug", "Lost focus !");
	
		    Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		    sendBroadcast(closeDialog);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
	            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
	            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock);
   	     
		 /*
		 lastActiveApp = getIntent().getStringExtra("lastActiveApp");
		 pm = getPackageManager();
     	 if (lastActiveApp != null) {
     		activityIntent = new Intent();
     	    activityIntent.setClassName(lastActiveApp, getIntent().getStringExtra("lastActiveActivity"));
     	    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
     	 }
     	 */
		
   	     adL = AdLogic.getInstance(this);
   	     advertisement = adL.getAdvertisement();
	     bitmap = advertisement.getImage();
	     
	     s_appearedAt =  new SimpleDateFormat("HH:mm:ss-dd-MM-yyyy").format(Calendar.getInstance().getTime());
	     
	     //MyService.stopAds();
         
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
			
			Intent homeIntent= new Intent(Intent.ACTION_MAIN);
        	homeIntent.addCategory(Intent.CATEGORY_HOME);
        	homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(homeIntent);
			s_swipedAt = new SimpleDateFormat("HH:mm:ss-dd-MM-yyyy").format(Calendar.getInstance().getTime());
			Stats stats = new Stats();
			stats.setAdId(advertisement.getId());
			stats.setUserId(Integer.parseInt(getSharedPreferences(myPreferences, Context.MODE_PRIVATE).getString(USER_ID, "0")));
			stats.setType(0);
			stats.setCompanyId(advertisement.getCompanyId());
			stats.setAppearedAtTime(s_appearedAt);
			stats.setSwipedAtTime(s_swipedAt);
			PostStatsAsyncTask pTask = new PostStatsAsyncTask(stats, getBaseContext());
			pTask.execute();
        	finish();
			//Toast.makeText(this, "Google selected", Toast.LENGTH_SHORT).show();

			break;
		default:
			// Code should never reach here.
		}

	}
	
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
		bitmap.recycle();
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
	 
}
