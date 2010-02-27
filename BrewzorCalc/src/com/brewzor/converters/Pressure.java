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
import com.brewzor.calculator.preferences.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public final class Pressure extends Unit<com.brewzor.converters.Pressure.Unit> {

	static public enum Unit {
		PSI,
		KPA, 
		BAR,
		ATMOSPHERE
	}
	
	public Pressure(double value, Pressure.Unit type, Context context, SharedPreferences prefs) {
		super(value, type, Preferences.DOUBLE_PRECISION, context, prefs);
	}

	@Override
	public double compare(Unit toType) {
		switch (getType()) {
			case PSI:			return getValue() * PSIToUnits(toType);
			case KPA:			return getValue() * KPaToUnits(toType);
			case BAR:			return getValue() * BarToUnits(toType);
			case ATMOSPHERE:	return getValue() * AtmosphereToUnits(toType);
			default: 			return getValue();
		}
	}

	@Override
	public String getLabel() {
		switch (getType()) {
			case PSI:			return context.getString(R.string.psi);
			case KPA:			return context.getString(R.string.kpa);
			case BAR:			return context.getString(R.string.bar);
			case ATMOSPHERE:	return context.getString(R.string.atmosphere);
			default: 			return UNKNOWN;
		}
	}

	@Override
	public String getLabelAbbr() {
		switch (getType()) {
			case PSI:			return context.getString(R.string.psi_abbr);
			case KPA:			return context.getString(R.string.kpa_abbr);
			case BAR:			return context.getString(R.string.bar_abbr);
			case ATMOSPHERE:	return context.getString(R.string.atmosphere_abbr);
			default: 			return UNKNOWN;
		}
	}

	@Override
	public String getLabelPlural() {
		switch (getType()) {
			case PSI:			return context.getString(R.string.psi_plural);
			case KPA:			return context.getString(R.string.kpa_plural);
			case BAR:			return context.getString(R.string.bar_plural);
			case ATMOSPHERE:	return context.getString(R.string.atmosphere_plural);
			default: 			return UNKNOWN;
		}
	}

	@Override
	public void setType(String toType) {
		for (Pressure.Unit unit : Pressure.Unit.values()) {
			//Log.v("PressureUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(toType)){
				//Log.v("PressureUnit", "type=" + unit.name());
				setType(unit);
			}
		}
	}

	@Override
	public Unit typeFromPref(String toType, Pressure.Unit defaultUnit) {
		String newType = prefs.getString(toType, UNKNOWN);
		for (Pressure.Unit unit : Pressure.Unit.values()) {
			//Log.v("PressureUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(newType)){
				//Log.v("PressureUnit", "type=" + unit.name());
				return unit;
			}
		}
		return defaultUnit;
	}
	
	public final double PSIToUnits(Pressure.Unit toType) {
		switch (toType) {
			case PSI:			return 1.0;		
			case KPA:			return 6.89475729;
			case BAR:			return 0.0689475729;
			case ATMOSPHERE:	return 0.0680459639;
			default:			return 1.0;
		}
	}

	public final double KPaToUnits(Pressure.Unit toType) {
		switch (toType) {
			case PSI:			return 0.145037738;		
			case KPA:			return 1.0;
			case BAR:			return 0.01;
			case ATMOSPHERE:	return 0.00986923267;
			default:			return 1.0;
		}
	}

	public final double BarToUnits(Pressure.Unit toType) {
		switch (toType) {
			case PSI:			return 14.5037738;		
			case KPA:			return 100.0;
			case BAR:			return 1.0;
			case ATMOSPHERE:	return 0.986923267;
			default:			return 1.0;
		}
	}

	public final double AtmosphereToUnits(Pressure.Unit toType) {
		switch (toType) {
			case PSI:			return 14.6959488;		
			case KPA:			return 101.325;
			case BAR:			return 1.01325;
			case ATMOSPHERE:	return 1.0;
			default:			return 1.0;
		}
	}

}
