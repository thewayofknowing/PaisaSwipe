package com.example.backup;

import com.example.backup.Views.TitleBar;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SignUp extends Activity {
	
	private ImageView s_leftNavButton = null; 
	private EditText s_searchText = null;
	private ImageView s_searchIcon = null;
	private TextView s_title = null;
	private ImageView s_cross = null;
	private RelativeLayout s_searchLayout = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		initTitle();
	}

	private void initTitle() {
		RelativeLayout title = (RelativeLayout) findViewById(R.id.titleBar);
		TitleBar tb = new TitleBar(this,title);
		s_leftNavButton = tb.getLeftOptionsImgBtn();
		
		s_searchText = tb.getSearchEditText();
		s_searchText.setVisibility(View.GONE);
		s_searchText.setEnabled(false);
		
		s_searchLayout = tb.getSearchLayout();
		s_searchLayout.setVisibility(View.GONE);
		s_searchLayout.setEnabled(false);
		
		s_searchIcon = tb.getSearchIcon();
		s_searchLayout.setVisibility(View.GONE);
		s_searchLayout.setEnabled(false);
		
		s_cross = tb.getCross();
		s_searchLayout.setVisibility(View.GONE);
		s_searchLayout.setEnabled(false);
		
	}
	
}

