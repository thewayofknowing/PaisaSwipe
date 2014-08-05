package com.example.backup.adapters;

import com.example.backup.constants.Constants;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter implements Constants {

	Context s_context;
	String[] s_listArray;
	LayoutInflater s_inflater;
	
	public SpinnerAdapter(Context context,String[] array) {
		this.s_context = context;
		this.s_listArray = array;
		this.s_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return s_listArray.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = s_inflater.inflate(com.example.backup.R.layout.spinner_item, null);
		TextView l_tv = (TextView) convertView.findViewById(com.example.backup.R.id.spinnerItemTextView);
		l_tv.setText(s_listArray[position]);
		return convertView;
	}

}
