package com.example.backup.adapters;

import com.example.backup.MainActivity;
import com.example.backup.R;
import com.example.backup.SettingsPage;
import com.example.backup.SplashScreen;
import com.example.backup.WalletFragment;
import com.example.backup.backgroundtasks.LockScreenService;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.Constants;
import com.example.backup.db.DataBaseHelper;
import com.example.backup.game.StartScreen;
import com.example.backup.listeners.NetworkChangeListener;
import com.facebook.Session;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavBarAdapter extends BaseAdapter implements Constants {

		private Context m_cont;
		private LayoutInflater m_inflater;
		private DataBaseHelper db;
		private GoogleApiClient mGoogleApiClient;
		private Activity activity;
		
		public NavBarAdapter(Context a_cont, GoogleApiClient client, Activity activity) {
			m_cont = a_cont;
			mGoogleApiClient = client;
			this.activity = activity;
			m_inflater = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			db = new DataBaseHelper(m_cont);
		}

		@Override
		public int getCount() {
			return mContents.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			TextView l_tv = null;
			
			convertView = m_inflater.inflate(R.layout.drawer_layout, null);
			l_tv = (TextView) convertView.findViewById(R.id.content);
			ImageView l_iv = (ImageView) convertView.findViewById(R.id.icon);
			l_tv.setText(mContents[position]);
			l_iv.setBackgroundDrawable(m_cont.getResources().getDrawable(mIcon[position]));
				
			RelativeLayout parentLayout = (RelativeLayout) convertView.findViewById(R.id.parentLayout);
			parentLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MainActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
					switch (position) {
					case 0:
						Intent intent = new Intent(m_cont,StartScreen.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
						m_cont.startActivity(intent);
						break;
					case 1:
						
						break;
					case 2:
						Intent walletIntent = new Intent(m_cont,WalletFragment.class);
						walletIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
						m_cont.startActivity(walletIntent);
						break;
					case 3:
						Intent settingIntent = new Intent(m_cont,SettingsPage.class);
						settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
						m_cont.startActivity(settingIntent);
						break;
						
					case 7:
						logOut();
						break;
					default:
						break;
					} 
					if(activity.getClass().equals(MainActivity.class) == false) {
						activity.finish();
					}
				}
			});
			
			return convertView;
		}

		private void logOut() {
			switch (m_cont.getSharedPreferences(myPreferences, Context.MODE_PRIVATE).getString(LOGIN_TYPE, "")) {
			case "gmail":
				if (mGoogleApiClient.isConnected()) {
				      Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				      Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
				      mGoogleApiClient.disconnect();
				      mGoogleApiClient.connect();
				 }
			case "facebook":
				Session session = Session.getActiveSession();
			    if (session != null) {
			        if (!session.isClosed()) {
			            session.closeAndClearTokenInformation();
			            //clear your preferences if saved
			        }
			    } else {
			        session = new Session(m_cont);
			        Session.setActiveSession(session);
			        session.closeAndClearTokenInformation();
			    }
				break;
			case "custom":
				
				break;
			default:
				break;
			}
			m_cont.getSharedPreferences(myPreferences, Context.MODE_PRIVATE).edit().remove(LOGIN_TYPE).commit();
			m_cont.stopService(new Intent(m_cont,LockScreenService.class));
			m_cont.stopService(new Intent(m_cont,MyService.class));
			m_cont.stopService(new Intent(m_cont,NetworkChangeListener.class));
			db.deleteAllAds();
			Intent splashIntent = new Intent(m_cont, SplashScreen.class);
			splashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			m_cont.startActivity(splashIntent);
		}
		
	}
