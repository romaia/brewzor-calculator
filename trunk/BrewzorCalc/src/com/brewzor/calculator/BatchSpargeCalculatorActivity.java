package com.brewzor.calculator;

import com.brewzor.calculator.preferences.Preferences;
import com.brewzor.calculator.R;
import com.brewzor.converters.Mass;
import com.brewzor.converters.Temperature;
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

// http://hbd.org/hbd/archive/4196.html#4196-1
public class BatchSpargeCalculatorActivity extends Activity {
		
	private SharedPreferences prefs;
	
	private EditText grainWeightEntry;
	private EditText strikeVolumeEntry;	
	private EditText boilVolumeEntry;	
	private TextView calculatedWaterToGrainRatio;
	private TextView calculatedGrainAbsorptionVolume;
	private TextView calculatedFirstSpargeVolume;
	private TextView calculatedSecondSpargeVolume;
	private TextView calculatedTotalMashVolume;
	
	private TextView grainWeightUnitType;
	private TextView strikeVolumeUnitType;
	private TextView boilVolumeUnitType;
	private TextView calculatedWaterToGrainRatioUnitType;
	private TextView calculatedGrainAbsorptionVolumeUnitType;
	private TextView calculatedFirstSpargeVolumeUnitType;
	private TextView calculatedSecondSpargeVolumeUnitType;
	private TextView calculatedTotalMashVolumeUnitType;
	
	private Volume.Unit volumeType;
	private Volume.Unit mashVolumeType;
	private Mass.Unit massType;
	private Temperature.Unit temperatureType;

