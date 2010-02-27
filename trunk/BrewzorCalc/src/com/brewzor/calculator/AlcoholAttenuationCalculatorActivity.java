package com.brewzor.calculator;

import com.brewzor.calculator.R;
import com.brewzor.converters.Gravity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AlcoholAttenuationCalculatorActivity extends Activity {

	private SharedPreferences prefs;

	private Spinner unitTypeSpinner;
	
	private Gravity originalGravity;
	private Gravity currentGravity;
	private Gravity.Unit gravityUnit;
	
	private EditText originalEntry;
	private TextView originalUnitType;
	private EditText currentEntry;
	private TextView currentUnitType;
	
	private TextView abv;
	private TextView abw;
	private TextView aa;
	private TextView ra;
	//private TextView calories;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.alcohol_attenuation)));
        setContentView(R.layout.calculator_alcohol_attenuation);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        gravityUnit = Gravity.Unit.SG;
        
        originalGravity = new Gravity(0, Gravity.Unit.SG, getBaseContext(), prefs);
        currentGravity = new Gravity(0, Gravity.Unit.SG, getBaseContext(), prefs);
        
        originalEntry = (EditText) findViewById(R.id.originalEntry);
        originalEntry.setOnKeyListener(mKeyListener);
        originalUnitType = (TextView) findViewById(R.id.originalUnitType);
        currentEntry = (EditText) findViewById(R.id.currentEntry);
        currentEntry.setOnKeyListener(mKeyListener);
        currentUnitType = (TextView) findViewById(R.id.currentUnitType);
        
        abv = (TextView) findViewById(R.id.abv);
        abw = (TextView) findViewById(R.id.abw);
        aa = (TextView) findViewById(R.id.aa);
        ra = (TextView) findViewById(R.id.ra);
        //calories = (TextView) findViewById(R.id.calories);
        
        unitTypeSpinner = (Spinner) findViewById(R.id.unitTypeSpinner);
        unitTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        	@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		    	
				String selected = getResources().getStringArray(R.array.gravityUnitsIdList)[unitTypeSpinner.getSelectedItemPosition()];
		    	
				if (Gravity.Unit.SG.toString().equals(selected)) {
					gravityUnit = Gravity.Unit.SG;
				} else if (Gravity.Unit.GU.toString().equals(selected)) {
					gravityUnit = Gravity.Unit.GU;
				} else if (Gravity.Unit.PLATO.toString().equals(selected)) {
					gravityUnit = Gravity.Unit.PLATO;
				} else if (Gravity.Unit.BRIX.toString().equals(selected)) {
					gravityUnit = Gravity.Unit.BRIX;
				}				

				originalGravity.setType(gravityUnit);
				originalUnitType.setText(originalGravity.getLabelAbbr());
				currentGravity.setType(gravityUnit);
				currentUnitType.setText(currentGravity.getLabelAbbr());
				
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

	// http://www.pointbrewsupply.com/pdf/BeerAlcoholCalories.pdf
	private void calculate() {
		//Log.v("calc", "calculate() start");
		originalGravity.setValue(originalEntry, 0);
		originalGravity.setType(gravityUnit);
		currentGravity.setValue(currentEntry, 0);
		currentGravity.setType(gravityUnit);

		if (gravityUnit == Gravity.Unit.BRIX) {
			double est_cg_plato = 1.001843 - 0.002318474*originalGravity.getValue() - 0.000007775*java.lang.Math.pow(originalGravity.getValue(), 2) - 0.000000034*java.lang.Math.pow(originalGravity.getValue(), 3) 
            + 0.00574*currentGravity.getValue() + 0.00003344*java.lang.Math.pow(currentGravity.getValue(), 2) + 0.000000086*java.lang.Math.pow(originalGravity.getValue(), 3);
			
			currentGravity.setPrecision(3);
			currentGravity.setValue(est_cg_plato);
			currentGravity.setType(Gravity.Unit.SG);
		}
		
		originalGravity.convert(Gravity.Unit.PLATO);
		currentGravity.convert(Gravity.Unit.PLATO);
		
		// RE = 0.1808*튡(initial) + 0.8192*튡(final)
		double re_raw = 0.1808 * originalGravity.getValue() + 0.8192 * currentGravity.getValue();
		
		// AA = 1 - [튡(final) / 튡(initial)]
		double aa_raw = 1 - (currentGravity.getValue() / originalGravity.getValue());
		if (aa_raw > 1 || aa_raw < 0) aa_raw = 0;
		
		// RA = 1 - [RE / 튡(initial)]
		double ra_raw = 1 - (re_raw / originalGravity.getValue());
		if (ra_raw > 1 || ra_raw < 0) ra_raw = 0;
		
		// ABV = (OG - FG) / 0.75
		double abv_raw = (originalGravity.compare(Gravity.Unit.SG) - currentGravity.compare(Gravity.Unit.SG)) / 0.75;
		if (abv_raw > 1 || abv_raw < 0) abv_raw = 0;

		// ABW = (0.79*ABV) / FG
		double abw_raw = (0.79 * abv_raw) / currentGravity.compare(Gravity.Unit.SG);
		if (abw_raw > 1 || abw_raw < 0) abw_raw = 0;
	
		// cal/12 oz beer = [6.9*ABW + 4.0*(RE - 0.1)]*FG*3.55
		//double calories_raw = (6.9 * abw_raw + 4.0 * (re_raw - 0.1)) * currentGravity.getValue() * 3.55;
	    
		abv.setText(getString(R.string.alcohol_attenuation_result_format, abv_raw * 100.0));
		abw.setText(getString(R.string.alcohol_attenuation_result_format, abw_raw * 100.0));
		aa.setText(getString(R.string.alcohol_attenuation_result_format, aa_raw * 100.0));
		ra.setText(getString(R.string.alcohol_attenuation_result_format, ra_raw * 100.0));
		//calories.setText(getString(R.string.double_format, calories_raw));
		
	}
	
	private void getPrefs() {
		
	}
}
