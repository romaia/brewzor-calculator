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

//import android.R;
import com.brewzor.calculator.R;

import android.content.Context;
import android.content.SharedPreferences;

public final class Distance extends Unit<com.brewzor.converters.Distance.Unit> {
	
	public enum Unit {
		CENTIMETER,
		INCH, 
	}

	public Distance(double value, Unit type, Context context, SharedPreferences prefs) {
		super(value, type, context.getString(R.string.double_format), context, prefs);
	}

	@Override
	public final String getLabel() {
		switch (getType()) {
			case CENTIMETER:	return context.getString(R.string.centimeter);
			case INCH:			return context.getString(R.string.inch);
			default:			return UNKNOWN;
		}
	}

	@Override
	public final String getLabelPlural() {
		switch (getType()) {
			case CENTIMETER:	return context.getString(R.string.centimeter_plural);
			case INCH:			return context.getString(R.string.inch_plural);
			default:			return UNKNOWN;
		}
	}

	@Override
	public final String getLabelAbbr() {
		switch (getType()) {
			case CENTIMETER:	return context.getString(R.string.centimeter_abbr);
			case INCH:			return context.getString(R.string.inch_abbr);
			default:			return UNKNOWN;
		}
	}
	
	@Override
	public final double compare(Unit toType) {
		switch (getType()) {
			case CENTIMETER:	return getValue() * CentimeterToUnits(toType);
			case INCH:			return getValue() * InchToUnits(toType);
			default:			return getValue();
		}
	}

	@Override
	public final void setType(String toType) {
		for (Distance.Unit unit : Distance.Unit.values()) {
			//Log.v("DistanceUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(toType)){
				//Log.v("DistanceUnit", "type=" + unit.name());
				setType(unit);
			}
		}
	}

	@Override
	public final Distance.Unit typeFromPref(String toType, Distance.Unit defaultUnit) {
			String newType = prefs.getString(toType, UNKNOWN);
			for (Distance.Unit unit : Distance.Unit.values()) {
				//Log.v("DistanceUnit", "name: " + unit.name() + " newType: " + newType);
				if (unit.name().equals(newType)){
					//Log.v("DistanceUnit", "type=" + unit.name());
					return unit;
				}
			}
		return defaultUnit;
	}

	static public final double InchToUnits(Distance.Unit toType) {
		switch (toType) {
			case CENTIMETER:	return 2.54;
			case INCH:			return 1.0;
			default:			return 1.0;
		}
	}
	
	static public final double CentimeterToUnits(Distance.Unit toType) {
		switch (toType) {
			case CENTIMETER:	return 1.0;		
			case INCH:			return 0.393700787;
			default:			return 1.0;
		}
	}
	

}
