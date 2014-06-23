package com.example.backup.ads;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.example.backup.R;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.*;
import com.fima.glowpadview.GlowPadView;
import com.fima.glowpadview.GlowPadView.OnTriggerListener;

import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class AdScreen extends Activity implements Constants, OnTriggerListener {
	
	static Activity activity = null;
	
    private GestureDetectorCompat mDetector; 
    private static String packageName;
    private Bitmap bitmap;
    private ActivityManager am;
	private PackageManager pm;
	private List<RunningTaskInfo> tasks;
	private static Intent myService;

    private GlowPadView mGlowPadView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad);
		
		activity = this;
		
		myService = new Intent(getBaseContext(),MyService.class);
		stopService(myService);
		
        packageName = getIntent().getExtras().getString("name");
        ImageView iv = (ImageView) findViewById(R.id.imageView1);

        AdLogic adL = new AdLogic(this);
	    bitmap = adL.getImageUri(this);
       
	    mGlowPadView = (GlowPadView) findViewById(R.id.glow_pad_view);
        mGlowPadView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        
		 mGlowPadView.setOnTriggerListener(this);
		
		// uncomment this to make sure the glowpad doesn't vibrate on touch
		// mGlowPadView.setVibrateEnabled(false);
		
		// uncomment this to hide targets
		 mGlowPadView.setShowTargetsOnIdle(true);
	}
	
	@Override
    public void onBackPressed() {
        return;
    }
	
	public static Activity getInstance(){
		   return  activity;
		 }

	    private void checkForAppKill() throws NameNotFoundException {
			 am = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
	    	 tasks = am.getRunningTasks(Integer.MAX_VALUE);
		 	 Boolean flag = false;
	    	 for (int i=0;i<tasks.size();i++) { 
		 			  if (packageName.equals(tasks.get(i).baseActivity.getPackageName())) {
		 				  flag = true;
		 				  break;
		 			  }
	    	 }
	    	 /*
	    	  * Apps which shut down on ad launch
	    	  */
	    	  
	    	 if(!flag) {
	 			  Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            	  LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            	  startActivity(LaunchIntent);
	    	 }
    		  myService.putExtra("wait","1");
    		  startService(myService);
	    	  
	    }
	
       @Override
    protected void onPause() {
    	super.onPause();
    }

    @Override
    protected void onStop() {
    	Log.d(TAG,"Ad onStop");
    	stopService(myService);
   		myService.putExtra("wait","1");
    	startService(myService);
       	finish();
    	super.onStop();
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
	public void onGrabbedStateChange(View v, int handle) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onFinishFinalAnimation() {
		// TODO Auto-generated method stub
		
	}
	
}
