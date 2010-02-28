package com.brewzor.recipemanager;

import java.text.DecimalFormat;

import com.brewzor.calculator.OptionsMenuHandler;
import com.brewzor.calculator.R;

import android.app.ListActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MashProfilesListActivity extends ListActivity {

	private SharedPreferences prefs;

	protected DBHelper db;
	protected Cursor cProfiles;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.mash_profiles)));
        setContentView(R.layout.mash_profiles_list);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        getPrefs();
        
        db = new DBHelper(this);
        db.open();
        cProfiles = db.getAllMashProfiles();
        setListAdapter(new MashProfilesAdapter(this, cProfiles));
    }
	
	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//return OptionsMenuHandler.createMashProfileMenu(this, menu);
		return false;
	}

	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return OptionsMenuHandler.showMenu(this, item);
	}

	@Override
	protected void onDestroy() {
		cProfiles.close();
		db.close();
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		getPrefs();
		cProfiles.requery();
		super.onResume();
	}
	
	private void getPrefs() {
		//colorUnit = grainColor.typeFromPref(Preferences.GLOBAL_BEER_COLOR_UNIT, BeerColor.Unit.SRM);
	}

	public final class MashProfilesAdapter extends SimpleCursorAdapter {
		
		private Context context; 
		private LayoutInflater inflater;

		private TextView nameView;
		
		private int id_idx = -1;
		private int name_idx = -1;
				
		public MashProfilesAdapter(Context context, Cursor c) {
			super(context, R.layout.mash_profile_list_item, c, MashProfile.aFields, MashProfile.aViews);
	        this.context = context; 
	        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	        id_idx = c.getColumnIndex(MashProfile.FIELDS.ID);
	        name_idx = c.getColumnIndex(MashProfile.FIELDS.NAME);
	        
		}

		@Override
		public void bindView(View v, final Context context, Cursor cursor) {
			nameView = (TextView) v.findViewById(R.id.name); 
			nameView.setText(cursor.getString(name_idx)); 

	        v.setTag(new MashProfileHolder(	cursor.getLong(id_idx),
	        								cursor.getString(name_idx) ));

	        v.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(final View v) {
					//Log.v("DB", "onLongClick()");
					displayModifyDialog(v);
					return false;
				} });
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return inflater.inflate(R.layout.mash_profile_list_item, null);
		}
		
		private void displayModifyDialog(final View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(((MashProfileHolder)v.getTag()).name);
			builder.setItems(getResources().getStringArray(R.array.editDeleteList), new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        
			    	switch (item) {
				    	case 0: 
				    		// Edit
				    		Intent i = new Intent(context, MashProfileActivity.class);
				    		i.putExtra("com.brewzor.recipemanager.MashProfileActivity.id", ((MashProfileHolder)v.getTag()).id);
				    		context.startActivity(i);
				    		break;
				    	case 1: 
				    		// Delete
				    		if (db.deleteFermentable(((MashProfileHolder)v.getTag()).id)) {
				    			Toast.makeText(context.getApplicationContext(), getString(R.string.mash_profiles_item_deleted), Toast.LENGTH_SHORT).show();
				    			cProfiles.requery();
				    		} else {
				    			Toast.makeText(context.getApplicationContext(), getString(R.string.mash_profiles_error_item_deleted), Toast.LENGTH_SHORT).show();
				    		}
				    		break;
			    	}
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		
		private final class MashProfileHolder {

			public long id = -1;
			public String name;

			/**
			 * @param id
			 * @param name
			 */
			public MashProfileHolder(long id, String name) {
				super();
				this.id = id;
				this.name = name;
			}

		}
		
	}

}
