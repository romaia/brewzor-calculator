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

public final class Gravity extends Unit<com.brewzor.converters.Gravity.Unit> {

	static public enum Unit {
		SG, 
		GU,
		PLATO
	}

	public Gravity(double value, Unit type, Context context, SharedPreferences prefs) {
		super(value, type, context.getString(R.string.double_format), context, prefs);
	}

	@Override
	public final String getLabel() {
		switch (getType()) {
			case SG:	return context.getString(R.string.sg);
			case PLATO:	return context.getString(R.string.plato);
			case GU:	return context.getString(R.string.gu);
			default:	return UNKNOWN;
		}
	}

	@Override
	public final String getLabelPlural() {
		switch (getType()) {
			case SG:	return context.getString(R.string.sg);
			case PLATO:	return context.getString(R.string.plato);
			case GU:	return context.getString(R.string.gu);
			default:	return UNKNOWN;
		}
	}

	@Override
	public final String getLabelAbbr() {
		switch (getType()) {
			case SG:	return context.getString(R.string.sg_abbr);
			case PLATO:	return context.getString(R.string.plato_abbr);
			case GU:	return context.getString(R.string.gu_abbr);
			default:	return UNKNOWN;
		}
	}

	@Override
	public final double compare(Unit toType) {
		switch (getType()) {
			case SG:	return SGToUnits(getValue(), toType);
			case PLATO:	return PlatoToUnits(getValue(), toType);
			case GU:	return GUToUnits(getValue(), toType);
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
	
	static public final double SGToUnits(double value, Gravity.Unit toType) {
		switch (toType) {
			case SG:	return value;
			case PLATO:	return (668.72 * value) - 463.37 - (205.347 * java.lang.Math.pow(value, 2));
			case GU:	return (value - 1.0) * 1000.0;
			default:	return value;
		}
	}
	
	static public final double PlatoToUnits(double value, Gravity.Unit toType) {
		switch (toType) {
			case SG:	return ((0.082636 + (3.8480 * value) + (0.014563 * java.lang.Math.pow(value, 2))) / 1000.0) + 1.0;
			case PLATO:	return value;
			case GU:	return 0.082636 + (3.8480 * value) + (0.014563 * java.lang.Math.pow(value, 2));
//			case GU:	SGToUnits(PlatoToUnits(value, Gravity.SG), Gravity.GU);
			default:	return value;
		}
	}
	
	static public final double GUToUnits(double value, Gravity.Unit toType) {
		switch (toType) {
			case SG:	return 1.0 + (value / 1000.0);
			case PLATO:	return SGToUnits(GUToUnits(value, Gravity.Unit.SG), Gravity.Unit.PLATO);
			case GU:	return value;
			default:	return value;
		}
	}
	
}
