package com.example.backup.listeners;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.backup.constants.Constants;
import com.example.backup.data.Stats;
import com.example.backup.db.DataBaseHelper;
import com.example.backup.postinfo.Post;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

public class NetworkChangeListener extends Service implements Constants {

	BroadcastReceiver networkStateReceiver;
	DataBaseHelper db;
	public static Boolean connected = false;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		networkStateReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				//String connected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)?"DisConnected":"Connected";
				if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false) == false) {
					connected = true;
					db = new DataBaseHelper(context);
					List<Stats> stats = db.getAllStats();
					for(Stats item: stats) {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("user_id", item.getUserId() + ""));
						nameValuePairs.add(new BasicNameValuePair("adv_id", item.getAdId() + ""));
						nameValuePairs.add(new BasicNameValuePair("flag", item.getType() + ""));
						nameValuePairs.add(new BasicNameValuePair("company_id", item.getCompanyId() + ""));
						nameValuePairs.add(new BasicNameValuePair("appeared_at", item.getAppearedAtTime()));
						nameValuePairs.add(new BasicNameValuePair("swiped_at", item.getSwipedAtTime()));
						nameValuePairs.add(new BasicNameValuePair("clicked_at", item.getClickedAtTime()));
						//nameValuePairs.add(new BasicNameValuePair("completion_time", item.getCompletionTime() + ""));
						//nameValuePairs.add(new BasicNameValuePair("previews", item.getPreviews() + ""));
						Log.d(TAG, item.getUserId() + ":" + item.getAdId() + ":" + item.getCompanyId());
						Post post = new Post(context, server_url + post_stats_page, nameValuePairs);
						post.execute();
						db.deleteStat(item);
					}
				}
				else {
					connected = false;
				}
			}
		};

		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);        
		registerReceiver(networkStateReceiver, filter);
		startForeground(22, new Notification());
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(networkStateReceiver);
		stopForeground(true);
		super.onDestroy();
	}

}
