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

import com.brewzor.calculator.preferences.Preferences;
import com.brewzor.calculator.R;
import com.brewzor.utils.NumberFormat;

import android.content.Context;
import android.content.SharedPreferences;

public final class Gravity extends Unit<com.brewzor.converters.Gravity.Unit> {

	static public enum Unit {
		SG, 
		GU,
		PLATO, 
		BRIX
	}

	static private double brixCorrectionFactor = 1;
	
	public Gravity(double value, Unit type, Context context, SharedPreferences prefs) {
		super(value, type, Preferences.DOUBLE_PRECISION, context, prefs);
		Gravity.setBrixCorrectionFactor(NumberFormat.parseDouble(prefs.getString(Preferences.GLOBAL_REFRACTOMETER_CORRECTION_FACTOR, "1"), 1));
	}

	public Gravity(double value, Unit type, double bcf, Context context, SharedPreferences prefs) {
		super(value, type, Preferences.DOUBLE_PRECISION, context, prefs);
		Gravity.setBrixCorrectionFactor(bcf);
	}

	@Override
	public final String getLabel() {
		switch (getType()) {
			case SG:	return context.getString(R.string.sg);
			case PLATO:	return context.getString(R.string.plato);
			case GU:	return context.getString(R.string.gu);
			case BRIX:	return context.getString(R.string.brix);
			default:	return UNKNOWN;
		}
	}

	@Override
	public final String getLabelPlural() {
		switch (getType()) {
			case SG:	return context.getString(R.string.sg_plural);
			case PLATO:	return context.getString(R.string.plato_plural);
			case GU:	return context.getString(R.string.gu_plural);
			case BRIX:	return context.getString(R.string.brix_plural);
			default:	return UNKNOWN;
		}
	}

	@Override
	public final String getLabelAbbr() {
		switch (getType()) {
			case SG:	return context.getString(R.string.sg_abbr);
			case PLATO:	return context.getString(R.string.plato_abbr);
			case GU:	return context.getString(R.string.gu_abbr);
			case BRIX:	return context.getString(R.string.brix_abbr);
			default:	return UNKNOWN;
		}
	}

	@Override
	public final double compare(Unit toType) {
		switch (getType()) {
			case SG:	return SGToUnits(getValue(), toType);
			case PLATO:	return PlatoToUnits(getValue(), toType);
			case GU:	return GUToUnits(getValue(), toType);
			case BRIX:	return BrixToUnits(getValue(), toType);
			default:	return getValue();
		}
	}

	@Override
	public final void setType(String toType) {
		for (Gravity.Unit unit : Gravity.Unit.values()) {
			//Log.v("GravityUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(toType)){
				//Log.v("GravityUnit", "type=" + unit.name());
				setType(unit);
			}
		}
	}

	@Override
	public final Gravity.Unit typeFromPref(String toType, Gravity.Unit defaultUnit) {
			String newType = prefs.getString(toType, UNKNOWN);
			for (Gravity.Unit unit : Gravity.Unit.values()) {
				//Log.v("GravityUnit", "name: " + unit.name() + " newType: " + newType);
				if (unit.name().equals(newType)){
					//Log.v("GravityUnit", "type=" + unit.name());
					return unit;
				}
			}
		return defaultUnit;
	}
	
	static public final double BrixToUnits(double value, Gravity.Unit toType) {
		switch (toType) {
			case SG:	
			case GU:	
			case PLATO:	return PlatoToUnits(value / Gravity.getBrixCorrectionFactor(), toType);
			case BRIX:	return value;
			default:	return value;
		}

	}
	
	static public final double SGToUnits(double value, Gravity.Unit toType) {
		switch (toType) {
			case SG:	return value;
			case PLATO:	return (1111.14 * value) - (630.272 * java.lang.Math.pow(value, 2)) + (135.997 * java.lang.Math.pow(value, 3)) - 616.868;
			case GU:	return (value - 1.0) * 1000.0;
			case BRIX:	return SGToUnits(value, Gravity.Unit.PLATO) * Gravity.getBrixCorrectionFactor();
			default:	return value;
		}
	}
	
	static public final double PlatoToUnits(double value, Gravity.Unit toType) {
		switch (toType) {
			case SG:	return (668.0 - java.lang.Math.sqrt(java.lang.Math.pow(668.0, 2) - (820.0 * (463.0 + value)))) / 410.0;	
			case PLATO:	return value;
			case GU:	return (PlatoToUnits(value, Gravity.Unit.SG) - 1.0) * 1000.0;
			case BRIX:	return value * Gravity.getBrixCorrectionFactor();
			default:	return value;
		}
	}
	
	static public final double GUToUnits(double value, Gravity.Unit toType) {
		switch (toType) {
			case SG:	return 1.0 + (value / 1000.0);
			case PLATO:	return (0.258587 * value) - (0.00022281 * java.lang.Math.pow(value, 2)) + (0.000000135997 * java.lang.Math.pow(value, 3))- 0.003;
			case GU:	return value;
			case BRIX:	return GUToUnits(value, Gravity.Unit.PLATO) * Gravity.getBrixCorrectionFactor();
			default:	return value;
		}
	}
	
	static public final void setBrixCorrectionFactor(double cf) {
		Gravity.brixCorrectionFactor = cf;
	}

	static public final double getBrixCorrectionFactor() {
		return Gravity.brixCorrectionFactor;
	}
	
}
