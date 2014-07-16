package com.example.backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.backup.Views.TitleBar;
import com.example.backup.constants.Constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends Activity implements Constants{
	
	private ImageView s_leftNavButton = null; 
	private EditText s_searchText = null;
	private ImageView s_searchIcon = null;
	private TextView s_title = null;
	private ImageView s_cross = null;
	private RelativeLayout s_searchLayout = null;
	
	private TextView s_emailError,s_nameError,s_passError,s_repassError,s_pincodeError;
	
	private RadioGroup s_genderButtonGroup = null;
	private Button s_signup;
	private EditText s_passwordEditText,s_repasswordEditText,s_nameEditText,s_emailEditText,s_pincodeEditText;
	private ProgressDialog s_progressDialog = null;
	private String s_progressDialogMessage;
	private List<NameValuePair> s_nameValuePairs;
	
	private SharedPreferences s_sharedPref = null;
	private Editor s_editor;
	
	String s_email;
	String s_name;
	String s_type;
	String s_gender;
	String s_password;
	String s_repassword;
	String s_pincode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		initTitle();
		initErrorTextViews();
		setSignupButtonListener();
		
		s_sharedPref = getSharedPreferences(myPreferences, MODE_PRIVATE);
		s_editor = s_sharedPref.edit();
		s_genderButtonGroup = (RadioGroup) findViewById(R.id.gender_radioGroup);
		s_nameEditText = (EditText) findViewById(R.id.name);
		s_passwordEditText = (EditText) findViewById(R.id.pass_input);
		s_repasswordEditText = (EditText) findViewById(R.id.repass_input);
		s_emailEditText = (EditText) findViewById(R.id.email);
		s_pincodeEditText = (EditText) findViewById(R.id.pincode);
		autoFillData();
		
		
	}
	
	private void setSignupButtonListener() {
		findViewById(R.id.signup_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String error = "";
				
				if(s_type.equals("custom")) {					  
					  
					  /* *****Email Validation***** */
					  s_email = s_emailEditText.getText().toString();
					  Pattern email_regex = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+[.][a-zA-Z0-9-.]+$");
			    	  Matcher m = email_regex.matcher(s_email);
			    	  if(m.matches() == false) {
			    		  error = "Not a Valid e-mail address";
			    		  s_emailError.setVisibility(View.VISIBLE);
			    		  s_emailError.setText(error);
			    		  //s_emailEditText.requestFocus();
			    	  }
			    	  else {
			    		  s_emailError.setVisibility(View.GONE);
			    	  }
			    	  
			    	  /* *****Name Validation***** */
			    	  s_name = s_nameEditText.getText().toString();
			    	  Pattern name_regex = Pattern.compile("^[a-zA-Z ]+");
			    	  m = name_regex.matcher(s_name);
			    	  s_name = s_name.replaceAll("[ ]+", " ");
			    	  if(m.matches() == false) {
			    		  error = "Please Enter a valid Name";
			    		  s_nameError.setVisibility(View.VISIBLE);
			    		  s_nameError.setText(error);
			    		  //s_nameEditText.requestFocus();
			    	  }
			    	  else {
			    		  s_nameError.setVisibility(View.GONE);
			    	  }
			    	  
			    	  /* *****Password Validation***** */
			    	  s_password = s_passwordEditText.getText().toString();
			    	  if(s_password.length()<7) {
			    		  error = "Password has to be 7 characters or more";
			    		  s_passError.setVisibility(View.VISIBLE);
			    		  s_passError.setText(error);
			    		  //s_passwordEditText.requestFocus();
			    	  }
			    	  else {
			    		  s_passError.setVisibility(View.GONE);
			    	  }
			    	  
			    	  /* *****Re Enter Password Validation***** */
			    	  s_repassword = s_repasswordEditText.getText().toString();
			    	  if(s_password.equals(s_repassword) == false) {
			    		  error = "Passwords donot match";
			    		  s_repassError.setVisibility(View.VISIBLE);
			    		  s_repassError.setText(error);
			    		  //s_repasswordEditText.requestFocus();
			    	  }
			    	  else {
			    		  s_repassError.setVisibility(View.GONE);
			    	  }
			      }
				
				/* *****Pincode Validation***** */
				s_pincode = s_pincodeEditText.getText().toString();
				if(s_pincode.length()!=6) {
					error = "Please enter a valid 6 digit pincode";
					s_pincodeError.setVisibility(View.VISIBLE);
					s_pincodeError.setText(error);
					//s_pincodeEditText.requestFocus();
				}
				else {
					s_pincodeError.setVisibility(View.GONE);
				}
			
				if (error.equals("") == true) {
					s_gender = ((RadioButton) findViewById(s_genderButtonGroup.getCheckedRadioButtonId()) ).getText().toString();
					finishSignUp();
				}
			}
		});
	}
	
	private void autoFillData() {
		if (s_sharedPref.contains("user_id")) {
			s_email = s_sharedPref.getString("email", "");
			s_name = s_sharedPref.getString("name", "");
			s_type = s_sharedPref.getString("type", "");
			s_progressDialogMessage = "Updating Profile!!";
			
			if (s_type.equals("custom") == false) {
				removePasswordFields();
			}
			
			s_pincodeEditText.setText(s_sharedPref.getString("pincode", ""));
			if (s_sharedPref.getString("gender", "").equals("male") == true) {
				findViewById(R.id.radiobutton_male).setSelected(true);
			}
			else {
				findViewById(R.id.radiobutton_female).setSelected(true);
			}
			s_nameEditText.setText(s_name);
			s_emailEditText.setText(s_email);
			s_emailEditText.setTextColor(Color.rgb(78, 78, 78));
			s_emailEditText.setEnabled(false);
		}
		else {
			Bundle extras = getIntent().getExtras();
			s_type = extras.getString("type");
			s_progressDialogMessage = "Signing Up!!";
			
			if (s_type.equals("custom") == false) {
				s_email = extras.getString("email");
				s_name = extras.getString("name");
				removePasswordFields();
				s_nameEditText.setText(s_name);
				s_emailEditText.setText(s_email);
				s_emailEditText.setTextColor(Color.rgb(78, 78, 78));
				s_emailEditText.setEnabled(false);
			}
		}
		
	}

	/* remove password fields */
	private void removePasswordFields() {
		s_passwordEditText = (EditText) findViewById(R.id.pass_input);
		s_passwordEditText.setEnabled(false);
		s_passwordEditText.setVisibility(View.GONE);
		s_repasswordEditText = (EditText) findViewById(R.id.repass_input);
		s_repasswordEditText.setEnabled(false);
		s_repasswordEditText.setVisibility(View.GONE);
		
		s_nameEditText.setTextColor(Color.rgb(78, 78, 78));
		s_nameEditText.setEnabled(false);
	}
	
	/* Call AsyncTask */
	private void finishSignUp() {
		s_nameValuePairs = new ArrayList<NameValuePair>();
		s_nameValuePairs.add(new BasicNameValuePair("email", s_email));
		s_nameValuePairs.add(new BasicNameValuePair("first_name", s_name.split(" ")[0]));
		s_nameValuePairs.add(new BasicNameValuePair("last_name", s_name.split(" ")[1]));
		s_nameValuePairs.add(new BasicNameValuePair("gender", s_gender));
		s_nameValuePairs.add(new BasicNameValuePair("pincode", s_pincode));
		s_nameValuePairs.add(new BasicNameValuePair("type", s_type));
		if (s_type.equals("custom") == true) {
			s_nameValuePairs.add(new BasicNameValuePair("password", s_password));
		}
		//Send Data
		SubmitSignUpForm submit = new SubmitSignUpForm();
		submit.execute();
	}

	private void initErrorTextViews() {
		s_emailError = (TextView) findViewById(R.id.email_error);
		s_nameError = (TextView) findViewById(R.id.name_error);
		s_passError = (TextView) findViewById(R.id.pass_error);
		s_repassError = (TextView) findViewById(R.id.repass_error);
		s_pincodeError = (TextView) findViewById(R.id.pincode_error);
		
		s_emailError.setVisibility(View.GONE);
		s_nameError.setVisibility(View.GONE);
		s_passError.setVisibility(View.GONE);
		s_repassError.setVisibility(View.GONE);
		s_pincodeError.setVisibility(View.GONE);
	}
	
	private void initTitle() {
		RelativeLayout title = (RelativeLayout) findViewById(R.id.titleBar);
		TitleBar tb = new TitleBar(this,title);
		
		s_leftNavButton = tb.getLeftOptionsImgBtn();
		s_leftNavButton.setVisibility(View.GONE);
		s_leftNavButton.setEnabled(false);
		
		s_searchLayout = tb.getSearchLayout();
		s_searchLayout.setVisibility(View.GONE);
		for(int i=0; i<s_searchLayout.getChildCount(); i++) {
			s_searchLayout.getChildAt(i).setEnabled(false);
		}
		
		s_searchIcon = tb.getSearchIcon();
		s_searchIcon.setVisibility(View.GONE);
		s_searchIcon.setEnabled(false);
	
	}
	
	private class SubmitSignUpForm extends AsyncTask<String, String, String>  {

		@Override
		protected void onPreExecute() {
			s_progressDialog= new ProgressDialog(SignUp.this);
            s_progressDialog.setIndeterminate(true);
            s_progressDialog.setCancelable(false);
            s_progressDialog.setMessage("Signing Up!");
            s_progressDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(server_url + signup_page);//"http://54.187.181.173/start_up/insert_userDetails");

			try {
				// Add your data        
		        post.setEntity(new UrlEncodedFormEntity(s_nameValuePairs));
		        HttpResponse response = client.execute(post);
		        Log.d(TAG,"CODE: " + response.getStatusLine().getStatusCode() + "");
		        if (response.getStatusLine().getStatusCode() == 200) {
		        	String result = "";
		            BufferedReader reader = new BufferedReader(
		                    new InputStreamReader(response.getEntity().getContent()));

		            String line = null;
		            while ((line = reader.readLine()) != null) {
		                result += line;
		            }
		            Log.d(TAG,"Result:" + result);
		            return result;
		        }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "error:" + e.getMessage();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
		    s_progressDialog.dismiss();
		    try {
				JSONObject json_result = new JSONObject(result);
				String user_id = json_result.getJSONObject("user_id").toString();
				String user_balance = json_result.getJSONObject("user_balance").toString();
				Toast.makeText(getApplicationContext(), user_id, Toast.LENGTH_LONG).show();				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			startActivity(new Intent(getBaseContext(),MainActivity.class));
			super.onPostExecute(result);
			finish();
		}
		
	}
	
}

