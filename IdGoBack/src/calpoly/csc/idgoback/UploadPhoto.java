package calpoly.csc.idgoback;


import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.parse.ParseFile;
import com.parse.ParseObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadPhoto extends Activity {
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String TAG = "MainFragment";
	Bitmap bm = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_photo);

		Intent i = getIntent();
		final byte[] byteArray = i.getByteArrayExtra("thumbs");
		final String  name = i.getStringExtra("filename");
		final String id = i.getStringExtra("placeId");

		bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
	    Bitmap resize = Bitmap.createScaledBitmap(bm,320,320, false);
		ImageView imageView = (ImageView) findViewById(R.id.upload_photo);
	
		imageView.setImageBitmap(resize);

		final EditText tv =  (EditText) findViewById(R.id.picture_review);

		Button btn = (Button) findViewById(R.id.upload);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String reviews = tv.getText().toString();
				ParseFile file = new ParseFile(name,byteArray);
				file.saveInBackground();			
				ParseObject myObject = new ParseObject("Photos");
				myObject.put("userName", UserInfo.username);
				myObject.put("placeId", id);
				myObject.put("fileName", name);
				myObject.put("picture", file);
				myObject.put("reviews", reviews);
				myObject.saveInBackground();
				setResult(Activity.RESULT_OK, new Intent().putExtra("reviews",reviews));
				finish();
			}

		});
		
		Button postFacebookButton = (Button) findViewById(R.id.uploadFB);
		postFacebookButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postFacebookPhoto(tv.getText().toString());
			}
		});

	}
	
	private void postFacebookPhoto(String review) {
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
			postParams.putString("message", review);
			
			Bitmap pic = Bitmap.createScaledBitmap(bm, bm.getWidth()*2, bm.getHeight()*2, false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] photoBytes = stream.toByteArray();
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
