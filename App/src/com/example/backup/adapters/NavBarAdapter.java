package com.example.backup.adapters;

import com.example.backup.R;
import com.example.backup.R.id;
import com.example.backup.R.layout;
import com.example.backup.constants.Constants;
import com.example.backup.game.PuzzleActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavBarAdapter extends BaseAdapter implements Constants {

		private Context m_cont;
		private LayoutInflater m_inflater;

		public NavBarAdapter(Context a_cont) {
			m_cont = a_cont;
			m_inflater = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mContents.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			TextView l_tv = null;
			
			convertView = m_inflater.inflate(R.layout.drawer_layout, null);
			l_tv = (TextView) convertView.findViewById(R.id.content);
			ImageView l_iv = (ImageView) convertView.findViewById(R.id.icon);
			l_tv.setText(mContents[position]);
			l_iv.setBackground((m_cont.getResources().getDrawable(mIcon[position])));
				
			RelativeLayout parentLayout = (RelativeLayout) convertView.findViewById(R.id.parentLayout);
			parentLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(position==0) {
						Intent intent = new Intent(m_cont,PuzzleActivity.class);
						intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP);
						m_cont.startActivity(intent);
					}
				}
			});
			
			return convertView;
		}

	}
