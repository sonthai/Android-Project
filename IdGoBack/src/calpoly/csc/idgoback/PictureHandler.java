package calpoly.csc.idgoback;

import com.parse.ParseFile;
import com.parse.ParseObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureHandler extends Activity {
	byte[] photoBytes = null;
	Bitmap photo = null;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.picture_handler_without_sharing);

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
		Bitmap resize = Bitmap.createScaledBitmap(photo,320, 415, false);

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
	
	

}
