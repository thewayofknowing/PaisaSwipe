package com.example.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.backup.Views.TitleBar;
import com.example.backup.backgroundtasks.LockScreenService;
import com.example.backup.backgroundtasks.MyService;
import com.example.backup.constants.Constants;
import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsPage extends Activity implements Constants{

	private ImageView s_leftNavButton = null; 
	private ImageView s_searchIcon = null;
	private TextView s_title = null;
	private RelativeLayout s_searchLayout = null;
	
	private SharedPreferences s_sharedPref = null;
	private Editor s_editor;
	private Switch s_lockScreenSwitch,s_appLockSwitch;
	
	ExpandableListAdapter s_listAdapter;
    ExpandableListView s_expListView;
    List<String> s_listDataHeader;
    HashMap<String, List<String>> s_listDataChild;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_page);
				
		s_sharedPref = getSharedPreferences(myPreferences, MODE_PRIVATE);
		s_editor = s_sharedPref.edit();
		
		// get the listview
        s_expListView = (ExpandableListView) findViewById(R.id.lvExp);
        // preparing list data
        prepareListData();
        s_listAdapter = new com.example.backup.adapters.ExpandableListAdapter(getBaseContext(), s_listDataHeader, s_listDataChild);
        
        // setting list adapter
        s_expListView.setAdapter(s_listAdapter);
        
        s_expListView.setOnChildClickListener(new OnChildClickListener() {
        	 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                
            	switch (childPosition) {
				case 0:
					
					break;
				case 1:
					startActivity(new Intent(getBaseContext(),SetScreenLock.class));
					break;
				case 2:
					
					break;
				default:
					break;
				}
            	
                return false;
            }
        });
		
		initTitle();
		initSwitchListeners();
		
	}

	private void initTitle() {
		RelativeLayout title = (RelativeLayout) findViewById(R.id.titleBar);
		TitleBar tb = new TitleBar(this,title);
		
		s_leftNavButton = tb.getLeftOptionsImgBtn();
		
		s_searchLayout = tb.getSearchLayout();
		s_searchLayout.setVisibility(View.GONE);
		for(int i=0; i<s_searchLayout.getChildCount(); i++) {
			s_searchLayout.getChildAt(i).setEnabled(false);
		}
		
		s_searchIcon = tb.getSearchIcon();
		s_searchIcon.setVisibility(View.GONE);
		s_searchIcon.setEnabled(false);
	}
	
	private void initSwitchListeners() {
		s_lockScreenSwitch = (Switch) findViewById(R.id.screenLockSwitch);
		if (s_sharedPref.getBoolean(SCREENLOCK_ACTIVATED, true)) {
			s_lockScreenSwitch.setChecked(true);
		}
		s_lockScreenSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
				    LockScreenService.enableLock();
				}
				else {
					LockScreenService.disableLock();
				}
				s_editor.putBoolean(SCREENLOCK_ACTIVATED, isChecked).commit();
			}
		});
		
		s_appLockSwitch = (Switch) findViewById(R.id.appLockSwitch);
		if (s_sharedPref.getBoolean(APPLOCK_ACTIVATED, false)) {
			s_appLockSwitch.setChecked(true);
		}
		s_appLockSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if(isChecked) {
					MyService.enableLock();
				}
				else {
					MyService.disableLock();
				}
				s_editor.putBoolean(APPLOCK_ACTIVATED, isChecked).commit();
			}
		});
		
	}
	
	private void prepareListData() {
        s_listDataHeader = new ArrayList<String>();
        s_listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        s_listDataHeader.add("Set App Lock");
       
        List<String> items = new ArrayList<String>();
        items.add("None");
        items.add("Pattern");
        items.add("Password");
        
        s_listDataChild.put(s_listDataHeader.get(0), items); // Header, Child data
    }
	
	protected void onDestroy() {
		super.onDestroy();
	};
		
	
}
