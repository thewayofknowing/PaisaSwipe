package com.example.backup;

import com.haibison.android.lockpattern.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.*;

import java.util.*;

public class LockPattern extends Activity {

	private Button createPat;
	private Button usePat;
	private static final int REQ_CREATE_PATTERN = 1;
	private static final int REQ_ENTER_PATTERN = 2;
	Intent intent;
	char[] savedPattern;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pattern);

		createPat = (Button) findViewById(R.id.creatP);
		createPat.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN,
						null, getApplicationContext(),
						LockPatternActivity.class);
				startActivityForResult(intent, REQ_CREATE_PATTERN);

			}
		});

		usePat = (Button) findViewById(R.id.useP);
		usePat.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				intent = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null,
				        getApplicationContext(), LockPatternActivity.class);
				intent.putExtra(LockPatternActivity.EXTRA_PATTERN, savedPattern);
				startActivityForResult(intent, REQ_ENTER_PATTERN);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_CREATE_PATTERN: {
			if (resultCode == RESULT_OK) {
				char[] pattern = data
						.getCharArrayExtra(LockPatternActivity.EXTRA_PATTERN);
				savedPattern = pattern;
			}
			break;
		}// REQ_CREATE_PATTERN
		 case REQ_ENTER_PATTERN: {
		        /*
		         * NOTE that there are 4 possible result codes!!!
		         */
		        switch (resultCode) {
		        case RESULT_OK:
		            // The user passed
		            break;
		        case RESULT_CANCELED:
		            // The user cancelled the task
		            break;
		        case LockPatternActivity.RESULT_FAILED:
		            // The user failed to enter the pattern
		            break;
		        case LockPatternActivity.RESULT_FORGOT_PATTERN:
		            // The user forgot the pattern and invoked your recovery Activity.
		            break;
		        }
		        int retryCount = data.getIntExtra(
		                LockPatternActivity.EXTRA_RETRY_COUNT, 0);

		        break;
		 }
		}
		
		
	}

}
