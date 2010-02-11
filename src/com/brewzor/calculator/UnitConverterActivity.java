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
import com.brewzor.converters.Distance;
import com.brewzor.converters.Gravity;
import com.brewzor.converters.Mass;
import com.brewzor.converters.Pressure;
import com.brewzor.converters.Temperature;
import com.brewzor.converters.Volume;
import com.brewzor.utils.NumberFormat;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class UnitConverterActivity extends ListActivity {
    
	private EditText unitEntry;
	private Spinner unitCategory;
	private Spinner unitType;
	private String unitCategorySelected = "";

    private SharedPreferences prefs;

	private Volume volume;
	private Mass mass;
	private Gravity gravity;
	private Temperature temperature;
	private Distance distance;
	private Pressure pressure;
	
	private ArrayList<UnitListItem> convertedList = null;
    private UnitListItemAdapter unitListAdapter;

	private ArrayAdapter<CharSequence> spinnerAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	// http://www.softwarepassion.com/android-series-custom-listview-items-and-adapters/
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.unit_converter);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        volume = new Volume(0, Volume.Unit.GALLON, getBaseContext(), prefs);
        mass = new Mass(0, Mass.Unit.POUND, getBaseContext(), prefs);
        gravity = new Gravity(0, Gravity.Unit.SG, getBaseContext(), prefs);
        temperature = new Temperature(0, Temperature.Unit.FAHRENHEIT, getBaseContext(), prefs);
        distance = new Distance(0, Distance.Unit.CENTIMETER, getBaseContext(), prefs);
        pressure = new Pressure(0.0, Pressure.Unit.PSI, getBaseContext(), prefs);
        
        volume.setFormat(getString(R.string.unit_converter_format));
        mass.setFormat(getString(R.string.unit_converter_format));
        gravity.setFormat(getString(R.string.unit_converter_format));
        temperature.setFormat(getString(R.string.unit_converter_format));
        distance.setFormat(getString(R.string.unit_converter_format));
        pressure.setFormat(getString(R.string.unit_converter_format));
        
        unitEntry = (EditText) findViewById(R.id.unit_entry);
        unitEntry.setOnKeyListener(mOnKeyListener);
        
        unitCategory = (Spinner) findViewById(R.id.unit_type_spinner);
        unitCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		    	
				unitCategorySelected = getResources().getStringArray(R.array.unitTypesList)[unitCategory.getSelectedItemPosition()];
		    	// reset type list to the first item in the list
		    	unitType.setSelection(0);
		    	buildCategorySpinner();
		    	getConversions();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
        });

        unitType = (Spinner) findViewById(R.id.unit_spinner);
        unitType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	getConversions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }
        });
        
        // conversion list table setup
        convertedList = new ArrayList<UnitListItem>();
        unitListAdapter = new UnitListItemAdapter(this, R.layout.unit_converter_list_row, convertedList);
        setListAdapter(this.unitListAdapter);
                
        spinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);        
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitType.setAdapter(spinnerAdapter);
        
        // fill up the spinners with the default
        getConversions();

    };

	@Override
	public void onResume() {
		super.onResume();
		getPrefs();
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
	
	public OnKeyListener mOnKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() != KeyEvent.ACTION_UP) return false;
			if (unitEntry.getText().length() <= 0) return false;
			getConversions();
			return false;
		}
	};

	private void getPrefs() {
        Gravity.setBrixCorrectionFactor(NumberFormat.parseDouble(prefs.getString(Preferences.GLOBAL_REFRACTOMETER_CORRECTION_FACTOR, "1"), 1.0));
	}
    
    private void getConversions(){

    	unitListAdapter.clear();    	
    	convertedList = new ArrayList<UnitListItem>();
    	
    	if (unitCategorySelected.equals(getString(R.string.unit_distance_length))) {

    		distance.setValue(unitEntry, 0.0);
	    	if (unitType.getSelectedItemPosition() >= 0){
	        	distance.setType(getResources().getStringArray(R.array.distanceUnitsIdList)[unitType.getSelectedItemPosition()].toUpperCase());    		
	    	}
	    	
	    	Distance converted = new Distance(0, Distance.Unit.CENTIMETER, getBaseContext(), prefs);
	    	converted.setFormat(getString(R.string.unit_converter_format));
	    	for (Distance.Unit unit : Distance.Unit.values()) {
		    	converted.setValue(distance.getValue());
		    	converted.setType(distance.getType());
	    		converted.convert(unit);
	    		convertedList.add(new UnitListItem(converted.toString(), converted.getLabelAbbr() + " (" + converted.getLabel() + ")"));
	    	}
	    	
    	} else if (unitCategorySelected.equals(getString(R.string.unit_mass_weight))) {

    		mass.setValue(unitEntry, 0.0);
	    	if (unitType.getSelectedItemPosition() >= 0){
	        	mass.setType(getResources().getStringArray(R.array.massUnitsIdList)[unitType.getSelectedItemPosition()].toUpperCase());    		
	    	}
	    	
	    	Mass converted = new Mass(0, Mass.Unit.GRAM, getBaseContext(), prefs);
	    	converted.setFormat(getString(R.string.unit_converter_format));
	    	for (Mass.Unit unit : Mass.Unit.values()) {
		    	converted.setValue(mass.getValue());
		    	converted.setType(mass.getType());
	    		converted.convert(unit);
	    		convertedList.add(new UnitListItem(converted.toString(), converted.getLabelAbbr() + " (" + converted.getLabel() + ")"));
	    	}
    	
    	} else if (unitCategorySelected.equals(getString(R.string.unit_gravity))){

    		gravity.setValue(unitEntry, 0.0);
	    	if (unitType.getSelectedItemPosition() >= 0){
	        	gravity.setType(getResources().getStringArray(R.array.gravityUnitsIdList)[unitType.getSelectedItemPosition()].toUpperCase());    		
	    	}
	    	
	    	Gravity converted = new Gravity(0, Gravity.Unit.SG, getBaseContext(), prefs);
	    	converted.setFormat(getString(R.string.unit_converter_format));
	    	for (Gravity.Unit unit : Gravity.Unit.values()) {
		    	converted.setValue(gravity.getValue());
		    	converted.setType(gravity.getType());
	    		converted.convert(unit);
		    	//Log.v("unit", "conv: " + gravity.getType().toString() + " -> " + converted.getType().toString());
	    		convertedList.add(new UnitListItem(converted.toString(), converted.getLabelAbbr() + " (" + converted.getLabel() + ")"));
	    	}
    	
    	} else if (unitCategorySelected.equals(getString(R.string.unit_temperature))) {

    		temperature.setValue(unitEntry, 0.0);
	    	if (unitType.getSelectedItemPosition() >= 0){
	        	temperature.setType(getResources().getStringArray(R.array.temperatureUnitsIdList)[unitType.getSelectedItemPosition()].toUpperCase());    		
	    	}
	 
	    	Temperature converted = new Temperature(0, Temperature.Unit.FAHRENHEIT, getBaseContext(), prefs);
	    	converted.setFormat(getString(R.string.unit_converter_format));
	    	for (Temperature.Unit unit : Temperature.Unit.values()) {
		    	converted.setValue(temperature.getValue());
		    	converted.setType(temperature.getType());
	    		converted.convert(unit);
	    		convertedList.add(new UnitListItem(converted.toString(), converted.getLabelAbbr() + " (" + converted.getLabel() + ")"));
	    	}
    	
    	} else if (unitCategorySelected.equals(getString(R.string.unit_volume))) {

    		volume.setValue(unitEntry, 0.0);
	    	if (unitType.getSelectedItemPosition() >= 0){
	        	volume.setType(getResources().getStringArray(R.array.volumeUnitsIdList)[unitType.getSelectedItemPosition()].toUpperCase());    		
	    	}

	    	Volume converted = new Volume(0, Volume.Unit.MILLILITER, getBaseContext(), prefs);
	    	converted.setFormat(getString(R.string.unit_converter_format));
	    	for (Volume.Unit unit : Volume.Unit.values()) {
		    	converted.setValue(volume.getValue());
		    	converted.setType(volume.getType());
	    		converted.convert(unit);
	    		convertedList.add(new UnitListItem(converted.toString(), converted.getLabelAbbr() + " (" + converted.getLabel() + ")"));
	    	}

    	} else if (unitCategorySelected.equals(getString(R.string.unit_pressure))) {

    		pressure.setValue(unitEntry, 0.0);
    		if (unitType.getSelectedItemPosition() >= 0){
    			pressure.setType(getResources().getStringArray(R.array.pressureUnitsIdList)[unitType.getSelectedItemPosition()].toUpperCase());
    		}

    		Pressure converted = new Pressure(0, Pressure.Unit.PSI, getBaseContext(), prefs);
	    	converted.setFormat(getString(R.string.unit_converter_format));
	    	for (Pressure.Unit unit : Pressure.Unit.values()) {
		    	converted.setValue(pressure.getValue());
		    	converted.setType(pressure.getType());
	    		converted.convert(unit);
	    		convertedList.add(new UnitListItem(converted.toString(), converted.getLabelAbbr() + " (" + converted.getLabel() + ")"));
	    	}

    	}

        if(convertedList != null && convertedList.size() > 0){
            unitListAdapter.notifyDataSetChanged();
            for(int i=0;i<convertedList.size();i++)
            unitListAdapter.add(convertedList.get(i));
            unitListAdapter.notifyDataSetChanged();
        }

    }
    
    public void buildCategorySpinner() {

    	spinnerAdapter.clear();
		
    	if (unitCategorySelected.equals(getString(R.string.unit_distance_length))) {
    		for (Distance.Unit unit : Distance.Unit.values()) {
    			distance.setType(unit);
    			spinnerAdapter.add(distance.getLabel());
    		}
    	} else if (unitCategorySelected.equals(getString(R.string.unit_mass_weight))) {
    		for (Mass.Unit unit : Mass.Unit.values()) {
    			mass.setType(unit);
    			spinnerAdapter.add(mass.getLabel());
    		}
    	} else if (unitCategorySelected.equals(getString(R.string.unit_gravity))){
    		for (Gravity.Unit unit : Gravity.Unit.values()) {
    			gravity.setType(unit);
    			spinnerAdapter.add(gravity.getLabel());
    		}
    	} else if (unitCategorySelected.equals(getString(R.string.unit_temperature))) {
    		for (Temperature.Unit unit : Temperature.Unit.values()) {
	    		temperature.setType(unit);
	    		spinnerAdapter.add(temperature.getLabel());
    		}
    	} else if (unitCategorySelected.equals(getString(R.string.unit_volume))) {
    		for (Volume.Unit unit : Volume.Unit.values()) {
    			volume.setType(unit);
    			spinnerAdapter.add(volume.getLabel());
    		}
    	} else if (unitCategorySelected.equals(getString(R.string.unit_pressure))) {
    		for (Pressure.Unit unit : Pressure.Unit.values()) {
    			pressure.setType(unit);
    			spinnerAdapter.add(pressure.getLabel());
    		}
    	}

    }
    
    private class UnitListItemAdapter extends ArrayAdapter<UnitListItem> {

        private ArrayList<UnitListItem> items;

        public UnitListItemAdapter(Context context, int textViewResourceId, ArrayList<UnitListItem> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.unit_converter_list_row, null);
                }
                UnitListItem o = items.get(position);
                if (o != null) {
                        TextView value = (TextView) v.findViewById(R.id.value);
                        TextView label = (TextView) v.findViewById(R.id.label);
                        if (value != null) {
                              value.setText(o.getValue());                            }
                        if(label != null){
                              label.setText(o.getLabel());
                        }
                }
                return v;
        }
    }

}
