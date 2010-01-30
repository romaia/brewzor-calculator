/*
    This file is part of Brewzor.

    Copyright (C) 2010 James Whiddon

    Brewzor is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Brewzor is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Brewzor.  If not, see <http://www.gnu.org/licenses/>.

*/
package com.brewzor.calculator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import com.brewzor.calculator.R;
import com.brewzor.calculator.preferences.Preferences;
import com.brewzor.utils.ErrorReporter;

// http://androidblogger.blogspot.com/2009/12/how-to-improve-your-application-crash.html
public class BrewzorActivity extends ListActivity {
	private ArrayAdapter<String> aa;
	private ArrayList<String> listCalculators;
	private String[] calculators;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Resources aResource = this.getResources();
		calculators = aResource.getStringArray(R.array.calculatorList);
		listCalculators = new ArrayList<String>(Arrays.asList(calculators));
		aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listCalculators);
		setListAdapter(aa);
		
    	Eula.show(this, getBaseContext());
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Editor editor = prefs.edit();

        if (prefs.getBoolean(Preferences.GLOBAL_INIT, false) == false) {

	        editor.putBoolean(Preferences.GLOBAL_INIT, true);
	        
	        editor.putString(Preferences.GLOBAL_TEMPERATURE_UNIT, "FAHRENHEIT");
	        editor.putString(Preferences.GLOBAL_GRAVITY_UNIT, "SG");
	        editor.putString(Preferences.GLOBAL_EXTRACT_MASS_UNIT, "OUNCE");
	        editor.putString(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE, "60");
	        
	        editor.putString(Preferences.BATCH_VOLUME_UNIT, "GALLON");
	        editor.putString(Preferences.BATCH_FINAL_VOLUME, "6");
	        editor.putString(Preferences.BATCH_GRAIN_MASS_UNIT, "POUND");
	        editor.putString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, ".31");
	        editor.putString(Preferences.BATCH_BOIL_MINUTES, "60");
	    		
	        editor.putString(Preferences.KETTLE_DISTANCE_UNIT, "INCH");
	        editor.putString(Preferences.KETTLE_DIAMETER, "0");
	        editor.putString(Preferences.KETTLE_FALSE_BOTTOM_HEIGHT, "0");
	        editor.putString(Preferences.KETTLE_EVAPORATION_RATE, "10");
	        editor.putString(Preferences.KETTLE_COOLING_LOSS, "4");

	        editor.putBoolean(Preferences.KETTLE_CORRECT_FOR_EXPANSION, false);
	        	        
	        editor.commit();	
        }
        
        final ErrorReporter reporter = ErrorReporter.getInstance();
        reporter.Init(this);
        if (reporter.bIsThereAnyErrorFile()) {
	        new AlertDialog.Builder(this)
	        		.setTitle(getString(R.string.crash_report_dialog_title))
	        		.setMessage(getString(R.string.crash_report_dialog_text))
	        		.setPositiveButton(getString(R.string.crash_report_dialog_ok), new OnClickListener() {
	
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        reporter.CheckErrorAndSendMail(BrewzorActivity.this);
			}})
	        .setNegativeButton(getString(R.string.crash_report_dialog_cancel), new OnClickListener(){
	
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	reporter.deleteAllReports();
	        	dialog.dismiss();
	        }})
	        .show();

        }
	}
	
	@Override
	protected void onListItemClick(ListView aL, View aV, int aPosition, long aId) {
		super.onListItemClick(aL, aV, aPosition, aId);
		launchCalculator(aPosition);
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

	protected void launchCalculator(int calculatorId) {
		Intent i;
		switch (calculatorId) {
			case 0: i = new Intent(this, StrikeTemperatureCalculatorActivity.class); break;
			case 1: i = new Intent(this, GravityCorrectionCalculatorActivity.class); break;
			case 2: i = new Intent(this, BoilOffCalculatorActivity.class); break;
			case 3: i = new Intent(this, CylinderVolumeCalculatorActivity.class); break;
			case 4: i = new Intent(this, CylinderHeightCalculatorActivity.class); break;
			case 5: i = new Intent(this, CarbonationCalculatorActivity.class); break;
			case 6: i = new Intent(this, HydrometerCorrectionCalculatorActivity.class); break;
			case 7: i = new Intent(this, UnitConverterActivity.class); break;
			default: i = new Intent(this, BrewzorPreferencesActivity.class);
		}
		startActivity(i);
	}
};