package calpoly.csc.idgoback;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import calpoly.csc.idgoback.PhotoFragment.ImageInfo;
import calpoly.csc.idgoback.ParseSelectedItem.*;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class InfoDetail extends Activity{
	String id = "";
	String rating="";
	String currLat= "", currLon="", desLat="", desLon="";
	public ParseSelectedItem i =  new ParseSelectedItem();
	public DetailItemInfo item = i.new DetailItemInfo();
	public ArrayList<CustomerReview> reviews = new ArrayList<CustomerReview>();
	public static String name = "";
	public Bitmap mIcon = null;
	
	public ArrayList<Bitmap> savedPhotos = null;
	public ArrayList<ImageInfo> savedReview = null;
	public boolean photoSaved = false;
	
	public ArrayList<Bitmap> savedLibrary = null;
	public ArrayList<ImageInfo> savedFileName = null;
	public boolean librarySaved = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Retrieve id and rating passed from ItemList class */
		Intent intent = getIntent();
		id = intent.getStringExtra(ItemList.ID);
		rating = intent.getStringExtra(ItemList.Rating);
		currLat = intent.getStringExtra(ItemList.currLat);
		currLon = intent.getStringExtra(ItemList.currLon);

		new MyAsyncTask().execute();

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.setDisplayShowTitleEnabled(true);

		/* Creating Reviews Info Tab*/
		Tab tab = actionBar
				.newTab()
				.setText("Info")
				.setTabListener(
						new CustomTabListener<InfoFragment>(this, "info",
								InfoFragment.class));
		actionBar.addTab(tab);

		/* Creating Reviews Tab */
		tab = actionBar
				.newTab()
				.setText("Reviews")
				.setTabListener(
						new CustomTabListener<ReviewFragment>(this, "review",
								ReviewFragment.class));
		actionBar.addTab(tab);

		/* Creating Reviews Photo Tab */
		tab = actionBar
				.newTab()
				.setText("Photo")
				.setTabListener(
						new CustomTabListener<PhotoFragment>(this, "photo",
								PhotoFragment.class));
		actionBar.addTab(tab);
		
		/* Creating Upload Photo Tab */
		tab = actionBar
				.newTab()
				.setText("Upload Photo")
				.setTabListener(
						new CustomTabListener<UploadPhotoFragment>(this, "photo",
								UploadPhotoFragment.class));
		actionBar.addTab(tab);

	}

	private class MyAsyncTask extends AsyncTask<Void,Void,Void>{
		ProgressDialog progressDialog;
		ParseSelectedItem handler;

		@Override
		protected void onPreExecute()
		{               
			super.onPreExecute();
			progressDialog = new ProgressDialog(InfoDetail.this);
			progressDialog.setMessage("Loading Item Detail.Please Wait!!!");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			/*Load information detail for the chosen place */
			handler = new ParseSelectedItem();
			String myURL = "http://api.citygridmedia.com/content/places/v2/detail?id=" + id
					+ "&id_type=cs&placement=search_page&review_count=20&client_ip=123.4.56.78&publisher=test";

			handler.parseSelectedItem(myURL);

			item = handler.getItem();
			item.rating = rating;

			populateReviews(handler.getCustomerReviews());

			displayMap();

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();

			/*Set place name in TextView */
			TextView txtName = (TextView) findViewById(R.id.name);
			txtName.setText(item.name);

			/* Set the Rating */
			RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
			ratingBar.setRating(Float.parseFloat(rating));

			/* Set Business Hours if it exists */
			TextView txtHours = (TextView) findViewById(R.id.hours);
			if (item.hours.equals("")) {
				TextView hourLabel = (TextView) findViewById(R.id.txtHours);
				txtHours.setVisibility(View.GONE);
				hourLabel.setText("Business Hours not available");
			}
			else
				txtHours.setText(item.hours);
			
			/* Set the address TextView */
			TextView txtAddress = (TextView) findViewById(R.id.address);
			String addr = item.street + "\n" + item.city + "," + item.state + " " + item.zipCode;
			txtAddress.setText(addr);

			/* Set the contact TextView */
			TextView txtContact = (TextView) findViewById(R.id.contact);
			String contact = formatPhoneNumber(item.phoneNumber);
			txtContact.setText(contact);


			/* Set the website link if it exists */
			TextView link = (TextView) findViewById(R.id.link);	
			if (!item.url.equals("")) {
				link.setText(item.url.toLowerCase(Locale.US));
			}
			else{
				link.setVisibility(View.GONE);
			}

			/* Set Google map image*/
			ImageView bmImage = (ImageView) findViewById(R.id.imageView1);
			bmImage.setImageBitmap(mIcon);

			View view = (View) findViewById(R.id.info);
			view.setVisibility(View.VISIBLE);
		}
	}

	/* Format the phone number */
	public static String formatPhoneNumber(String phoneNumber) {
		String formatNumber="";

		for(int i=0; i< phoneNumber.length(); i++) {
			if (i == 0) {
				formatNumber = "(";
			}

			formatNumber += phoneNumber.charAt(i);

			if (i == 2) {
				formatNumber += ") ";
			}

			if (i == 5) {
				formatNumber += " ";
			}
		}

		return formatNumber;
	}

	/* Display static Google map on Info Tab */
	public void displayMap() {
		String url = "http://maps.googleapis.com/maps/api/staticmap?center="+ item.lat + "," + item.lon  +"&zoom=15&size=400x400&" + 
				"maptype=roadmap&markers=color:blue|label:A|" + item.lat + "," + item.lon + "&sensor=false";

		try {
			InputStream in = new URL(url).openStream();
			mIcon = BitmapFactory.decodeStream(in);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

	/* Populate Reviews into Review Tab*/
	private void populateReviews(final ArrayList<CustomerReview> list) {
		/* Run the query from Parse to load all the reviews store from parse*/
		ParseQuery query = new ParseQuery("Reviews");
		query.whereEqualTo("placeId",id);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					if (objects.size() == 0) {
						reviews = list;
					}
					else {
						ParseSelectedItem ps = new ParseSelectedItem();
						for (int i = objects.size() - 1; i>=0; i--) {
							CustomerReview cr = ps.new CustomerReview();
							cr.author = objects.get(i).getString("author");
							cr.customer_rate = Float.parseFloat(objects.get(i).getString("rate"));
							cr.date_review = objects.get(i).getString("date_add");
							cr.review = objects.get(i).getString("userReview");
							reviews.add(cr);
						}

						for (CustomerReview cr : list) {
							reviews.add(cr);
						}
					}
				}
			}
		});
	}
}
