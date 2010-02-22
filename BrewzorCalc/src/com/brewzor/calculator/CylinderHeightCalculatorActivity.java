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
import com.brewzor.converters.Distance;
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

public class CylinderHeightCalculatorActivity extends Activity {
	
	private EditText diameterEntry;
	private TextView diameterUnitType;
	private EditText volumeEntry;
	private TextView volumeUnitType;
	private EditText falseBottomHeightEntry;
	private TextView falseBottomHeightUnitType;
	private TextView calculatedHeight;
	private TextView calculatedHeightUnitType;
	
	private TextView correctForExpansion;
	private boolean correctForExpansionPref;
	private double expansionValue;
		
	private SharedPreferences prefs;
	
	private Distance.Unit distanceType;
	private Volume.Unit volumeType;
	
	private Distance diameter;
	private Distance height;
	private Distance falseBottomHeight;
	private Volume volume;
	private Button heatCorrectionButton;
		
	private enum Expansion {
		NONE, 
		HEAT,
		COOL
	}

	private Expansion expansion;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.cylinder_height)));
        setContentView(R.layout.calculator_cylinder_height);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
  
        heatCorrectionButton = (Button) findViewById(R.id.correctForExpansion);        
        heatCorrectionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				toggleButton();
			}
        });
        
        calculatedHeight = (TextView) findViewById(R.id.calculatedCylinderHeight); 
        calculatedHeightUnitType = (TextView) findViewById(R.id.calculatedCylinderHeightUnitType);

        diameterEntry = (EditText) findViewById(R.id.diameterEntry);
        diameterEntry.setOnKeyListener(mKeyListener);
        diameterUnitType = (TextView) findViewById(R.id.diameterUnitType);

        volumeEntry = (EditText) findViewById(R.id.volumeEntry);
        volumeEntry.setOnKeyListener(mKeyListener);
        volumeUnitType = (TextView) findViewById(R.id.volumeUnitType);
        
        falseBottomHeightEntry = (EditText) findViewById(R.id.falseBottomHeightEntry);
        falseBottomHeightEntry.setOnKeyListener(mKeyListener);
        falseBottomHeightUnitType = (TextView) findViewById(R.id.falseBottomHeightUnitType);
    
        correctForExpansion = (TextView) findViewById(R.id.correctForExpansion);

        diameter = new Distance(0.0, Distance.Unit.INCH, getBaseContext(), prefs);
        height = new Distance(0.0, Distance.Unit.INCH, getBaseContext(), prefs);
        falseBottomHeight = new Distance(0.0, Distance.Unit.INCH, getBaseContext(), prefs);
        volume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);

    	expansion = Expansion.NONE;
    	
	}

	private void toggleButton() {
		switch (expansion) {
			case HEAT:
				expansion = Expansion.COOL;
				break;
			case COOL:
				expansion = Expansion.NONE;
				break;
			case NONE:
			default:
				expansion = Expansion.HEAT;					
		}
		setButtonText();
		calculate();
	}		

	private void setButtonText() {
		switch (expansion) {
			case HEAT:
				heatCorrectionButton.setText(getString(R.string.correct_for_expansion_format, expansionValue));
				break;
			case COOL:
				heatCorrectionButton.setText(getString(R.string.correct_for_loss_format, expansionValue));
				break;
			case NONE:
			default:
				heatCorrectionButton.setText(getString(R.string.no_expansion));
		}
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
		
		diameter.setValue(diameterEntry, 0.0);
		volume.setValue(volumeEntry, 0.0);
		falseBottomHeight.setValue(falseBottomHeightEntry, 0.0);
		
		//volume / (pi * (diameter / 2)^2) - fb = height 

		if (correctForExpansionPref) {
		
			correctForExpansion.setVisibility(View.VISIBLE);
			switch (expansion) {
				case HEAT:
					volume.setValue(volume.getValue() * (1.0 + (expansionValue / 100.0)));
					break;
				case COOL:
					volume.setValue(volume.getValue() * (1.0 - (expansionValue / 100.0)));
					break;
				case NONE:
				default:
			}

		} else {
			correctForExpansion.setText("");
			correctForExpansion.setVisibility(View.GONE);
		}

		switch (distanceType) {
			case CENTIMETER:
				height.setValue((volume.compare(Volume.Unit.MILLILITER) / (java.lang.Math.PI * java.lang.Math.pow((diameter.getValue() / 2), 2))) - falseBottomHeight.getValue());
				break;
			case INCH:
				height.setValue((volume.compare(Volume.Unit.CUBIC_INCH) / (java.lang.Math.PI * java.lang.Math.pow((diameter.getValue() / 2), 2))) - falseBottomHeight.getValue());
				break;
		}
			
		if (height.getValue() <= 0 || volume.getValue() <= 0 || diameter.getValue() <= 0) height.setValue(0.0);
		calculatedHeight.setText(height.toString());
		
	}
	
	private void getPrefs() {
    
        expansionValue = NumberFormat.parseDouble(prefs.getString(Preferences.KETTLE_COOLING_LOSS, "4.00"), 4.0);
        correctForExpansionPref = prefs.getBoolean(Preferences.KETTLE_CORRECT_FOR_EXPANSION, false);
		
        if (correctForExpansionPref) {
        	heatCorrectionButton.setVisibility(View.VISIBLE);
        } else {
        	heatCorrectionButton.setVisibility(View.GONE);
        }
        
		volumeType = volume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON);
        volume.setType(volumeType);
    	volumeUnitType.setText(volume.getLabelAbbr());
		
		distanceType = diameter.typeFromPref(Preferences.KETTLE_DISTANCE_UNIT, Distance.Unit.INCH);
		height.setType(distanceType);
    	calculatedHeightUnitType.setText(height.getLabelPlural());
		
		diameterEntry.setText(prefs.getString(Preferences.KETTLE_DIAMETER, "0.00"));
        diameter.setType(distanceType);
    	diameterUnitType.setText(diameter.getLabelAbbr());
        
        falseBottomHeightEntry.setText(prefs.getString(Preferences.KETTLE_FALSE_BOTTOM_HEIGHT, "0.00"));		
        falseBottomHeight.setType(distanceType);
    	falseBottomHeightUnitType.setText(falseBottomHeight.getLabelAbbr());

		setButtonText();
    	
	}

}
