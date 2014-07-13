package com.example.backup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import org.apache.http.NameValuePair;

import com.example.backup.ads.AdLogic;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.Constants;
import com.fima.glowpadview.GlowPadView;
import com.fima.glowpadview.GlowPadView.OnTriggerListener;
import com.haibison.android.lockpattern.LockPatternActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class LockScreenActivity extends Activity implements Constants,
		OnTriggerListener {

	private Bitmap bitmap;
	private GlowPadView mGlowPadView;
	Intent myIntent;
	MyService s_myService;
	Calendar calendar;
	String s_appearedAt, s_swipedAt;
	List<NameValuePair> s_postElements;
	Stack<String> s_activityStack;
	private Button createPat;
	private Button usePat;
	private static final int REQ_CREATE_PATTERN = 1;
	private static final int REQ_ENTER_PATTERN = 2;
	Intent intent;
	char[] savedPattern;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock);

		myIntent = getIntent();
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		AdLogic adL = new AdLogic(this);
		bitmap = adL.getImageUri(this);

		calendar = Calendar.getInstance();
		s_appearedAt = new SimpleDateFormat("HH:mm:ss-dd-MM-yyyy")
				.format(calendar.getTime());
		// myService = new Intent(getBaseContext(),MyService.class);
		// stopService(myService);

		Intent mIntent = new Intent(this, MyService.class);
		bindService(mIntent, mConnection, BIND_AUTO_CREATE);
		s_myService.stopAds();

		mGlowPadView = (GlowPadView) findViewById(R.id.glow_pad_view);
		mGlowPadView.setBackground(new BitmapDrawable(bitmap));

		mGlowPadView.setOnTriggerListener(this);

		// uncomment this to make sure the glowpad doesn't vibrate on touch
		// mGlowPadView.setVibrateEnabled(false);

		// uncomment this to hide targets
		mGlowPadView.setShowTargetsOnIdle(true);

		intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null,
				getApplicationContext(), LockPatternActivity.class);
		startActivityForResult(intent, REQ_CREATE_PATTERN);

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
			// Toast.makeText(this, "Camera selected",
			// Toast.LENGTH_SHORT).show();
			break;

		case R.drawable.ic_item_google:

			s_swipedAt = new SimpleDateFormat("HH:mm:ss-dd-MM-yyyy")
					.format(calendar.getTime());
			/*
			 * Stats stats = new Stats(); stats.setAdId(id);
			 * stats.setUserId(userId); stats.setCompanyId(companyId);
			 * stats.setAppearedAtTime(s_appearedAt);
			 * stats.setSwipedAtTime(s_swipedAt); PostStatsAsyncTask pTask = new
			 * PostStatsAsyncTask(stats, getBaseContext()); pTask.execute();
			 */
			finish();
			// Toast.makeText(this, "Google selected",
			// Toast.LENGTH_SHORT).show();

			break;
		default:
			// Code should never reach here.
		}

	}

	ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			s_myService = ((MyService.LocalBinder) service).getService();
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

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				|| (keyCode == KeyEvent.KEYCODE_CAMERA)) {
			Log.d(TAG, "Power");
			return true;
		} else if ((keyCode == KeyEvent.KEYCODE_POWER)) {
			return false;
		}
		if ((keyCode == KeyEvent.KEYCODE_HOME)) {
			// Toast.makeText(getBaseContext(), "Home",
			// Toast.LENGTH_SHORT).show();
			return false;
		}

		return false;
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
			return false;
		} else if ((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)) {
			return false;
		}
		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {
			// Toast.makeText(getBaseContext(), "Home",
			// Toast.LENGTH_SHORT).show();
			return false;
		}
		return false;
	}

	/*
	 * private boolean isAdDisplayed() { boolean isAdRunning = false;
	 * ActivityManager mngr = (ActivityManager) getSystemService(
	 * ACTIVITY_SERVICE ); List<ActivityManager.RunningTaskInfo> taskList =
	 * mngr.getRunningTasks(10); String Main = getApplication().getPackageName()
	 * + ".ads.AdScreen"; for(ActivityManager.RunningTaskInfo r : taskList) {
	 * if(r.topActivity.getClassName().equals(Main)) { isAdRunning = true;
	 * break; } } return isAdRunning; }
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_CREATE_PATTERN: {
			if (resultCode == RESULT_OK) {
				char[] pattern = data
						.getCharArrayExtra(LockPatternActivity.EXTRA_PATTERN);
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
				break;
			case RESULT_CANCELED:
				// The user cancelled the task
				break;
			case LockPatternActivity.RESULT_FAILED:
				// The user failed to enter the pattern
				break;
			case LockPatternActivity.RESULT_FORGOT_PATTERN:
				// The user forgot the pattern and invoked your recovery
				// Activity.
				break;
			}
			int retryCount = data.getIntExtra(
					LockPatternActivity.EXTRA_RETRY_COUNT, 0);

			break;
		}
		}

	}
}