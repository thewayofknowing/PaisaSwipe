package com.example.backup.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.backup.MainActivity;
import com.example.backup.MainActivity.Process;

import android.app.ListFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AppListFragment extends ListFragment {

	private List<String> s_appLabels;
	private List<String> s_packageNames;
	private List<Drawable> s_appIcons;
	List<Process> processes;
	private CustomListAdapter adapter;
	
	public AppListFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    initVars();
		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);  
	}
	
	private void initVars() {
		if(MainActivity.result_processes.isEmpty()) {
			adapter = new CustomListAdapter(getActivity(), s_appIcons, s_appLabels, s_packageNames, false);
		}
		else {
			s_appLabels = new ArrayList<String>();
			s_packageNames = new ArrayList<String>();
			s_appIcons = new ArrayList<Drawable>();
			for (Process process: MainActivity.result_processes) {
				s_appLabels.add(process.appLabel);
				s_appIcons.add(process.icon);
				s_packageNames.add(process.packageName);
			}
			adapter = new CustomListAdapter(getActivity(), s_appIcons, s_appLabels, s_packageNames, true);
		}
	}
	
}
