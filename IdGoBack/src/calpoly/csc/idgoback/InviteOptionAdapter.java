package calpoly.csc.idgoback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InviteOptionAdapter extends BaseAdapter{
	private String[] itemList;
	private Integer[] icons1= {R.drawable.sms,R.drawable.facebook,R.drawable.mail};
	private Integer[] icons2= {R.drawable.sms,R.drawable.mail};
	private LayoutInflater mInflater;

	public InviteOptionAdapter(Context context,String[] items) {
		itemList = items;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return itemList.length;
	}
	
	@Override
	public Object getItem(int position) {
		return itemList[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.invite_option_custom, null);
			holder = new ViewHolder();
			holder.type = (TextView) convertView.findViewById(R.id.options);
			holder.type.setText(itemList[position]);
			if (itemList.length == 3)
				holder.type.setCompoundDrawablesWithIntrinsicBounds(icons1[position], 0, 0, 0);
			else
				holder.type.setCompoundDrawablesWithIntrinsicBounds(icons2[position], 0, 0, 0);
			
			holder.type.setCompoundDrawablePadding(10);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();


		return convertView;
	}

	static class ViewHolder {
		TextView type;
	}
}
