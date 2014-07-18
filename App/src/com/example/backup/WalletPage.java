package com.example.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.backup.Views.TitleBar;
import com.example.backup.backgroundtasks.LockScreenService;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.Constants;
import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class WalletPage extends Activity implements Constants {

	private ImageView s_leftNavButton = null;
	private ImageView s_searchIcon = null;
	private TextView s_title = null;
	private RelativeLayout s_searchLayout = null;

	private int Money_amount = 0;
	private int tranfer_amount = 0;

	private ImageView facebook;
	private ImageView googleplus;
	private ImageView linkedin;
	private ImageView twitter;
	private Button butt_money_transfer;
	private Button butt_bank_transfer;
	private TextView transfer_share;
	private TextView total_amount;

	boolean isDown_mobile = false;
	boolean isDown_bank = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallet);

		initTitle();

		facebook = (ImageView) findViewById(R.id.im_fb);
		googleplus = (ImageView) findViewById(R.id.im_gplus);
		linkedin = (ImageView) findViewById(R.id.im_lin);
		twitter = (ImageView) findViewById(R.id.im_tw);
		butt_money_transfer = (Button) findViewById(R.id.Mob_Rech);
		butt_bank_transfer = (Button) findViewById(R.id.Bank_Trans);

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

	private void set_on_clicks() {

		facebook.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			}
		});

		googleplus.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			}
		});

		linkedin.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			}
		});

		twitter.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			}
		});

		butt_money_transfer.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				if (butt_money_transfer.isPressed()) {

					if (isDown_mobile == false) {
						butt_money_transfer.setBackgroundColor(Color.rgb(109,
								160, 66));
						findViewById(R.id.Details).setVisibility(View.VISIBLE);
						isDown_mobile = true;
						
						if(isDown_bank==true)
						{
							butt_bank_transfer.setBackgroundColor(Color.rgb(144, 200,84));
							isDown_bank=false;
							findViewById(R.id.Detail_bank).setVisibility(View.GONE);
						}
						
						
					} else {
						butt_money_transfer.setBackgroundColor(Color.rgb(144,
								200, 84));
						findViewById(R.id.Details).setVisibility(View.GONE);
						isDown_mobile = false;
					}

				}

			}
		});
		butt_bank_transfer.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				if (butt_bank_transfer.isPressed()) {
					
					if (isDown_bank == false) {
						butt_bank_transfer.setBackgroundColor(Color.rgb(109,
								160, 66));
						findViewById(R.id.Detail_bank).setVisibility(View.VISIBLE);
						isDown_bank = true;
						
						if(isDown_mobile==true)
						{
							butt_money_transfer.setBackgroundColor(Color.rgb(144, 200,84));
							findViewById(R.id.Details).setVisibility(View.GONE);
							isDown_mobile=false;
						}
						
						
					} else {
						butt_bank_transfer.setBackgroundColor(Color.rgb(144,
								200, 84));
						isDown_bank = false;
						findViewById(R.id.Detail_bank).setVisibility(View.GONE);
					}

				}

			}
		});

	}

	protected void onDestroy() {
		super.onDestroy();
	};

}
