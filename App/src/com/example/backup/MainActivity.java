package com.example.backup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.backup.Views.TitleBar;
import com.example.backup.adapters.AppListFragment;
import com.example.backup.adapters.AppLockFragment;
import com.example.backup.adapters.CustomListAdapter;
import com.example.backup.adapters.NavBarAdapter;
import com.example.backup.backgroundtasks.FakeService;
import com.example.backup.backgroundtasks.LockScreenService;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.*;

public class MainActivity extends Activity implements Constants {

	/*
	 * @param: app_ad- set of activated ad strings (If an app is activated, it
	 * is present in this set, absent otherwise)
	 * 
	 * @param: app_ad_off - set of deactivated ad strings (so that you don't
	 * keep displaying an ad after an app opens once
	 * 
	 * @param: app_ad_list- list of user activated ads (mostly the same as
	 * app_ad, a few exceptional occasions)
	 * 
	 * @param:toggleservice- toggle button to activate ads on or off
	 * 
	 * @param: intent- Intent for the background service
	 */

	public static SharedPreferences sharedPreferences;
	public ListView list;
	public static CustomListAdapter adapter;
	private PackageManager pm;
	private static List<Process> processes;
	public static List<Process> result_processes;
	public static HashSet<String> app_ad;
	public static HashSet<String> app_ad_off;
	public static HashSet<String> app_ad_list;
	public static HashSet<String> app_ad_lock;

	public static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	private ImageView s_leftNavButton = null;
	private EditText s_searchText = null;
	private ImageView s_searchIcon = null;
	private TextView s_title = null;
	private ImageView s_cross = null;
	private RelativeLayout s_searchLayout = null;

	AppListFragment s_appListFragment;
	AppLockFragment s_appLockFragment;
	FragmentManager s_fragmentManager;
	FragmentTransaction s_fragmentTransaction;
	ImageView s_tab1, s_tab2;
	Drawable s_appAdTabSelected, s_appAdTabUnSelected, s_appLockTabSelected,
			s_appLockTabUnSelected;
	int tabId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences(myPreferences, MODE_PRIVATE);

		app_ad_list = new HashSet<String>();
		app_ad = new HashSet<String>();
		app_ad_lock = new HashSet<String>();

		initAppLockList();

		setContentView(R.layout.activity_main);
		initTitle();
		initDrawer();
		setTabListener();

		// Start LockScreen/AppScreen Service
		startService(new Intent(getBaseContext(), LockScreenService.class));
		startService(new Intent(getBaseContext(), MyService.class));
		startService(new Intent(getBaseContext(), FakeService.class));

		// INITIALIZE VARIABLES
		pm = getPackageManager();
		List<PackageInfo> apps = pm.getInstalledPackages(0);
		processes = new ArrayList<Process>();
		result_processes = new ArrayList<Process>();
		Process process = new Process();

		/*
		 * GET A LIST OF INSTALLED APPS
		 */

		if (apps != null) {
			for (PackageInfo info : apps) {
				// if((info.applicationInfo.flags & (ApplicationInfo.FLAG_SYSTEM
				// | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) == 0) {
				if (!(info.applicationInfo.packageName.equals(getPackageName()))
						&& pm.getApplicationLabel(info.applicationInfo)
								.toString().indexOf("com.") < 0) {
					process = new Process();
					process.setPackageName(info.applicationInfo.packageName);
					process.setLabel(""
							+ pm.getApplicationLabel(info.applicationInfo));
					process.setIcon(pm.getApplicationIcon(info.applicationInfo));
					processes.add(process);
				}
				// }
			}
		} else {
			process.appLabel = "No Apps Installed";
			processes.add(process);
		}

		Collections.sort(processes, new CustomComparator());

