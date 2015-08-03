package calpoly.csc.idgoback;

import java.util.ArrayList;

import calpoly.csc.idgoback.DataHandler.ItemInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyCustomBaseAdapter extends BaseAdapter {
	private ArrayList<ItemInfo> itemArrayList;
	private LayoutInflater mInflater;
	
	public MyCustomBaseAdapter(Context context, ArrayList<ItemInfo> items) {
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
			convertView = mInflater.inflate(R.layout.custom_row_view, null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.name);
			holder.txtDistance = (TextView) convertView.findViewById(R.id.distance);
			holder.rate = (RatingBar) convertView.findViewById(R.id.rate);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.txtName.setText(itemArrayList.get(position).name);
		holder.txtDistance.setText(itemArrayList.get(position).distance);
		holder.rate.setRating(itemArrayList.get(position).rate);
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView txtName;
		TextView txtDistance;
		RatingBar rate;
	}

}
