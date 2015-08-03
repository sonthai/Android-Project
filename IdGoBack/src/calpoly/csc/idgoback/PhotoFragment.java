package calpoly.csc.idgoback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PhotoFragment extends Fragment {
	ArrayList<Bitmap> thumIds;
	ArrayList<ImageInfo> imgR;
	GridView gridV;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.photo_fragment, container,false);
		
		thumIds = new ArrayList<Bitmap>();
		imgR = new ArrayList<ImageInfo>();

		if (((InfoDetail) getActivity()).photoSaved) {
			thumIds = ((InfoDetail) getActivity()).savedPhotos;
			imgR = ((InfoDetail) getActivity()).savedReview;
			((InfoDetail) getActivity()).photoSaved = false;
			gridV.invalidateViews();
		}
		else 
			loadImage(((InfoDetail) getActivity()).id);
		
		gridV = (GridView) v.findViewById(R.id.gridView);
		gridV.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), thumIds));
		gridV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				Intent intent = new Intent(getActivity().getApplicationContext(),FullImageActivity.class);
				Bitmap bm = imgR.get(pos).bm;
				String s = imgR.get(pos).info;
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				intent.putExtra("thumbs", byteArray);
				intent.putExtra("comment", s);
				startActivity(intent);				
			}
		});
		
		return v;
	}
	
	
	private void loadImage(String id) {
		ParseQuery query = new ParseQuery("Photos");
		query.whereEqualTo("placeId",id);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					System.out.println("size of objects in Fragment" +objects.size());
					if (objects.size() > 0) {
						for (ParseObject o: objects) {
							ParseFile pf = (ParseFile) o.get("picture");
							final String r = o.getString("reviews");
							pf.getDataInBackground(new GetDataCallback() {
								@Override
								public void done(byte[] data, ParseException ex) {
									if (ex == null) {
										Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
										thumIds.add(bm);
										imgR.add(new ImageInfo(bm,r));
										gridV.invalidateViews();
									}
								}
							});
						}

					}
				}
			}
		});
	}
	
	@Override
	public void onStop() {
		super.onStop();
		((InfoDetail) getActivity()).savedPhotos =  thumIds;
		((InfoDetail) getActivity()).savedReview = imgR;
		((InfoDetail) getActivity()).photoSaved = true;
	}
	
	class ImageInfo {
		public Bitmap bm;
		public String info;
		
		public ImageInfo(Bitmap b, String i) {
			bm = b;
			info = i;	
		}
	}
}
