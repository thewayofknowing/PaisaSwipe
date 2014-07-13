package com.example.backup.ads;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;

import com.example.backup.R;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.*;
import com.fima.glowpadview.GlowPadView;
import com.fima.glowpadview.GlowPadView.OnTriggerListener;
import com.haibison.android.lockpattern.LockPatternActivity;

import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

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

	MyService s_myService;
	
	private static final int REQ_CREATE_PATTERN = 1;
	private static final int REQ_ENTER_PATTERN = 2;
	Intent intent;
	char[] savedPattern = {'1','2','3','6'};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad);
		
		activity = this;
		
		//myService = new Intent(getBaseContext(),MyService.class);
		//stopService(myService);
		
		 Intent mIntent = new Intent(this, MyService.class);
	     bindService(mIntent, mConnection, BIND_AUTO_CREATE);
	     s_myService.stopAds();
	     
	     
		
        packageName = getIntent().getExtras().getString("name");
        ImageView iv = (ImageView) findViewById(R.id.imageView1);

        AdLogic adL = new AdLogic(this);
	    bitmap = adL.getImageUri(this);
       
	    intent = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null,
		        getApplicationContext(), LockPatternActivity.class);
		startActivityForResult(intent, REQ_ENTER_PATTERN);
	    
	    //mGlowPadView = (GlowPadView) findViewById(R.id.glow_pad_view);
        //mGlowPadView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        
		 //mGlowPadView.setOnTriggerListener(this);
		
		// uncomment this to make sure the glowpad doesn't vibrate on touch
		// mGlowPadView.setVibrateEnabled(false);
		
		// uncomment this to hide targets
		 //mGlowPadView.setShowTargetsOnIdle(true);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQ_CREATE_PATTERN: {
				if (resultCode == RESULT_OK) {
					char[] pattern = data
							.getCharArrayExtra(LockPatternActivity.EXTRA_PATTERN);
					Toast.makeText(getBaseContext(), pattern.toString() , Toast.LENGTH_LONG).show();	
					savedPattern = pattern;
				}
				break;
			}// REQ_CREATE_PATTERN
			 case REQ_ENTER_PATTERN: {
			        /*
			         * NOTE that there are 4 possible result codes!!!
			         */
			        switch (resultCode) {
			        case RESULT_OK:
			            // The user passed
			        	Toast.makeText(getBaseContext(), "Passed", Toast.LENGTH_LONG).show();
			            break;
			        case RESULT_CANCELED:
			            // The user cancelled the task
			            break;
			        case LockPatternActivity.RESULT_FAILED:
			        	Toast.makeText(getBaseContext(), "Failed!!", Toast.LENGTH_LONG).show();
			            // The user failed to enter the pattern
			            break;
			        case LockPatternActivity.RESULT_FORGOT_PATTERN:
			            // The user forgot the pattern and invoked your recovery Activity.
			            break;
			        }
			        int retryCount = data.getIntExtra(
			                LockPatternActivity.EXTRA_RETRY_COUNT, 0);
	
			        break;
			 }
	     }
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
    		  //myService.putExtra("wait","1");
    		  //startService(myService);
	    	  s_myService.delayAds();
	    	  
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
    	Log.d(TAG,"Ad onStop");
    	//stopService(myService);
   		//myService.putExtra("wait","1");
    	//startService(myService);
    	s_myService.delayAds();
       	finish();
    	super.onStop();
    }

    @Override
    protected void onDestroy() {
    	unbindService(mConnection);
    	super.onDestroy();
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
