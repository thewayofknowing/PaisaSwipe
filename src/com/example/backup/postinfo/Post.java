package com.example.backup.postinfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.backup.constants.Constants;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Post extends AsyncTask<String, String, String> implements Constants{

	Activity activity;
	
	public Post(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://54.187.181.173/start_up/insert_userDetails");

		try {
			// Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        nameValuePairs.add(new BasicNameValuePair("city",params[0]));
	        nameValuePairs.add(new BasicNameValuePair("provider", params[1]));
	        nameValuePairs.add(new BasicNameValuePair("contact", params[2]));	        
	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        HttpResponse response = client.execute(post);
	        if (response.getStatusLine().getStatusCode() == 200) {
	        	return "true";
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "error:" + e.getMessage();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (result!=null && result.equals("true")) {
			Log.d(TAG,"User Info Posted");
			activity.getSharedPreferences(myPreferences, Context.MODE_PRIVATE).edit().putBoolean("DataPosted", true);							
		}
		super.onPostExecute(result);
	}
	
}