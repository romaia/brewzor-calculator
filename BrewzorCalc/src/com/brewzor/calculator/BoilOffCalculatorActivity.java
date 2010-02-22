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
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BoilOffCalculatorActivity extends Activity {

	private EditText volumeEntry;
	private TextView volumeUnitType;
	private EditText evaporationRateEntry;
	private EditText boilTimeEntry;
	private EditText coolingLossPercentageEntry;
	private TextView calculatedVolumeBoiledOff;
	private TextView calculatedVolumeBoiledOffUnitType;
	private TextView calculatedCoolingLossVolume;
	private TextView calculatedCoolingLossVolumeUnitType;
	private TextView calculatedFinalVolume;
	private TextView calculatedFinalVolumeUnitType;
	private TextView resultDescription;
	private Button startEndToggleButton;
	private boolean startEndStatus = true;
	
	private Volume.Unit volumeType;
	
	private Volume volume;
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
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.boil_off)));
        setContentView(R.layout.calculator_boil_off);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        volumeEntry = (EditText) findViewById(R.id.startingVolumeEntry);
        volumeEntry.setOnKeyListener(mKeyListener);
        volumeUnitType = (TextView) findViewById(R.id.startingVolumeUnitType);
        
        startEndToggleButton = (Button) findViewById(R.id.startEndToggle);
        startEndToggleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				toggleButton();
			}
        });

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

        resultDescription = (TextView) findViewById(R.id.resultDescription); 
        
        volume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
        boilOffVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
        coolingLossVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
        finalVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
	
	}

	private void toggleButton() {
		startEndStatus = (startEndStatus) ? false : true;		
		setButtonText();
		calculate();

	}
	
	private void setButtonText() {
		if (startEndStatus) {
			resultDescription.setText(getString(R.string.ending_volume));
			startEndToggleButton.setText(getString(R.string.starting_volume));
		} else {
			resultDescription.setText(getString(R.string.starting_volume));
			startEndToggleButton.setText(getString(R.string.ending_volume));
		}
	}
	
	private void calculate() {	
		evaporationRate = NumberFormat.parseDouble(evaporationRateEntry.getText().toString(), 10);
		boilTime = NumberFormat.parseDouble(boilTimeEntry.getText().toString(), 60);
		coolingLossPercent = NumberFormat.parseDouble(coolingLossPercentageEntry.getText().toString(), 4);

		volume.setValue(volumeEntry, 0.0);
		if (startEndStatus) {
			// given the start volume and calculating the ending volume 
			boilOffVolume.setValue(volume.getValue() * (boilTime / 60) * (evaporationRate / 100));
			coolingLossVolume.setValue((volume.getValue() - boilOffVolume.getValue()) * (coolingLossPercent / 100));
			finalVolume.setValue(volume.getValue() - boilOffVolume.getValue() - coolingLossVolume.getValue());

			calculatedVolumeBoiledOff.setText(boilOffVolume.toString());
			calculatedCoolingLossVolume.setText(coolingLossVolume.toString());
			calculatedFinalVolume.setText(finalVolume.toString());
		} else {
			// given the end volume and calculating the starting volume 
			coolingLossVolume.setValue((volume.getValue() / ((100 - coolingLossPercent) / 100)) - volume.getValue());
			finalVolume.setValue(((volume.getValue() + coolingLossVolume.getValue()) / (1 - ((boilTime / 60) * (evaporationRate / 100)))));
			boilOffVolume.setValue(finalVolume.getValue() - volume.getValue() - coolingLossVolume.getValue());

			calculatedVolumeBoiledOff.setText(boilOffVolume.toString());
			calculatedCoolingLossVolume.setText(coolingLossVolume.toString());
			calculatedFinalVolume.setText(finalVolume.toString());
		}
	}
		
	@Override
	public void onResume() {
		super.onResume();
		getPrefs();
        setButtonText();
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
		
		volumeType = volume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON);
		
        volume.setType(volumeType);
    	volumeUnitType.setText(volume.getLabelAbbr());

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