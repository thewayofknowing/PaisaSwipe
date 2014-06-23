package com.example.backup;

import java.util.List;

import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> implements Constants {
	private final Activity context;
	private final List<String> appLabels;
	private final List<Drawable> icons;
	SharedPreferences sharedPreferences;
	Drawable on,off;
	static class ViewHolder {
		TextView txtTitle;
		ImageView imageView;
		ImageView toggle;
	}
	
	@Override
	public int getCount() {
		return appLabels.size()+1;
	}
	 
	/*
	 * @params: appLabels - Application labels for the installed applications
	 * @params: icons- set of icons for the list of applications installed
	 */
	public CustomListAdapter(Activity context, List<Drawable> icons, List<String> objects) {
		super(context, R.layout.list_element, objects);
		this.context = context;
		this.appLabels = objects;
		this.icons = icons;
		sharedPreferences = context.getSharedPreferences(myPreferences, context.MODE_PRIVATE);
		on = context.getResources().getDrawable(R.drawable.on);
		off = context.getResources().getDrawable(R.drawable.off);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	
	ViewHolder viewHolder;	
	
	LayoutInflater inflater = context.getLayoutInflater();
    convertView = inflater.inflate(R.layout.list_element, parent, false);
    viewHolder = new ViewHolder();
	convertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_small));
    
	//Setting the various elements of a row
//	viewHolder.tb = (ToggleButton) convertView.findViewById(R.id.toggleButton1);
	viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.textView1);
	viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
	viewHolder.toggle = (ImageView) convertView.findViewById(R.id.imageView2); 
	
	if (position == 0) {
		viewHolder.txtTitle.setText("Applications");
		viewHolder.txtTitle.setTextColor(Color.rgb(33, 33, 204));
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.txtTitle.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.setMargins(6, 0, 0, 0);
		viewHolder.txtTitle.setLayoutParams(params);
		viewHolder.txtTitle.setTextSize(22);
		viewHolder.txtTitle.setTypeface(null, Typeface.BOLD);

		viewHolder.imageView.setVisibility(View.GONE);
		viewHolder.toggle.setVisibility(View.GONE);
	}
	else {
		final String appName = appLabels.get(position-1);
		viewHolder.txtTitle.setText(appName);
		viewHolder.imageView.setImageDrawable(icons.get(position-1));
		
		if(MainActivity.app_ad_list.contains(appName)) {
			viewHolder.toggle.setContentDescription("on");
			viewHolder.toggle.setBackgroundDrawable(on);
		}
		else {
			viewHolder.toggle.setBackgroundDrawable(off);
		}
		
		/*
		 * Image button click.. toggle app add on/off
		 * contentDescription : on/off, app ad on/off respectively
		 */
		viewHolder.toggle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v.getContentDescription().equals("off")) {
					v.setContentDescription("on");
					v.setBackgroundDrawable(on);
					MainActivity.app_ad_list.add(appName);
				}
				else {
					v.setBackgroundDrawable(off);;
					v.setContentDescription("off");
					MainActivity.app_ad_list.remove(appName);
				}
			   Log.d(TAG,"Adapter:" + MainActivity.app_ad_list.toString() + "");
			   context.getSharedPreferences(myPreferences,context.MODE_PRIVATE).edit().putStringSet(ACTIVATED_LIST, MainActivity.app_ad_list).commit();
			   if(MainActivity.sharedPreferences.getBoolean(STATUS, false)) {
				   Intent intent = new Intent(context, MyService.class);
				   context.stopService(intent);
				   context.startService(intent);
			   }
		 }
			
		});
	}
	
	return convertView;
  }
	
	
}
