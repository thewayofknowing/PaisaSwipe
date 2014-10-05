package com.example.backup;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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

public class WalletPage extends Fragment implements Constants, ConnectionCallbacks, OnConnectionFailedListener {

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
	
	View v;

	public static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	/* Client used to interact with Google APIs. */
	public static GoogleApiClient mGoogleApiClient;
	
	public static WalletPage newInstance() {
		WalletPage f = new WalletPage();

		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.wallet, container, false);
		
		return v;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

//		findTheViews();
//		set_on_clicks();
	}


	
	private void findTheViews() {

		facebook = (ImageView) v.findViewById(R.id.im_fb);
		googleplus = (ImageView) v.findViewById(R.id.im_gplus);
		linkedin = (ImageView) v.findViewById(R.id.im_lin);
		twitter = (ImageView) v.findViewById(R.id.im_tw);
		submit = (Button) v.findViewById(R.id.submit);
		
		tab1 = (ImageView) v.findViewById(R.id.tab1);
		tab1_layout =  (LinearLayout) v.findViewById(R.id.MobileRechargeDetails);
		tab2 = (ImageView) v.findViewById(R.id.tab2);
		tab2_layout = (LinearLayout) v.findViewById(R.id.BankTransferDetails);
		
		tab1.setBackgroundResource(R.drawable.mobile_recharge_selected);
		tab2.setBackgroundResource(R.drawable.bank_transfer_unselected);

		
		s_operator = (Spinner) v.findViewById(R.id.operator);
		s_operator.setAdapter( new SpinnerAdapter(getActivity(), PROVIDERS));
		s_state = (Spinner) v.findViewById(R.id.state);
		s_state.setAdapter(new SpinnerAdapter(getActivity(), STATES));
		
		
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
		transfer_share = (TextView) v.findViewById(R.id.processed);
		transfer_share.setText("Your recent request for Mobile Recharge of Rs."+ tranfer_amount + " has been processed");
		v.findViewById(R.id.Share_it).setVisibility(View.VISIBLE);
		
		Handler handler = new Handler();
	    Runnable r=new Runnable() {
	              @Override
	              public void run() {
	            	  v.findViewById(R.id.Share_it).setVisibility(View.GONE);
	              }         
	            };
	        handler.postDelayed(r, 8000); 
		
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
