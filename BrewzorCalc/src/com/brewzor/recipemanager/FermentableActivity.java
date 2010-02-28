package com.brewzor.recipemanager;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brewzor.calculator.OptionsMenuHandler;
import com.brewzor.calculator.R;
import com.brewzor.calculator.preferences.Preferences;
import com.brewzor.converters.BeerColor;
import com.brewzor.converters.Gravity;
import com.brewzor.converters.Volume;
import com.brewzor.utils.NumberFormat;

public class FermentableActivity extends Activity {
	
	private SharedPreferences prefs;

	DBHelper db;
	Cursor cFermentable;
	Fermentable fermentable;
	
	EditText nameEntry;
	EditText manufacturerEntry;
	EditText potentialEntry;
	EditText yieldEntry;
	EditText colorEntry;
	TextView potentialType;
	TextView colorType;
	Spinner typeSpinner;
	
	long id = -1;
	
	BeerColor color;
	BeerColor.Unit colorUnit = BeerColor.Unit.SRM;
	
	Gravity gravity;
	Gravity.Unit gravityUnit = Gravity.Unit.SG;
	ArrayList<String> typeList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.fermentables_edit_fermentables)));
        setContentView(R.layout.fermentable_edit);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Intent i = getIntent();
        id = i.getLongExtra("com.brewzor.recipemanager.FermentableActivity.id", -1);

        db = new DBHelper(this);
        db.open();
        
        nameEntry = (EditText) findViewById(R.id.name);
        manufacturerEntry = (EditText) findViewById(R.id.manufacturer);
        potentialEntry = (EditText) findViewById(R.id.potential);
        yieldEntry = (EditText) findViewById(R.id.yield);
        colorEntry = (EditText) findViewById(R.id.color);
        typeSpinner = (Spinner) findViewById(R.id.type);
        
        potentialType = (TextView) findViewById(R.id.potential_type);
        colorType = (TextView) findViewById(R.id.color_type);
        
        color = new BeerColor(0, BeerColor.Unit.SRM, getBaseContext(), prefs);
        gravity = new Gravity(0, Gravity.Unit.SG, getBaseContext(), prefs);

		typeList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.fermentableTypesIdList)));
        
        getPrefs();
        loadData();
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
		if (cFermentable != null) {
			//Log.v("FA", "closing cursor");
			cFermentable.close();
		}
		if (db != null) {
			//Log.v("FA", "closing db");
			db.close();
		}
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		//Log.v("FA", "onPause()");
		saveFermentable();
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
	
	private void saveFermentable() {
		//Log.v("FA", "saveFermentable()");
		color.setValue(colorEntry, 0.0);
		color.setType(colorUnit);
		gravity.setValue(potentialEntry, 0.0);
		gravity.setType(gravityUnit);
		Log.v("FA", "type index=" + typeSpinner.getSelectedItemId());
		if (id < 1) {
			
			db.insertFermentable(	
									nameEntry.getText().toString(), 
									typeList.get((int)typeSpinner.getSelectedItemId()),
									manufacturerEntry.getText().toString(), 
									gravity, 
									NumberFormat.parseDouble(yieldEntry.getText().toString(), 0.0), 
									color
									);
			
		} else {
			db.updateFermentable(	id, 
									nameEntry.getText().toString(), 
									typeList.get((int)typeSpinner.getSelectedItemId()),
									manufacturerEntry.getText().toString(), 
									gravity, 
									NumberFormat.parseDouble(yieldEntry.getText().toString(), 0.0), 
									color
									);
	
		}

	}
		
	private void loadData() {
		if (id > 0) {
			getCursor(id);
			nameEntry.setText(cFermentable.getString(cFermentable.getColumnIndex(Fermentable.FIELDS.NAME)));
			manufacturerEntry.setText(cFermentable.getString(cFermentable.getColumnIndex(Fermentable.FIELDS.MANUFACTURER)));
			yieldEntry.setText(cFermentable.getString(cFermentable.getColumnIndex(Fermentable.FIELDS.YIELD)));
			gravity.setValue(cFermentable.getDouble(cFermentable.getColumnIndex(Fermentable.FIELDS.POTENTIAL)));
			gravity.setType(Gravity.Unit.SG); // database is SG
			gravity.convert(gravityUnit);
			potentialEntry.setText(gravity.toString());
			color.setValue(cFermentable.getDouble(cFermentable.getColumnIndex(Fermentable.FIELDS.COLOR)));
			color.setType(BeerColor.Unit.SRM); // database is SRM
			color.convert(colorUnit);
			colorEntry.setText(color.toString());
			typeSpinner.setSelection(typeList.indexOf(cFermentable.getString(cFermentable.getColumnIndex(Fermentable.FIELDS.TYPE))));
		}
	}

	private void getCursor(long rowId) {
        try {
            cFermentable = db.getFermentable(rowId);
        } catch (SQLException e) {
        	//
        }

        if (cFermentable.getCount() == 0){
    		Toast.makeText(getApplicationContext(), getString(R.string.fermentables_error_invalid_id), Toast.LENGTH_SHORT).show();
    		finish();
        }
	}
	
	private void getPrefs() {
		colorUnit = color.typeFromPref(Preferences.GLOBAL_BEER_COLOR_UNIT, BeerColor.Unit.SRM);
        color.setType(colorUnit);
        colorType.setText(color.getLabelAbbr());
    	
        gravityUnit = gravity.typeFromPref(Preferences.GLOBAL_GRAVITY_UNIT, Gravity.Unit.SG);
        gravity.setType(gravityUnit);
        potentialType.setText(gravity.getLabelAbbr());
        
        //volumeUnitType.setText(volume.getLabelAbbr());

	}
	
}
