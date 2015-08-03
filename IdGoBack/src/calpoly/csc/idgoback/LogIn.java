package calpoly.csc.idgoback;


import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.RequestPasswordResetCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends Activity {

	protected TextView errorMessage = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //initiateFacebook();
        errorMessage = (TextView)findViewById(R.id.loginError);
        errorMessage.setVisibility(View.GONE);

        final TextView tv = (TextView) findViewById(R.id.tv_forgotPassword);
        tv.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		LayoutInflater inflater = LayoutInflater.from(LogIn.this);
        		final View dialogLayout = inflater.inflate(R.layout.reset_password,null);
        		final AlertDialog.Builder pwdDialog = new AlertDialog.Builder(LogIn.this);
        		pwdDialog.setView(dialogLayout);
        		pwdDialog.setTitle("Reset your password");
        		pwdDialog.setMessage("Please enter your email");
        		final EditText email = (EditText) dialogLayout.findViewById(R.id.email_box);
        		pwdDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
        				String email_address = email.getText().toString();
        				ParseUser.requestPasswordResetInBackground(email_address, new RequestPasswordResetCallback() {
        					@Override
        					public void done(ParseException e) {
        						String msg = "An email was successfully sent with reset instructions";
        						if (e == null) {
        							Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        							toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        							toast.show();
        						}
        					}
        				});
        			}
        		});
        		pwdDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
        				return;	
        			}
        		});
        		pwdDialog.show();
        	}
        });
	}
	
	public void signIn(View v) {
		EditText input_username = null;
		EditText input_password = null;
		
		input_username = (EditText)findViewById(R.id.login_username);
		input_password = (EditText)findViewById(R.id.login_password);
		
		final String string_name = input_username.getText().toString();
		final String string_pw = input_password.getText().toString();
			
		ParseUser.logInInBackground(input_username.getText().toString(),
				input_password.getText().toString(), new LogInCallback() {
			public void done (ParseUser user, ParseException e) {
				if (user != null) {
					//User is logged in
					UserInfo.email = user.getEmail();
					startMain(string_name, string_pw);
				}
				else {
					//LogIn failed
			        errorMessage.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	public void startMain(String un, String pw) {
		Intent intent = new Intent(this,MainUI.class);
		UserInfo.username = un;
		UserInfo.password = pw;
		intent.putExtra("FacebookId", "");
		startActivity(intent);
	}
	
}


/*

public void initiateFacebook() {
// start Facebook Login
Session.openActiveSession(this, true, new Session.StatusCallback() {

  // callback when session changes state
  @Override
  public void call(Session session, SessionState state, Exception exception) {
	  final Session sesh = session;
	  if (session.isOpened()) {

      // make request to the /me API
      Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
        // callback after Graph API response with user object
        @Override
        public void onCompleted(GraphUser user, Response response) {
          if (user != null) {
            System.out.println("Hello " + user.getName() + "!");
          }
        }
      });
      
    }
	  else{
		  System.out.println("SESSION NOT OPENED");
	  }
  }
});
}

*/