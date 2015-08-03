package calpoly.csc.idgoback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainUI extends Activity {
	Bitmap profile = null;
	Bitmap facebookPic = null;
	String id="";
	String lastLoc ="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		/** Creating EXPLORE Tab */
		Tab tab = actionBar
				.newTab()
				.setText("Explore")
				.setTabListener(
						new CustomTabListener<ExploreUI>(this, "explore",
								ExploreUI.class))
				.setIcon(R.drawable.ic_action_search);
		actionBar.addTab(tab);

		/** Creating CAMERA Tab */
		tab = actionBar
				.newTab()
				.setText("Camera")
				.setTabListener(
						new CustomTabListener<CameraUI>(this, "camera",
								CameraUI.class)).setIcon(R.drawable.camera);
		actionBar.addTab(tab);

		/** Creating HOME Tab */
		tab = actionBar
				.newTab()
				.setText("Home")
				.setTabListener(
						new CustomTabListener<HomeUI>(this, "home",
								HomeUI.class)).setIcon(R.drawable.home);
		actionBar.addTab(tab);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("FacebookId");
		if (!id.equals("")) {
			ParseQuery query = new ParseQuery("Profile");
			query.whereEqualTo("userName", UserInfo.username);
			query.findInBackground(new FindCallback() {
				@Override
				public void done(List<ParseObject> o, ParseException e) {
					if (e== null) {
						if (o.size() == 0)
							new LoadFBPic().execute();
					}	
				};
			});
		}
	}
	
	public class LoadFBPic extends AsyncTask<Void,Void,Void> {
		@Override
		protected Void doInBackground(Void... params) {
			String url = "http://graph.facebook.com/"+ id +"/picture?type=large";
			System.out.println(url);
			InputStream in;
			try {
				in = new URL(url).openStream();
				facebookPic = BitmapFactory.decodeStream(in);
				facebookPic = Bitmap.createScaledBitmap(facebookPic, 110, 110, false);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			ParseObject myObject = new ParseObject("Profile");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			facebookPic.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			final ParseFile file = new ParseFile("photo.png", byteArray);
			file.saveInBackground();
			myObject.put("userName", UserInfo.username);
			myObject.put("photo", file);
			myObject.saveInBackground();	
		}
	}
}
