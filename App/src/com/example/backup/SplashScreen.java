package com.example.backup;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.example.backup.constants.Constants;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
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

public class SplashScreen extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, Constants {

	Boolean s_onSplash = true;
	LinearLayout signup_screen, loading_screen;
	String s_personName = "";
	String s_email = "";
	int s_gender = -1;
	String s_type = "custom";

	static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
	static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
	static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
	private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
	private String mEmail;

	public static enum Type {
		FOREGROUND, BACKGROUND, BACKGROUND_WITH_SYNC
	}



	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;

	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;

	/*
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;
	/*
	 * Track whether the sign-in button has been clicked so that we know to
	 * resolve all issues preventing sign-in without waiting.
	 */
	private boolean mSignInClicked;

	/*
	 * Store the connection result from onConnectionFailed callbacks so that we
	 * can resolve them when the user clicks sign-in.
	 */
	private ConnectionResult mConnectionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		/*
		File file = new File(getApplicationContext().getFilesDir().getAbsolutePath());
		for(File item: file.listFiles()) {
			Log.d(TAG,item.toString() + "");
		}
		*/
		
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		findViewById(R.id.signup_button_custom).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						s_type = "custom";
						gotoSignupForm();
					}
				});

		findViewById(R.id.sign_in_button_google).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.d(TAG, "clicked");
						if ( mGoogleApiClient.isConnecting()==false) {
							Log.d(TAG,"connected");
							mSignInClicked = true;
							resolveSignInError();
						}
					}
				});

		/*
		 * ****************************fACEBOOK LOGIN HERE
		 * ******************************************* LoginButton authButton =
		 * (LoginButton) findViewById(R.id.authButton); // set permission list,
		 * Don't foeget to add email
		 * authButton.setReadPermissions(Arrays.asList(
		 * "publish_actions","email")); // session state call back event /*
		 * findViewById(R.id.sign_in_button_facebook).setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * Session.openActiveSession(SplashScreen.this, false, new
		 * Session.StatusCallback() {
		 * 
		 * @Override public void call(Session session, SessionState state,
		 * Exception exception) {
		 * 
		 * if (session.isOpened()) { Log.i(TAG,"Access Token"+
		 * session.getAccessToken()); Request.executeMeRequestAsync(session, new
		 * Request.GraphUserCallback() {
		 * 
		 * @Override public void onCompleted(GraphUser user,Response response) {
		 * if (user != null) { s_personName = user.getName(); s_email =
		 * user.asMap().get("email").toString(); s_type = "facebook";
		 * gotoSignupForm(); } } }); }
		 * 
		 * } }); } });
		 */
		final LoginButton authButton = (LoginButton) findViewById(R.id.sign_in_button_facebook);
		authButton.setBackgroundResource(R.drawable.facebook_signup);
		authButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		authButton.setOnErrorListener(new OnErrorListener() {

			@Override
			public void onError(FacebookException error) {
				Log.i(TAG, "Error " + error.getMessage());
			}
		});
		// set permission list, Don't foeget to add email
		authButton.setReadPermissions(Arrays.asList("email"));
		// session state call back event
		authButton.setSessionStatusCallback(new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {

				if (session.isOpened()) {
					Log.i(TAG, "Access Token" + session.getAccessToken());
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {
										Log.i(TAG, "User ID " + user.getId());
										Log.i(TAG,
												"Email "
														+ user.asMap().get(
																"email"));
										s_personName = user.getName();
										s_email = user.asMap().get("email")
												.toString();
										s_type = "facebook";
										gotoSignupForm();
									}
								}
							});
				}

			}
		});

		initSplash();

	}

	private void initSplash() {

		loading_screen = (LinearLayout) findViewById(R.id.loading_screen);
		signup_screen = (LinearLayout) findViewById(R.id.signup);
		signup_screen.setVisibility(View.GONE);
		for (int i = 0; i < signup_screen.getChildCount(); i++) {
			signup_screen.getChildAt(i).setEnabled(false);
		}

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(getSharedPreferences(myPreferences, Context.MODE_PRIVATE).contains(LOGIN_TYPE) == false){
					for (int i = 0; i < signup_screen.getChildCount(); i++) {
						signup_screen.getChildAt(i).setEnabled(true);
					}
					Animation close_splash = AnimationUtils.loadAnimation(
							getBaseContext(), R.anim.close_splash);
					Animation open_signup = AnimationUtils.loadAnimation(
							getBaseContext(), R.anim.open_signup);
					// findViewById(R.id.containerLL).startAnimation(close_splash);
					loading_screen.startAnimation(close_splash);
					signup_screen.startAnimation(open_signup);
					signup_screen.setVisibility(View.VISIBLE);
					s_onSplash = false;
				}
				else {
					Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
					startActivity(mainIntent);
					finish();
				}
			}
		}, 2000);

	}
 /*
	@Override
	public void onBackPressed() {
		if (s_onSplash) {
			super.onBackPressed();
		} else {
			for (int i = 0; i < signup_screen.getChildCount(); i++) {
				signup_screen.getChildAt(i).setEnabled(false);
			}
			Animation open_splash = AnimationUtils.loadAnimation(
					getBaseContext(), R.anim.open_splash);
			Animation close_signup = AnimationUtils.loadAnimation(
					getBaseContext(), R.anim.close_signup);
			loading_screen.startAnimation(open_splash);
			signup_screen.startAnimation(close_signup);
			signup_screen.setVisibility(View.GONE);
			s_onSplash = true;
			return;
		}

	}
*/
	
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
		    if (resultCode != RESULT_OK) {
		      mSignInClicked = false;
		    }

		    mIntentInProgress = false;

		    if (!mGoogleApiClient.isConnecting()) {
		      mGoogleApiClient.connect();
		    }
		  }

		if(Session.getActiveSession() != null) Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	/*
	 * protected void onActivityResult(int requestCode, int responseCode, Intent
	 * intent) { if (requestCode == RC_SIGN_IN) { if (responseCode != RESULT_OK)
	 * { mSignInClicked = false; }
	 * 
	 * mIntentInProgress = false;
	 * 
	 * if (!mGoogleApiClient.isConnecting()) { mGoogleApiClient.connect(); } } }
	 */
	@Override
	protected void onStart() {
		mGoogleApiClient.connect();
		super.onStart();
	}

	@Override
	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress) {
			// Store the ConnectionResult so that we can use it later when the
			// user clicks
			// 'sign-in'.
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		//Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Person currentPerson = Plus.PeopleApi
					.getCurrentPerson(mGoogleApiClient);
			s_personName = currentPerson.getDisplayName();
		    s_gender = currentPerson.getGender();
			// String personPhoto = currentPerson.getImage();
			s_email = Plus.AccountApi.getAccountName(mGoogleApiClient);
			// Toast.makeText(this, personName, Toast.LENGTH_LONG).show();
			// Toast.makeText(getApplicationContext(), s_email,
			// Toast.LENGTH_LONG).show();
			s_type = "gmail";
			Log.d(TAG, s_email);
			gotoSignupForm();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}

	/*
	 * Check connectivity of Internet
	 */
	public static boolean isConnected(Activity context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			if (info.isConnected()) {
				return true;
			}
		}
		return false;
	}

	private void gotoSignupForm() {
		Intent signup_intent = new Intent(getBaseContext(), SignUp.class);
		signup_intent.putExtra("name", s_personName);
		signup_intent.putExtra("email", s_email);
		signup_intent.putExtra("type", s_type);
		startActivity(signup_intent);
		finish();
	}

	private boolean isDeviceOnline() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
}
