package calpoly.csc.idgoback;

import java.io.IOException;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExploreUI extends Fragment {
	public static String CHOICE = "calpoly.csc.idgoback.CHOICE";
	public static String LOCATION = "calpoly.csc.idgoback.LOCATION";
	public static String SORT ="calpoly.csc.idgoback.SORT";
	String[] options = {"Restaurant","Movie Theater","Hotel","Shopping",
			"Bar Club", "Spa Beauty","Business"};
	double lon,lat;
	EditText text = null;
	String strAddress="";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.explore, container, false);
		text = (EditText) view.findViewById(R.id.editText1);

		LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		
		
		/* 
		 * Create a drop-down list and populate the possible values into the
		 * spinner
		 */
		final Spinner s = (Spinner) view.findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
				.getApplicationContext(), android.R.layout.simple_spinner_item,
				options) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				((TextView) v).setTextSize(16);
				((TextView) v).setBackgroundColor(Color.BLACK);
				((TextView) v).setTextColor(Color.WHITE);
				((TextView) v).setGravity(Gravity.CENTER);
				return v;
			}

			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);
				((TextView) v).setGravity(Gravity.CENTER);
				((TextView) v).setTextColor(Color.BLACK);
				return v;
			}
		};
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		s.setAdapter(adapter);

		/* Add listener event for explore button */
		explore(view,s,text);

		return view;
	}

	/*
	 * Add listener event for the Explore button, pass the
	 * selected value into the ItemList class and switch to the
	 * ItemList view 
	 */
	public void explore(final View view,final Spinner s, final EditText text) {
		final Button explore = (Button) view.findViewById(R.id.explore);
		explore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RadioButton rating = (RadioButton) view.findViewById(R.id.rating_sort);
				String sortChoice ="";
				if (rating.isChecked())
					sortChoice="rating";
				else
					sortChoice = "distance";

				Intent i = new Intent(getActivity(),ItemList.class);
				i.putExtra(SORT, sortChoice);
				i.putExtra(CHOICE, s.getSelectedItem().toString());
				i.putExtra(LOCATION, text.getText().toString());
				startActivity(i);
			}
		});
	}
	
	@Override 
	public void onResume() {
		super.onResume();
		if (!((MainUI) getActivity()).lastLoc.equals("")) {
			text.setText(((MainUI) getActivity()).lastLoc);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		((MainUI) getActivity()).lastLoc =  text.getText().toString();
	}

	public class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location loc) {
			lat = loc.getLatitude();
			lon = loc.getLongitude();

			String Text = "My current location is: " + "Latitude = " + lat +
					" Longitude = " + lon;
			Toast.makeText(getActivity().getApplicationContext(),
					Text,Toast.LENGTH_SHORT).show();
			
			Geocoder gc = new Geocoder(getActivity());
			List<Address> list = null;
			try {
				list = gc.getFromLocation(lat, lon, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Address address = list.get(0);

			StringBuffer str = new StringBuffer();
			str.append("" + address.getFeatureName());
			str.append(" " + address.getThoroughfare());
			str.append(" " + address.getLocality());
			str.append(" " + address.getAdminArea());
			str.append(" "+ address.getPostalCode());
			str.append(" " + address.getCountryCode());
			
		
			if (strAddress.equals("")) {
				strAddress = str.toString();
				text.setText(strAddress);
			}
			Toast.makeText(getActivity().getApplicationContext(),
					strAddress,Toast.LENGTH_SHORT).show();
			System.out.println("Address " + strAddress);
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Gps Disabled",Toast.LENGTH_SHORT ).show();

		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Gps Enabled",Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

	}
}
