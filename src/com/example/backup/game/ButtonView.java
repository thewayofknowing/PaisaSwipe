/*******************************************************************************
 * Copyright 2013 Marian Schedenig
 *
 * This file is part of n Tile Puzzle.
 *
 * n Tile Puzzle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * n Tile Puzzle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * n Tile Puzzle. If not, see http://www.gnu.org/licenses/.
 *******************************************************************************/
package com.example.backup.game;

import com.example.backup.R;

import android.content.Intent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ButtonView extends FrameLayout
{
	private View contentView;

	public ButtonView(final PuzzleActivity context, View contentView)
	{
		super(context);
		
		this.contentView = contentView;
		contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(contentView);
		
		RelativeLayout overlay = new RelativeLayout(context);
		overlay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(overlay);
		
		final ImageView btnMenu = new ImageView(getContext());
		btnMenu.setImageDrawable(getResources().getDrawable(R.drawable.preview_button));
		btnMenu.setClickable(true);
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layoutParams.setMargins(0, 1, 0, 2);
		btnMenu.setLayoutParams(layoutParams);
		btnMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getContext(), "Generating Preview...", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context,Preview.class);
				context.startActivity(intent);
			}
				
		});
		
		overlay.addView(btnMenu);
		
	}

	public View getContentView()
	{
		return contentView;
	}
}
