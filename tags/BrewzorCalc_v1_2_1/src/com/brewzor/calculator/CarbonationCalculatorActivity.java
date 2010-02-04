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
import com.brewzor.converters.Mass;
import com.brewzor.converters.Pressure;
import com.brewzor.converters.Temperature;
import com.brewzor.converters.Volume;
import com.brewzor.utils.NumberFormat;

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

public class CarbonationCalculatorActivity extends Activity {

	private EditText volumesCO2DesiredEntry;
	private TextView calculatedCO2PressureUnitType;
	private EditText beerTemperatureEntry;
	private TextView beerTemperatureUnitType;
	private EditText beerVolumeEntry;
	private TextView beerVolumeUnitType;
	private TextView calculatedCO2Pressure;
	private TextView calculatedTableSugar;
	private TextView calculatedTableSugarUnitType;
	private TextView calculatedCornSugar;
	private TextView calculatedCornSugarUnitType;
	private TextView calculatedDME;
	private TextView calculatedDMEUnitType;
	
	private Volume.Unit volumeType;
	private Mass.Unit massType;
	private Temperature.Unit temperatureType;
	private Pressure.Unit pressureType;
	
	private double volumesCO2;
	private Volume beerVolume;
	private Temperature temperature;
	private Pressure pressure;
	private Mass cornSugar;
	private Mass dme;
	private Mass tableSugar;
	private double flatBeerCarbonation = 0;

	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        
		super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_carbonation);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        volumesCO2DesiredEntry = (EditText) findViewById(R.id.volumesCO2DesiredEntry);
        volumesCO2DesiredEntry.setOnKeyListener(mKeyListener);
        
        beerTemperatureEntry = (EditText) findViewById(R.id.beerTemperatureEntry);
        beerTemperatureEntry.setOnKeyListener(mKeyListener);
        beerTemperatureUnitType = (TextView) findViewById(R.id.beerTemperatureUnitType);        
        
        beerVolumeEntry = (EditText) findViewById(R.id.beerVolumeEntry);
        beerVolumeEntry.setOnKeyListener(mKeyListener);
        beerVolumeUnitType = (TextView) findViewById(R.id.beerVolumeUnitType);        

        calculatedCO2Pressure = (TextView) findViewById(R.id.calculatedCO2Pressure);
        calculatedCO2PressureUnitType = (TextView) findViewById(R.id.calculatedCO2PressureUnitType);
        
        calculatedTableSugar = (TextView) findViewById(R.id.calculatedTableSugar);
        calculatedTableSugarUnitType = (TextView) findViewById(R.id.calculatedTableSugarUnitType);
        
        calculatedCornSugar = (TextView) findViewById(R.id.calculatedCornSugar);
        calculatedCornSugarUnitType = (TextView) findViewById(R.id.calculatedCornSugarUnitType);
        
        calculatedDME = (TextView) findViewById(R.id.calculatedDME);
        calculatedDMEUnitType = (TextView) findViewById(R.id.calculatedDMEUnitType);
	
        beerVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
    	temperature = new Temperature(0.0, Temperature.Unit.FAHRENHEIT, getBaseContext(), prefs);
    	cornSugar = new Mass(0.0, Mass.Unit.GRAM, getBaseContext(), prefs);
    	dme = new Mass(0.0, Mass.Unit.GRAM, getBaseContext(), prefs);
    	tableSugar = new Mass(0.0, Mass.Unit.GRAM, getBaseContext(), prefs);
    	pressure = new Pressure(0.0, Pressure.Unit.PSI, getBaseContext(), prefs);
    	
	}

	private void getPrefs() {

		pressureType = pressure.typeFromPref(Preferences.GLOBAL_PRESSURE_UNIT, Pressure.Unit.PSI);
		pressure.setType(pressureType);
		Log.v("carb", pressure.getLabel());
		calculatedCO2PressureUnitType.setText(pressure.getLabelAbbr());
		
		temperatureType = temperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.FAHRENHEIT);		
        temperature.setType(temperatureType);
        beerTemperatureUnitType.setText(temperature.getLabelAbbr());

		volumeType = beerVolume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON);
        beerVolume.setType(volumeType);        
        beerVolumeUnitType.setText(beerVolume.getLabelAbbr());

		massType = dme.typeFromPref(Preferences.GLOBAL_EXTRACT_MASS_UNIT, Mass.Unit.GRAM);
        cornSugar.setType(massType);
        calculatedCornSugarUnitType.setText(cornSugar.getLabelAbbr());

        dme.setType(massType);
        calculatedDMEUnitType.setText(dme.getLabelAbbr());

        tableSugar.setType(massType);
        calculatedTableSugarUnitType.setText(tableSugar.getLabelAbbr());
        
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
		
		volumesCO2 = NumberFormat.parseDouble(volumesCO2DesiredEntry.getText().toString(), 0.0);
		temperature.setValue(beerTemperatureEntry, 0.0);
		temperature.setType(temperatureType);
		beerVolume.setValue(beerVolumeEntry, 0.0);
		beerVolume.setType(volumeType);
		
		// http://www.byo.com/stories/techniques/article/indices/39-kegging/165-balancing-your-draft-system-advanced-brewing
		// The formula for setting the regulator to the correct 
		// pressure, P (in pounds per square inch, or PSI) for 
		// the desired level of carbonation, V (in volumes of CO2) 
		// at a beer temperature of T (in °F) is:
		//P = -16.6999 - (0.0101059 * T) + (0.00116512 * T2) + (0.173354 * T * V) + (4.24267 * V) - (0.0684226 * V2)
		
		// convert temp to F and volume to liters for the calculation
		double tempF = temperature.compare(Temperature.Unit.FAHRENHEIT);
		double volumeLiters = beerVolume.compare(Volume.Unit.LITER);

		pressure.setValue(-16.6999 - (0.0101059 * tempF) + (0.00116512 * java.lang.Math.pow(tempF, 2)) + (0.173354 * tempF * volumesCO2) + (4.24267 * volumesCO2) - (0.0684226 * java.lang.Math.pow(volumesCO2, 2)));
		if (pressure.getValue() < 0) pressure.setValue(0);
		pressure.setType(Pressure.Unit.PSI);
		calculatedCO2Pressure.setText(getString(R.string.double_format, pressure.compare(pressureType)));

		// http://www.myhomebrew.com/bp0.html
		flatBeerCarbonation = 3.0378 - 5.0062e-2 * tempF + 2.6555e-4 * java.lang.Math.pow(tempF, 2);
						
		//http://www.myhomebrew.com/bp0.html
		//PrimingRate(GetSelectValue(f.pi)) * v * gco2

		//if (u == "glucose") return 4.02;
		cornSugar.setValue(4.02 * volumeLiters * (volumesCO2 - flatBeerCarbonation)); 
				
		//if (u == "dme") return 510 / (55 OR 70 OR 75);
		dme.setValue((510.0 / 75.0) * volumeLiters * (volumesCO2 - flatBeerCarbonation)); 

		//if (u == "sucrose") return 3.82;
		tableSugar.setValue(3.82 * volumeLiters * (volumesCO2 - flatBeerCarbonation));
		
		//Log.v("Carb", String.format("fbc=%02.2f vol=%02.2f volco2=%02.2f cs=%02.2f dme=%02.2f", flatBeerCarbonation, beerVolume, volumesCO2, cornSugar, dme));

		tableSugar.setType(Mass.Unit.GRAM);
		cornSugar.setType(Mass.Unit.GRAM);
		dme.setType(Mass.Unit.GRAM);
		
		// weights are in grams.  convert to desired unit
		tableSugar.convert(massType);
		cornSugar.convert(massType);
		dme.convert(massType);

		calculatedCornSugar.setText(cornSugar.toString());
		calculatedTableSugar.setText(tableSugar.toString());
		calculatedDME.setText(dme.toString());
	
	}

}
