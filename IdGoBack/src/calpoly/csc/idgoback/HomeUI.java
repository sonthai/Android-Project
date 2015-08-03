package calpoly.csc.idgoback;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.QuickContact;
import android.provider.MediaStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

public class HomeUI extends Fragment{
	public Button signout, authButton;
	Bitmap resize;
	public QuickContactBadge badge;
	int PICTURE_ACTIVITY = 1;
	int IMAGE_LOADING = 2;
	int IMAGE_PICK = 3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.home, container, false);
		String[] optionList1 = {"Invite by SMS", "Invite by Facebook",
		"Invite by Mail" };
		String[] optionList2 = {"Invite by SMS","Invite by Mail"};
		

		/* Set up the invite options for the list view */
		ListView inviteListView = (ListView) v.findViewById(R.id.inviteOptions);
		if (Session.getActiveSession().isOpened())
			inviteListView.setAdapter(new InviteOptionAdapter(getActivity()
					.getApplicationContext(), optionList1));
		else {
			inviteListView.setAdapter(new InviteOptionAdapter(getActivity()
					.getApplicationContext(), optionList2));	
		}

		/* Set the listener event for the list view */
		inviteListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> aParentView, View view,
					int aPosition, long aId) {
				String option = aParentView.getItemAtPosition(aPosition)
						.toString();
				if (option.equals("Invite by SMS")) {
					String msg = "Hey I started using IdGoBack. It's a cool free app that "
							+ "help you search for the best business in everywhere!";
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setType("vnd.android-dir/mms-sms");
					intent.putExtra("sms_body", msg);
					startActivity(intent);
				} else if (option.equals("Invite by Facebook")) {
					Session session = Session.getActiveSession();
					if (session.isOpened()) {
						sendFacebookInvitation();
					} /*else {
						System.out.println("Facebook is not logged in");
						//signInFacebook();
					}*/
				} else if (option.equals("Invite by Mail")) {
					createInviteByMail();
				}

			}

		});
		
		TextView privacyButton = (TextView) v.findViewById(R.id.privacy);
		privacyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),Privacy.class);
				startActivity(intent);			
			}
			
		});
		setUpUI(v);	
		return v;
	}

	/* Sent invitation to friend list on facebook if user logs in with Facebook account */
	public void sendFacebookInvitation() {
		Bundle params = new Bundle();
		params.putString("message","This is a message");

		WebDialog requestsDialog = (
				new WebDialog.RequestsDialogBuilder(getActivity(),
						Session.getActiveSession(),
						params))
						.setOnCompleteListener(new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {
								if (error != null) {
									if (error instanceof FacebookException) {
										Toast.makeText(getActivity().getApplicationContext(), 
												"Request cancelled", 
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(getActivity().getApplicationContext(), 
												"Network Error", 
												Toast.LENGTH_SHORT).show();
									}
								} else {
									final String requestId = values.getString("request");
									if (requestId != null) {
										Toast.makeText(getActivity().getApplicationContext(), 
												"Request sent",  
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(getActivity().getApplicationContext(), 
												"Request cancelled", 
												Toast.LENGTH_SHORT).show();
									}
								}   
							}

						})
						.build();
		requestsDialog.show();
	}

	/* Set up initial UI for Home screen */
	public void setUpUI(View v) {
		badge = (QuickContactBadge) v.findViewById(R.id.badge);
		badge.setMode(QuickContact.MODE_MEDIUM);
		badge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createChoiceDialog();
			}

		});

		if (((MainUI) getActivity()).facebookPic != null) {
			badge.setImageBitmap(((MainUI) getActivity()).facebookPic);
		}
		else {
			if (((MainUI) getActivity()).profile != null)
				badge.setImageBitmap(((MainUI) getActivity()).profile);
			else {
				//ParseUser user = ParseUser.getCurrentUser();
				ParseQuery query = new ParseQuery("Profile");
				query.whereEqualTo("userName", UserInfo.username);
				query.findInBackground(new FindCallback() {
					@Override
					public void done(List<ParseObject> objects, ParseException e) {
						if (e == null) {
							if (objects.size() == 0)
								System.out.println("Photo is not available");
							else {
								ParseFile file = (ParseFile) objects.get(0).get(
										"photo");
								file.getDataInBackground(new GetDataCallback() {
									@Override
									public void done(byte[] data, ParseException e) {
										if (e == null) {
											resize = BitmapFactory.decodeByteArray(
													data, 0, data.length);
											badge.setImageBitmap(resize);
										}
									}

								});
							}
						}
					}
				});
			}
		}

		TextView userName = (TextView) v.findViewById(R.id.userName);
		userName.setText(UserInfo.username);
		TextView email = (TextView) v.findViewById(R.id.userEmail);
		email.setText(UserInfo.email);

		signout = (Button) v.findViewById(R.id.signout);
		signout.setBackgroundColor(Color.CYAN);
		signout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseUser.logOut();
				Intent intent = new Intent(getActivity(), Welcome.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				Session session = Session.getActiveSession();
				if (session !=null) 
					session.closeAndClearTokenInformation();
				startActivity(intent);
				getActivity().finish();
			}

		});


	}

	public void createChoiceDialog() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogLayout = inflater.inflate(R.layout.choice_dialog,
				null);
		dialogLayout.setBackgroundResource(R.color.custom_theme_color);
		final AlertDialog.Builder optionDialog = new AlertDialog.Builder(
				getActivity(), R.style.DialogSlideAnim);
		optionDialog.setView(dialogLayout);
		final Dialog d = optionDialog.create();

		Button camera = (Button) dialogLayout.findViewById(R.id.camera);
		camera.setBackgroundColor(Color.WHITE);
		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, PICTURE_ACTIVITY);
				d.cancel();
			}

		});

		Button choose_from_album = (Button) dialogLayout
				.findViewById(R.id.from_album);
		choose_from_album.setBackgroundColor(Color.WHITE);
		choose_from_album.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/");
				startActivityForResult(intent, IMAGE_LOADING);
				d.cancel();
			}

		});

		Button choose_from_online_library = (Button) dialogLayout
				.findViewById(R.id.from_online);
		choose_from_online_library.setBackgroundColor(Color.WHITE);
		choose_from_online_library.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						OnlinePhotoLibrary.class);
				startActivityForResult(intent, IMAGE_PICK);
				d.cancel();
			}

		});

		Button cancel = (Button) dialogLayout.findViewById(R.id.cancel);
		cancel.setBackgroundColor(Color.GRAY);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.cancel();
			}

		});

		d.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == PICTURE_ACTIVITY
					&& resultCode == Activity.RESULT_OK) {
				Bitmap pictureTaken = (Bitmap) data.getExtras().get("data");
				resize = Bitmap.createScaledBitmap(pictureTaken,110,110, false);
				badge.setImageBitmap(resize);

				/* Store picture in Parse hosting */
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				pictureTaken.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();

				String fileName = Long.toString(System.currentTimeMillis())
						+ ".png";
				ParseFile file = new ParseFile(fileName, byteArray);
				file.saveInBackground();
				ParseObject myObject = new ParseObject("Album");
				myObject.put("fileName", fileName);
				myObject.put("userName", UserInfo.username);
				myObject.put("picture", file);
				myObject.saveInBackground();
			} else if (requestCode == IMAGE_LOADING
					&& resultCode == Activity.RESULT_OK) {
				try {
					Uri contentURI = Uri.parse(data.getDataString());
					ContentResolver cr = getActivity().getContentResolver();
					InputStream in = cr.openInputStream(contentURI);
					Bitmap thump = BitmapFactory.decodeStream(in);
					resize = Bitmap.createScaledBitmap(thump,110,110, true);
					badge.setImageBitmap(resize);

					/* Convert profile photo into byte array */
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					resize.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] byteArray = stream.toByteArray();
					updateProfilePhoto(byteArray);

				} catch (Exception e) {

				}
			} else if (requestCode == IMAGE_PICK
					&& resultCode == Activity.RESULT_OK) {
				byte[] imgArr = data.getByteArrayExtra("profile");
				resize = BitmapFactory
						.decodeByteArray(imgArr, 0, imgArr.length);
				resize = Bitmap.createScaledBitmap(resize,110,110, false);
				badge.setImageBitmap(resize);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				resize.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				updateProfilePhoto(byteArray);
			}
		}
	}

	public void updateProfilePhoto(byte[] byteArray) {
		final ParseFile file = new ParseFile("photo.png", byteArray);
		file.saveInBackground();

		final ParseUser user = ParseUser.getCurrentUser();
		ParseQuery query = new ParseQuery("Profile");
		query.whereEqualTo("userName", UserInfo.username);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(final List<ParseObject> objects, ParseException e) {
				if (e == null) {
					if (objects.size() == 0) {
						ParseObject myObject = new ParseObject("Profile");
						/* Push profile photo into database if it does not exist */
						myObject.put("userName", user.getUsername());
						myObject.put("photo", file);
						myObject.saveInBackground();
					} else {
						/* Update profile photo in database if it exists */
						objects.get(0).saveInBackground(new SaveCallback() {
							@Override
							public void done(ParseException e) {
								objects.get(0).put("photo", file);
								objects.get(0).saveInBackground();
							}
						});
					}
				}
			}

		});

	}

	public void createInviteByMail() {
		/* Create layout for Invite dialog box */
		LinearLayout layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.HORIZONTAL);

		/* Add an email label */
		final TextView tv = new TextView(getActivity());
		tv.setText("Email");
		layout.addView(tv);

		/* Add an edit text field */
		final EditText input = new EditText(getActivity());
		layout.addView(input);

		AlertDialog.Builder ibox = new AlertDialog.Builder(getActivity());
		ibox.setView(layout);

		ibox.setPositiveButton("Send", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String to = input.getText().toString();
				Intent email = new Intent(android.content.Intent.ACTION_SEND);
				email.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { to });
				email.putExtra(android.content.Intent.EXTRA_SUBJECT, "Join IdGoBack");
				String msg =  "Hey I started using IdGoBack. It's a cool free app that "
						+ "help you search for the best business in everywhere!";
				email.putExtra(android.content.Intent.EXTRA_TEXT,msg);
				email.setType("plain/text");
				startActivity(Intent.createChooser(email, "Email:"));
				return;
			}
		});

		ibox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;

			}
		});

		ibox.show();
	}
	

	@Override 
	public void onStop() {
		super.onStop();
		((MainUI) getActivity()).profile = resize;
	}
}
