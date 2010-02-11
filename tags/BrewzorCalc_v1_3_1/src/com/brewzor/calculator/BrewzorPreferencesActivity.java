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
import com.brewzor.converters.Distance;
import com.brewzor.converters.Gravity;
import com.brewzor.converters.Mass;
import com.brewzor.converters.Pressure;
import com.brewzor.converters.Temperature;
import com.brewzor.converters.Volume;
import com.brewzor.utils.NumberFormat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;

public class BrewzorPreferencesActivity extends PreferenceActivity {

	private Preference pref;
	private SharedPreferences sPref;
	private SharedPreferences.Editor editor;
	
	private Volume volume;
	private Gravity gravity;
	private Mass mass;
	private Temperature temperature;
	private Distance distance;
	private Pressure pressure;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	
		sPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		editor = sPref.edit();
		
		volume = new Volume(0, Volume.Unit.GALLON, getBaseContext(), sPref);
		gravity = new Gravity(0, Gravity.Unit.SG, getBaseContext(), sPref);
		mass = new Mass(0, Mass.Unit.GRAM, getBaseContext(), sPref);
		temperature = new Temperature(0, Temperature.Unit.FAHRENHEIT, getBaseContext(), sPref);
		distance = new Distance(0, Distance.Unit.INCH, getBaseContext(), sPref);
		pressure = new Pressure(0, Pressure.Unit.PSI, getBaseContext(), sPref);
		
		pref = findPreference(Preferences.GLOBAL_UNIT_CHANGE);
		pref.setOnPreferenceChangeListener(mUnitChangeListener);
		
		pref = findPreference(Preferences.GLOBAL_TEMPERATURE_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);
		
		pref = findPreference(Preferences.GLOBAL_GRAVITY_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);
		
		pref = findPreference(Preferences.GLOBAL_REFRACTOMETER_CORRECTION_FACTOR);
		pref.setOnPreferenceChangeListener(mPrefListener);
		
		pref = findPreference(Preferences.GLOBAL_EXTRACT_MASS_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);

		pref = findPreference(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE);
		pref.setOnPreferenceChangeListener(mPrefListener);

		pref = findPreference(Preferences.GLOBAL_PRESSURE_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);


		
		pref = findPreference(Preferences.BATCH_VOLUME_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);
		
		pref = findPreference(Preferences.BATCH_MASH_VOLUME_UNIT);
		pref.setOnPreferenceChangeListener(mMashVolumeUnitPrefListener);
		
		pref = findPreference(Preferences.BATCH_FINAL_VOLUME);
		pref.setOnPreferenceChangeListener(mPrefListener);
		
		pref = findPreference(Preferences.BATCH_GRAIN_MASS_UNIT);
		pref.setOnPreferenceChangeListener(mGrainMassUnitPrefListener);
		
		pref = findPreference(Preferences.BATCH_WATER_TO_GRAIN_RATIO);
		pref.setOnPreferenceChangeListener(mPrefListener);
				
		pref = findPreference(Preferences.BATCH_BOIL_MINUTES);
		pref.setOnPreferenceChangeListener(mPrefListener);

/*	 	// not yet implemented
		pref = findPreference(Preferences.BATCH_GRAIN_ABSORPTION_RATIO);
		pref.setOnPreferenceChangeListener(mPrefListener);

		pref = findPreference(Preferences.BATCH_GRAIN_VOLUME_RATIO);
		pref.setOnPreferenceChangeListener(mPrefListener);
*/
		pref = findPreference(Preferences.BATCH_INFUSION_WATER_TEMPERATURE);
		pref.setOnPreferenceChangeListener(mPrefListener);

		
		
		pref = findPreference(Preferences.KETTLE_DISTANCE_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);
				
		pref = findPreference(Preferences.KETTLE_DIAMETER);
		pref.setOnPreferenceChangeListener(mPrefListener);
				
		pref = findPreference(Preferences.KETTLE_FALSE_BOTTOM_HEIGHT);
		pref.setOnPreferenceChangeListener(mPrefListener);
				