		// PREPARE THE LIST OF APPS (CUSTOM ADAPTER)
		prepareList(processes);

	}

	public class Process {
		public Drawable icon;
		public String appLabel;
		public String packageName;

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

	@Override
	protected void onResume() {
		/*
		 * LOADING THE LIST OF ACTIVATED ADS
		 */
		if (sharedPreferences.contains(ACTIVATED_LIST)) {
			app_ad_list.addAll(sharedPreferences.getStringSet(ACTIVATED_LIST,
					new HashSet<String>()));
			app_ad.addAll(app_ad_list);
			app_ad_lock.addAll(sharedPreferences.getStringSet(LOCKED_LIST,
					new HashSet<String>()));
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		/*
		 * SAVE THE LIST OF ACTIVATED ADS
		 */
		sharedPreferences.edit().putStringSet(ACTIVATED_LIST, app_ad_list)
				.commit();
		sharedPreferences.edit().putStringSet(LOCKED_LIST, app_ad_lock)
				.commit();
		super.onPause();
	}

	/*
	 * READING THE LIST OF ACTIVATED APPS AT THE START
	 */
	private void initAppLockList() {
		if (sharedPreferences.contains(ACTIVATED_LIST)) {
			app_ad_list.addAll(sharedPreferences.getStringSet(ACTIVATED_LIST,
					new HashSet<String>()));
			app_ad.addAll(app_ad_list);
			app_ad_lock.addAll(sharedPreferences.getStringSet(LOCKED_LIST,
					new HashSet<String>()));
		}
	}

	private void initTitle() {
		RelativeLayout title = (RelativeLayout) findViewById(R.id.titleBar);
		TitleBar tb = new TitleBar(this, title);
		s_leftNavButton = tb.getLeftOptionsImgBtn();

		s_searchText = tb.getSearchEditText();
		s_searchLayout = tb.getSearchLayout();
		s_searchLayout.setVisibility(View.GONE);
		s_title = tb.getTitle();

		TextWatcher searchTextWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String search = s_searchText.getText().toString();
				if (search.length() == 0) {
					prepareList(processes);
				} else {
					prepareList(searchMatches(search));
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
		final Animation searchBarAnimation = AnimationUtils.loadAnimation(this,
				R.anim.search_bar_animation);
		final Animation titleAnimation = AnimationUtils.loadAnimation(this,
				R.anim.title_animation);
		final Animation searchIconAway = AnimationUtils.loadAnimation(this,
				R.anim.search_icon_away);
		s_searchIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				s_searchLayout.startAnimation(searchBarAnimation);
				s_searchIcon.startAnimation(searchIconAway);
				s_searchIcon.setClickable(false);
				s_searchText.setEnabled(true);
				s_cross.setEnabled(true);
				s_searchLayout.setVisibility(View.VISIBLE);
				s_title.startAnimation(titleAnimation);
			}
		});

		final Animation searchBarAwayAnimation = AnimationUtils.loadAnimation(
				this, R.anim.search_bar_away_animation);
		final Animation titleReturnAnimation = AnimationUtils.loadAnimation(
				this, R.anim.title_return_animation);
		final Animation searchIconBack = AnimationUtils.loadAnimation(this,
				R.anim.search_icon_back);
		s_cross = tb.getCross();
		s_cross.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				s_searchLayout.startAnimation(searchBarAwayAnimation);
				s_searchIcon.startAnimation(searchIconBack);
				s_searchIcon.setClickable(true);
				s_searchText.setEnabled(false);
				s_cross.setEnabled(false);
				s_title.startAnimation(titleReturnAnimation);
			}
		});
	}

	private List<Process> searchMatches(String keyword) {
		String pattern = "(?i)^( *)" + keyword + "(.*)";
		Pattern r = Pattern.compile(pattern);
		Matcher m;
		List<Process> searchMatches = new ArrayList<Process>();
		for (Process process : processes) {
			m = r.matcher(process.appLabel);
			if (m.find()) {
				searchMatches.add(process);
			}
		}
		return searchMatches;
	}

	private void initDrawer() {
		mDrawerList = (ListView) findViewById(R.id.drawer_list);
		NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(this);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
	 * PREPARE THE LIST FROM CUSTOM ADAPTER
	 */
	private void prepareList(List<Process> Processes) {
		result_processes.clear();
		result_processes.addAll(Processes);
		s_appListFragment = new AppListFragment();
		s_appLockFragment = new AppLockFragment();
		s_fragmentManager = getFragmentManager();
		s_fragmentTransaction = s_fragmentManager.beginTransaction();
		if (tabId == 1)
			s_fragmentTransaction
					.replace(R.id.list_fragment, s_appListFragment);
		else
			s_fragmentTransaction
					.replace(R.id.list_fragment, s_appLockFragment);
		s_fragmentTransaction.commit();
	}

	private void setTabListener() {
		tabId = 1;
		// s_appAdTabSelected =
		// getResources().getDrawable(R.drawable.app_ads_selected);
		// s_appAdTabUnSelected =
		// getResources().getDrawable(R.drawable.app_ads_unselected);
		// s_appLockTabSelected =
		// getResources().getDrawable(R.drawable.app_lock_selected);
		// s_appLockTabUnSelected =
		// getResources().getDrawable(R.drawable.app_lock_unselected);
		s_tab1 = (ImageView) findViewById(R.id.tab1);
		s_tab2 = (ImageView) findViewById(R.id.tab2);

		s_tab1.setBackgroundResource(R.drawable.app_ads_selected);
		s_tab2.setBackgroundResource(R.drawable.app_lock_unselected);

		s_tab1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tabId == 2) {
					tabId = 1;
					s_tab1.setBackgroundResource(R.drawable.app_ads_selected);
					s_tab2.setBackgroundResource(R.drawable.app_lock_unselected);
					s_fragmentTransaction = s_fragmentManager
							.beginTransaction();
					s_fragmentTransaction.replace(R.id.list_fragment,
							s_appListFragment);
					s_fragmentTransaction.commit();
				}
			}
		});

		s_tab2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tabId == 1) {
					tabId = 2;
					s_tab2.setBackgroundResource(R.drawable.app_lock_selected);
					s_tab1.setBackgroundResource(R.drawable.app_ads_unselected);
					s_fragmentTransaction = s_fragmentManager
							.beginTransaction();
					s_fragmentTransaction.replace(R.id.list_fragment,
							s_appLockFragment);
					s_fragmentTransaction.commit();
				}
			}
		});
	}

	/*
	 * Check connectivity of Internet
	 */
	public static boolean isConnented(Activity context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			if (info.isConnected()) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
