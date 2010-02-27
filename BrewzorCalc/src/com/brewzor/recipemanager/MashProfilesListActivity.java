package com.brewzor.recipemanager;

import com.brewzor.calculator.OptionsMenuHandler;
import com.brewzor.calculator.R;
import com.brewzor.calculator.preferences.Preferences;
import com.brewzor.converters.BeerColor;
import com.brewzor.recipemanager.FermentablesListActivity.FermentablesAdapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class MashProfilesListActivity extends Activity {

	private SharedPreferences prefs;

    public static final String[] displayFields = new String[] {	Fermentable.FIELDS.NAME, 
														        Fermentable.FIELDS.MANUFACTURER,
														        Fermentable.FIELDS.COLOR};

	public static final int[] displayViews = new int[] { 	R.id.name, 
															R.id.manufacturer,
															R.id.color};

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
        //setListAdapter(new MashProfilesAdapter(this, cProfiles));	
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

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		cProfiles.close();
		db.close();
		super.onDestroy();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getPrefs();
		cProfiles.requery();
		super.onResume();
	}
	
	private void getPrefs() {
		//colorUnit = grainColor.typeFromPref(Preferences.GLOBAL_BEER_COLOR_UNIT, BeerColor.Unit.SRM);
	}

	
}
