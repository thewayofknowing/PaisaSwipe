package com.example.backup;

import com.example.backup.Views.TitleBar;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.Constants;
import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetScreenLock extends Activity implements Constants {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_screen_lock);
		Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null,
		        getBaseContext(), LockPatternActivity.class);
		Settings.Security.setAutoSavePattern(getBaseContext(), true);
		startActivityForResult(intent, REQ_CREATE_PATTERN);
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
	    switch (requestCode) {
	    case REQ_CREATE_PATTERN: {
	        if (resultCode == RESULT_OK) {
	            char[] pattern = data.getCharArrayExtra(
	                    LockPatternActivity.EXTRA_PATTERN);
	            getSharedPreferences(myPreferences, Context.MODE_PRIVATE).edit().putInt(APP_LOCK_TYPE, 1).commit();
	            MyService.enableLock();
	        }
		    finish();
	        break;
	    }// REQ_CREATE_PATTERN
	    }
	}
	
}
