package com.example.backup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backup.Views.TitleBar;
import com.example.backup.adapters.NavBarAdapter;
import com.example.backup.backgroundtasks.LockScreenService;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.haibison.android.lockpattern.LockPatternActivity;

public class SettingsPage extends Activity implements Constants, OnConnectionFailedListener, ConnectionCallbacks{

	private ImageView s_leftNavButton = null; 
	private ImageView s_searchIcon = null;
	private ImageView s_title = null;
	private RelativeLayout s_searchLayout = null;
	
	private SharedPreferences s_sharedPref = null;
	private Editor s_editor;
	private Switch s_lockScreenSwitch,s_appLockSwitch;
	private RadioGroup s_lockOptions = null;
	int locktype;
	
	public static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	/* Client used to interact with Google APIs. */
	public static GoogleApiClient mGoogleApiClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_screen_lock);
		s_sharedPref = getSharedPreferences(myPreferences, MODE_PRIVATE);
		s_editor = s_sharedPref.edit();
		locktype = s_sharedPref.getInt(APP_LOCK_TYPE, 0);
		if (locktype == 0) {
			initLayout();
		}
		else if(locktype == 1){
			Intent intent = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null,
			        getBaseContext(), LockPatternActivity.class);
			intent.putExtra(LockPatternActivity.EXTRA_PATTERN, LockPatternActivity.EXTRA_PATTERN);
			startActivityForResult(intent, REQ_ENTER_PATTERN);
		}
		else {
			initLayout();
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
	    switch (requestCode) {
	    case REQ_ENTER_PATTERN: {
	        /*
	         * NOTE that there are 4 possible result codes!!!
	         */
	        if( resultCode == RESULT_OK ) {	
	    		initLayout();
	            break;
	        }
	        else {
	        	finish();
	        }

	        /*
	         * In any case, there's always a key EXTRA_RETRY_COUNT, which holds
	         * the number of tries that the user did.
	         */
	        int retryCount = data.getIntExtra(
	                LockPatternActivity.EXTRA_RETRY_COUNT, 0);
	    }// REQ_ENTER_PATTERN
	   }
	}
	
	private void initLayout() {
		setContentView(R.layout.settings_page);
		initTitle();
		initDrawer();
		initSwitchListeners();
		initRadioGroupListener();
	}
	
	private void initTitle() {
		RelativeLayout title = (RelativeLayout) findViewById(R.id.titleBar);
		TitleBar tb = new TitleBar(this,title);
		
		s_leftNavButton = tb.getLeftOptionsImgBtn();
		
		s_searchLayout = tb.getSearchLayout();
		s_searchLayout.setVisibility(View.GONE);
		for(int i=0; i<s_searchLayout.getChildCount(); i++) {
			s_searchLayout.getChildAt(i).setEnabled(false);
		}
		
		s_searchIcon = tb.getSearchIcon();
		s_searchIcon.setVisibility(View.GONE);
		s_searchIcon.setEnabled(false);
	}
	
	private void initDrawer() {
		 mDrawerList = (ListView) findViewById(R.id.drawer_list);
		 mGoogleApiClient = new GoogleApiClient.Builder(this)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this).addApi(Plus.API)
			.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	     mGoogleApiClient.connect();
		 NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(this, mGoogleApiClient, SettingsPage.this);
	     
		 mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		 mDrawerList.setAdapter(l_leftNavBarListAdapter);
		
		 s_leftNavButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mDrawerLayout.openDrawer(Gravity.LEFT);
					mDrawerList.bringToFront();
					mDrawerList.requestLayout();
				}
			});
	}
	
	
	private void initSwitchListeners() {
		s_lockScreenSwitch = (Switch) findViewById(R.id.screenLockSwitch);
		if (s_sharedPref.getBoolean(SCREENLOCK_ACTIVATED, true)) {
			s_lockScreenSwitch.setChecked(true);
		}
		s_lockScreenSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
				    LockScreenService.enableLock();
				}
				else {
					LockScreenService.disableLock();
				}
				s_editor.putBoolean(SCREENLOCK_ACTIVATED, isChecked).commit();
			}
		});
		
		s_appLockSwitch = (Switch) findViewById(R.id.appLockSwitch);
		if (s_sharedPref.getBoolean(APPLOCK_ACTIVATED, true)) {
			s_appLockSwitch.setChecked(true);
		}
		s_appLockSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if(isChecked) {
					MyService.startAds();;
				}
				else {
					MyService.stopAds();
				}
				s_editor.putBoolean(APPLOCK_ACTIVATED, isChecked).commit();
			}
		});
		
	}
	
	private void initRadioGroupListener() {
		s_lockOptions = (RadioGroup) findViewById(R.id.lock_radioGroup);
		switch (locktype) {
		case 0:
			((RadioButton) findViewById(R.id.radiobutton_none)).setChecked(true);;
			break;
		case 1:
			((RadioButton) findViewById(R.id.radiobutton_pattern)).setChecked(true);
			break;
		case 2:
			((RadioButton) findViewById(R.id.radiobutton_password)).setChecked(true);
			break;
		default:
			break;
		}
		
		s_lockOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.radiobutton_none:
					s_editor.putInt(APP_LOCK_TYPE, 0).commit();
					MyService.disableLock();
					break;
				case R.id.radiobutton_pattern:
					startActivity(new Intent(getBaseContext(),SetScreenLock.class));
					break;
				case R.id.radiobutton_password:
					s_editor.putInt(APP_LOCK_TYPE, 2).commit();
					break;
				default:
					break;
				}
			}
			
		});
	}
	
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	};
		
	
}
