package com.fix.filterlite;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import com.fix.filterlite.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.Ragnarok.BitmapFilter;

public class MainActivity extends Activity {
	String fileName;
	String fName;
	Bitmap sourceBitmap;
	Bitmap change;
	String _Location;
	String _DateTime;
	String _MAKE;
	String _MODEL;
	String metaData;
	String picturePath;
	Handler handler;
	String effect="original"; 
	String ext="jpg";
	int itemPosition;
	
	ProgressDialog progressDialog = null;
	
	int styleId = 0;
	private static int RESULT_LOAD_IMAGE = 1;
	private static int RESULT_GET_STYLEID = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ImageView imageView = (ImageView) findViewById(R.id.imgView);
		sourceBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
	}
	
	public void pickImage(View v) {
		Log.w("mymessage","into pick button event...........");
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         
        startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Log.w("path", picturePath);
            String fileName = new File(picturePath).getName();
            String fName =fileName.substring(0, fileName.lastIndexOf('.'));
            Log.w("filename",fName);
            final String FPATH = picturePath;
            Filename pic = new Filename(FPATH, '/', '.');
            Log.w("filename", "filename"+pic.filename());
            Log.w("filename", "extnsn"+pic.extension());
            ext = pic.extension();
  

            
            //Toast.makeText(this, ""+picturePath,Toast.LENGTH_SHORT).show();
            //Bitmap resizedBmp = resizedBitmap(change, 640, 800);
            cursor.close();
        	_Location = null;
        	_DateTime = null;
        	_MAKE = null;
        	_MODEL = null;
            computeMetaData();
             
            metaData = "";
            if(_DateTime!=null) {
            	metaData += "\n"+_DateTime;
            }
            if(_Location!=null) {
            	metaData += "\n"+ _Location;
            }
            else {
            	if (_MAKE!=null) {
            		metaData += "taken with "+ _MAKE;
            	}
            	if (_MODEL!=null) {
            		metaData += " _MODEL";
            	}
            }
            
            if(metaData == "") {
            	metaData = "Oops! we weren't able to detect when and where this photo is clicked :( ";
            }
            
			TextView tv = (TextView) findViewById(R.id.meta);
			tv.setText(metaData);
            Log.w("sadfdsafdsafsadf", metaData);
             
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            sourceBitmap = BitmapFactory.decodeFile(picturePath);
            //Bitmap bmp =drawTextToBitmap(getBaseContext(),sourceBitmap, metaData, 20, 255);
            imageView.setImageBitmap(sourceBitmap);
         
        }
        
        if (requestCode == RESULT_GET_STYLEID && resultCode == RESULT_OK && null != data) {
        	itemPosition = data.getExtras().getInt("styleId");
        	switch(itemPosition+1) {
    		case 1:
    			ImageView imageView = (ImageView) findViewById(R.id.imgView);
    			//Bitmap bmp =drawTextToBitmap(getBaseContext(),sourceBitmap,"Hello! Pick an Image and apply Effects... ", 20, 255);
    			imageView.setImageBitmap(sourceBitmap);
    			break;

    		case 2:
    			styleId = BitmapFilter.GRAY_STYLE;
    			applyFilter(getString(R.string.gray));
    			effect = getString(R.string.gray);
    			break;
    		case 3:
    			styleId = BitmapFilter.OLD_STYLE;
    			applyFilter(getString(R.string.old));
    			effect = getString(R.string.old);
    			break;
    		case 4:
    			styleId = BitmapFilter.SHARPEN_STYLE;
    			applyFilter(getString(R.string.sharp));
    			effect = getString(R.string.sharp);
    			break;
    		case 5:
    			styleId = BitmapFilter.BLUR_STYLE;
    			applyFilter(getString(R.string.blur));
    			effect = getString(R.string.blur);
    			break;
    		case 6:
    			styleId = BitmapFilter.LOMO_STYLE;
    			applyFilter(getString(R.string.lomo));
    			effect = getString(R.string.lomo);
    			break;
    		case 7:
    			styleId = BitmapFilter.OIL_STYLE;
    			applyFilter(getString(R.string.oil));
    			effect = getString(R.string.oil);
    			break;
    		case 8:
    			styleId = BitmapFilter.SOFT_GLOW_STYLE;
    			applyFilter(getString(R.string.SoftGlow));
    			effect = getString(R.string.SoftGlow);
    			break;
    		case 9:
    			styleId = BitmapFilter.PIXELATE_STYLE;
    			applyFilter(getString(R.string.pixelate));
    			effect = getString(R.string.pixelate);
    			break;
    		case 10:
    			styleId = BitmapFilter.HDR_STYLE;
    			applyFilter(getString(R.string.HDR));
    			effect = getString(R.string.HDR);
    			break;
    		case 11:
    			styleId = BitmapFilter.GAUSSIAN_BLUR_STYLE;
    			applyFilter(getString(R.string.gaussBlur));
    			effect = getString(R.string.gaussBlur);
    			break;
    		case 12:
    			styleId = BitmapFilter.LIGHT_STYLE;
    			applyFilter(getString(R.string.light));
    			effect = getString(R.string.light);
    			break;
    		case 14:
    			styleId = BitmapFilter.NEON_STYLE;
    			applyFilter(getString(R.string.neon));
    			effect = getString(R.string.neon);
    			break;
    		case 13:
    			styleId = BitmapFilter.INVERT_STYLE;
    			applyFilter(getString(R.string.invert));
    			effect = getString(R.string.invert);
    			break;
    		case 15:
    			styleId = BitmapFilter.TV_STYLE;
    			applyFilter(getString(R.string.tv));
    			effect = getString(R.string.tv);
    			break;
    		case 16:
    			styleId = BitmapFilter.RELIEF_STYLE;
    			applyFilter(getString(R.string.relief));
    			effect = getString(R.string.relief);
    			break;
    		}
        	
        }
        
    }
	
	private void ShowExif(ExifInterface exif)
    {
     String myAttribute="Exif information ---\n";
     myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif);
     myAttribute += getTagString(ExifInterface.TAG_FLASH, exif);
     myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE, exif);
     myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif);
     myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif);
     myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif);
     myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH, exif);
     myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH, exif);
     myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
     myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);
     myAttribute += getTagString(ExifInterface.TAG_ORIENTATION, exif);
     myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE, exif);
     Log.w("tags", myAttribute);
    }
    
    private String getTagString(String tag, ExifInterface exif)
    {
     return(tag + " : " + exif.getAttribute(tag) + "\n");
    }

   public void getLocationAddress(double longitude, double latitude) {
    //LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    //String provider = locationManager.getBestProvider(new Criteria(), true);

    //Location locations = locationManager.getLastKnownLocation(provider);
    //List<String>  providerList = locationManager.getAllProviders();
    //if(null!=locations && null!=providerList && providerList.size()>0){                 
    //double longitude = locations.getLongitude();
    //double latitude = locations.getLatitude();
    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());                 
    try {
        List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
        if(null!=listAddresses&&listAddresses.size()>0){
            String _Location = listAddresses.get(0).getAddressLine(1);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

   
   public void computeMetaData() {
       try {
    	   ExifInterface exif = new ExifInterface(picturePath);
    	   ShowExif(exif);
    	   if(exif.getAttribute(ExifInterface.TAG_DATETIME)!=null) {
    		   _DateTime = getTagString(ExifInterface.TAG_DATETIME, exif);
    	   }
    	   Log.w("adsfdsafdsaf", getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif));
    	   if(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)!=null && exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)!=null) {
    		   Log.w("dsfdsafad","into if...");
    		   double longitude = Double.parseDouble(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
    		   double latitude = Double.parseDouble(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
    		   getLocationAddress(longitude, latitude);
    	   }
    	   
    	   if(exif.getAttribute(ExifInterface.TAG_MAKE)!=null) {
    		   _MAKE = exif.getAttribute(ExifInterface.TAG_MAKE);
    	   }
    	   
    	   if(exif.getAttribute(ExifInterface.TAG_MODEL)!=null) {
    		   _MODEL = getTagString(ExifInterface.TAG_MODEL, exif);
    	   }
    	   
       } catch (IOException e) {
    	   // TODO Auto-generated catch block
    	   e.printStackTrace();
    	   Toast.makeText(this, "Error!",Toast.LENGTH_LONG).show();
       }
	   
   }
   
   
    
    
    public void saveImage(View v) {
      	 ImageView imageView = (ImageView) findViewById(R.id.imgView);
     	 imageView.buildDrawingCache();
    	 Bitmap bm=imageView.getDrawingCache();
    	 
    	 OutputStream fOut = null;
    	 Uri outputFileUri;
    	 try {
    	    File root = new File(Environment.getExternalStorageDirectory()
    	      + File.separator + "PhotoFilter" + File.separator);
    	    root.mkdirs();
    	    //extnsn =  fileName.substring(fileName.lastIndexOf("."));
    	    //Log.w("ectension", ext + "");
    	    //Log.w("qwerty", "fname " + fileName.substring(fileName.lastIndexOf(".")));
    	    File sdImageMainDirectory = new File(root, fName+effect+"."+ext);
    	    outputFileUri = Uri.fromFile(sdImageMainDirectory);
    	    fOut = new FileOutputStream(sdImageMainDirectory);
    	    
         } catch (Exception e) {
    	    Toast.makeText(this, "Error occured. Please try again later.",
    	      Toast.LENGTH_SHORT).show();
         }

    	   try {
    	    bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
    	    Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
    	    fOut.flush();
    	    fOut.close();
    	   } catch (Exception e) {
    	   }
    }
    
    Bitmap resizedBitmap(Bitmap bitmapOrg, int newWidth, int newHeight) {
    	 int width = bitmapOrg.getWidth();
         int height = bitmapOrg.getHeight();
         Log.w("width", ""+width);
        
         // calculate the scale - in this case = 0.4f
         float scaleWidth = ((float) newWidth) / width;
         float scaleHeight = ((float) newHeight) / height;
        
         // createa matrix for the manipulation
         Matrix matrix = new Matrix();
         // resize the bit map
         matrix.postScale(scaleWidth, scaleHeight);
  
         // recreate the new Bitmap
         Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
                           width, height, matrix, true);
    
         // make a Drawable from Bitmap to allow to set the BitMap
         // to the ImageView, ImageButton or what ever
    	return resizedBitmap;
    }

