package calpoly.csc.idgoback;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class PictureHandlerWithFB extends Activity{
	byte[] photoBytes = null;
	Bitmap photo = null;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String TAG = "MainFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.picture_handler);

		Intent intent = getIntent();
		photoBytes = intent.getByteArrayExtra("photo");
		photo = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
		displayPhoto();
		
		Button savePictureButton = (Button) findViewById(R.id.button_savePic);
		savePictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				savePic();
			}
		});
		
		Button postFacebookButton = (Button) findViewById(R.id.button_uploadFacebook);
		postFacebookButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postFacebookPhoto();
			}
		});
		
		Button backToHomeButton = (Button) findViewById(R.id.ph_backButton);
		backToHomeButton.setVisibility(View.VISIBLE);
		backToHomeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
			
	}
	
	private void displayPhoto() {
		ImageView displayPic = (ImageView) findViewById(R.id.ph_displayImage);	
		Bitmap resize = Bitmap.createScaledBitmap(photo,320, 340, false);
		displayPic.setImageBitmap(resize);
	}

	
	private void savePic() {
		String fileName =  Long.toString(System.currentTimeMillis()) + ".png";
		ParseFile file = new ParseFile(fileName, photoBytes);
		file.saveInBackground();
		ParseObject pictureSave = new ParseObject("Album");
		pictureSave.put("userName", UserInfo.username);
		pictureSave.put("picture", file);
		pictureSave.put("fileName", fileName);
		pictureSave.saveInBackground();
		Toast.makeText(getApplicationContext(),
				"Picture has been saved",
				Toast.LENGTH_SHORT).show();
	}
	
	
	private void postFacebookPhoto() {
		Session session = Session.getActiveSession();

		if (session != null){
			// Check for publish permissions    
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				//pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session
						.NewPermissionsRequest(this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			Bundle postParams = new Bundle();
			EditText reviewText = (EditText) findViewById(R.id.editText1);
			postParams.putString("message", reviewText.getText().toString());
			
			Bitmap pic = Bitmap.createScaledBitmap(photo, photo.getWidth()*2, photo.getHeight()*2, false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
            photoBytes = stream.toByteArray();
			postParams.putByteArray("picture", photoBytes);
			
			Request.Callback callback= new Request.Callback() {
				public void onCompleted(Response response) {
					JSONObject graphResponse = response
							.getGraphObject()
							.getInnerJSONObject();
					String postId = null;
					try {
						postId = graphResponse.getString("id");
					} catch (JSONException e) {
						Log.i(TAG,
								"JSON error "+ e.getMessage());
					}
					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(getApplicationContext(),
								error.getErrorMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), 
								"Photo was uploaded",
								Toast.LENGTH_LONG).show();
					}
				}
			};

			//		        Request request = new Request(session, "me/feed", postParams, 
			//		                              HttpMethod.POST, callback);
			Request request = new Request(session, "me/photos", postParams, 
					HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();

		}

	}
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	
}
