package calpoly.csc.idgoback;

import java.util.Locale;

import calpoly.csc.idgoback.ParseSelectedItem.DetailItemInfo;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoFragment extends Fragment {
	static DetailItemInfo item;
	public static String currLa = "calpoly.csc.idgoback.currLa";
	public static String currLo = "calpoly.csc.idgoback.currLo";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.info_fragment, container,false);
		v.setVisibility(View.INVISIBLE);
		
		Button direction  = (Button) v.findViewById(R.id.direction);
		direction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* Open google map for direction */
				String s = "http://maps.google.com/maps?saddr=" +  ((InfoDetail) getActivity()).currLat
						+ "," + ((InfoDetail) getActivity()).currLon + "&daddr=" 
						+ ((InfoDetail) getActivity()).item.lat + "," + ((InfoDetail) getActivity()).item.lon;		
				Intent  i = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(s));
				i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				startActivity(i);
				
			}
		});
		return v;
	}
	

	public void onResume() {
		super.onResume();
		item =  ((InfoDetail) getActivity()).item;
		if (!item.name.equals("")) {
			TextView txtName = (TextView) getActivity().findViewById(R.id.name);
			txtName.setText(item.name);
			
			TextView txtHours = (TextView) getActivity().findViewById(R.id.hours);
			if (item.hours.equals("")) {
				TextView hourLabel = (TextView) getActivity().findViewById(R.id.txtHours);
				txtHours.setVisibility(View.GONE);
				hourLabel.setText("Business Hours not available");
			}
			else
				txtHours.setText("Hours  " + item.hours);
			
			/*Set Address TextView*/
			TextView txtAddress = (TextView) getActivity().findViewById(R.id.address);
			String addr = item.street + "\n" + item.city + "," + item.state + " " + item.zipCode;
			txtAddress.setText(addr);
			
			/*Set Contact TextView */
			TextView txtContact = (TextView) getActivity().findViewById(R.id.contact);
			String contact = InfoDetail.formatPhoneNumber(item.phoneNumber);
			txtContact.setText(contact);
			
			/*Set link website if it exists*/
			TextView link = (TextView) getActivity().findViewById(R.id.link);		
			if (!item.url.equals("")) {
				link.setText(item.url.toLowerCase(Locale.US));
			}
			else{
				link.setVisibility(View.GONE);
			}
			
			/*Set static Google map image*/
			Bitmap mIcon = ((InfoDetail) getActivity()).mIcon;
			ImageView bmImage = (ImageView) getActivity().findViewById(R.id.imageView1);
			bmImage.setImageBitmap(mIcon);
			
			View v = (View) getActivity().findViewById(R.id.info);
			v.setVisibility(View.VISIBLE);
		}
	}
}