	private Volume BoilVolume; 
	private Mass GrainWeight;
	private Volume StrikeWaterVolume;
	private Temperature StrikeWaterTemperature;
	private Volume FirstSpargeVolume;
	private Volume SecondSpargeVolume;	
	private double GrainAbsorptionRatio;
	private double GrainVolumeRatio;
	private double WaterToGrainRatio;
	private Volume GrainAbsorptionVolume;
	private Volume TotalMashVolume;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.batch_sparge)));
        setContentView(R.layout.calculator_batch_sparge);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        grainWeightEntry = (EditText) findViewById(R.id.grainWeightEntry);
        grainWeightEntry.setOnKeyListener(mKeyListener);
        grainWeightUnitType = (TextView) findViewById(R.id.grainWeightUnitType);

        strikeVolumeEntry = (EditText) findViewById(R.id.strikeVolumeEntry);
        strikeVolumeEntry.setOnKeyListener(mKeyListener);
        strikeVolumeUnitType = (TextView) findViewById(R.id.strikeVolumeUnitType);

        boilVolumeEntry = (EditText) findViewById(R.id.boilVolumeEntry);
        boilVolumeEntry.setOnKeyListener(mKeyListener);
        boilVolumeUnitType = (TextView) findViewById(R.id.boilVolumeUnitType);

    	calculatedWaterToGrainRatio = (TextView) findViewById(R.id.calculatedWaterToGrainRatio);
    	calculatedGrainAbsorptionVolume = (TextView) findViewById(R.id.calculatedGrainAbsorptionVolume);
    	calculatedFirstSpargeVolume = (TextView) findViewById(R.id.calculatedFirstSpargeVolume);
    	calculatedSecondSpargeVolume = (TextView) findViewById(R.id.calculatedSecondSpargeVolume);
    	calculatedTotalMashVolume = (TextView) findViewById(R.id.calculatedTotalMashVolume);

    	calculatedWaterToGrainRatioUnitType = (TextView) findViewById(R.id.calculatedWaterToGrainRatioUnitType);
    	calculatedGrainAbsorptionVolumeUnitType = (TextView) findViewById(R.id.calculatedGrainAbsorptionVolumeUnitType);
    	calculatedFirstSpargeVolumeUnitType = (TextView) findViewById(R.id.calculatedFirstSpargeVolumeUnitType);
    	calculatedSecondSpargeVolumeUnitType = (TextView) findViewById(R.id.calculatedSecondSpargeVolumeUnitType);
    	calculatedTotalMashVolumeUnitType = (TextView) findViewById(R.id.calculatedTotalMashVolumeUnitType);
    	
        // inputs
        GrainWeight = new Mass(0.0, Mass.Unit.POUND, getBaseContext(), prefs);
        BoilVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
        StrikeWaterVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
	
        // outputs
        GrainAbsorptionVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);

        StrikeWaterTemperature = new Temperature(0.0, Temperature.Unit.FAHRENHEIT, getBaseContext(), prefs);
        FirstSpargeVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
        SecondSpargeVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
        TotalMashVolume = new Volume(0.0, Volume.Unit.GALLON, getBaseContext(), prefs);
	}

	private void calculate() {

		//Log.v("batch", "calculate() start");
		
		GrainWeight.setValue(grainWeightEntry, 0.0);
		BoilVolume.setValue(boilVolumeEntry, 0.0);
		BoilVolume.setType(volumeType);
		BoilVolume.convert(mashVolumeType);
		StrikeWaterVolume.setValue(strikeVolumeEntry, 0.0);
		StrikeWaterVolume.setType(mashVolumeType);
		
		// amount of water absorbed by the grain after the initial strike
		GrainAbsorptionVolume.setValue(GrainAbsorptionRatio * GrainWeight.getValue());
		GrainAbsorptionVolume.setType(mashVolumeType);
		//Log.v("batch", String.format("GrainAbsorbtionVolume=%01.4f", GrainAbsorptionVolume.getValue()));
		
		FirstSpargeVolume.setValue((BoilVolume.compare(mashVolumeType) / 2) - (StrikeWaterVolume.getValue() - GrainAbsorptionVolume.getValue()));
		FirstSpargeVolume.setType(mashVolumeType);
		//Log.v("batch", String.format("FirstSpargeVolume=%01.4f", FirstSpargeVolume.getValue()));
		
		SecondSpargeVolume.setValue(BoilVolume.compare(mashVolumeType) / 2);
		SecondSpargeVolume.setType(mashVolumeType);
		//Log.v("batch", String.format("SecondSpargeVolume=%01.4f", SecondSpargeVolume.getValue()));
		
		TotalMashVolume.setValue((WaterToGrainRatio * GrainWeight.getValue()) + (GrainVolumeRatio * GrainWeight.getValue()));
		TotalMashVolume.setType(mashVolumeType);
		TotalMashVolume.convert(volumeType);
		//Log.v("batch", String.format("TotalMashVolume=%01.4f", TotalMashVolume.getValue()));
		
		//Log.v("batch", String.format("1st+2nd sparge=%01.4f", SecondSpargeVolume.getValue() + FirstSpargeVolume.getValue() + (StrikeWaterVolume.getValue() - GrainAbsorptionVolume.getValue())));
				
		calculatedWaterToGrainRatio.setText(String.format("%01.2f", WaterToGrainRatio));
		calculatedGrainAbsorptionVolume.setText(GrainAbsorptionVolume.toString());
		calculatedFirstSpargeVolume.setText(FirstSpargeVolume.toString());
		calculatedSecondSpargeVolume.setText(SecondSpargeVolume.toString());
		calculatedTotalMashVolume.setText(TotalMashVolume.toString());

	}

	private void getPrefs() {

	    WaterToGrainRatio = NumberFormat.parseDouble(prefs.getString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, "0"), 0);
		
	    mashVolumeType = StrikeWaterVolume.typeFromPref(Preferences.BATCH_MASH_VOLUME_UNIT, Volume.Unit.GALLON);
	    volumeType = BoilVolume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON);
		massType = GrainWeight.typeFromPref(Preferences.BATCH_GRAIN_MASS_UNIT, Mass.Unit.POUND);
		temperatureType = StrikeWaterTemperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.FAHRENHEIT);

		// http://www.franklinbrew.org/brewinfo/nbsparge.html
	    Volume grainVolume = new Volume(0, Volume.Unit.GALLON, getBaseContext(), prefs);
	    Mass grainMass = new Mass(0, Mass.Unit.POUND, getBaseContext(), prefs);

	    grainVolume.setValue(0.13); // 0.13 gal / lb
	    grainMass.setValue(1);	    
	    GrainAbsorptionRatio = grainVolume.compare(mashVolumeType) / grainMass.compare(massType);

	    grainVolume.setValue(0.08); // 0.08 gal / lb
	    grainMass.setValue(1);	    
	    GrainVolumeRatio = grainVolume.compare(mashVolumeType) / grainMass.compare(massType); 

		GrainWeight.setType(massType);
		StrikeWaterVolume.setType(mashVolumeType);
		BoilVolume.setType(volumeType);
		StrikeWaterTemperature.setType(temperatureType);
		GrainAbsorptionVolume.setType(mashVolumeType);
		FirstSpargeVolume.setType(mashVolumeType);
		SecondSpargeVolume.setType(mashVolumeType);
		TotalMashVolume.setType(volumeType);
		
		grainWeightUnitType.setText(GrainWeight.getLabelAbbr());
		strikeVolumeUnitType.setText(StrikeWaterVolume.getLabelAbbr());
		boilVolumeUnitType.setText(BoilVolume.getLabelAbbr());

		calculatedWaterToGrainRatioUnitType.setText(getString(R.string.batch_sparge_water_to_grain_ratio_format, StrikeWaterVolume.getLabelAbbr(), GrainWeight.getLabelAbbr()));
		calculatedGrainAbsorptionVolumeUnitType.setText(GrainAbsorptionVolume.getLabelAbbr());
		calculatedFirstSpargeVolumeUnitType.setText(FirstSpargeVolume.getLabelAbbr());
		calculatedSecondSpargeVolumeUnitType.setText(SecondSpargeVolume.getLabelAbbr());
		calculatedTotalMashVolumeUnitType.setText(TotalMashVolume.getLabelAbbr());
		
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
			if (event.getAction() == KeyEvent.ACTION_UP) {
				calculate();
			}
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
	

}