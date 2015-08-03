package calpoly.csc.idgoback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class FullImageActivity extends Activity {
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.full_image);
	 
	        // get intent data
	        Intent i = getIntent();
	        byte[] byteArray = i.getByteArrayExtra("thumbs");
	        String comment = i.getStringExtra("comment");
	        
	        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
	        bm = Bitmap.createScaledBitmap(bm,
					(int) bm.getWidth(),
					(int) (bm.getHeight() * 1.7), false);
	 
	        ImageView imageView = (ImageView) findViewById(R.id.imageView);
	        imageView.setImageBitmap(bm);
	        
	        TextView tv =  (TextView) findViewById(R.id.comment);
	        tv.setText(comment);
	   }
}
