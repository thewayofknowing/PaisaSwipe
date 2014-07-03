package com.example.backup.Views;

import com.example.backup.R;
import com.example.backup.constants.Constants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleBar extends ViewGroup implements Constants {

	private static ImageView leftNavButton = null;
	private static EditText searchText = null;
	private static ViewGroup viewGroup = null;
	private static ImageView searchIcon = null;
	private static TextView title = null;
	private static RelativeLayout search_layout = null;
	private static ImageView cross = null;
	
	public TitleBar(Context context, ViewGroup v) {
		super(context);
		initTitleBar(context,v);
	}
	
	public static void initTitleBar(Context context,ViewGroup v) {
		LayoutInflater infl = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewGroup = (ViewGroup) infl.inflate(R.layout.title, v, true);
		leftNavButton = (ImageView) viewGroup.findViewById(R.id.navButton);
		searchText = (EditText) viewGroup.findViewById(R.id.search);
		searchIcon = (ImageView) viewGroup.findViewById(R.id.search_icon);
		title = (TextView) viewGroup.findViewById(R.id.title);
		cross = (ImageView) viewGroup.findViewById(R.id.cross);
		search_layout = (RelativeLayout) viewGroup.findViewById(R.id.search_layout);
	}
	
	public ImageView getLeftOptionsImgBtn() {
		return leftNavButton;
	}

	public EditText getSearchEditText() {
		return searchText;
	}
	
	public ImageView getSearchIcon() {
		return searchIcon;
	}
	
	public TextView getTitle() {
		return title;
	}
	
	public ImageView getCross() {
		return cross;
	}
	
	public RelativeLayout getSearchLayout() {
		return search_layout;
	}
	
	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}
	
}
