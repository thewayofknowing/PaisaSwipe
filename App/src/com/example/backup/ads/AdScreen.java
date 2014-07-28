package com.example.backup.ads;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import com.example.backup.R;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.*;
import com.fima.glowpadview.GlowPadView;
import com.fima.glowpadview.GlowPadView.OnTriggerListener;
import com.haibison.android.lockpattern.LockPatternActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class AdScreen extends Activity implements Constants, OnTriggerListener {
	
	static Activity activity = null;
    private static String packageName;
    private Boolean s_appLock;
    private Bitmap bitmap;
    private ActivityManager am;
	private PackageManager pm;
	private List<RunningTaskInfo> tasks;

    private GlowPadView mGlowPadView;
	
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad);
		
		activity = this;
		
	    MyService.stopAds();
	     
        packageName = getIntent().getExtras().getString("name");
        s_appLock = getIntent().getExtras().getBoolean("app_lock");
        ImageView iv = (ImageView) findViewById(R.id.imageView1);

        AdLogic adL = new AdLogic(this);
	    bitmap = adL.getImageUri(this);
	    mGlowPadView = (GlowPadView) findViewById(R.id.glow_pad_view);

	   if(s_appLock) {
		   intent = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null,
			        getApplicationContext(), LockPatternActivity.class);
			startActivityForResult(intent, REQ_ENTER_PATTERN);
		    findViewById(R.id.parent).setBackground(new BitmapDrawable(bitmap));
		    mGlowPadView.setVisibility(View.GONE);
	   }
	   else {
		   	 mGlowPadView.setBackgroundDrawable(new BitmapDrawable(bitmap));
	         mGlowPadView.setOnTriggerListener(this);
			 mGlowPadView.setVibrateEnabled(false);
			 mGlowPadView.setShowTargetsOnIdle(true);  
	   }
         
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
			// REQ_CREATE_PATTERN
			 case REQ_ENTER_PATTERN: {
			        /*
			         * NOTE that there are 4 possible result codes!!!
			         */
			        switch (resultCode) {
			        case RESULT_OK:
			            // The user passed
			        	checkForAppKill();
			            break;
			        case RESULT_CANCELED:
			        	Intent homeIntent= new Intent(Intent.ACTION_MAIN);
			        	homeIntent.addCategory(Intent.CATEGORY_HOME);
			        	homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        	startActivity(homeIntent);
			        	finish();
			            break;
			        case LockPatternActivity.RESULT_FAILED:
			        	Intent homeIntentb= new Intent(Intent.ACTION_MAIN);
			        	homeIntentb.addCategory(Intent.CATEGORY_HOME);
			        	homeIntentb.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        	startActivity(homeIntentb);
			        	finish();
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

	    private void checkForAppKill() {
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
	    	 MyService.delayAds();
	    	 finish();
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
