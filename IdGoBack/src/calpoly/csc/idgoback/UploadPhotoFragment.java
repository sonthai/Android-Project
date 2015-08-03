package calpoly.csc.idgoback;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import calpoly.csc.idgoback.PhotoFragment.ImageInfo;

import com.facebook.Session;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class UploadPhotoFragment extends Fragment {
	ArrayList<Bitmap> thumIds;
	ArrayList<ImageInfo> imgI;
	Bitmap bm;
	GridView gridV;
	int PICTURE_ACTIVITY = 1;
	int UPLOAD = 2;
	int RESULT_OK = -1;
	PhotoFragment p = new PhotoFragment();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.photo_library,container, false);

		thumIds = new ArrayList<Bitmap>();
		imgI = new ArrayList<ImageInfo>();

		if (((InfoDetail) getActivity()).librarySaved) {
			thumIds = ((InfoDetail) getActivity()).savedLibrary;
			imgI = ((InfoDetail) getActivity()).savedFileName;
			((InfoDetail) getActivity()).librarySaved = false;
		}
		else
			loadImage();
		gridV = (GridView) v.findViewById(R.id.gridView);
		gridV.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), thumIds));
		gridV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				Intent intent;
				if (Session.getActiveSession().isOpened())
					intent = new Intent(getActivity().getApplicationContext(),UploadPhoto.class);
				else
					intent = new Intent(getActivity().getApplicationContext(),UpLoadWithoutFB.class);
				bm = imgI.get(pos).bm;
				String name = imgI.get(pos).info;
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				intent.putExtra("thumbs", byteArray);
				intent.putExtra("filename", name);
				intent.putExtra("placeId", ((InfoDetail) getActivity()).id);
				startActivityForResult(intent,UPLOAD);				
			}

		});

		Button capture = (Button) v.findViewById(R.id.camera);
		capture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, PICTURE_ACTIVITY);	
			}

		});

		return v;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("Rquest Code " + requestCode + "result code " + resultCode);
		if (data != null) {
			if (requestCode == PICTURE_ACTIVITY && resultCode == RESULT_OK) {
				Bitmap pictureTaken = (Bitmap) data.getExtras().get("data");
				thumIds.add(pictureTaken);
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						gridV.invalidateViews();

					}				
				});

				/* Store picture in Parse hosting */
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				pictureTaken.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();

				/* Create file name for picture */
				String fileName =  Long.toString(System.currentTimeMillis()) + ".png";

				imgI.add(p.new ImageInfo(pictureTaken,fileName));

				ParseFile file = new ParseFile(fileName,byteArray);
				file.saveInBackground();
				ParseObject myObject = new ParseObject("Album");
				myObject.put("fileName", fileName);
				myObject.put("userName", UserInfo.username);
				//myObject.put("placeId", ((InfoDetail) getActivity()).id);
				myObject.put("picture", file);
				myObject.saveInBackground();

				/* Upload picture into Photo tab also*/
				create_review_dialog(fileName,file,pictureTaken);	

			}
			else if (requestCode == UPLOAD) {
				if (((InfoDetail) getActivity()).savedPhotos != null){
					String r = data.getStringExtra("reviews");
					((InfoDetail) getActivity()).savedPhotos.add(bm);
					ImageInfo rev = p.new ImageInfo(bm,r);
					((InfoDetail) getActivity()).savedReview.add(rev);
					((InfoDetail) getActivity()).photoSaved = true;
				}
			}
		}
	}



	private void loadImage() {
		/* Changing the class name */
		ParseQuery query = new ParseQuery("Album");
		query.whereEqualTo("userName",UserInfo.username);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					if (objects.size() > 0) {
						for (ParseObject o: objects) {
							final ParseFile pf = (ParseFile) o.get("picture");
							final String name = o.getString("fileName");
							pf.getDataInBackground(new GetDataCallback() {
								@Override
								public void done(byte[] data, ParseException ex) {
									if (ex == null) {
										Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
										thumIds.add(bm);
										imgI.add(p.new ImageInfo(bm,name));
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
	
	private void create_review_dialog(final String fileName,final ParseFile file, final Bitmap pictureTaken) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogLayout = inflater.inflate(R.layout.review_dialog,(ViewGroup) getActivity().getCurrentFocus());
		final AlertDialog.Builder reviewDialog = new AlertDialog.Builder(getActivity());
		reviewDialog.setView(dialogLayout);	
		reviewDialog.setTitle("Add your review");
		
		reviewDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText reviewTxt  = (EditText) dialogLayout.findViewById(R.id.reviewText);
				String review = reviewTxt.getText().toString();
				ParseObject object = new ParseObject("Photos");
				object.put("fileName", fileName);
				object.put("userName", UserInfo.username);
				object.put("placeId", ((InfoDetail) getActivity()).id);
				object.put("picture", file);
				object.put("reviews", review);
				object.saveInBackground();
				
				if (((InfoDetail) getActivity()).savedPhotos != null){
					((InfoDetail) getActivity()).savedPhotos.add(pictureTaken);
					ImageInfo rev = p.new ImageInfo(pictureTaken,review);
					((InfoDetail) getActivity()).savedReview.add(rev);
					((InfoDetail) getActivity()).photoSaved = true;
				}
				
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

	@Override
	public void onStop() {
		super.onStop();
		((InfoDetail) getActivity()).savedLibrary =  thumIds;
		((InfoDetail) getActivity()).savedFileName = imgI;
		((InfoDetail) getActivity()).librarySaved = true;
	}
}
