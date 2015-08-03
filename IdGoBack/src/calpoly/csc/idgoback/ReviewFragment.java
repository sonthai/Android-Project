package calpoly.csc.idgoback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.parse.ParseObject;

import calpoly.csc.idgoback.ParseSelectedItem.CustomerReview;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.review_fragment, container,false);
		final ListView lv = (ListView) v.findViewById(R.id.reviewList);
		String rating = ((InfoDetail) getActivity()).rating;
		RatingBar rateBar = (RatingBar) v.findViewById(R.id.ratingBar);
		rateBar.setRating(Float.parseFloat(rating));
			
		final ArrayList<CustomerReview> customers = ((InfoDetail) getActivity()).reviews;
		
		final TextView tv = (TextView) v.findViewById(R.id.review_count);
		int reviewCount = customers.size();
		if (reviewCount > 1)
			tv.setText(reviewCount + " reviews");
		else
			tv.setText(reviewCount + " review");
		
		Button addReview = (Button) v.findViewById(R.id.add_review);
		addReview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final LayoutInflater inflater = getActivity().getLayoutInflater();
				final View dialogLayout = inflater.inflate(R.layout.add_review,(ViewGroup) getActivity().getCurrentFocus());
				final AlertDialog.Builder reviewDialog = new AlertDialog.Builder(getActivity());
				reviewDialog.setView(dialogLayout);
				
				TextView date = (TextView) dialogLayout.findViewById(R.id.textdate);
				final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault());
				final Date currDate = new Date();
				date.setText(dateFormat.format(currDate).toString());
				
				reviewDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {			
						EditText userReview = (EditText) dialogLayout.findViewById(R.id.userReview);
						RatingBar rate =  (RatingBar) dialogLayout.findViewById(R.id.ratingBar1);
						
						/* Save new review into Parse hosting */
						ParseObject myObject = new ParseObject("Reviews");
						myObject.put("placeId", ((InfoDetail) getActivity()).id);
						myObject.put("author", UserInfo.username);
						myObject.put("rate", Float.toString(rate.getRating()));
						myObject.put("date_add",dateFormat.format(currDate).toString());
						myObject.put("userReview", userReview.getText().toString());
						myObject.saveInBackground();
						
						/* Create a new CustomerReview object */
						ParseSelectedItem ps = new ParseSelectedItem();
						CustomerReview cr = ps.new CustomerReview();
						cr.author = UserInfo.username;
						cr.customer_rate = rate.getRating();
						cr.date_review = dateFormat.format(currDate).toString();
						cr.review = userReview.getText().toString();
						
						/* Refresh current list view */
						customers.add(0,cr);
						int reviewCount = customers.size();
						if (reviewCount > 1)
							tv.setText(reviewCount + " reviews");
						else
							tv.setText(reviewCount + " review");
						lv.invalidateViews();

						return;
					}
					
				});
				
				reviewDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
					
				});
				reviewDialog.show();
			}
			
		});

		lv.setAdapter(new CustomerReviewAdapter(getActivity().getApplicationContext(),customers));
		
		return v;
	}
}
