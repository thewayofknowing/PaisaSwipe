package com.example.backup;

import android.app.Activity;
import android.app.AlertDialog;
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

public class Login extends Activity {
	
	private static final String PERMISSION = "publish_actions";
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
	private LoginButton loginButton;
	private TextView greeting;
	private ProfilePictureView profilePictureView;
	private Button postStatusUpdateButton;
	private Button pickPlaceButton;
	PendingAction pendingAction = PendingAction.NONE;
	private ViewGroup controlsContainer;
	private GraphUser user;
	private GraphPlace place;
	private List<GraphUser> tags;
	private boolean canPresentShareDialog;
    
	private enum PendingAction {
	        NONE,
	        POST_PHOTO,
	        POST_STATUS_UPDATE
	    }
	
	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

		    
		    loginButton = (LoginButton) findViewById(R.id.login_button);
	        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
	            @Override
	            public void onUserInfoFetched(GraphUser user) {
	                Login.this.user = user;
	                updateUI();
	                // It's possible that we were waiting for this.user to be populated in order to post a
	                // status update.
	                handlePendingAction();
	            }
	        });
	        
	        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
	        greeting = (TextView) findViewById(R.id.greeting);

	        postStatusUpdateButton = (Button) findViewById(R.id.postStatusUpdateButton);
	        postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	                onClickPostStatusUpdate();
	            }
	        });

	               
	        canPresentShareDialog = FacebookDialog.canPresentShareDialog(this,
	        FacebookDialog.ShareDialogFeature.SHARE_DIALOG);
	        
		    // start Facebook Login
		   /* Session.openActiveSession(this, true, new Session.StatusCallback() {

		      // callback when session changes state
		      @Override
		      public void call(Session session, SessionState state, Exception exception) {
		        if (session.isOpened()) {

		          // make request to the /me API
		          Request.newMeRequest(session, new Request.GraphUserCallback() {

		            // callback after Graph API response with user object
		            @Override
		            public void onCompleted(GraphUser user, Response response) {
		              if (user != null) {
		                TextView welcome = (TextView) findViewById(R.id.welcome);
		                welcome.setText("Hello " + user.getName() + "!");
		              }
		            }
		          }).executeAsync();
		        }
		      }
		    });
		  }

		  @Override
		  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		      super.onActivityResult(requestCode, resultCode, data);
		      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		  }*/

		}
	 
     
    private void updateUI() {
        Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());

        postStatusUpdateButton.setEnabled(enableButtons || canPresentShareDialog);
        
        if (enableButtons && user != null) {
            profilePictureView.setProfileId(user.getId());
            greeting.setText(getString(R.string.hello_user, user.getFirstName()));
        } else {
            profilePictureView.setProfileId(null);
            greeting.setText(null);
        }
    }

    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }

    private interface GraphObjectWithId extends GraphObject {
        String getId();
    }

    private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
        String title = null;
        String alertMessage = null;
        if (error == null) {
            title = getString(R.string.success);
            String id = result.cast(GraphObjectWithId.class).getId();
            alertMessage = getString(R.string.successfully_posted_post, message, id);
        } else {
            title = getString(R.string.error);
            alertMessage = error.getErrorMessage();
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(alertMessage)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void onClickPostStatusUpdate() {
        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
    }

    private FacebookDialog.ShareDialogBuilder createShareDialogBuilderForLink() {
        return new FacebookDialog.ShareDialogBuilder(this)
                .setName("Hello Facebook")
                .setDescription("The 'Hello Facebook' sample application showcases simple Facebook integration")
                .setLink("http://developers.facebook.com/android");
    }

    private void postStatusUpdate() {
        if (canPresentShareDialog) {
            FacebookDialog shareDialog = createShareDialogBuilderForLink().build();
           
        } else if (user != null && hasPublishPermission()) {
            final String message = getString(R.string.status_update, user.getFirstName(), (new Date().toString()));
            Request request = Request
                    .newStatusUpdateRequest(Session.getActiveSession(), message, place, tags, new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            showPublishResult(message, response.getGraphObject(), response.getError());
                        }
                    });
            request.executeAsync();
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }

    

  
     

   

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }

    private void performPublish(PendingAction action, boolean allowNoSession) {
        Session session = Session.getActiveSession();
        if (session != null) {
            pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
                handlePendingAction();
                return;
            } else if (session.isOpened()) {
                // We need to get new permissions, then complete the action when we get called back.
                session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISSION));
                return;
            }
        }

        if (allowNoSession) {
            pendingAction = action;
            handlePendingAction();
        }
    }
}