		pref = findPreference(Preferences.KETTLE_EVAPORATION_RATE);
		pref.setOnPreferenceChangeListener(mPrefListener);
				
		pref = findPreference(Preferences.KETTLE_EVAPORATION_RATE);
		pref.setOnPreferenceChangeListener(mPrefListener);
				
		pref = findPreference(Preferences.KETTLE_EQUIPMENT_LOSS);
		pref.setOnPreferenceChangeListener(mPrefListener);
				
	}

	private void updateList() {

		pref = findPreference(Preferences.GLOBAL_TEMPERATURE_UNIT);
		temperature.setType(temperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.FAHRENHEIT));
		pref.setSummary(temperature.getLabelPlural());
		
		pref = findPreference(Preferences.GLOBAL_GRAVITY_UNIT);
		
		gravity.setType(gravity.typeFromPref(Preferences.GLOBAL_GRAVITY_UNIT, Gravity.Unit.SG));
		pref.setSummary(gravity.getLabelPlural());

		pref = findPreference(Preferences.GLOBAL_REFRACTOMETER_CORRECTION_FACTOR);
		pref.setSummary(getString(R.string.refractometer_correction_factor_pref_format, sPref.getString(Preferences.GLOBAL_REFRACTOMETER_CORRECTION_FACTOR, "1.04"), volume.getLabelPlural()));

		pref = findPreference(Preferences.GLOBAL_EXTRACT_MASS_UNIT);
		mass.setType(mass.typeFromPref(Preferences.GLOBAL_EXTRACT_MASS_UNIT, Mass.Unit.OUNCE));
		pref.setSummary(mass.getLabelPlural());
		
		pref = findPreference(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE);
		mass.setType(mass.typeFromPref(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE, Mass.Unit.OUNCE));
		pref.setSummary(mass.getLabelPlural());
		
		pref = findPreference(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE);
		pref.setSummary(getString(R.string.hydrometer_calibration_temperature_pref_summary_format, sPref.getString(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE, "60"), temperature.getLabelAbbr()));

		pref = findPreference(Preferences.GLOBAL_PRESSURE_UNIT);
		pressure.setType(pressure.typeFromPref(Preferences.GLOBAL_PRESSURE_UNIT, Pressure.Unit.PSI));
		pref.setSummary(pressure.getLabelPlural());
		
		
		pref = findPreference(Preferences.BATCH_VOLUME_UNIT);
		volume.setType(volume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON));
		pref.setSummary(volume.getLabelPlural());
		
		pref = findPreference(Preferences.BATCH_FINAL_VOLUME);
		pref.setSummary(sPref.getString(Preferences.BATCH_FINAL_VOLUME, "6.00") + " " + volume.getLabelPlural());

		pref = findPreference(Preferences.BATCH_MASH_VOLUME_UNIT);
		volume.setType(volume.typeFromPref(Preferences.BATCH_MASH_VOLUME_UNIT, Volume.Unit.GALLON));
		pref.setSummary(volume.getLabelPlural());
		
		pref = findPreference(Preferences.BATCH_GRAIN_MASS_UNIT);
		mass.setType(mass.typeFromPref(Preferences.BATCH_GRAIN_MASS_UNIT, Mass.Unit.POUND));
		pref.setSummary(mass.getLabelPlural());

		pref = findPreference(Preferences.BATCH_WATER_TO_GRAIN_RATIO);
		volume.setType(volume.typeFromPref(Preferences.BATCH_MASH_VOLUME_UNIT, Volume.Unit.GALLON));
		pref.setSummary(getString(R.string.water_to_grain_ratio_pref_summary_format, sPref.getString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, ".31"), volume.getLabelPlural(), mass.getLabel()));
		
		pref = findPreference(Preferences.BATCH_BOIL_MINUTES);
		pref.setSummary(sPref.getString(Preferences.BATCH_BOIL_MINUTES, "60"));
	
		/*
		pref = findPreference(Preferences.BATCH_GRAIN_ABSORPTION_RATIO);
		pref.setSummary(sPref.getString(Preferences.BATCH_GRAIN_ABSORPTION_RATIO, ".13"));
	
		pref = findPreference(Preferences.BATCH_GRAIN_VOLUME_RATIO);
		pref.setSummary(sPref.getString(Preferences.BATCH_GRAIN_VOLUME_RATIO, ".08"));
	*/
		
		pref = findPreference(Preferences.BATCH_INFUSION_WATER_TEMPERATURE);
		pref.setSummary(sPref.getString(Preferences.BATCH_INFUSION_WATER_TEMPERATURE, "212") + " " + temperature.getLabelAbbr());
		
		pref = findPreference(Preferences.KETTLE_DISTANCE_UNIT);
		distance.setType(distance.typeFromPref(Preferences.KETTLE_DISTANCE_UNIT, Distance.Unit.INCH));
		pref.setSummary(distance.getLabelPlural());

		pref = findPreference(Preferences.KETTLE_DIAMETER);
		pref.setSummary(sPref.getString(Preferences.KETTLE_DIAMETER, "0.00") + " " + distance.getLabelPlural());
		
		pref = findPreference(Preferences.KETTLE_FALSE_BOTTOM_HEIGHT);
		pref.setSummary(sPref.getString(Preferences.KETTLE_FALSE_BOTTOM_HEIGHT, "0.00") + " " + distance.getLabelPlural());

		pref = findPreference(Preferences.KETTLE_EVAPORATION_RATE);
		pref.setSummary(sPref.getString(Preferences.KETTLE_EVAPORATION_RATE, "10") + "%");

		pref = findPreference(Preferences.KETTLE_COOLING_LOSS);
		pref.setSummary(sPref.getString(Preferences.KETTLE_COOLING_LOSS, "4") + "%");
		
		pref = findPreference(Preferences.KETTLE_EQUIPMENT_LOSS);
		volume.setType(volume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON));
		pref.setSummary(getString(R.string.equipment_loss_pref_summary_format, sPref.getString(Preferences.KETTLE_EQUIPMENT_LOSS, "0"), volume.getLabelPlural()));

	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateList();
	}

	private OnPreferenceChangeListener mMashVolumeUnitPrefListener = new OnPreferenceChangeListener() { 		
		@Override
		public boolean onPreferenceChange(Preference preference, java.lang.Object newValue) {

			volume.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, "1"), 1));
			volume.setType(volume.typeFromPref(Preferences.BATCH_MASH_VOLUME_UNIT, Volume.Unit.GALLON));

			editor.putString(preference.getKey(), newValue.toString());
			editor.commit();

			volume.convert(volume.typeFromPref(Preferences.BATCH_MASH_VOLUME_UNIT, Volume.Unit.GALLON));
			editor.putString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, volume.toString());
			
			editor.commit();
			
			updateList();
			return true;
		}
	};

	private OnPreferenceChangeListener mGrainMassUnitPrefListener = new OnPreferenceChangeListener() { 		
		@Override
		public boolean onPreferenceChange(Preference preference, java.lang.Object newValue) {

			volume.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, "1"), 1));
			volume.setType(volume.typeFromPref(Preferences.BATCH_MASH_VOLUME_UNIT, Volume.Unit.GALLON));
			mass.setValue(1);
			mass.setType(mass.typeFromPref(Preferences.BATCH_GRAIN_MASS_UNIT, Mass.Unit.POUND));
			
			editor.putString(preference.getKey(), newValue.toString());
			editor.commit();
			
			volume.convert(volume.typeFromPref(Preferences.BATCH_MASH_VOLUME_UNIT, Volume.Unit.GALLON));
			mass.convert(mass.typeFromPref(Preferences.BATCH_GRAIN_MASS_UNIT, Mass.Unit.POUND));
			volume.setValue(volume.getValue() / mass.getValue());
			editor.putString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, volume.toString());

			editor.commit();
			updateList();
			return true;
		}
	};

	private OnPreferenceChangeListener mPrefListener = new OnPreferenceChangeListener() { 		
		@Override
		public boolean onPreferenceChange(Preference preference, java.lang.Object newValue) {
			editor.putString(preference.getKey(), newValue.toString());
			editor.commit();
			updateList();
			return true;
		}
	};

	private OnPreferenceChangeListener mUnitChangeListener = new OnPreferenceChangeListener() { 		
		@Override
		public boolean onPreferenceChange(Preference preference, java.lang.Object newValue) {

			if ("US".equals(newValue.toString()) && "METRIC".equals(sPref.getString(Preferences.GLOBAL_UNIT_CHANGE, "US"))) {

				volume.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.BATCH_FINAL_VOLUME, "0"), 0));
				volume.setType(volume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.LITER));
				volume.convert(Volume.Unit.GALLON);
				editor.putString(Preferences.BATCH_FINAL_VOLUME, volume.toString());
				
				volume.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, "1"), 1));
				volume.setType(volume.typeFromPref(Preferences.BATCH_MASH_VOLUME_UNIT, Volume.Unit.LITER));
				mass.setValue(1);
				mass.setType(mass.typeFromPref(Preferences.BATCH_GRAIN_MASS_UNIT, Mass.Unit.KILOGRAM));
				volume.convert(Volume.Unit.GALLON);
				mass.convert(Mass.Unit.POUND);
				volume.setValue(volume.getValue() / mass.getValue());
				editor.putString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, volume.toString());
				
				temperature.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.BATCH_INFUSION_WATER_TEMPERATURE, "0"), 0));
				temperature.setType(temperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.CELSIUS));
				temperature.convert(Temperature.Unit.FAHRENHEIT);
				editor.putString(Preferences.BATCH_INFUSION_WATER_TEMPERATURE, temperature.toString());
				
				distance.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.KETTLE_DIAMETER, "0"), 0));
				distance.setType(distance.typeFromPref(Preferences.KETTLE_DISTANCE_UNIT, Distance.Unit.CENTIMETER));
				distance.convert(Distance.Unit.INCH);
				editor.putString(Preferences.KETTLE_DIAMETER, distance.toString());
				
				distance.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.KETTLE_FALSE_BOTTOM_HEIGHT, "0"), 0));
				distance.setType(distance.typeFromPref(Preferences.KETTLE_DISTANCE_UNIT, Distance.Unit.CENTIMETER));
				distance.convert(Distance.Unit.INCH);
				editor.putString(Preferences.KETTLE_FALSE_BOTTOM_HEIGHT, distance.toString());
				
				volume.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.KETTLE_EQUIPMENT_LOSS, "0"), 0));
				volume.setType(volume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.LITER));
				volume.convert(Volume.Unit.GALLON);
				editor.putString(Preferences.KETTLE_EQUIPMENT_LOSS, volume.toString());
				
				temperature.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE, "60"), 60));
				temperature.setType(temperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.CELSIUS));
				temperature.convert(Temperature.Unit.FAHRENHEIT);
				editor.putString(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE, temperature.toString());

				editor.putString(Preferences.GLOBAL_TEMPERATURE_UNIT, "FAHRENHEIT");
				editor.putString(Preferences.GLOBAL_GRAVITY_UNIT, "SG");
				editor.putString(Preferences.GLOBAL_EXTRACT_MASS_UNIT, "OUNCE");
				editor.putString(Preferences.GLOBAL_PRESSURE_UNIT, "PSI");
				editor.putString(Preferences.BATCH_VOLUME_UNIT, "GALLON");
				editor.putString(Preferences.BATCH_MASH_VOLUME_UNIT, "GALLON");
				editor.putString(Preferences.BATCH_GRAIN_MASS_UNIT, "POUND");
				editor.putString(Preferences.KETTLE_DISTANCE_UNIT, "INCH");
			
			} else if ("METRIC".equals(newValue.toString()) && "US".equals(sPref.getString(Preferences.GLOBAL_UNIT_CHANGE, "METRIC"))) {

				volume.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.BATCH_FINAL_VOLUME, "0"), 0));
				volume.setType(volume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON));
				volume.convert(Volume.Unit.LITER);
				editor.putString(Preferences.BATCH_FINAL_VOLUME, volume.toString());
				
				volume.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, "1"), 1));
				volume.setType(volume.typeFromPref(Preferences.BATCH_MASH_VOLUME_UNIT, Volume.Unit.GALLON));
				mass.setValue(1);
				mass.setType(mass.typeFromPref(Preferences.BATCH_GRAIN_MASS_UNIT, Mass.Unit.POUND));
				volume.convert(Volume.Unit.LITER);
				mass.convert(Mass.Unit.KILOGRAM);
				volume.setValue(volume.getValue() / mass.getValue());
				editor.putString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, volume.toString());
				
				temperature.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.BATCH_INFUSION_WATER_TEMPERATURE, "0"), 0));
				temperature.setType(temperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.FAHRENHEIT));
				temperature.convert(Temperature.Unit.CELSIUS);
				editor.putString(Preferences.BATCH_INFUSION_WATER_TEMPERATURE, temperature.toString());
				
				distance.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.KETTLE_DIAMETER, "0"), 0));
				distance.setType(distance.typeFromPref(Preferences.KETTLE_DISTANCE_UNIT, Distance.Unit.INCH));
				distance.convert(Distance.Unit.CENTIMETER);
				editor.putString(Preferences.KETTLE_DIAMETER, distance.toString());
				
				distance.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.KETTLE_FALSE_BOTTOM_HEIGHT, "0"), 0));
				distance.setType(distance.typeFromPref(Preferences.KETTLE_DISTANCE_UNIT, Distance.Unit.INCH));
				distance.convert(Distance.Unit.CENTIMETER);
				editor.putString(Preferences.KETTLE_FALSE_BOTTOM_HEIGHT, distance.toString());
				
				volume.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.KETTLE_EQUIPMENT_LOSS, "0"), 0));
				volume.setType(volume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON));
				volume.convert(Volume.Unit.LITER);
				editor.putString(Preferences.KETTLE_EQUIPMENT_LOSS, volume.toString());
				
				temperature.setValue(NumberFormat.parseDouble(sPref.getString(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE, "60"), 60));
				temperature.setType(temperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.FAHRENHEIT));
				temperature.convert(Temperature.Unit.CELSIUS);
				editor.putString(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE, temperature.toString());

				editor.putString(Preferences.GLOBAL_TEMPERATURE_UNIT, "CELSIUS");
				editor.putString(Preferences.GLOBAL_GRAVITY_UNIT, "SG");
				editor.putString(Preferences.GLOBAL_PRESSURE_UNIT, "KPA");
				editor.putString(Preferences.BATCH_VOLUME_UNIT, "LITER");
				editor.putString(Preferences.BATCH_MASH_VOLUME_UNIT, "LITER");
				editor.putString(Preferences.BATCH_GRAIN_MASS_UNIT, "KILOGRAM");
				editor.putString(Preferences.KETTLE_DISTANCE_UNIT, "CENTIMETER");
				editor.putString(Preferences.GLOBAL_EXTRACT_MASS_UNIT, "GRAM");

			}
			editor.putString(preference.getKey(), newValue.toString());
			
			//TODO: don't know how to update the values stored in the listprefs and editprefs.  
			//      the values get saved, but if you click on the preference without reloading the activity
			//      the original text is in there.  ideally we'd skip the toast and finish() and just update the 
			//      preferences.
			Toast.makeText(getApplicationContext(), "Updating Measurement Units...", Toast.LENGTH_SHORT).show();
			editor.commit();
			finish();
			//updateList();
			return true;
		}
	};

}
