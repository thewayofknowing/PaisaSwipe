package com.example.backup.game;

import com.example.backup.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class StartScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_start);
		LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout bottom_bar = (RelativeLayout) findViewById(R.id.game_bottom_bar);
		inf.inflate(R.layout.game_bottom_bar, bottom_bar, true);
		bottom_bar.setClickable(false);
		ImageView iv = (ImageView) findViewById(R.id.image);
		iv.setBackgroundResource(R.drawable.poster);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		iv.getLayoutParams().height = (int)(display.getHeight()*(float)11/12);
		iv.requestLayout();
		ImageView start_game = (ImageView) findViewById(R.id.start_game);
		start_game.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),PuzzleActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finish();
			}
		});
	}
	
}
