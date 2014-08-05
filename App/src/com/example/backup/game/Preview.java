package com.example.backup.game;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.example.backup.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class Preview extends Activity {
	
	Drawable preview_image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.preview);
		
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
		Uri uri = PuzzleActivity.imageUri;
		try {
		    InputStream inputStream = getContentResolver().openInputStream(uri);
		    preview_image = Drawable.createFromStream(inputStream, uri.toString() );
		} catch (FileNotFoundException e) {
		    preview_image = getResources().getDrawable(R.drawable.poster);
		}
		iv.setBackgroundDrawable(preview_image);
		
	}
}
