package com.example.backup;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.backup.Views.TitleBar;
import com.example.backup.adapters.CustomLockListAdapter;
import com.example.backup.adapters.NavBarAdapter;
import com.example.backup.backgroundtasks.FakeService;
import com.example.backup.backgroundtasks.LockScreenService;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.*;
import com.example.backup.listeners.NetworkChangeListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

public class MainActivity extends Activity implements Constants, ConnectionCallbacks, OnConnectionFailedListener {
	
	
	/*
	 * @param: app_ad- set of activated ad strings (If an app is activated, it is present in
	 * 													this set, absent otherwise)
	 * @param: app_ad_off - set of deactivated ad strings (so that you don't keep 
	 * 							displaying an ad after an app opens once
	 * @param: app_ad_list- list of user activated ads (mostly the same as app_ad, 
	 * 													 a few exceptional occasions)
	 * @param:toggleservice- toggle button to activate ads on or off
	 * @param: intent- Intent for the background service
	 */
	
	public static SharedPreferences sharedPreferences;
	public ListView list;
	private PackageManager pm;
	private static List<Process> processes;
	public static HashSet<String> app_ad_lock;
		
	public static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	
	private List<String> s_appLabels;
	private List<String> s_packageNames;
	private List<Drawable> s_appIcons;
	private List<String> s_appSubLabels;
	private CustomLockListAdapter s_lockAdapter;
	
	private ImageView s_leftNavButton = null; 
	private EditText s_searchText = null;
	private ImageView s_searchIcon = null;
	private ImageView s_title = null;
	private ImageView s_cross = null;
	private ImageView s_closeSearch = null;
	private RelativeLayout s_searchLayout = null;
	private Boolean isSearchBarVisible = false;
	
	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences(myPreferences,	MODE_PRIVATE);   
				
		app_ad_lock = new HashSet<String>();
		
		initAppLockList();	
		
		setContentView(R.layout.activity_main);
		initTitle();	
		initDrawer();
		
		//Start Services, Remove notification
		startService(new Intent(getBaseContext(),LockScreenService.class));
		startService(new Intent(getBaseContext(),MyService.class));
		startService(new Intent(getBaseContext(),NetworkChangeListener.class));
		startService(new Intent(getBaseContext(),FakeService.class));
		
		//INITIALIZE VARIABLES
		pm = getPackageManager();
		List<PackageInfo> apps = pm.getInstalledPackages(0);
	    processes = new ArrayList<Process>();
	    
	    /*
	    * GET A LIST OF INSTALLED APPS
	    */
		List<String> system_apps_list = Arrays.asList(SYSTEM_APPS);
	    int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
	    if (apps != null) {
			for (PackageInfo info: apps) {
				Boolean isSystemAppAllowed = true;
				Process process = new Process();
				process.type = "3rd Party Application";
				if((info.applicationInfo.flags & mask) != 0) {
					if(system_apps_list.contains(pm.getApplicationLabel(info.applicationInfo)) == false) {
						isSystemAppAllowed = false;
					}
					else process.type = "System Application";
				}
				if(isSystemAppAllowed && info.applicationInfo.packageName.equals(getPackageName()) == false) {	
					process.setPackageName(info.applicationInfo.packageName);
					String str = pm.getApplicationLabel(info.applicationInfo) + "";
					str = str.substring(0, 1).toUpperCase() + str.substring(1);
					process.setLabel("" + str);
					process.setIcon(pm.getApplicationIcon(info.applicationInfo));
					processes.add(process);
				}
			}
		}
		else {
			Process process = new Process();
			process.appLabel = "No Apps Installed";
			processes.add(process);
		}
		
	    Collections.sort(processes,new CustomComparator());
	    
