package com.example.backup;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.backup.Views.TitleBar;
import com.example.backup.constants.Constants;

public class WalletPage extends Activity implements Constants {

	private ImageView s_leftNavButton = null;
	private ImageView s_searchIcon = null;
	private TextView s_title = null;
	private RelativeLayout s_searchLayout = null;

	private int Money_amount = 0;
	private int tranfer_amount = 0;

	private ImageView facebook, googleplus, linkedin, twitter;
	private Button butt_money_transfer, butt_bank_transfer, butt_mob_sub,
			butt_bank_sub;
	private TextView transfer_share, total_amount;
	private EditText Account_name, Account_number, Amount, IFSC;
	boolean isDown_mobile = false;
	boolean isDown_bank = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallet);

		initTitle();
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

	private void findTheViews() {

		facebook = (ImageView) findViewById(R.id.im_fb);
		googleplus = (ImageView) findViewById(R.id.im_gplus);
		linkedin = (ImageView) findViewById(R.id.im_lin);
		twitter = (ImageView) findViewById(R.id.im_tw);
		butt_money_transfer = (Button) findViewById(R.id.Mob_Rech);
		butt_bank_transfer = (Button) findViewById(R.id.Bank_Trans);
		butt_mob_sub = (Button) findViewById(R.id.submit);
		butt_bank_sub = (Button) findViewById(R.id.Bank_submit);

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

		butt_bank_sub.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				submit(1);
			}
		});

		butt_mob_sub.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				submit(0);
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

						if (isDown_bank == true) {
							butt_bank_transfer.setBackgroundColor(Color.rgb(
									144, 200, 84));
							isDown_bank = false;
							findViewById(R.id.Detail_bank).setVisibility(
									View.GONE);
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
						findViewById(R.id.Detail_bank).setVisibility(
								View.VISIBLE);
						isDown_bank = true;

						if (isDown_mobile == true) {
							butt_money_transfer.setBackgroundColor(Color.rgb(
									144, 200, 84));
							findViewById(R.id.Details).setVisibility(View.GONE);
							isDown_mobile = false;
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

	protected void submit(int x) {

		boolean passable = true;
		if (x == 0) {

			Account_name = (EditText) findViewById(R.id.IAccN);
			Account_number = (EditText) findViewById(R.id.IAccNu);
			IFSC = (EditText) findViewById(R.id.IIFSC);
			Amount = (EditText) findViewById(R.id.IAmt);
		}

		if (x == 1) {
			Account_number = (EditText) findViewById(R.id.Bank_IAccN);
			Amount = (EditText) findViewById(R.id.Bank_IAmt);
			IFSC = (EditText) findViewById(R.id.Bank_IIfsc);
		}

		if (passable == true) {
			flash_share();
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
	};

}
