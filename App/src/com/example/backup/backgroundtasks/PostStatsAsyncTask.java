package com.example.backup.backgroundtasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.backup.constants.Constants;
import com.example.backup.data.Stats;
import com.example.backup.db.DataBaseHelper;
import com.example.backup.listeners.NetworkChangeListener;
import com.example.backup.postinfo.Post;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class PostStatsAsyncTask extends AsyncTask<String, String, String> implements Constants {

	Stats stats;
	Context context;
	
	public PostStatsAsyncTask(Stats stats, Context context) {
		this.stats = stats;
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
        
		if(NetworkChangeListener.connected){
			Log.d(TAG,"Post Internet:" + stats.toString());
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", stats.getUserId() + ""));
			nameValuePairs.add(new BasicNameValuePair("adv_id", stats.getAdId() + ""));
			nameValuePairs.add(new BasicNameValuePair("flag", stats.getType() + ""));
			nameValuePairs.add(new BasicNameValuePair("company_id", stats.getCompanyId() + ""));
			nameValuePairs.add(new BasicNameValuePair("appeared_at", stats.getAppearedAtTime()));
			nameValuePairs.add(new BasicNameValuePair("swiped_at", stats.getSwipedAtTime()));
			nameValuePairs.add(new BasicNameValuePair("clicked_at", stats.getClickedAtTime()));
			//nameValuePairs.add(new BasicNameValuePair("completion_time", stats.getCompletionTime() + ""));
			//nameValuePairs.add(new BasicNameValuePair("previews", stats.getPreviews() + ""));
			
			Post post = new Post(context, server_url + post_stats_page , nameValuePairs);
			post.execute();
		}
		else {
			Log.d(TAG,"Post Database:" + stats.toString());
			DataBaseHelper db = new DataBaseHelper(context);
			db.addStat(stats);
		}
		return null;
	}
}
