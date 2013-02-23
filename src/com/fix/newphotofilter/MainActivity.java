package com.fix.newphotofilter;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.Ragnarok.BitmapFilter;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	Bitmap sourceBitmap;
	Bitmap change;
	
	Handler handler;
	
	ProgressDialog progressDialog = null;
	
	int styleId = 0;
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static int RESULT_LOAD_IMAGE = 1;
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								"original",
								getString(R.string.gray),
								getString(R.string.old),
								getString(R.string.sharp),
								getString(R.string.blur),
								getString(R.string.lomo),
								getString(R.string.oil),
								getString(R.string.SoftGlow),
								getString(R.string.pixelate),
								getString(R.string.HDR),
								getString(R.string.gaussBlur),
								getString(R.string.light),
								getString(R.string.invert),
								getString(R.string.neon),
								getString(R.string.tv),
								getString(R.string.relief),								
								}), this);
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
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            sourceBitmap = BitmapFactory.decodeFile(picturePath);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
         
        }
    }


	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
void applyFilter() {
		
		progressDialog = ProgressDialog.show(this, "请稍后", "正在处理中...");
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
				imageView.setImageBitmap(change);
				progressDialog.dismiss();
			}
			
		};
		}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		/*
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit(); */
		switch(position+1) {
		case 1:
			ImageView imageView = (ImageView) findViewById(R.id.imgView);
			imageView.setImageBitmap(sourceBitmap);
			return true;
		case 2:
			styleId = BitmapFilter.GRAY_STYLE;
			applyFilter();
			return true;
		case 3:
			styleId = BitmapFilter.OLD_STYLE;
			applyFilter();
			return true;
		case 4:
			styleId = BitmapFilter.SHARPEN_STYLE;
			applyFilter();
			return true;
		case 5:
			styleId = BitmapFilter.BLUR_STYLE;
			applyFilter();
			return true;
		case 6:
			styleId = BitmapFilter.LOMO_STYLE;
			applyFilter();
			return true;
		case 7:
			styleId = BitmapFilter.OIL_STYLE;
			applyFilter();
			return true;
		case 8:
			styleId = BitmapFilter.SOFT_GLOW_STYLE;
			applyFilter();
			return true;
		case 9:
			styleId = BitmapFilter.PIXELATE_STYLE;
			applyFilter();
			return true;
		case 10:
			styleId = BitmapFilter.HDR_STYLE;
			applyFilter();
			return true;
		case 11:
			styleId = BitmapFilter.GAUSSIAN_BLUR_STYLE;
			applyFilter();
			return true;
		case 12:
			styleId = BitmapFilter.LIGHT_STYLE;
			applyFilter();
			return true;
		case 13:
			styleId = BitmapFilter.NEON_STYLE;
			applyFilter();
			return true;
		case 14:
			styleId = BitmapFilter.INVERT_STYLE;
			applyFilter();
			return true;
		case 15:
			styleId = BitmapFilter.TV_STYLE;
			applyFilter();
			return true;
		case 16:
			styleId = BitmapFilter.RELIEF_STYLE;
			applyFilter();
			return true;
		}

		
		return true;
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
	}

}
