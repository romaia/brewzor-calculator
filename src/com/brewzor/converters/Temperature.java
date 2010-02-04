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
package com.brewzor.converters;

import com.brewzor.calculator.R;

import android.content.Context;
import android.content.SharedPreferences;

public final class Temperature extends Unit<com.brewzor.converters.Temperature.Unit> {

	public static enum Unit { 
		FAHRENHEIT,
		CELSIUS
	}

	public Temperature(double value, Temperature.Unit type, Context context, SharedPreferences prefs) {
		super(value, type, context.getString(R.string.double_format), context, prefs);
	}

	@Override
	public final String getLabel() {
		switch (getType()) {
			case FAHRENHEIT:	return context.getString(R.string.fahrenheit);
			case CELSIUS:		return context.getString(R.string.celsius);
			default:			return UNKNOWN;
		}
	}

	@Override
	public final String getLabelPlural() {
		switch (getType()) {
			case FAHRENHEIT:	return context.getString(R.string.fahrenheit_plural);
			case CELSIUS:		return context.getString(R.string.celsius_plural);
			default:			return UNKNOWN;
		}
	}

	@Override
	public final String getLabelAbbr() {
		switch (getType()) {
			case FAHRENHEIT:	return context.getString(R.string.fahrenheit_abbr);
			case CELSIUS:		return context.getString(R.string.celsius_abbr);
			default:			return UNKNOWN;
		}
	}
	
	@Override
	public final double compare(Temperature.Unit toType) {
		switch (getType()) {
			case FAHRENHEIT:	return FahrenheitToUnits(toType);
			case CELSIUS:		return CelsiusToUnits(toType);
			default:			return getValue();
		}
	}

	@Override
	public final void setType(String toType) {
		for (Temperature.Unit unit : Temperature.Unit.values()) {
			//Log.v("TemperatureUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(toType)){
				//Log.v("TemperatureUnit", "type=" + unit.name());
				setType(unit);
			}
		}
	}
	
	@Override
	public final Temperature.Unit typeFromPref(String toType, Temperature.Unit defaultUnit) {
			String newType = prefs.getString(toType, UNKNOWN);
			for (Temperature.Unit unit : Temperature.Unit.values()) {
				//Log.v("TemperatureUnit", "name: " + unit.name() + " newType: " + newType);
				if (unit.name().equals(newType)){
					//Log.v("TemperatureUnit", "type=" + unit.name());
					return unit;
				}
			}
		return defaultUnit;
	}

	public final double FahrenheitToUnits(Temperature.Unit toType) {
		switch (toType) {
			case FAHRENHEIT:	return getValue();		
			case CELSIUS:		return (5.0 / 9.0) * (getValue() - 32.0);
			default:			return getValue();
		}
	}

	public final double CelsiusToUnits(Temperature.Unit toType) {
		switch (toType) {
			case FAHRENHEIT:	return ((9.0 / 5.0) * getValue()) + 32.0;		
			case CELSIUS:		return getValue();
			default:			return getValue();
		}
	}
	
}
