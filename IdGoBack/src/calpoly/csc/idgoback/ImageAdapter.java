package calpoly.csc.idgoback;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Bitmap> mThumIds;
	
	public ImageAdapter(Context c, ArrayList<Bitmap> mIds) {
		context = c;
		mThumIds = mIds;
	}
	@Override
	public int getCount() {
		return mThumIds.size();
	}

	@Override
	public Object getItem(int pos) {
		return mThumIds.get(pos);
	}
	
	@Override
	public long getItemId(int pos) {
		return 0;
	}
	

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(mThumIds.get(pos));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new GridView.LayoutParams(80,80));
        return imageView;
	}

}
