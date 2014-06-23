package com.example.backup.game;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.example.backup.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

public class Finish extends Activity {
	
	Drawable preview_image;
	Button goto_ad,next_ad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game_finish);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int center = metrics.widthPixels/2;
		
		initDrawable();
		
		ImageView preview = (ImageView) findViewById(R.id.imageView1);
		preview.setBackgroundDrawable(preview_image);
		preview.getLayoutParams().height = (int) (metrics.heightPixels*0.65);
		preview.getLayoutParams().width = (int) (metrics.widthPixels*0.90);
		
		goto_ad = (Button) findViewById(R.id.button1);
		next_ad = (Button) findViewById(R.id.button2);
		
		initButtons();
		
	}
	
	private void initDrawable() {
		Uri uri = (Uri) getIntent().getExtras().get("URI");
		try {
		    InputStream inputStream = getContentResolver().openInputStream(uri);
		    preview_image = Drawable.createFromStream(inputStream, uri.toString() );
		} catch (FileNotFoundException e) {
		    preview_image = getResources().getDrawable(R.drawable.back);
		}
	}
	
	private void initButtons() {
		
		next_ad.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(),PuzzleActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				finish();
			}
		});
		
	
		goto_ad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getBaseContext(), "No Ads Available Yet", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}


