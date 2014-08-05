package com.example.backup.game;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.example.backup.R;
import com.example.backup.constants.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

public class Finish extends Activity implements Constants {
	
	Bitmap preview_image;
	ImageView goto_ad;
	ImageView next_ad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game_finish);
		
		String time = getIntent().getStringExtra("time");
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(GAME_CONGRATULATIONS + " in " + time);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int center = metrics.widthPixels/2;
		
		initDrawable();
		
		ImageView preview = (ImageView) findViewById(R.id.imageView1);
		//preview.setBackgroundDrawable(new BitmapDrawable(preview_image));;
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		float ratio = (float)preview_image.getWidth() / preview_image.getHeight();
		preview.getLayoutParams().height = (int) (0.75 * display.getHeight());
		preview.getLayoutParams().width = (int) (ratio * 0.75 * display.getHeight());
		preview.requestLayout();
		goto_ad = (ImageView) findViewById(R.id.button1);
		next_ad = (ImageView) findViewById(R.id.button2);
		
		initButtons();
		
	}
	
	private void initDrawable() {
		Uri uri = (Uri) getIntent().getExtras().get("URI");
		try {
		    InputStream inputStream = getContentResolver().openInputStream(uri);
		    preview_image = BitmapFactory.decodeStream(inputStream);
		} catch (FileNotFoundException e) {
		    //preview_image = getResources().getDrawable(R.drawable.back);
		}
	}
		
	private void initButtons() {
		
		next_ad.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(),PuzzleActivity.class);
				startActivity(intent);
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


