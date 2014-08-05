package com.example.backup.adapters;

import java.util.List;

import com.example.backup.MainActivity;
import com.example.backup.R;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.*;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class CustomLockListAdapter extends ArrayAdapter<String> implements Constants {
	private final Activity context;
	private static List<String> appLabels;
	private static List<String> appSubLabels;
	private static List<String> packageNames;
	private static List<Drawable> icons;
	private boolean is_list;
	Editor edit;
	Typeface s_openSansSemiBold,s_openSansRegular;
	
	static class ViewHolder {
		TextView txtTitle;
		TextView txtInfo;
		ImageView imageView;
		Switch switchButton;
	}
	
	@Override
	public int getCount() {
		if (is_list) {
			return appLabels.size();
		}
		else {
			return 1;
		}
	}
	 
	/*
	 * @params: appLabels - Application labels for the installed applications
	 * @params: icons- set of icons for the list of applications installed
	 */
	public CustomLockListAdapter(Activity context, List<Drawable> icons, List<String> objects, List<String> subLabels, List<String> names, Boolean isList) {
		super(context, R.layout.list_element, objects);
		this.context = context;
		this.appLabels = objects;
		this.appSubLabels = subLabels;
		this.icons = icons;
		this.packageNames = names;
		this.is_list = isList;
		this.s_openSansSemiBold = Typeface.createFromAsset(context.getAssets(), OPEN_SANS_SEMIBOLD);
		this.s_openSansRegular = Typeface.createFromAsset(context.getAssets(), OPEN_SANS_REGULAR);
		edit = context.getSharedPreferences(myPreferences,Context.MODE_PRIVATE).edit();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	
	ViewHolder viewHolder = null;	
	
	LayoutInflater inflater = context.getLayoutInflater();
		if(is_list) {
			if (convertView == null) {
			    convertView = inflater.inflate(R.layout.list_element_lock, parent, false);
			    viewHolder = new ViewHolder();
				//convertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_small));
			    
				//Setting the various elements of a row
				viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.textView1);
				viewHolder.txtInfo = (TextView) convertView.findViewById(R.id.textView2);
				viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
				viewHolder.switchButton = (Switch) convertView.findViewById(R.id.switch1);
				convertView.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			final String appName = appLabels.get(position);
			final String packageName = packageNames.get(position);
			viewHolder.txtTitle.setTypeface(s_openSansSemiBold);
			viewHolder.txtTitle.setText(appName);
			viewHolder.txtInfo.setTypeface(s_openSansRegular);
			viewHolder.txtInfo.setText(appSubLabels.get(position));
			Bitmap bitmap = ((BitmapDrawable)icons.get(position)).getBitmap();
		    viewHolder.imageView.setImageBitmap(getRoundedCornerBitmap(bitmap, ROUND_RADIUS));
			
			if(MainActivity.app_ad_lock.contains(packageName)) {
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
						MainActivity.app_ad_lock.add(packageName);
					}
					else {
						MainActivity.app_ad_lock.remove(packageName);
					}
					   edit.putStringSet(LOCKED_LIST, MainActivity.app_ad_lock).commit();
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
