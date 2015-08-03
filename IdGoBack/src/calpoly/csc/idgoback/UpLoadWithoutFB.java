package calpoly.csc.idgoback;

import com.parse.ParseFile;
import com.parse.ParseObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class UpLoadWithoutFB extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_photo_without_sharing);
		
		Intent i = getIntent();
		final byte[] byteArray = i.getByteArrayExtra("thumbs");
		final String  name = i.getStringExtra("filename");
		final String id = i.getStringExtra("placeId");

		Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
	    Bitmap resize = Bitmap.createScaledBitmap(bm,320,320, false);
		ImageView imageView = (ImageView) findViewById(R.id.photo);
	
		imageView.setImageBitmap(resize);

		final EditText tv =  (EditText) findViewById(R.id.review);

		Button btn = (Button) findViewById(R.id.upload_button);
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
	}

}
