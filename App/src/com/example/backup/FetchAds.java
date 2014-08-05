package com.example.backup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.backup.constants.Constants;
import com.example.backup.data.Advertisement;
import com.example.backup.db.DataBaseHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class FetchAds extends Activity implements Constants {
	
	int s_totalSize,s_totalDownloaded;
	List<Advertisement> s_adList;
	List<DownloadImageContainer> s_imageDownloadList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fetch_ads_loading_screen);
		((ImageView) findViewById(R.id.imageView1)).setBackgroundResource(R.drawable.grey_icon);
		
		Bundle extras = getIntent().getExtras();
		s_totalSize = 0;
		s_totalDownloaded = 0;
		File myDir = getApplicationContext().getFilesDir();  
		s_imageDownloadList = new ArrayList<DownloadImageContainer>();
		s_adList = new ArrayList<Advertisement>();
		
		try {
			JSONArray adListArray = new JSONArray(extras.getString("json"));
			for(int i=0; i<adListArray.length(); i++) {
				Advertisement ad = new Advertisement();
				
				JSONObject object = adListArray.getJSONObject(i);		
				String image_name = object.getString("image_name") + ".jpg";
				File file = new File(myDir.getAbsolutePath() + "/" + image_name);
				ad.setImageName(image_name);
				ad.setId(Integer.parseInt(object.getString("adv_id")));
				ad.setCompanyId(Integer.parseInt(object.getString("company_id")));
				ad.setURL(object.getString("adv_url"));
				ad.setImpressionCount(Integer.parseInt(object.getString("ad_impressions")));
				
				if(file.exists() == false) {
					Log.d(TAG,file.toString() + " file downloaded");
					DownloadImageContainer item = new DownloadImageContainer();
					item.image_size = Integer.parseInt(object.getString("image_size"));
					item.image_name = image_name;
					item.image_url = object.getString("image_path").replaceAll("\\\\", "");
					s_totalSize += item.image_size;
					s_imageDownloadList.add(item);
				}
				
				s_adList.add(ad);
				//s_adList.add(object.getString("image_path").replaceAll("\\", "")); // ad image url
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(s_imageDownloadList.isEmpty()) {
			Intent mainIntent = new Intent(getBaseContext(),MainActivity.class);
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(mainIntent);
			finish();
		}
		else {
			DownloadImagesTask downloadImageTask = new DownloadImagesTask(this);
			downloadImageTask.execute();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
		   Toast.makeText(getBaseContext(), "Please wait while Download Completes\nPress HOME to minimize", Toast.LENGTH_SHORT).show();
		   return true;
		}
		return false;
	}
	
	public class DownloadImageContainer {
		public String image_url,image_name;
		public int image_size;
	}
	
	public class DownloadImagesTask extends AsyncTask<String, String, String>{

		public static final int CONNECTION_TIMEOUT = 10000;
		Dialog s_dialog;
		ProgressBar s_progressBar;
		TextView s_progressText;
		Context context;
		int sumSize;
		
		public DownloadImagesTask(Context context) {
			this.context = context;
			s_dialog = new Dialog(context);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			s_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			s_dialog.setContentView(R.layout.download_dialog);
			s_dialog.setCancelable(false);
			s_progressBar = (ProgressBar) s_dialog.findViewById(R.id.progressBar1);
			s_progressBar.setMax(100);
			s_progressBar.setProgress(0);
			s_progressText = (TextView) s_dialog.findViewById(R.id.textView1);
			s_dialog.show();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			Log.d(TAG,"Background download started");
			for(DownloadImageContainer item: s_imageDownloadList) {
				getImageBitmap(item);
			}
			DataBaseHelper db = new DataBaseHelper(context);
			for(Advertisement ad: s_adList) {
				db.addAdvertisement(ad);;
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			float fraction_progress = (float)(Integer.parseInt(values[0]))/s_totalSize;
			s_progressBar.setProgress((int)(fraction_progress*100));
			s_progressText.setText((Integer.parseInt(values[0])/1024) + "/" + (s_totalSize/1024) + " KB Downloaded");
		}
		
		public synchronized void getImageBitmap(DownloadImageContainer d) {
			int count;
			try {
				URL url = new URL(d.image_url);
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(CONNECTION_TIMEOUT);
				conn.connect();
			    InputStream input = new BufferedInputStream(url.openStream());
			    byte data[] = new byte[1024];
			    byte sumData[] = new byte[d.image_size];
			    sumSize = 0;
			        while ((count = input.read(data)) != -1) {
			        	s_totalDownloaded += count;
			            publishProgress("" + s_totalDownloaded);
			            for(int i=0;i<count;i++) {
			            	sumData[sumSize++] = data[i];
			            }
			        }
			   // s_totalDownloaded +=  total;
			    File file = new File( context.getFilesDir().getAbsolutePath()  + "/" + d.image_name);
				FileOutputStream fos;
				fos = new FileOutputStream(file);
				fos.write(sumData);
				fos.close();
			} catch (IOException e) {
				return;
			}
		}
		
		protected void onPostExecute(String result) {
			s_dialog.dismiss();
			Toast.makeText(context, "Download Successful", Toast.LENGTH_LONG).show();
			Intent mainIntent = new Intent(context,MainActivity.class);
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(mainIntent);
			finish();
		};

	}
	
}
