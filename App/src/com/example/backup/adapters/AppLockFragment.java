package com.example.backup.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.backup.MainActivity;
import com.example.backup.constants.Constants;

import android.app.ListFragment;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AppLockFragment extends ListFragment implements Constants {

	private List<String> s_appLabels;
	private List<String> s_packageNames;
	private List<Drawable> s_appIcons;
	private CustomLockListAdapter adapter;
	private PackageManager s_packageManager;
	
	public AppLockFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		s_packageManager = getActivity().getBaseContext().getPackageManager();
		initVars();
		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);  
	}
	
	private void initVars() {
		
			Log.d(TAG,MainActivity.app_ad_list.size() + "ad list size");
		    if(MainActivity.app_ad_list.isEmpty()) {
		    	MainActivity.app_ad_lock.clear();
				adapter = new CustomLockListAdapter(getActivity(), s_appIcons, s_appLabels, s_packageNames, false);
		    }
		    else {
		    	s_packageNames = new ArrayList<String>();
		    	s_appLabels = new ArrayList<String>();
				s_appIcons = new ArrayList<Drawable>();
				s_packageNames.addAll(MainActivity.app_ad_list);
		    	for(String label: s_packageNames) {
					try {
						s_appIcons.add(s_packageManager.getApplicationIcon(label));
						s_appLabels.add("" + s_packageManager.getApplicationLabel(s_packageManager.getApplicationInfo(label, 0)));
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
				}	
				adapter = new CustomLockListAdapter(getActivity(), s_appIcons, s_appLabels, s_packageNames, true);
		    }
	}
	
	
}
