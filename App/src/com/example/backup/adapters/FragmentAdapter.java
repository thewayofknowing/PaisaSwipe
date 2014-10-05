package com.example.backup.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import com.example.backup.WalletPage; 

public class FragmentAdapter extends FragmentPagerAdapter {

	String tag = "PagerAdapt";

	public FragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Log.i(tag, "getItem called" + position);
		switch (position) {
		case 0:
			return WalletPage.newInstance();
		case 1:
			return WalletPage.newInstance();
		
		default:
			return WalletPage.newInstance();
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

}
