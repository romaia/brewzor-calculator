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

import com.brewzor.calculator.R;
import com.brewzor.calculator.preferences.Preferences;
import com.brewzor.converters.Temperature;
import com.brewzor.converters.Mass;
import com.brewzor.converters.Volume;
import com.brewzor.utils.NumberFormat;

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

public class StrikeTemperatureCalculatorActivity extends Activity {

	private EditText grainWeightEntry;
	private TextView grainWeightUnitType;
	private EditText grainTemperatureEntry;
	private TextView grainTemperatureUnitType;
	private EditText strikeWaterVolumeEntry;
	private TextView strikeWaterVolumeUnitType;
	private EditText targetMashTemperatureEntry;
	private TextView targetMashTemperatureUnitType;
	private TextView calculatedStrikeWaterTemperature;

	private Mass.Unit massType;
	private Volume.Unit volumeType;
	private Temperature.Unit temperatureType;

	private Mass grainWeight;
	private Temperature grainTemperature;
	private Volume strikeWaterVolume;
	private Temperature targetMashTemperature;
	private Temperature strikeWaterTemperature;
	
	private double waterToGrainRatio;
	
	private SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_strike_temperature);
	
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        grainWeightEntry = (EditText) findViewById(R.id.grainWeightEntry);
        grainWeightEntry.setOnKeyListener(mKeyListener);
        grainWeightUnitType = (TextView) findViewById(R.id.grainWeightUnitType);

        grainTemperatureEntry = (EditText) findViewById(R.id.grainTemperatureEntry);
        grainTemperatureEntry.setOnKeyListener(mKeyListener);
        grainTemperatureUnitType = (TextView) findViewById(R.id.grainTemperatureUnitType);

        strikeWaterVolumeEntry = (EditText) findViewById(R.id.strikeWaterVolumeEntry);
        strikeWaterVolumeEntry.setOnKeyListener(mKeyListener);
        strikeWaterVolumeUnitType = (TextView) findViewById(R.id.strikeWaterVolumeUnitType);

        targetMashTemperatureEntry = (EditText) findViewById(R.id.targetMashTemperatureEntry);
        targetMashTemperatureEntry.setOnKeyListener(mKeyListener);
        targetMashTemperatureUnitType = (TextView) findViewById(R.id.targetMashTemperatureUnitType);
     
        calculatedStrikeWaterTemperature = (TextView) findViewById(R.id.calculatedStrikeWaterTemperature);

        grainWeight = new Mass(0.0, Mass.Unit.GRAM, getBaseContext(), prefs);
        grainTemperature = new Temperature(0.0, Temperature.Unit.FAHRENHEIT, getBaseContext(), prefs);
        strikeWaterVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
        targetMashTemperature = new Temperature(0.0, Temperature.Unit.FAHRENHEIT, getBaseContext(), prefs);
        strikeWaterTemperature = new Temperature(0.0, Temperature.Unit.FAHRENHEIT, getBaseContext(), prefs);
        
	}

	@Override
	public void onResume() {
		super.onResume();
        getPrefs();
		calculate();
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
	
	public OnKeyListener mKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_UP) calculate();
			return false;
		}
	};

	private void calculate() {
		
		grainWeight.setValue(grainWeightEntry, 0.0);
		grainTemperature.setValue(grainTemperatureEntry, 0.0);
		strikeWaterVolume.setValue(strikeWaterVolumeEntry, 0.0);
		targetMashTemperature.setValue(targetMashTemperatureEntry, 0.0);

        if (waterToGrainRatio > 0 && grainWeight.getValue() >= 0) {
        	strikeWaterVolume.setValue(waterToGrainRatio * grainWeight.getValue());
        	strikeWaterVolumeEntry.setText(strikeWaterVolume.toString());
        }
		
		// http://www.homebrewtalk.com/wiki/index.php/Infusion_mashing
        // http://brewingtechniques.com/library/backissues/issue5.4/palmer_sb.html
		if (grainWeight.getValue() == 0 || grainTemperature.getValue() == 0 || strikeWaterVolume.getValue() == 0 || targetMashTemperature.getValue() == 0) {
	
			calculatedStrikeWaterTemperature.setText("");
		
		} else {

			//grainWeight.convert(Mass.POUND);			
			//strikeWaterVolume.convert(Volume.QUART);
						
			// Strike Water Temperature Tw = (.2/R)(T2 - T1) + T2
			strikeWaterTemperature.setValue((.2 / (strikeWaterVolume.compare(Volume.Unit.QUART) / grainWeight.compare(Mass.Unit.POUND))) * (targetMashTemperature.getValue() - grainTemperature.getValue()) + targetMashTemperature.getValue());

			if (strikeWaterTemperature.getValue() > targetMashTemperature.getValue()) {

				calculatedStrikeWaterTemperature.setText(getString(R.string.calculated_strike_water_temperature_format, strikeWaterTemperature.compare(temperatureType), strikeWaterTemperature.getLabelAbbr()));

			} else {

				calculatedStrikeWaterTemperature.setText("");
			
			}
			
		}
	
	}
	
	private void getPrefs() {

        waterToGrainRatio = NumberFormat.parseDouble(prefs.getString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, "0"), 0);
        if (waterToGrainRatio > 0) strikeWaterVolumeEntry.setEnabled(false);
        else strikeWaterVolumeEntry.setEnabled(true);
        
        temperatureType = grainTemperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.FAHRENHEIT);        

        grainTemperature.setType(temperatureType);
    	grainTemperatureUnitType.setText(grainTemperature.getLabelAbbr());

    	targetMashTemperature.setType(temperatureType);
    	targetMashTemperatureUnitType.setText(targetMashTemperature.getLabelAbbr());

    	strikeWaterTemperature.setType(temperatureType);
        
        volumeType = strikeWaterVolume.typeFromPref(Preferences.BATCH_MASH_VOLUME_UNIT, Volume.Unit.GALLON);

        strikeWaterVolume.setType(volumeType);
    	strikeWaterVolumeUnitType.setText(strikeWaterVolume.getLabelAbbr());

        massType = grainWeight.typeFromPref(Preferences.BATCH_GRAIN_MASS_UNIT, Mass.Unit.POUND);
        
        grainWeight.setType(massType);
    	grainWeightUnitType.setText(grainWeight.getLabelAbbr());
        
	}

}
