package com.example.backup;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.backup.Views.TitleBar;
import com.example.backup.adapters.NavBarAdapter;
import com.example.backup.adapters.SpinnerAdapter;
import com.example.backup.constants.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

public class WalletPage extends Activity implements Constants, ConnectionCallbacks, OnConnectionFailedListener {

	private ImageView s_leftNavButton = null;
	private ImageView s_searchIcon = null;
	private ImageView s_title = null;
	private RelativeLayout s_searchLayout = null;

	private int Money_amount = 0;
	private int tranfer_amount = 0;

	private ImageView facebook, googleplus, linkedin, twitter;
	private Button submit = null;
	private ImageView tab1,tab2;
	private LinearLayout tab1_layout,tab2_layout;
	private int tabId = 1;
	private TextView transfer_share, total_amount;
	private EditText s_accountName, s_accountNumber, s_IFSC, s_bankAmount;
	private EditText s_mobileNumber, s_mobileAmount;
	private Spinner s_operator, s_state;

	public static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	/* Client used to interact with Google APIs. */
	public static GoogleApiClient mGoogleApiClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallet);

		initTitle();
		initDrawer();
		findTheViews();

		set_on_clicks();

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
		 NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(this, mGoogleApiClient, WalletPage.this);
	     
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
	
	private void findTheViews() {

		facebook = (ImageView) findViewById(R.id.im_fb);
		googleplus = (ImageView) findViewById(R.id.im_gplus);
		linkedin = (ImageView) findViewById(R.id.im_lin);
		twitter = (ImageView) findViewById(R.id.im_tw);
		submit = (Button) findViewById(R.id.submit);
		
		tab1 = (ImageView) findViewById(R.id.tab1);
		tab1_layout =  (LinearLayout) findViewById(R.id.MobileRechargeDetails);
		tab2 = (ImageView) findViewById(R.id.tab2);
		tab2_layout = (LinearLayout) findViewById(R.id.BankTransferDetails);
		
		tab1.setBackgroundResource(R.drawable.mobile_recharge_selected);
		tab2.setBackgroundResource(R.drawable.bank_transfer_unselected);

		
		s_operator = (Spinner) findViewById(R.id.operator);
		s_operator.setAdapter( new SpinnerAdapter(getBaseContext(), PROVIDERS));
		s_state = (Spinner) findViewById(R.id.state);
		s_state.setAdapter(new SpinnerAdapter(getBaseContext(), STATES));
		
		
	}

	private void set_on_clicks() {

		// facebook.setOnClickListener(new OnClickListener() {
		// public void onClick(View arg0) {
		// }
		// });
		//
		// googleplus.setOnClickListener(new OnClickListener() {
		// public void onClick(View arg0) {
		// }
		// });
		//
		// linkedin.setOnClickListener(new OnClickListener() {
		// public void onClick(View arg0) {
		// }
		// });
		//
		// twitter.setOnClickListener(new OnClickListener() {
		// public void onClick(View arg0) {
		// }
		// });
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		tab1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (tabId == 2) {
					tabId = 1;
					tab1.setBackgroundResource(R.drawable.mobile_recharge_selected);
					tab2.setBackgroundResource(R.drawable.bank_transfer_unselected);
					changeView(tab1_layout, true);
					changeView(tab2_layout, false);
				}
			}
		});
		
		tab2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (tabId == 1) {
					tabId = 2;
					tab1.setBackgroundResource(R.drawable.mobile_recharge_unselected);
					tab2.setBackgroundResource(R.drawable.bank_transfer_selected);
					changeView(tab2_layout, true);
					changeView(tab1_layout, false);
				}
			}
		});
	}
	
	private void changeView(LinearLayout view, boolean visible) {
		if(visible) 
			view.setVisibility(View.VISIBLE);
		else 
			view.setVisibility(View.GONE);
		for(int i=0;i<view.getChildCount();i++) {
			view.getChildAt(i).setEnabled(visible);
		}
	}
	
	protected void flash_share(){
		transfer_share = (TextView) findViewById(R.id.processed);
		transfer_share.setText("Your recent request for Mobile Recharge of Rs."+ tranfer_amount + " has been processed");
		findViewById(R.id.Share_it).setVisibility(View.VISIBLE);
		
		Handler handler = new Handler();
	    Runnable r=new Runnable() {
	              @Override
	              public void run() {
	            	  findViewById(R.id.Share_it).setVisibility(View.GONE);
	              }         
	            };
	        handler.postDelayed(r, 8000); 
		
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
