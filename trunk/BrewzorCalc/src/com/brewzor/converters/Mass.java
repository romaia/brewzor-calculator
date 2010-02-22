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

public final class Mass extends Unit<com.brewzor.converters.Mass.Unit> {

	static public enum Unit {
		GRAM,
		KILOGRAM,
		OUNCE,
		POUND
	}

	public Mass(double value, Mass.Unit type, Context context, SharedPreferences prefs) {
		super(value, type, context.getString(R.string.double_format), context, prefs);
	}

	@Override
	public final String getLabel() {
		switch (getType()) {
			case GRAM:		return context.getString(R.string.gram);
			case KILOGRAM:	return context.getString(R.string.kilogram);
			case OUNCE:		return context.getString(R.string.ounce);
			case POUND:		return context.getString(R.string.pound);
			default: 		return UNKNOWN;
		}
	}

	@Override
	public final String getLabelPlural() {
		switch (getType()) {
			case GRAM:		return context.getString(R.string.gram_plural);
			case KILOGRAM:	return context.getString(R.string.kilogram_plural);
			case OUNCE:		return context.getString(R.string.ounce_plural);
			case POUND:		return context.getString(R.string.pound_plural);
			default: 		return UNKNOWN;
		}
	}

	@Override
	public final String getLabelAbbr() {
		switch (getType()) {
			case GRAM:		return context.getString(R.string.gram_abbr);
			case KILOGRAM:	return context.getString(R.string.kilogram_abbr);
			case OUNCE:		return context.getString(R.string.ounce_abbr);
			case POUND:		return context.getString(R.string.pound_abbr);
			default: 		return UNKNOWN;
		}
	}

	@Override
	public final double compare(Mass.Unit toType) {
		switch (getType()) {
			case GRAM:		return getValue() * GramToUnits(toType);
			case KILOGRAM:	return getValue() * KilogramToUnits(toType);
			case OUNCE:		return getValue() * OunceToUnits(toType);
			case POUND:		return getValue() * PoundToUnits(toType);
			default: 		return getValue();
		}
	}

	@Override
	public final void setType(String toType) {
		for (Mass.Unit unit : Mass.Unit.values()) {
			//Log.v("MassUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(toType)){
				//Log.v("MassUnit", "type=" + unit.name());
				setType(unit);
			}
		}
	}

	@Override
	public final Mass.Unit typeFromPref(String toType, Mass.Unit defaultUnit) {
			String newType = prefs.getString(toType, UNKNOWN);
			for (Mass.Unit unit : Mass.Unit.values()) {
				//Log.v("MassUnit", "name: " + unit.name() + " newType: " + newType);
				if (unit.name().equals(newType)){
					//Log.v("MassUnit", "type=" + unit.name());
					return unit;
				}
			}
		return defaultUnit;
	}

	static public final double GramToUnits(Mass.Unit toType) {
		switch (toType) {
			case GRAM:		return 1.0;		
			case KILOGRAM:	return 0.001;
			case OUNCE:		return 0.0352739619;
			case POUND:		return 0.00220462262;
			default:		return 1.0;
		}
	}
	
	static public final double KilogramToUnits(Mass.Unit toType) {
		switch (toType) {
			case GRAM:		return 1000.0;
			case KILOGRAM:	return 1.0;
			case OUNCE:		return 35.2739619;
			case POUND:		return 2.20462262;
			default:		return 1.0;
		}
	}
	
	static public final double OunceToUnits(Mass.Unit toType) {
		switch (toType) {
			case GRAM:		return 28.3495231;
			case KILOGRAM:	return 0.0283495231;
			case OUNCE:		return 1.0;
			case POUND:		return 0.0625;
			default:		return 1.0;
		}
	}
	
	static public final double PoundToUnits(Mass.Unit toType) {
		switch (toType) {
			case GRAM:		return 453.59237;
			case KILOGRAM:	return 0.45359237;
			case OUNCE:		return 16.0;
			case POUND:		return 1.0;
			default:		return 1.0;
		}
	}
		
}
