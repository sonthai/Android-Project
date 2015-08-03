package calpoly.csc.idgoback;


import java.util.List;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class Welcome extends Activity{
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	private String facebookUsername;
	public String userID = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.welcome);
	    Parse.initialize(this, "EjLgC3PfAyLrx2VsMmEPgEcbuFUjAmaquckErIWX", 
	    						"gjty1VsKjEQ5RKWJkjYvQ2IN87662z4DeyjVzEOF");
	    
	    uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);

	}
	
	
	
	/* Log in facebook */
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        validateIdGoBackId(session, state);
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}

	@Override
	public void onResume() {
	    super.onResume();
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	private void setFacebookUsername(String name) {
		facebookUsername = name;
	}
	
	private void getFacebookUsername(Session session, SessionState state) {
		Request.executeMeRequestAsync(session, new Request.GraphUserCallback(){
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					setFacebookUsername(user.getUsername());
					userID = user.getId();
				}
			}
		});	
	}
	
	private void createUserAccount() {
		ParseUser newUser = new ParseUser();
		newUser.setUsername(facebookUsername);
		newUser.setPassword("def4u1t");
		//newUser.setEmail(input_email.getText().toString());
		
		newUser.signUpInBackground(new SignUpCallback() {
			  public void done(ParseException e) {
			    if (e == null) {
			      // Hooray! Let them use the app now.
			    	UserInfo.username = facebookUsername;
			    	startMainUI();
			    } else {
			      // Sign up didn't succeed. Look at the ParseException
			      // to figure out what went wrong
			        //RAISE ERROR
			    }
			  }
			});
	}
	

	private void validateIdGoBackId(Session session, SessionState state) {
		//Check if the User is a current IdGoBack user
		//If yes store information in UserInfo profile and proceed
		//If not, create user and store information in UserInfo profile then proceed
		getFacebookUsername(session, state);
		ParseQuery query = ParseUser.getQuery();
		query.whereEqualTo("username", facebookUsername);
		query.findInBackground(new FindCallback() {
		@Override
		  public void done(List<ParseObject> objects, ParseException e) {
		    if (e == null) {
		        // The query was successful.
		    	UserInfo.username = facebookUsername;
		    	startMainUI();
		    	//UserInfo.email = objects.get(0).;
		    } else {
		    	createUserAccount();
		        // Something went wrong.
		    }
		  }
		});
	}
	
	private void startMainUI() {
		Intent intent = new Intent(this, MainUI.class);
		intent.putExtra("FacebookId", userID);
		startActivity(intent);
	}
	
	public void signUp(View v) {
		Intent intent = new Intent(this,SignUp.class);
		startActivity(intent);
	}
	
 
	public void logIn(View v) {	
		try {
			Intent intent = new Intent(this, LogIn.class);
			startActivity(intent);
		}
		catch (Exception ex) {
			Log.e("logIn", ex.toString());
		}
	}




		
}
