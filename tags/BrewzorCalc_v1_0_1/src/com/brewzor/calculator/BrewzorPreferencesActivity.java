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
import com.brewzor.converters.Temperature;
import com.brewzor.converters.Volume;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;

public class BrewzorPreferencesActivity extends PreferenceActivity {

	private Preference pref;
	private SharedPreferences sPref;
	private SharedPreferences.Editor editor;
	
	private Volume volume;
	private Gravity gravity;
	private Mass mass;
	private Temperature temperature;
	private Distance distance;
	
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

		pref = findPreference(Preferences.GLOBAL_TEMPERATURE_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);
		
		pref = findPreference(Preferences.GLOBAL_GRAVITY_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);
		
		pref = findPreference(Preferences.GLOBAL_EXTRACT_MASS_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);

		pref = findPreference(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE);
		pref.setOnPreferenceChangeListener(mPrefListener);

		pref = findPreference(Preferences.BATCH_VOLUME_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);
		
		pref = findPreference(Preferences.BATCH_FINAL_VOLUME);
		pref.setOnPreferenceChangeListener(mPrefListener);
		
		pref = findPreference(Preferences.BATCH_GRAIN_MASS_UNIT);
		pref.setOnPreferenceChangeListener(mPrefListener);
		
		pref = findPreference(Preferences.BATCH_WATER_TO_GRAIN_RATIO);
		pref.setOnPreferenceChangeListener(mPrefListener);
				
		pref = findPreference(Preferences.BATCH_BOIL_MINUTES);
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
				
	}

	private void updateList() {

		pref = findPreference(Preferences.GLOBAL_TEMPERATURE_UNIT);
		temperature.setType(temperature.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.FAHRENHEIT));
		pref.setSummary(temperature.getLabelPlural());
		
		pref = findPreference(Preferences.GLOBAL_GRAVITY_UNIT);
		gravity.setType(gravity.typeFromPref(Preferences.GLOBAL_GRAVITY_UNIT, Gravity.Unit.SG));
		pref.setSummary(gravity.getLabelPlural());

		pref = findPreference(Preferences.GLOBAL_EXTRACT_MASS_UNIT);
		mass.setType(mass.typeFromPref(Preferences.GLOBAL_EXTRACT_MASS_UNIT, Mass.Unit.OUNCE));
		pref.setSummary(mass.getLabelPlural());
		
		pref = findPreference(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE);
		mass.setType(mass.typeFromPref(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE, Mass.Unit.OUNCE));
		pref.setSummary(mass.getLabelPlural());
		
		pref = findPreference(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE);
		pref.setSummary(getString(R.string.hydrometer_calibration_temperature_pref_summary_format, sPref.getString(Preferences.GLOBAL_HYDROMETER_CALIBRATION_TEMPERATURE, "60"), temperature.getLabelAbbr()));

		
		
		pref = findPreference(Preferences.BATCH_VOLUME_UNIT);
		volume.setType(volume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON));
		pref.setSummary(volume.getLabelPlural());
		
		pref = findPreference(Preferences.BATCH_FINAL_VOLUME);
		pref.setSummary(sPref.getString(Preferences.BATCH_FINAL_VOLUME, "6.00") + " " + volume.getLabelPlural());

		pref = findPreference(Preferences.BATCH_GRAIN_MASS_UNIT);
		mass.setType(mass.typeFromPref(Preferences.BATCH_GRAIN_MASS_UNIT, Mass.Unit.POUND));
		pref.setSummary(mass.getLabelPlural());

		pref = findPreference(Preferences.BATCH_WATER_TO_GRAIN_RATIO);
		pref.setSummary(getString(R.string.water_to_grain_ratio_pref_summary_format, sPref.getString(Preferences.BATCH_WATER_TO_GRAIN_RATIO, ".31"), volume.getLabelPlural(), mass.getLabel()));
		
		pref = findPreference(Preferences.BATCH_BOIL_MINUTES);
		pref.setSummary(sPref.getString(Preferences.BATCH_BOIL_MINUTES, "60"));
	
		
		
		
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


	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateList();
	}

	private OnPreferenceChangeListener mPrefListener = new OnPreferenceChangeListener() { 		
		@Override
		public boolean onPreferenceChange(Preference preference, java.lang.Object newValue) {
			editor.putString(preference.getKey(), newValue.toString());
			editor.commit();
			updateList();
			return false;
		}
	};

}
