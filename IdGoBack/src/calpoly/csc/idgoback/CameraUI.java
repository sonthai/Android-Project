package calpoly.csc.idgoback;


import java.io.ByteArrayOutputStream;

import com.facebook.Session;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;


public class CameraUI extends Fragment {
	final int PICTURE_ACTIVITY = 1; // This is only really needed if you are catching the results of more than one activity. It'll make sense later.
	final int RESULT_OK = -1;
	final int RESULT_CANCELED = 0;
	boolean takenPicture = false;
    
    @Override
    public void onCreate(Bundle bundle) {
    	super.onCreate(bundle);
    	takenPicture = false;
    }
    
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, PICTURE_ACTIVITY);
    }
    
    
    @Override
    public void onResume() {
    	super.onResume();
    	if (!takenPicture) {
    		dispatchTakePictureIntent();
    	}
    	else {
    		ActionBar bar = getActivity().getActionBar();
    		bar.setSelectedNavigationItem(0);
    		takenPicture = false;
    	}
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
    	if (requestCode == PICTURE_ACTIVITY)
    	{
	        if( resultCode == RESULT_OK) {
	        	System.out.println("PICTURE SAVED");
	        	takenPicture = true;
	            Bitmap photo = (Bitmap) data.getExtras().get("data");
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
	            byte[] byteArray = stream.toByteArray();
	            
	            System.out.println("Passing intent" + byteArray.length);
	            Intent intent;
	            if (Session.getActiveSession().isOpened()) {
	            	intent = new Intent(getActivity(),PictureHandlerWithFB.class);
	            }
	            else {
	            	 intent = new Intent(getActivity(),PictureHandler.class);
	            }
	    		intent.putExtra("photo", byteArray);
	    		startActivity(intent);	        
	        }
	        else if (resultCode == RESULT_CANCELED) {
	        	System.out.println("PICTURE CANCELED");
	        	takenPicture = true;
	        }
    	}
    	else {
    		getActivity().finish();
    		takenPicture = true;
    	}
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	System.out.println("On pause");
    }
    
    
    
    
//    public void postImageonWall(final Bitmap photo) {
//    	final File file = new File("/home/kane/Pictures/Explosion3.png");
//		System.out.println("HERE?");
//
//        Session session = Session.getActiveSession();
//        if (!session.isOpened() && !session.isClosed()) {
//            session.openForRead(new Session.OpenRequest(super.getActivity()).setCallback(statusCallback));
//        } else {
//
//			Session.openActiveSession(super.getActivity(), true, statusCallback);
//		
//        }
//    }	
    
    
//    private class SessionStatusCallback implements Session.StatusCallback {
//        @Override
//        public void call(Session session, SessionState state, Exception exception) {
//			System.out.println("CREATED SESSION IN CAMERA");
//			
//			if (session.isOpened()) {
//		        Request request6 = Request.newUploadPhotoRequest(
//		        		session, photo, new Request.Callback() {
//
//		    				@Override
//		    				public void onCompleted(Response response) {
//		    					FacebookRequestError fbre = response.getError();
//		    					System.out.println("Error1: " + fbre.getErrorType());
//		    					System.out.println("Error2: " + fbre.getErrorMessage());
//		    					System.out.println("CHEE_HEE");
//		    				}
//		    			});
//		        
//		        RequestAsyncTask task6 = new RequestAsyncTask(request6);
//		        task6.execute();
//				System.out.println("SIGNED IN THROUGH MAIN");
//			}
//			else {
//				System.out.println("Session NOT OPENED THROUGH MAIN");
//			}
//        }
//    }
		
//		try {
//	        Request request6 = Request.newUploadPhotoRequest(
//	        		UserInfo.facebookSession, photo, new Request.Callback() {
//
//	    				@Override
//	    				public void onCompleted(Response response) {
//	    					FacebookRequestError fbre = response.getError();
//	    					System.out.println("Error1: " + fbre.getErrorType());
//	    					System.out.println("Error2: " + fbre.getErrorMessage());
//	    					System.out.println("CHEE_HEE");
//	    				}
//	    			});
//	        
//	        RequestAsyncTask task6 = new RequestAsyncTask(request6);
//	        task6.execute();
//					
////			Request.newUploadPhotoRequest(UserInfo.facebookSession, photo , new Request.Callback() {
////
////				@Override
////				public void onCompleted(Response response) {
////					System.out.println("CHEE_HEE");
////				}
////			});
//		} catch (Exception ex) {//FileNotFoundException e) {
//			System.out.println("EXCEPTION CAUGHT FOR REQUEST:" + ex); 
//		}
    	
    	
    	
    	
    	
    	
    	
    	
    	/*
    	Request.newUploadPhotoRequest(UserInfo.facebookSession, photo, callback)

  		Session.openActiveSession(super.getActivity(),true, new Session.StatusCallback() {
			// callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					System.out.println("SESSION OPENED");
					try {
						Request.newUploadPhotoRequest(session,photo , new Request.Callback() {

//					Request.newUploadPhotoRequest(session, photo, new Request.Callback() {

							// callback after Graph API response with user object
							@Override
							public void onCompleted(Response response) {
								System.out.println("CHEE_HEE");
							}
						});
					} catch (Exception ex) {//FileNotFoundException e) {
						System.out.println("EXCEPTION CAUGHT FOR REQUEST:" + ex); 
					}
				}

			}
  		});
*/
 

}
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//    	super.onActivityResult(requestCode, resultCode, intent);
//
//    	AlertDialog msgDialog;
//    	if (resultCode == RESULT_CANCELED) { // The user didn't like the photo. ;_;
//    		msgDialog = createAlertDialog("Q_Q", "Kitty wouldn't sit still, eh? It's ok - you can try again!", "OK! I'LL TRY AGAIN!");
//
//    	} else {
//	/*
//	This is where you would trap the requestCode (in this case PICTURE_ACTIVITY). Seeing as how this is the ONLY
//	Activity that we are calling from THIS activity, it's kind of a moot point. If you had more than one activity that
//	you were calling for results, you would need to throw a switch statement in here or a bunch of if-then-else
//	constructs. Whatever floats your boat.
//	*/
//    		msgDialog = createAlertDialog("ZOMG!", "YOU TOOK A PICTURE! WITH YOUR PHONE! HOLY CRAP!", "I KNOW RITE??!?");
//
//	/*
//	Yes, I know that throwing a simple alert dialog doesn't really do anything impressive.
//	If you wanna do something with the picture (save it, display it, shoot it to a web server, etc) then you can get the
//	image data like this:
//	Bitmap = getIntent().getExtras().get("data");
//	Then do whatever you want with it.
//	*/
//
//    	}
//
//    	msgDialog.show();
//    }

    
//    private AlertDialog createAlertDialog(String title, String msg, String buttonText){
//    	AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//    	AlertDialog msgDialog = dialogBuilder.create();
//    	msgDialog.setTitle(title);
//    	msgDialog.setMessage(msg);
//    	msgDialog.setButton(buttonText, new DialogInterface.OnClickListener(){
//    		@Override
//    		public void onClick(DialogInterface dialog, int idx){
//    			return; // Nothing to see here...
//    		}
//    	});
//
//    	return msgDialog;
//    }
    



