package com.example.backup.adapters;

import java.util.List;

import com.example.backup.MainActivity;
import com.example.backup.R;
import com.example.backup.R.drawable;
import com.example.backup.R.id;
import com.example.backup.R.layout;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.*;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> implements Constants {
	private final Activity context;
	private final List<String> s_appLabels;
	private List<String> s_packageNames;
	private final List<Drawable> s_icons;
	private boolean is_list;
	SharedPreferences s_sharedPreferences;
	static class ViewHolder {
		TextView txtTitle;
		ImageView imageView;
		Switch switchButton;
	}
	MyService s_myService;

	
	@Override
	public int getCount() {
		if (is_list) {
			return s_appLabels.size();
		}
		else {
			return 1;
		}
	}
	 
	ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
	        
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
	        s_myService = null;
		}
	};
	
	/*
	 * @params: appLabels - Application labels for the installed applications
	 * @params: icons- set of icons for the list of applications installed
	 */
	public CustomListAdapter(Activity context, List<Drawable> icons, List<String> objects, List<String> names, Boolean isList) {
		super(context, R.layout.list_element, objects);
		this.context = context;
		this.s_appLabels = objects;
		this.s_icons = icons;
		this.s_packageNames = names;
		this.is_list = isList;
		s_sharedPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	
	ViewHolder viewHolder;	
	
	LayoutInflater inflater = context.getLayoutInflater();
		if(is_list) {
		    convertView = inflater.inflate(R.layout.list_element, parent, false);
		    viewHolder = new ViewHolder();
			//convertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_small));
		    
			//Setting the various elements of a row
			viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.textView1);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
			viewHolder.switchButton = (Switch) convertView.findViewById(R.id.switch1);
		
			final String appName = s_appLabels.get(position);
			final String packageName = s_packageNames.get(position);
			viewHolder.txtTitle.setText(appName);
			Bitmap bitmap = ((BitmapDrawable)s_icons.get(position)).getBitmap();
		    viewHolder.imageView.setImageBitmap(getRoundedCornerBitmap(bitmap, ROUND_RADIUS));
			//viewHolder.imageView.setImageDrawable();
			
			if(MainActivity.app_ad_list.contains(packageName)) {
				viewHolder.switchButton.setChecked(true);
			}
			
			/*
			 * Image button click.. toggle app add on/off
			 * contentDescription : on/off, app ad on/off respectively
			 */
			viewHolder.switchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					if(isChecked) {
						MainActivity.app_ad_list.add(packageName);
					}
					else {
						MainActivity.app_ad_list.remove(packageName);
						MainActivity.app_ad_lock.remove(packageName);
					}
					Log.d(TAG,"Adapter:" + MainActivity.app_ad_list.toString() + "");
					   context.getSharedPreferences(myPreferences,Context.MODE_PRIVATE).edit().putStringSet(ACTIVATED_LIST, MainActivity.app_ad_list).commit();
					   context.getSharedPreferences(myPreferences,Context.MODE_PRIVATE).edit().putStringSet(LOCKED_LIST, MainActivity.app_ad_lock).commit();
					   MyService.stopAds();
					   MyService.initVariables();
					   MyService.startAds();
				}
			});
		}
		else {
			convertView = inflater.inflate(R.layout.search_no_match, parent, false);
			//convertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_small));
		}
	return convertView;
  }
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	
	
}
