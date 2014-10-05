package com.example.backup;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.backup.Views.TitleBar;
import com.example.backup.adapters.FragmentAdapter;
import com.example.backup.adapters.NavBarAdapter;
import com.example.backup.constants.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

public class WalletFragment extends FragmentActivity implements Constants, ConnectionCallbacks, OnConnectionFailedListener{


	private ImageView s_leftNavButton = null;
	private ImageView s_searchIcon = null;
	private ImageView s_title = null;
	private RelativeLayout s_searchLayout = null;
	private int mNum;
	public static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	/* Client used to interact with Google APIs. */
	public static GoogleApiClient mGoogleApiClient;



	// private TextView banner;
	FragmentAdapter mPagerAdapter;
	ViewPager mViewPager;

	private static ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragmentcontainer);
		initTitle();
		initDrawer();

	
		getActionBar().setHomeButtonEnabled(true);

		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
		mPagerAdapter = new FragmentAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);

		actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		ActionBar.OnNavigationListener mNavListener = new ActionBar.OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				// delayedHide(LONGHIDE);
				return true;
			}
		};

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				// When the tab is selected, switch to the
				// corresponding page in the ViewPager.
				mViewPager.setCurrentItem(tab.getPosition());
				mNum = tab.getPosition();

			}

			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {

			}

			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

			}
		};

		ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				getActionBar().setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				// delayedHide(1600);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		};

		mViewPager.setOnPageChangeListener(pageListener);

		actionBar.addTab(actionBar.newTab().setText("a").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("b").setTabListener(tabListener));
	
		pageListener.onPageSelected(0);

	}




	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);



	}

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

	private void initTitle() {
		RelativeLayout title = (RelativeLayout) findViewById(R.id.titleBar);
		TitleBar tb = new TitleBar(this, title);

		s_leftNavButton = tb.getLeftOptionsImgBtn();

		s_searchLayout = tb.getSearchLayout();
		s_searchLayout.setVisibility(View.GONE);
		for (int i = 0; i < s_searchLayout.getChildCount(); i++) {
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
		 NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(this, mGoogleApiClient,WalletFragment.this);
	     
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
	


	

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}

}