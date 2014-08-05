package com.example.backup.postinfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.backup.constants.Constants;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class Post extends AsyncTask<String, String, String> implements Constants{

	Context s_activity;
	List<NameValuePair> s_nameValuePairs;
	String s_url;
	
	public Post(Context activity, String url, List<NameValuePair> nameValuePairs) {
		this.s_activity = activity;
		this.s_nameValuePairs = nameValuePairs;
		this.s_url = url;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(s_url);

		try {
			// Add your data        
	        post.setEntity(new UrlEncodedFormEntity(s_nameValuePairs));
	        HttpResponse response = client.execute(post);
	        return response.getStatusLine().getStatusCode() + "";
	        
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
		Log.d(TAG,"Code: " + result);
		if (result!=null && result.equals("200")) {
			Log.d(TAG,"Posted Successfully");
			//s_activity.getSharedPreferences(myPreferences, Context.MODE_PRIVATE).edit().putBoolean("DataPosted", true);							
		}
		super.onPostExecute(result);
	}
	
}