package com.brewzor.recipemanager;

import com.brewzor.calculator.OptionsMenuHandler;
import com.brewzor.calculator.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MashProfileActivity extends Activity {
	private SharedPreferences prefs;

	DBHelper db;
	Cursor cProfile;
	Cursor cSteps;
	MashProfile profile;
	
	long id = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.mash_profiles_edit_mash_profile)));
        setContentView(R.layout.mash_profile_edit);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Intent i = getIntent();
        id = i.getLongExtra("com.brewzor.recipemanager.MashProfileActivity.id", -1);

        db = new DBHelper(this);
        db.open();
        
	}

	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return OptionsMenuHandler.createMenu(this, menu);
	}

	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return OptionsMenuHandler.showMenu(this, item);
	}
	
	@Override
	public void onResume() {
		super.onResume();
        getPrefs();
        loadData();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		//Log.v("FA", "onDestroy()");
		if (cProfile!= null) cProfile.close();
		if (cSteps!= null) cSteps.close();
		if (db != null) db.close();
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		//Log.v("FA", "onPause()");
		saveMashProfile();
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		//Log.v("FA", "onStop()");
		super.onStop();
	}
	
	private void saveMashProfile() {
		
	}

	private void loadData() {
		
		
	}
	
	private void getProfileCursor(long rowId) {
        try {
            cProfile = db.getMashProfile(rowId);
        } catch (SQLException e) {
        	//
        }

        if (cProfile.getCount() == 0){
    		Toast.makeText(getApplicationContext(), getString(R.string.mash_profiles_error_invalid_id), Toast.LENGTH_SHORT).show();
    		finish();
        }
	}
	
	private void getStepsCursor(long rowId) {
        try {
            cSteps = db.getMashProfileStep(rowId);
        } catch (SQLException e) {
        	//
        }

        if (cSteps.getCount() == 0){
    		Toast.makeText(getApplicationContext(), getString(R.string.mash_profiles_error_invalid_id), Toast.LENGTH_SHORT).show();
    		finish();
        }
	}
	
	private void getPrefs() {
		//colorUnit = color.typeFromPref(Preferences.GLOBAL_BEER_COLOR_UNIT, BeerColor.Unit.SRM);
        //color.setType(colorUnit);
        //colorType.setText(color.getLabelAbbr());
    	
        //gravityUnit = gravity.typeFromPref(Preferences.GLOBAL_GRAVITY_UNIT, Gravity.Unit.SG);
        //gravity.setType(gravityUnit);
        //potentialType.setText(gravity.getLabelAbbr());
        
        //volumeUnitType.setText(volume.getLabelAbbr());
	}
	

	
}
