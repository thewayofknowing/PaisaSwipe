package com.example.backup.Views;

import com.example.backup.R;
import com.example.backup.constants.Constants;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TitleBar extends ViewGroup implements Constants {

	public static ImageView leftNavButton = null;
	private static ViewGroup viewGroup = null;
	
	public TitleBar(Context context, ViewGroup v) {
		super(context);
		initTitleBar(context,v);
	}
	
	public static void initTitleBar(Context context,ViewGroup v) {
		LayoutInflater infl = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		viewGroup = (ViewGroup) infl.inflate(R.layout.title, v, true);
		leftNavButton = (ImageView) viewGroup.findViewById(R.id.navButton);
		
	}
	
	public ImageView getLeftOptionsImgBtn() {
		return leftNavButton;
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}
	
}