void applyFilter(String effect) {
		
		progressDialog = ProgressDialog.show(this, "Applying...", effect);
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				change = BitmapFilter.changeStyle(sourceBitmap, styleId);
				Message msg = Message.obtain();
				msg.what = 1;
				handler.sendMessage(msg);
			}
			
		}.start();
		
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				ImageView imageView = (ImageView) findViewById(R.id.imgView);
				
				//Bitmap bmp =drawTextToBitmap(getBaseContext(),change,"hello", 20, 255 );
				imageView.setImageBitmap(change);
				progressDialog.dismiss();

			}
			
		};
		}

public Bitmap drawTextToBitmap(Context mContext,  Bitmap bitmap,  String mText, int size, int color) {
    try {
         Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Log.w("scale", "" + scale);
         //   Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
            // set default bitmap config if none
            if(bitmapConfig == null) {
              bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.rgb(color, color, color));
            // text size in pixels
            paint.setTextSize((int) (size * scale  ));
            // text shadow
            paint.setShadowLayer(5f, 0f, 0f, Color.BLACK);

            Log.w("bitmp width", bitmap.getWidth()+"");

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width())/6;
            int y = (bitmap.getHeight() + bounds.height())/2;
            

            canvas.drawText(mText, x * scale , y * scale   , paint);

            return bitmap;
    } catch (Exception e) {
        // TODO: handle exception



        return null;
    }

  }



class Filename {
	  private String fullPath;
	  private char pathSeparator, extensionSeparator;

	  public Filename(String str, char sep, char ext) {
	    fullPath = str;
	    pathSeparator = sep;
	    extensionSeparator = ext;
	  }

	  public String extension() {
	    int dot = fullPath.lastIndexOf(extensionSeparator);
	    return fullPath.substring(dot + 1);
	  }

	  public String filename() { // gets filename without extension
	    int dot = fullPath.lastIndexOf(extensionSeparator);
	    int sep = fullPath.lastIndexOf(pathSeparator);
	    return fullPath.substring(sep + 1, dot);
	  }

	  public String path() {
	    int sep = fullPath.lastIndexOf(pathSeparator);
	    return fullPath.substring(0, sep);
	  }
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void effectList(View v) {
		  Intent intent = new Intent(this, FilterList.class);
		  startActivityForResult(intent, RESULT_GET_STYLEID);
	}
	public void onPause() {
		metaData = "";
		super.onPause();
	}

	public void onResume() {
		metaData="";
		super.onResume();
	}
}
