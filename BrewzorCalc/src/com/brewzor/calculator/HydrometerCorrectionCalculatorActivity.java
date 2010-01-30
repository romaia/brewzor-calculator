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
import com.brewzor.converters.Gravity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

public class HydrometerCorrectionCalculatorActivity extends Activity {
	
	private EditText SGEntry;
	private EditText temperatureEntry;
	private TextView calculatedCorrectedSG;
	private TextView temperatureUnitType;
	private TextView calibrationText;
	
	private Temperature temperature;
	private Temperature calibrationTemperature;
	private Gravity startGravity;
	private Gravity correctedGravity;
	
	private SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_hydrometer_correction);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        SGEntry = (EditText) findViewById(R.id.SGEntry);
        SGEntry.setOnKeyListener(mKeyListener);
        
        temperatureEntry = (EditText) findViewById(R.id.temperatureEntry);
        temperatureEntry.setOnKeyListener(mKeyListener);

        temperatureUnitType = (TextView) findViewById(R.id.temperatureUnitType);
        
        calculatedCorrectedSG = (TextView) findViewById(R.id.calculatedCorrectedSG);

        calibrationText = (TextView) findViewById(R.id.calibrationText);
        
		SGEntry.setText(getString(R.string.sg_default));
		temperatureEntry.setText(getString(R.string.hydrometer_correction_temperature_default));

		temperature = new Temperature(0.0, Temperature.Unit.FAHRENHEIT, getBaseContext(), prefs);
		calibrationTemperature = new Temperature(0.0, Temperature.Unit.FAHRENHEIT, getBaseContext(), prefs);
		startGravity = new Gravity(1.000, Gravity.Unit.SG, getBaseContext(), prefs);
		startGravity.setFormat(getString(R.string.sg_format));
		correctedGravity = new Gravity(1.000, Gravity.Unit.SG, getBaseContext(), prefs);
		correctedGravity.setFormat(getString(R.string.sg_format));
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

		startGravity.setValue(SGEntry, 1.000);
		temperature.setValue(temperatureEntry, 0.0);
		
		double measuredTemp = temperature.compare(Temperature.Unit.FAHRENHEIT);
		double calibrateTemp = calibrationTemperature.compare(Temperature.Unit.FAHRENHEIT);

		
		// http://hbd.org/brewery/library/HydromCorr0992.html
		// Correction(@59F) = 1.313454 - 0.132674*T + 2.057793e-3*T**2 - 2.627634e-6*T**3
		// where T is in degrees F.
		//correctedGravity.setValue(((1.313454 - (0.132674 * temperature.getValue()) + (0.002057793 * java.lang.Math.pow(temperature.getValue(), 2)) - (0.000002627634 * java.lang.Math.pow(temperature.getValue(), 3))) / 1000.0) + startGravity.getValue());
	
		correctedGravity.setValue(startGravity.getValue() * ( (1.00130346 - 0.000134722124 * measuredTemp + 0.00000204052596 * java.lang.Math.pow(measuredTemp, 2) - 0.00000000232820948 * java.lang.Math.pow(measuredTemp, 3)) / (1.00130346 - 0.000134722124 * calibrateTemp + 0.00000204052596 * java.lang.Math.pow(calibrateTemp, 2) - 0.00000000232820948 * java.lang.Math.pow(calibrateTemp, 3))));
		Log.v("HYDRO", "corr=" + correctedGravity.getValue());

		if (startGravity.getValue() <= 1.0) {
			calculatedCorrectedSG.setText(getString(R.string.sg_default));
		} else {
			calculatedCorrectedSG.setText(correctedGravity.toString());
		}
	}

	private void getPrefs() {
		
        temperature.setType(temperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.FAHRENHEIT));
    	temperatureUnitType.setText(temperature.getLabelAbbr());
   
        calibrationTemperature.setType(temperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.FAHRENHEIT));
        calibrationTemperature.setValue(prefs.getString(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE, "60"), 60.0);

        calibrationText.setText(getString(R.string.calibration_text_format, calibrationTemperature.getValue(), calibrationTemperature.getLabelAbbr()));
        
	}
	
}
