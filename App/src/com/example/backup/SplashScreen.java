package com.example.backup;

import com.example.backup.constants.Constants;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;


public class SplashScreen extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, Constants {

	  Boolean s_onSplash = true;
	  LinearLayout signup_screen, loading_screen;
	  String s_personName;
	  String s_email;
	  int s_gender;
	
	  /* Request code used to invoke sign in user interactions. */
	  private static final int RC_SIGN_IN = 0;
	  
	  /* Client used to interact with Google APIs. */
	  private GoogleApiClient mGoogleApiClient;

	  /* A flag indicating that a PendingIntent is in progress and prevents
	   * us from starting further intents.
	   */
	  private boolean mIntentInProgress;
	  /* Track whether the sign-in button has been clicked so that we know to resolve
	   * all issues preventing sign-in without waiting.
	   */
	  private boolean mSignInClicked;

	  /* Store the connection result from onConnectionFailed callbacks so that we can
	   * resolve them when the user clicks sign-in.
	   */
	  private ConnectionResult mConnectionResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_LOGIN)
        .build();
		
		findViewById(R.id.sign_in_button_google).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG,"clicked");
				 if ( !mGoogleApiClient.isConnecting()) {
					    Log.d(TAG,"connected");
					    mSignInClicked = true;
					    resolveSignInError();
				 }
			}
		});
		
		
		/* ****************************fACEBOOK LOGIN HERE ******************************************* */
		
		findViewById(R.id.sign_in_button_facebook).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// start Facebook Login
				Session.openActiveSession(SplashScreen.this, true, new Session.StatusCallback() {

				      // callback when session changes state
				      @Override
				      public void call(Session session, SessionState state, Exception exception) {
				        if (session.isOpened()) {

				          // make request to the /me API
				          
				          Request.newMeRequest(session, new Request.GraphUserCallback() {

				            // callback after Graph API response with user object
							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								Toast.makeText(getBaseContext(), "Welcome! " + user.getName(), Toast.LENGTH_LONG).show();
							}
				          }).executeAsync();
				        }
				      }
				    });
				
			}
		});
		
		Animation close_splash = AnimationUtils.loadAnimation(getBaseContext(), R.anim.close_splash);
		Animation open_signup = AnimationUtils.loadAnimation(getBaseContext(), R.anim.open_signup);
		
		initSplash();
		
  }

	private void initSplash() {
		
		 loading_screen = (LinearLayout) findViewById(R.id.loading_screen);
		 signup_screen = (LinearLayout) findViewById(R.id.signup);
		 signup_screen.setVisibility(View.GONE);
		 for(int i=0; i< signup_screen.getChildCount();i++) {
			 signup_screen.getChildAt(i).setEnabled(false);
		 }
		
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		    @Override
		    public void run() {
		    	 for(int i=0; i< signup_screen.getChildCount();i++) {
					 signup_screen.getChildAt(i).setEnabled(true);
				 }
		    	Animation close_splash = AnimationUtils.loadAnimation(getBaseContext(), R.anim.close_splash);
				Animation open_signup = AnimationUtils.loadAnimation(getBaseContext(), R.anim.open_signup);
				//findViewById(R.id.containerLL).startAnimation(close_splash);
				loading_screen.startAnimation(close_splash);
				signup_screen.startAnimation(open_signup);
				signup_screen.setVisibility(View.VISIBLE);
				s_onSplash = false;
				startActivity(new Intent(SplashScreen.this,MainActivity.class));
		    }
		}, 3000);
		
	}
	
	@Override
	public void onBackPressed() {
		if (s_onSplash) {
			super.onBackPressed();
		}
		else {
			 for(int i=0; i< signup_screen.getChildCount();i++) {
				 signup_screen.getChildAt(i).setEnabled(false);
			 }
			 Animation open_splash = AnimationUtils.loadAnimation(getBaseContext(), R.anim.open_splash);
			 Animation close_signup = AnimationUtils.loadAnimation(getBaseContext(), R.anim.close_signup);
			 loading_screen.startAnimation(open_splash);
			 signup_screen.startAnimation(close_signup);
			 signup_screen.setVisibility(View.GONE);
			 s_onSplash = true;
			 return;
		}
		
	}
	
	/* A helper method to resolve the current ConnectionResult error. */
	private void resolveSignInError() {
	  if (mConnectionResult.hasResolution()) {
	    try {
	      mIntentInProgress = true;
	      mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
	    } catch (SendIntentException e) {
	      // The intent was canceled before it was sent.  Return to the default
	      // state and attempt to connect to get an updated ConnectionResult.
	      mIntentInProgress = false;
	      mGoogleApiClient.connect();
	    }
	  }
	}
	
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		  if (requestCode == RC_SIGN_IN) {
			    if (responseCode != RESULT_OK) {
			      mSignInClicked = false;
			    }
			    mIntentInProgress = false;
			    if (!mGoogleApiClient.isConnecting()) {
			      mGoogleApiClient.connect();
			    }
		  }
		  else {
		      Session.getActiveSession().onActivityResult(this, requestCode, responseCode, intent);
		  }
		  
	}
	
	@Override
	protected void onStart() {
	    //mGoogleApiClient.connect();
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}


	public void onConnectionFailed(ConnectionResult result) {
		  if (!mIntentInProgress) {
		    // Store the ConnectionResult so that we can use it later when the user clicks
		    // 'sign-in'.
		    mConnectionResult = result;

		    if (mSignInClicked) {
		      // The user has already clicked 'sign-in' so we attempt to resolve all
		      // errors until the user is signed in, or they cancel.
		      resolveSignInError();
		    }
		  }
	}


	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		 if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			    s_personName = currentPerson.getDisplayName();
			    s_gender = currentPerson.getGender();
			    //String personPhoto = currentPerson.getImage();
			    s_email = Plus.AccountApi.getAccountName(mGoogleApiClient);
			    //Toast.makeText(this, personName, Toast.LENGTH_LONG).show();
			    Toast.makeText(getApplicationContext(), s_email, Toast.LENGTH_LONG).show();
			    Log.d(TAG,s_email);
			    //gotoSignupForm();
			  }
	}
	

	@Override
	public void onConnectionSuspended(int arg0) {
		
	}
	
	/*
	 * Check connectivity of Internet
	 */
	public static boolean isConnected(Activity context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info!=null) {
			if(info.isConnected()) {
				return true;
			}
		}
		return false;
	}
	
	private void gotoSignupForm() {
		String[] gender = {"male","female","other"};
		Intent signup_intent = new Intent(getBaseContext(),SignUp.class);
		signup_intent.putExtra("name", s_personName);
		signup_intent.putExtra("email", s_email);
		signup_intent.putExtra("gender", gender[s_gender]);
		startActivity(signup_intent);
	}


	
}
