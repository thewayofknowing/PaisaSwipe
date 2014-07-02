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
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("ad_id", stats.getAdId() + ""));
			nameValuePairs.add(new BasicNameValuePair("comp_id", stats.getCompanyId() + ""));
			nameValuePairs.add(new BasicNameValuePair("appeared_at", stats.getAppearedAtTime()));
			nameValuePairs.add(new BasicNameValuePair("swiped_at", stats.getSwipedAtTime()));
			nameValuePairs.add(new BasicNameValuePair("clicked_at", stats.getClickedAtTime()));
			nameValuePairs.add(new BasicNameValuePair("completion_time", stats.getCompletionTime() + ""));
			nameValuePairs.add(new BasicNameValuePair("previews", stats.getPreviews() + ""));
			
			//nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
			Post post = new Post(context, server_url, nameValuePairs);
			post.execute();
		}
		else {
			DataBaseHelper db = new DataBaseHelper(context);
			db.addStat(stats);
		}
		return null;
	}
}
