package com.fix.filterlite;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FilterList extends ListActivity {
	
	int itemPosition;

	Activity activity = this;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_list);
		// Show the Up button in the action bar.
		setupActionBar();
		String[] effects = {
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
				};
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, effects);
		setListAdapter(adapter);
        
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		
			    //Toast.makeText(getBaseContext(), position + "", Toast.LENGTH_LONG).show();
			    itemPosition = position;
			    activity.finish();
			    			    
			}
			
		});
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra("styleId", itemPosition);
		setResult(RESULT_OK, intent);
		super.finish();
	}

}
