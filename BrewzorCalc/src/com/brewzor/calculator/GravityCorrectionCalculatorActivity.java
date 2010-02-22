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

import com.brewzor.calculator.preferences.Preferences;
import com.brewzor.calculator.R;
import com.brewzor.converters.Gravity;
import com.brewzor.converters.Mass;
import com.brewzor.converters.Volume;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

public class GravityCorrectionCalculatorActivity extends Activity {
	
	private EditText currentGravityEntry;
	private TextView currentGravityUnitType;
	private EditText desiredGravityEntry;
	private TextView desiredGravityUnitType;
	private EditText currentVolumeEntry;
	private TextView currentVolumeUnitType;
	private TextView gravityCorrectionText;
	
	private Mass.Unit massType;
	private Volume.Unit volumeType;
	private Gravity.Unit gravityType;
	
	private Gravity currentGravity;
	private Gravity desiredGravity;
	private Volume currentVolume;
	private Mass lme;
	private Mass dme;
	private Volume waterVolume;

	private SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.gravity_correction)));
        setContentView(R.layout.calculator_gravity_correction);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        currentGravityEntry = (EditText) findViewById(R.id.currentGravityEntry);
        currentGravityEntry.setOnKeyListener(mKeyListener);
        currentGravityUnitType = (TextView) findViewById(R.id.currentGravityUnitType);
        
        desiredGravityEntry = (EditText) findViewById(R.id.desiredGravityEntry);
        desiredGravityEntry.setOnKeyListener(mKeyListener);
        desiredGravityUnitType = (TextView) findViewById(R.id.desiredGravityUnitType);

        currentVolumeEntry = (EditText) findViewById(R.id.currentVolumeEntry);
        currentVolumeEntry.setOnKeyListener(mKeyListener);
        currentVolumeUnitType = (TextView) findViewById(R.id.currentVolumeUnitType);
        
        gravityCorrectionText = (TextView) findViewById(R.id.gravityCorrectionDescription);
    
        currentGravity = new Gravity(0.0, Gravity.Unit.SG, getBaseContext(), prefs);
        desiredGravity = new Gravity(0.0, Gravity.Unit.SG, getBaseContext(), prefs);
        currentVolume = new Volume(0.0, Volume.Unit.MILLILITER, getBaseContext(), prefs);
        waterVolume = new Volume(0.0, Volume.Unit.MILLILITER, getBaseContext(), prefs);
        lme = new Mass(0.0, Mass.Unit.GRAM, getBaseContext(), prefs);
        dme = new Mass(0.0, Mass.Unit.GRAM, getBaseContext(), prefs);
        
	}

	@Override
	public void onResume() {
		super.onResume();
		getPrefs();
		calculate();
	}
		
	public OnKeyListener mKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_UP) calculate();
			return false;
		}
	};
	
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

	private void calculate() {
		
		currentGravity.setValue(currentGravityEntry, 0.0);
		currentGravity.setType(gravityType);
		
		desiredGravity.setValue(desiredGravityEntry, 0.0);
		desiredGravity.setType(gravityType);
		
		currentVolume.setValue(currentVolumeEntry, 0.0);
		currentVolume.setType(volumeType);
				
		double cVolume = currentVolume.compare(Volume.Unit.GALLON);
		double cGravity = currentGravity.compare(Gravity.Unit.GU) * cVolume;
		double dGravity = desiredGravity.compare(Gravity.Unit.GU) * cVolume;
		
		// Formulas from: http://www.brewboard.com/index.php?showtopic=31009
		if (cVolume <= 0 || cGravity == 0 || dGravity == 0) {

			// empty fields
			gravityCorrectionText.setText("");
		
		} else if (dGravity < cGravity) {
			
			// dilute
			waterVolume.setValue(((cVolume * cGravity) / dGravity) - cVolume);
			waterVolume.setType(Volume.Unit.GALLON);
			
			// convert from gallons
			waterVolume.convert(volumeType);
			gravityCorrectionText.setText(getString(R.string.add_water_format, waterVolume.getValue(), waterVolume.getLabelAbbr()));
			
		} else if (desiredGravity.getValue() > currentGravity.getValue()) {

			// add extract
			dme.setValue((dGravity - cGravity) / 44.0);
			dme.setType(Mass.Unit.POUND);
			dme.convert(massType);
			
			lme.setValue((dGravity - cGravity) / 36.0);
			lme.setType(Mass.Unit.POUND);
			lme.convert(massType);
			
			gravityCorrectionText.setText(getString(R.string.add_extract_format, dme.getValue(), dme.getLabelAbbr(), lme.getValue(), lme.getLabelAbbr()));
			
		} else {
			// do nothing
			gravityCorrectionText.setText(getString(R.string.add_nothing_format));
		}
		
	}
	
	private void getPrefs() {
		
		gravityType = currentGravity.typeFromPref(Preferences.GLOBAL_GRAVITY_UNIT, Gravity.Unit.SG);

        currentGravity.setType(gravityType);
    	currentGravityUnitType.setText(currentGravity.getLabelAbbr());

    	desiredGravity.setType(gravityType);
    	desiredGravityUnitType.setText(desiredGravity.getLabelAbbr());
                
        volumeType = currentVolume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON);
        
        currentVolume.setType(volumeType);
        waterVolume.setType(volumeType);
        
    	currentVolumeUnitType.setText(currentVolume.getLabelAbbr());

        massType = dme.typeFromPref(Preferences.GLOBAL_EXTRACT_MASS_UNIT, Mass.Unit.GRAM);
        
        dme.setType(massType);
        lme.setType(massType);
	
	}

}
