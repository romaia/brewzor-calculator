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

public class BoilOffCalculatorActivity extends Activity {

	private EditText startingVolumeEntry;
	private TextView startingVolumeUnitType;
	private EditText evaporationRateEntry;
	private EditText boilTimeEntry;
	private EditText coolingLossPercentageEntry;
	private TextView calculatedVolumeBoiledOff;
	private TextView calculatedVolumeBoiledOffUnitType;
	private TextView calculatedCoolingLossVolume;
	private TextView calculatedCoolingLossVolumeUnitType;
	private TextView calculatedFinalVolume;
	private TextView calculatedFinalVolumeUnitType;
	
	private Volume.Unit volumeType;
	
	private Volume startingVolume;
	private double evaporationRate = 0;
	private double boilTime = 0;
	private double coolingLossPercent = 0;
	private Volume boilOffVolume;
	private Volume coolingLossVolume;
	private Volume finalVolume;
	
	private SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_boil_off);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        startingVolumeEntry = (EditText) findViewById(R.id.startingVolumeEntry);
        startingVolumeEntry.setOnKeyListener(mKeyListener);
        startingVolumeUnitType = (TextView) findViewById(R.id.startingVolumeUnitType);
        
        evaporationRateEntry = (EditText) findViewById(R.id.evaporationRateEntry);
        evaporationRateEntry.setOnKeyListener(mKeyListener);
        
        boilTimeEntry = (EditText) findViewById(R.id.boilTimeEntry);
        boilTimeEntry.setOnKeyListener(mKeyListener);
                
        coolingLossPercentageEntry = (EditText) findViewById(R.id.coolingLossPercentageEntry);
        coolingLossPercentageEntry.setOnKeyListener(mKeyListener);
        
        calculatedVolumeBoiledOff = (TextView) findViewById(R.id.calculatedVolumeBoiledOff);        
        calculatedVolumeBoiledOffUnitType = (TextView) findViewById(R.id.calculatedVolumeBoiledOffUnitType);        
        
        calculatedCoolingLossVolume = (TextView) findViewById(R.id.calculatedCoolingLossVolume);
        calculatedCoolingLossVolumeUnitType = (TextView) findViewById(R.id.calculatedCoolingLossVolumeUnitType);
        
        calculatedFinalVolume = (TextView) findViewById(R.id.calculatedFinalVolume);
        calculatedFinalVolumeUnitType = (TextView) findViewById(R.id.calculatedFinalVolumeUnitType);

        startingVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
        boilOffVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
        coolingLossVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
        finalVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
	}

	private void calculate() {	
		startingVolume.setValue(startingVolumeEntry, 0.0);
		evaporationRate = NumberFormat.parseDouble(evaporationRateEntry.getText().toString(), 10);
		boilTime = NumberFormat.parseDouble(boilTimeEntry.getText().toString(), 60);
		coolingLossPercent = NumberFormat.parseDouble(coolingLossPercentageEntry.getText().toString(), 4);

		boilOffVolume.setValue(startingVolume.getValue() * (boilTime / 60) * (evaporationRate / 100));
		coolingLossVolume.setValue((startingVolume.getValue() - boilOffVolume.getValue()) * (coolingLossPercent / 100));
		finalVolume.setValue(startingVolume.getValue() - boilOffVolume.getValue() - coolingLossVolume.getValue());

		calculatedVolumeBoiledOff.setText(boilOffVolume.toString());
		calculatedCoolingLossVolume.setText(coolingLossVolume.toString());
		calculatedFinalVolume.setText(finalVolume.toString());
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
	
	
	private void getPrefs() {
		
		volumeType = startingVolume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON);
		
        startingVolume.setType(volumeType);
    	startingVolumeUnitType.setText(startingVolume.getLabelAbbr());

    	boilOffVolume.setType(volumeType);
    	calculatedVolumeBoiledOffUnitType.setText(boilOffVolume.getLabelAbbr());

    	coolingLossVolume.setType(volumeType);
    	calculatedCoolingLossVolumeUnitType.setText(coolingLossVolume.getLabelAbbr());

    	finalVolume.setType(volumeType);
    	calculatedFinalVolumeUnitType.setText(finalVolume.getLabelAbbr());

    	evaporationRateEntry.setText(prefs.getString(Preferences.KETTLE_EVAPORATION_RATE, "10"));
        coolingLossPercentageEntry.setText(prefs.getString(Preferences.KETTLE_COOLING_LOSS, "4"));
        boilTimeEntry.setText(prefs.getString(Preferences.BATCH_BOIL_MINUTES, "60"));

	}	
}