	    //PREPARE THE LIST OF APPS (CUSTOM ADAPTER)
	    list = (ListView) findViewById(R.id.list);
		prepareLockList(processes);
		
		
	}
	
	public class Process {
		public Drawable icon;
		public String appLabel;
		public String packageName;
		public String type;
		
		public void setIcon(Drawable icon) {
			this.icon = icon;
		}
		
		public void setLabel(String label) {
			this.appLabel = label;
		}
		
		public void setPackageName(String name) {
			this.packageName = name;
		}
		
	}
	
	public class CustomComparator implements Comparator<Process> {
	    @Override
	    public int compare(Process o1, Process o2) {
	        return o1.appLabel.compareTo(o2.appLabel);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * READING THE LIST OF ACTIVATED APPS AT THE START
	 */
	private void initAppLockList() {
		    app_ad_lock.addAll( sharedPreferences.getStringSet(LOCKED_LIST, new HashSet<String>()) );
	}
	
	private void initTitle() {
		RelativeLayout title = (RelativeLayout) findViewById(R.id.titleBar);
		TitleBar tb = new TitleBar(this,title);
		s_leftNavButton = tb.getLeftOptionsImgBtn();
		
		s_searchText = tb.getSearchEditText();
		s_closeSearch = tb.getCloseSearchButton();
		s_searchLayout = tb.getSearchLayout();
		s_searchLayout.setVisibility(View.GONE);
		s_title = tb.getTitle();
		
		final Animation searchBarAnimation = AnimationUtils.loadAnimation(this, R.anim.search_bar_animation);
		final Animation titleAnimation = AnimationUtils.loadAnimation(this, R.anim.title_animation);
		final Animation searchIconAway = AnimationUtils.loadAnimation(this, R.anim.search_icon_away);
		final Animation searchBarAwayAnimation = AnimationUtils.loadAnimation(this, R.anim.search_bar_away_animation);
		final Animation titleReturnAnimation = AnimationUtils.loadAnimation(this, R.anim.title_return_animation);
		final Animation searchIconBack = AnimationUtils.loadAnimation(this, R.anim.search_icon_back);
				
		TextWatcher searchTextWatcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String search = s_searchText.getText().toString();
				if(search.length() == 0) {
					prepareLockList(processes);
				}
				else {
					prepareLockList(searchMatches(search));
				}	
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {				
			}
		};
		s_searchText.addTextChangedListener(searchTextWatcher);
		
		s_searchIcon = tb.getSearchIcon();
		s_searchIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isSearchBarVisible = true;
				s_searchLayout.startAnimation(searchBarAnimation);
				s_searchIcon.startAnimation(searchIconAway);
				s_searchIcon.setEnabled(false);
				s_leftNavButton.startAnimation(searchIconAway);
				s_searchText.requestFocus();
				s_closeSearch.setClickable(true);
				s_searchLayout.setVisibility(View.VISIBLE);
				for(int i=0; i<s_searchLayout.getChildCount();i++) {
					View child = s_searchLayout.getChildAt(i);
					child.setEnabled(true);
				}
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			    if (inputMethodManager != null) {
			        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			    }
				s_title.startAnimation(titleAnimation);
			}
		});
		
		/*
		s_cross = tb.getCross();
		s_cross.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				s_searchText.setText("");
				
				if (tabId == 1) {
					prepareLockList(processes);
				}
				else {
					prepareList(processes);
				}
				
			}
		});
		*/
		
		s_closeSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				isSearchBarVisible = false;
				s_searchText.setText("");
				s_searchLayout.startAnimation(searchBarAwayAnimation);
				s_searchIcon.startAnimation(searchIconBack);
				s_searchIcon.setEnabled(true);
				s_leftNavButton.startAnimation(searchIconBack);
				s_closeSearch.setClickable(false);
				s_searchLayout.setVisibility(View.GONE);
				for(int i=0; i<s_searchLayout.getChildCount();i++) {
					View child = s_searchLayout.getChildAt(i);
					child.setEnabled(false);
				}
				s_title.startAnimation(titleReturnAnimation);
			}
		});
	}
	
	private List<Process> searchMatches(String keyword) {
		String pattern = "(?i)^( *)" + keyword + "(.*)";
	    Pattern r = Pattern.compile(pattern);
	    Matcher m;
	    List<Process> searchMatches = new ArrayList<Process>();
	    for (Process process: processes) {
	    	m = r.matcher(process.appLabel);
	    	if(m.find()) {
	    		searchMatches.add(process);
	    	}
	    }
	    return searchMatches;
	}
	
	private void initDrawer() {
		 mDrawerList = (ListView) findViewById(R.id.drawer_list);
	     mGoogleApiClient = new GoogleApiClient.Builder(this)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this).addApi(Plus.API)
			.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	     mGoogleApiClient.connect();
	     NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(this, mGoogleApiClient, MainActivity.this);
	     
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
	
	/*
	 * PREPARE THE LIST FROM CUSTOM LOCK ADAPTER
	 */
	private void prepareLockList(List<Process> Processes) {
		if(Processes.isEmpty()) {
			s_lockAdapter = new CustomLockListAdapter(MainActivity.this, s_appIcons, s_appLabels, null, s_packageNames, false);
		}
		else {
			s_appLabels = new ArrayList<String>();
			s_packageNames = new ArrayList<String>();
			s_appSubLabels = new ArrayList<String>();
			s_appIcons = new ArrayList<Drawable>();
			for (Process process: Processes) {
				s_appLabels.add(process.appLabel);
				s_appIcons.add(process.icon);
				s_appSubLabels.add(process.type);
				s_packageNames.add(process.packageName);
			}
			s_lockAdapter = new CustomLockListAdapter(MainActivity.this, s_appIcons, s_appLabels, s_appSubLabels, s_packageNames, true);
		}
		list.setAdapter(s_lockAdapter);
	}
	
	
	/*
	 * Check connectivity of Internet
	 */
	public static boolean isConnented(Activity context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info!=null) {
			if(info.isConnected()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		if (isSearchBarVisible) {
			final Animation searchBarAwayAnimation = AnimationUtils.loadAnimation(this, R.anim.search_bar_away_animation);
			final Animation titleReturnAnimation = AnimationUtils.loadAnimation(this, R.anim.title_return_animation);
			final Animation searchIconBack = AnimationUtils.loadAnimation(this, R.anim.search_icon_back);
			isSearchBarVisible = false;
			s_searchText.setText("");
			s_searchLayout.startAnimation(searchBarAwayAnimation);
			s_searchIcon.startAnimation(searchIconBack);
			s_searchIcon.setEnabled(true);
			s_leftNavButton.startAnimation(searchIconBack);
			s_closeSearch.setClickable(false);
			s_searchLayout.setVisibility(View.GONE);
			for(int i=0; i<s_searchLayout.getChildCount();i++) {
				View child = s_searchLayout.getChildAt(i);
				child.setEnabled(false);
			}
			s_title.startAnimation(titleReturnAnimation);
		}
		else {
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onResume() {
		/*
		 * LOADING THE LIST OF ACTIVATED ADS
		 */
		app_ad_lock.addAll( sharedPreferences.getStringSet(LOCKED_LIST, new HashSet<String>()) );
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		/*
		 * SAVE THE LIST OF ACTIVATED ADS 
		 */
		mGoogleApiClient.disconnect();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		
	}
	

}
