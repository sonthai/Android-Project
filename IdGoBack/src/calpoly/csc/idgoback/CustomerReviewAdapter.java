package calpoly.csc.idgoback;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import calpoly.csc.idgoback.ParseSelectedItem.CustomerReview;

public class CustomerReviewAdapter extends BaseAdapter{
	private ArrayList<CustomerReview> itemArrayList;
	private LayoutInflater mInflater;

	public CustomerReviewAdapter(Context context, ArrayList<CustomerReview> items) {
		itemArrayList = items;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return itemArrayList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return itemArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.customer_review, null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.customer_name);
			holder.txtDate = (TextView) convertView.findViewById(R.id.customer_date);
			holder.txtReview = (TextView) convertView.findViewById(R.id.customer_review);
			holder.rate = (RatingBar) convertView.findViewById(R.id.rating);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		holder.txtName.setText(itemArrayList.get(position).author);
		if (itemArrayList.get(position).date_review.length() > 10) {
			String [] elemArray = itemArrayList.get(position).date_review.substring(0,10).split("-");
			String date = elemArray[1] + "/" + elemArray[2] + "/" + elemArray[0];
			holder.txtDate.setText(date);
		}
		else
			holder.txtDate.setText(itemArrayList.get(position).date_review);
		holder.txtReview.setText(itemArrayList.get(position).review);
		holder.rate.setRating(itemArrayList.get(position).customer_rate);

		return convertView;
	}

	static class ViewHolder {
		TextView txtName;
		TextView txtReview;
		TextView txtDate;
		RatingBar rate;
	}
}
