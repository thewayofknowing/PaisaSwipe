package com.example.backup.ads;

import com.example.backup.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.TableLayout;
import com.example.backup.extmedia.ExternalMedia;

public class AdLogic {

	private static Bitmap bitmap;
	private static ExternalMedia s_ext;
	
	public AdLogic(Activity context) {
		s_ext = ExternalMedia.getInstance();
	}
	
	public static Bitmap getImageUri(Activity context) {
		
		 DisplayMetrics metrics = new DisplayMetrics();
	     context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	     int dstWidth = (int)( metrics.widthPixels * metrics.density);
	     int dstHeight =(int)( metrics.heightPixels * metrics.density);
	     
	     bitmap = s_ext.loadImage("poster.jpg");
	     if(bitmap == null) bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.poster);
	     
	     //bitmap = Bitmap.createScaledBitmap(bitmap, (int) dstWidth,(int) dstHeight, true);
		 return bitmap;

	}
}
