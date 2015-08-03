package calpoly.csc.idgoback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import calpoly.csc.idgoback.PhotoFragment.ImageInfo;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class OnlinePhotoLibrary extends Activity{
	ArrayList<Bitmap> thumIds;
	ArrayList<ImageInfo> imgI;
	Bitmap bm;
	GridView gridV;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_photo_library);
		thumIds = new ArrayList<Bitmap>();
		loadImage();
		gridV = (GridView) findViewById(R.id.gridView);
		gridV.setAdapter(new ImageAdapter(getApplicationContext(), thumIds));
		gridV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				Bitmap bm = thumIds.get(pos);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				setResult(Activity.RESULT_OK, new Intent().putExtra("profile",byteArray));
				finish();
				
			}
		});
	}

	private void loadImage() {
		/* Changing the class name */
		ParseQuery query = new ParseQuery("Album");
		query.whereEqualTo("userName",UserInfo.username);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					if (objects.size() > 0) {
						for (ParseObject o: objects) {
							final ParseFile pf = (ParseFile) o.get("picture");
							pf.getDataInBackground(new GetDataCallback() {
								@Override
								public void done(byte[] data, ParseException ex) {
									if (ex == null) {
										Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
										thumIds.add(bm);
										gridV.invalidateViews();
									}
								}
							});
						}

					}
				}
			}
		});
	}
}
