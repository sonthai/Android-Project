package calpoly.csc.idgoback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import calpoly.csc.idgoback.DataHandler.ItemInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ItemList extends Activity {
	public static String ID = "calpoly.csc.idgoback.ID";
	public static String Rating = "calpoly.csc.idgoback.Rating";
	public static String currLat = "calpoly.csc.idgoback.currLat";
	public static String currLon = "calpoly.csc.idgoback.currLon";
	String choice ="", location="", sort="", lon="", lat="";
	ListView lv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlist);

		/*
		 * Retrieve information of choice and location passed from Explore class 
		 */
		Intent intent = getIntent();
		choice = intent.getStringExtra(ExploreUI.CHOICE);
		location = intent.getStringExtra(ExploreUI.LOCATION);
		sort = intent.getStringExtra(ExploreUI.SORT);

		setTitle(choice);

		/*
		 * Send http request to retrieve information from web service
		 * and run in the background to retrieve information of all possible
		 * items from chosen categories
		 */
		MyAsyncTask task = new MyAsyncTask();
		task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

		lv = (ListView) findViewById(R.id.listView1);

		/* Set Listener event when user clicks on the item on the list */
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> aParentView, View view, int aPosition,
					long aId) {
				ItemInfo selectedItem = (ItemInfo)aParentView.getItemAtPosition(aPosition);
				Intent i = new Intent(ItemList.this,InfoDetail.class);
				i.putExtra(ID, selectedItem.id);
				i.putExtra(Rating,Float.toString(selectedItem.rate));
				i.putExtra(currLat, lat);
				i.putExtra(currLon, lon);
				startActivity(i);	
			}

		});
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class MyAsyncTask extends AsyncTask<Void,Void,Void>{
		ProgressDialog progressDialog;
		DataHandler handler;
		FindDistance find;

		@Override
		protected void onPreExecute()
		{               
			super.onPreExecute();
			progressDialog = new ProgressDialog(ItemList.this);
			progressDialog.setMessage("Loading.Please Wait!!!");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
		}; 


		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				/* Load all the places from chosen category*/
				handler = new DataHandler();
				String myURL= "http://api.citygridmedia.com/content/places/v2/search/where?what="
						+ URLEncoder.encode(choice,"UTF-8") + "&where=" + URLEncoder.encode(location,"UTF-8") +"&publisher=test";
				handler.loadData(myURL);

				/* Load distance from current place to all the places in the list from Google map */
				String des = URLEncoder.encode(buildDes(handler.getList()),"UTF-8");
				String desURL = "http://maps.googleapis.com/maps/api/distancematrix/xml?origins=" 
						+ handler.getCurrLat() + "," + handler.getCurrLong() 
						+ "&destinations=" + des 
						+ "&units=imperial&mode=driving&sensor=false";


				find = new FindDistance();
				find.loadDistance(desURL);
			}
			catch (UnsupportedEncodingException	 e) {
				Log.i("Exception",e.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();

			for(int i = 0; i < handler.getList().size(); i++) {
				handler.setDistance(i, find.getDistList().get(i));
			}

			/* Sort by distance */
			if (sort.equals("distance")) {
				Collections.sort(handler.getList(), new Comparator<ItemInfo>() {
					@Override
					public int compare(ItemInfo lhs, ItemInfo rhs) {
						String [] i1 = lhs.distance.trim().split(" ");
						String [] i2 = rhs.distance.trim().split(" ");
						Float d1 = Float.parseFloat(i1[0]);
						Float d2 = Float.parseFloat(i2[0]);

						if (i1[1].equals("ft")) {
							d1 = (float) (d1*0.000189394);
						}

						if (i2[1].equals("ft"))
						{
							d2 = (float) (d2*0.000189394);
						}

						int res = comp(d2,d1);
						if (res == 0) {
							Float r1 = lhs.rate;
							Float r2 = rhs.rate;
							return comp(r1,r2);
						}
						return res;
					}
				});
			}
			else {
				/* Sort by rating */
				Collections.sort(handler.getList(), new Comparator<ItemInfo>() {
					@Override
					public int compare(ItemInfo lhs, ItemInfo rhs) {
						Float r1 = lhs.rate;
						Float r2 = rhs.rate;

						int res = comp(r1,r2);
						if (res == 0) {
							String [] i1 = lhs.distance.trim().split(" ");
							String [] i2 = rhs.distance.trim().split(" ");
							Float d1 = Float.parseFloat(i1[0]);
							Float d2 = Float.parseFloat(i2[0]);

							if (i1[1].equals("ft")) {
								d1 = (float) (d1*0.000189394);
							}

							if (i2[1].equals("ft"))
							{
								d2 = (float) (d2*0.000189394);
							}

							return comp(d2,d1);
						}
						return res;
					}
				});
			}

			lv.setAdapter(new MyCustomBaseAdapter(getApplicationContext(),handler.getList()));
			lon = handler.getCurrLong();
			lat = handler.getCurrLat();
		}
	}

	/* Comparison methods to compare 2 different distances or different ratings */
	public int comp(float f1, float f2) {
		if (f1 < f2)
			return 1;
		else if (f1 > f2)
			return -1;
		else {
			return 0;
		}
	}

	/* Format the URLs into the right form */
	public String buildDes(ArrayList<ItemInfo> list) {
		String desList ="";
		for (int i =0; i < list.size(); i++) {
			desList += list.get(i).lat + "," + list.get(i).lon;
			if (i < list.size() - 1) 
				desList += "|";
		}
		return desList;
	}
}
