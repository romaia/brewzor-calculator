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
import com.brewzor.utils.NumberFormat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class RefractometerCalculatorActivity extends Activity {

	private SharedPreferences prefs;
	
	private enum Mode {
		UNFERMENTED,
		FERMENTING,
		FINISHED,
		CALIBRATE
	}
	
	private Mode mode = Mode.UNFERMENTED;
	
	private TableRow row;
	private Spinner modeSpinner;
	private EditText currentEntry;
	private EditText originalEntry;
	private EditText hydrometerEntry;
	private TextView correctionFactorValue;
	private TextView calculatedValue;
	private TextView calculatedValueDescription;
	private Button saveToPrefs;
		
	private Gravity gravity;
	private Gravity gravitySG;
	private Gravity.Unit gravityType;
	
	private double correctionFactor = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.refractometer)));
        setContentView(R.layout.calculator_refractometer);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        currentEntry = (EditText) findViewById(R.id.currentEntry);
        currentEntry.setOnKeyListener(mKeyListener);

        originalEntry = (EditText) findViewById(R.id.originalEntry);
        originalEntry.setOnKeyListener(mKeyListener);

        hydrometerEntry = (EditText) findViewById(R.id.hydrometerEntry);
        hydrometerEntry.setOnKeyListener(mKeyListener);

        correctionFactorValue = (TextView) findViewById(R.id.correctionFactorValue);
    	calculatedValue = (TextView) findViewById(R.id.calculatedValue);
    	calculatedValueDescription = (TextView) findViewById(R.id.calculatedValueDescription);

        gravity = new Gravity(0, Gravity.Unit.BRIX, getBaseContext(), prefs);
        gravitySG = new Gravity(0, Gravity.Unit.SG, getBaseContext(), prefs);
                
        saveToPrefs = (Button) findViewById(R.id.saveToPrefs);
        saveToPrefs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Editor editor = prefs.edit();
				editor.putString(Preferences.GLOBAL_REFRACTOMETER_CORRECTION_FACTOR, correctionFactorValue.getText().toString());
				editor.commit();
				getPrefs();
			}
        });

        modeSpinner = (Spinner) findViewById(R.id.modeSpinner);
        modeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        	@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		    	
				String selected = getResources().getStringArray(R.array.refractometerModeIdList)[modeSpinner.getSelectedItemPosition()];
		    	
				if (Mode.UNFERMENTED.toString().equals(selected)) {
					mode = Mode.UNFERMENTED;
				} else if (Mode.FERMENTING.toString().equals(selected)) {
					mode = Mode.FERMENTING;
				} else if (Mode.FINISHED.toString().equals(selected)) {
					mode = Mode.FINISHED;
				} else if (Mode.CALIBRATE.toString().equals(selected)) {
					mode = Mode.CALIBRATE;
				} else {
					mode = Mode.UNFERMENTED;
				}				
				configureLayout();
				calculate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
        });

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
		switch (mode) {
			case UNFERMENTED:
				gravity.setValue(currentEntry, 0); 
				gravity.setType(Gravity.Unit.BRIX);
				gravity.convert(gravityType);
				gravity.setFormat(getString(R.string.sg_format));
				calculatedValue.setText(gravity.toString());
				break;
			case FERMENTING:

				gravity.setValue(currentEntry, 0);
				gravity.setType(Gravity.Unit.PLATO);
				gravitySG.setValue(originalEntry, 1);
				gravitySG.setType(Gravity.Unit.SG);
				gravitySG.convert(Gravity.Unit.PLATO);
				double est_cg_plato = 1.001843 - 0.002318474*gravitySG.getValue() - 0.000007775*java.lang.Math.pow(gravitySG.getValue(), 2) - 0.000000034*java.lang.Math.pow(gravitySG.getValue(), 3) 
				                      + 0.00574*gravity.getValue() + 0.00003344*java.lang.Math.pow(gravity.getValue(), 2) + 0.000000086*java.lang.Math.pow(gravitySG.getValue(), 3);
				// = 1.001843 - 0.002318474*C4 - 0.000007775*C4^2 - 0.000000034*C4^3 + 0.00574*B4 + 0.00003344*B4^2 + 0.000000086*C4^3
				Gravity estCG = new Gravity(est_cg_plato, Gravity.Unit.SG, getBaseContext(), prefs);
				estCG.setFormat(getString(R.string.sg_format));
				
				if (estCG.getValue() > 0) calculatedValue.setText(estCG.toString());
				else calculatedValue.setText(getString(R.string.sg_default));

				break;
			case FINISHED:
				
				gravity.setValue(currentEntry, 0);
				gravity.setType(Gravity.Unit.PLATO);
				gravitySG.setValue(hydrometerEntry, 1);
				gravitySG.setType(Gravity.Unit.SG);
				
				double est_og_plato = (100*((194.5935 + (129.8*gravitySG.getValue()) + ((1.33302 + (0.001427193*gravity.getValue()) +
						(0.000005791157*(java.lang.Math.pow(gravity.getValue(), 2))))*((410.8815*(1.33302 + (0.001427193*gravity.getValue()) +
								(0.000005791157*(java.lang.Math.pow(gravity.getValue(), 2))))) - 790.8732))) + (2.0665*(1017.5596 - (277.4*gravitySG.getValue()) +
								((1.33302 + (0.001427193*gravity.getValue()) + (0.000005791157*(java.lang.Math.pow(gravity.getValue(), 2))))*((937.8135*(1.33302 +
								(0.001427193*gravity.getValue()) + (0.000005791157*(java.lang.Math.pow(gravity.getValue(), 2))))) - 1805.1228)))))) / (100 +
								(1.0665*(1017.5596 - (277.4*gravitySG.getValue()) + ((1.33302 + (0.001427193*gravity.getValue()) +
								(0.000005791157*(gravity.getValue())))*((937.8135*(1.33302 + (0.001427193*gravity.getValue()) +
								(0.000005791157*(gravity.getValue())))) - 1805.1228)))));
				//Log.v("unit", getString(R.string.double_format, est_og_plato));		
				Gravity estOG = new Gravity(est_og_plato, Gravity.Unit.PLATO, getBaseContext(), prefs);
				estOG.convert(Gravity.Unit.SG);
				estOG.setFormat(getString(R.string.sg_format));
				calculatedValue.setText(estOG.toString());
				
				if (estOG.getValue() > 0) calculatedValue.setText(estOG.toString());
				else calculatedValue.setText(getString(R.string.sg_default));

				break;
			case CALIBRATE:
				gravity.setValue(currentEntry, 0.0); 
				gravity.setType(Gravity.Unit.PLATO);
				
				gravitySG.setValue(hydrometerEntry, 1.0);
				gravitySG.setType(Gravity.Unit.SG);
				gravitySG.convert(Gravity.Unit.PLATO);
				
				correctionFactorValue.setText(getString(R.string.refractometer_correction_factor_format, gravity.getValue() / gravitySG.getValue()));
				break;
		}
	
	}
	
	private void getPrefs() {
		gravityType = gravity.typeFromPref(Preferences.GLOBAL_GRAVITY_UNIT, Gravity.Unit.SG);
		
		correctionFactor = NumberFormat.parseDouble(prefs.getString(Preferences.GLOBAL_REFRACTOMETER_CORRECTION_FACTOR, "1"), 1.0);        
        correctionFactorValue.setText(getString(R.string.refractometer_correction_factor_format, correctionFactor));
        Gravity.setBrixCorrectionFactor(correctionFactor);
	}

	private void configureLayout() {
		switch (mode) {
			case UNFERMENTED:
				row = (TableRow) findViewById(R.id.hydrometerReadingEntryRow);
				row.setVisibility(View.GONE);
				
				row = (TableRow) findViewById(R.id.originalReadingEntryRow);
				row.setVisibility(View.GONE);

				calculatedValueDescription.setText(getString(R.string.refractometer_corrected_gravity));
				calculatedValue.setText(getString(R.string.double_default));
				
				saveToPrefs.setVisibility(View.GONE);
				
				calculatedValue.setVisibility(View.VISIBLE);
				calculatedValueDescription.setVisibility(View.VISIBLE);

				break;
			case FERMENTING:
				row = (TableRow) findViewById(R.id.hydrometerReadingEntryRow);
				row.setVisibility(View.GONE);
				
				row = (TableRow) findViewById(R.id.originalReadingEntryRow);
				row.setVisibility(View.VISIBLE);
				
				saveToPrefs.setVisibility(View.GONE);

				calculatedValue.setVisibility(View.VISIBLE);
				calculatedValueDescription.setVisibility(View.VISIBLE);
				calculatedValueDescription.setText(getString(R.string.refractometer_corrected_gravity));


				break;
			case FINISHED:
				row = (TableRow) findViewById(R.id.hydrometerReadingEntryRow);
				row.setVisibility(View.VISIBLE);
				
				row = (TableRow) findViewById(R.id.originalReadingEntryRow);
				row.setVisibility(View.GONE);
				
				saveToPrefs.setVisibility(View.GONE);

				calculatedValue.setVisibility(View.VISIBLE);
				calculatedValueDescription.setVisibility(View.VISIBLE);
				calculatedValueDescription.setText(getString(R.string.refractometer_original_gravity));
				break;
			case CALIBRATE:
				row = (TableRow) findViewById(R.id.hydrometerReadingEntryRow);
				row.setVisibility(View.VISIBLE);
				
				row = (TableRow) findViewById(R.id.originalReadingEntryRow);
				row.setVisibility(View.GONE);

				calculatedValue.setVisibility(View.GONE);
				calculatedValueDescription.setVisibility(View.GONE);
				
				saveToPrefs.setVisibility(View.VISIBLE);

				break;
		}
		
	}
	
}
