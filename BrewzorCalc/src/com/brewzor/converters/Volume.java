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

public final class Volume extends Unit<com.brewzor.converters.Volume.Unit> {

	static public enum Unit {
		MILLILITER,
		LITER,
		TEASPOON,
		TABLESPOON,
		CUBIC_INCH,
		FLUID_OUNCE,
		CUP,
		BOTTLE_12_OZ,
		US_PINT,
		IMPERIAL_PINT,
		BOTTLE_22_OZ,
		BOTTLE_330_ML,
		BOTTLE_500_ML,
		BOTTLE_750_ML,
		QUART,
		GALLON,
		BARREL
	}

	public Volume(double value, Volume.Unit type, Context context, SharedPreferences prefs) {
		super(value, type, Preferences.DOUBLE_PRECISION, context, prefs);
	}

	@Override
	public final String getLabel() {
		switch (getType()) {
			case MILLILITER: 	return context.getString(R.string.milliliter);
			case LITER: 		return context.getString(R.string.liter);
			case TEASPOON: 		return context.getString(R.string.teaspoon);
			case TABLESPOON: 	return context.getString(R.string.tablespoon);
			case CUBIC_INCH: 	return context.getString(R.string.cubic_inch);
			case FLUID_OUNCE: 	return context.getString(R.string.fluid_ounce);
			case CUP: 			return context.getString(R.string.cup);
			case BOTTLE_12_OZ:	return context.getString(R.string.bottle_12_oz);
			case US_PINT: 		return context.getString(R.string.us_pint);
			case IMPERIAL_PINT:	return context.getString(R.string.imperial_pint);
			case BOTTLE_22_OZ:	return context.getString(R.string.bottle_22_oz);
			case BOTTLE_330_ML:	return context.getString(R.string.bottle_330_ml);
			case BOTTLE_500_ML:	return context.getString(R.string.bottle_500_ml);
			case BOTTLE_750_ML:	return context.getString(R.string.bottle_750_ml);
			case QUART: 		return context.getString(R.string.quart);
			case GALLON:		return context.getString(R.string.gallon);
			case BARREL:		return context.getString(R.string.barrel);
			default:			return UNKNOWN;
		}
	}

	@Override
	public final String getLabelPlural() {
		switch (getType()) {
			case MILLILITER: 	return context.getString(R.string.milliliter_plural);
			case LITER: 		return context.getString(R.string.liter_plural);
			case TEASPOON: 		return context.getString(R.string.teaspoon_plural);
			case TABLESPOON: 	return context.getString(R.string.tablespoon_plural);
			case CUBIC_INCH: 	return context.getString(R.string.cubic_inch_plural);
			case FLUID_OUNCE: 	return context.getString(R.string.fluid_ounce_plural);
			case CUP: 			return context.getString(R.string.cup_plural);
			case BOTTLE_12_OZ:	return context.getString(R.string.bottle_12_oz_plural);
			case US_PINT: 		return context.getString(R.string.us_pint_plural);
			case IMPERIAL_PINT:	return context.getString(R.string.imperial_pint_plural);
			case BOTTLE_22_OZ:	return context.getString(R.string.bottle_22_oz_plural);
			case BOTTLE_330_ML:	return context.getString(R.string.bottle_330_ml_plural);
			case BOTTLE_500_ML:	return context.getString(R.string.bottle_500_ml_plural);
			case BOTTLE_750_ML:	return context.getString(R.string.bottle_750_ml_plural);
			case QUART: 		return context.getString(R.string.quart_plural);
			case GALLON:		return context.getString(R.string.gallon_plural);
			case BARREL:		return context.getString(R.string.barrel_plural);
			default:			return UNKNOWN;
		}
	}

	@Override
	public final String getLabelAbbr() {
		switch (getType()) {
			case MILLILITER: 	return context.getString(R.string.milliliter_abbr);
			case LITER: 		return context.getString(R.string.liter_abbr);
			case TEASPOON: 		return context.getString(R.string.teaspoon_abbr);
			case TABLESPOON: 	return context.getString(R.string.tablespoon_abbr);
			case CUBIC_INCH: 	return context.getString(R.string.cubic_inch_abbr);
			case FLUID_OUNCE: 	return context.getString(R.string.fluid_ounce_abbr);
			case CUP: 			return context.getString(R.string.cup_abbr);
			case BOTTLE_12_OZ:	return context.getString(R.string.bottle_12_oz_abbr);
			case US_PINT: 		return context.getString(R.string.us_pint_abbr);
			case IMPERIAL_PINT:	return context.getString(R.string.imperial_pint_abbr);
			case BOTTLE_22_OZ:	return context.getString(R.string.bottle_22_oz_abbr);
			case BOTTLE_330_ML:	return context.getString(R.string.bottle_330_ml_abbr);
			case BOTTLE_500_ML:	return context.getString(R.string.bottle_500_ml_abbr);
			case BOTTLE_750_ML:	return context.getString(R.string.bottle_750_ml_abbr);
			case QUART: 		return context.getString(R.string.quart_abbr);
			case GALLON:		return context.getString(R.string.gallon_abbr);
			case BARREL:		return context.getString(R.string.barrel_abbr);
			default:			return UNKNOWN;
		}
	}

	@Override
	public final double compare(Volume.Unit toType) {
		switch (getType()) {
			case MILLILITER: 	return getValue() * MilliliterToUnit(toType);
			case LITER: 		return getValue() * LiterToUnit(toType);
			case TEASPOON: 		return getValue() * TeaspoonToUnit(toType);
			case TABLESPOON: 	return getValue() * TablespoonToUnit(toType);
			case CUBIC_INCH: 	return getValue() * CubicInchToUnit(toType);
			case FLUID_OUNCE: 	return getValue() * FluidOunceToUnit(toType);
			case CUP: 			return getValue() * CupToUnit(toType);
			case BOTTLE_12_OZ: 	return getValue() * Bottle12OzToUnit(toType);
			case BOTTLE_22_OZ: 	return getValue() * Bottle22OzToUnit(toType);
			case BOTTLE_330_ML: return getValue() * Bottle330MlToUnit(toType);
			case BOTTLE_500_ML: return getValue() * Bottle500MlToUnit(toType);
			case BOTTLE_750_ML: return getValue() * Bottle750MlToUnit(toType);
			case US_PINT: 		return getValue() * USPintToUnit(toType);
			case IMPERIAL_PINT:	return getValue() * ImperialPintToUnit(toType);
			case QUART: 		return getValue() * QuartToUnit(toType);
			case GALLON:		return getValue() * GallonToUnit(toType);
			case BARREL:		return getValue() * BarrelToUnit(toType);
			default:			return getValue();
		}
	}

	@Override
	public final void setType(String toType) {
		for (Volume.Unit unit : Volume.Unit.values()) {
			//Log.v("VolumeUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(toType)){
				//Log.v("VolumeUnit", "type=" + unit.name());
				setType(unit);
			}
		}
	}

	@Override
	public final Volume.Unit typeFromPref(String toType, Volume.Unit defaultUnit) {
			
		String newType = prefs.getString(toType, UNKNOWN);
			for (Volume.Unit unit : Volume.Unit.values()) {
				//Log.v("VolumeUnit", "name: " + unit.name() + " newType: " + newType);
				if (unit.name().equals(newType)){
					//Log.v("VolumeUnit", "type=" + unit.name());
					return unit;
				}
			}
		return defaultUnit;
	}
	
	static public final double MilliliterToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 1.0;
			case LITER:			return 0.001;
			case TEASPOON:		return 0.202884136;
			case TABLESPOON:	return 0.0676280454;
			case FLUID_OUNCE:	return 0.0338140227;
			case CUP:			return 0.00422675284;
			case BOTTLE_12_OZ:	return 0.00281783523;
			case BOTTLE_22_OZ:	return 0.00153700103;
			case BOTTLE_330_ML:	return 0.00303030303;
			case BOTTLE_500_ML:	return 0.002;
			case BOTTLE_750_ML:	return 0.00133333333;
			case US_PINT:		return 0.00211337642;
			case IMPERIAL_PINT:	return 0.00175975326;
			case QUART:			return 0.00105668821;
			case GALLON:		return 0.000264172052;
			case BARREL:		return 8.52167911e-6;
			default: 			return 1.0;
		}
	}
	
	static public final double LiterToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 1000.0;
			case LITER:			return 1.0;
			case TEASPOON:		return 202.884136;
			case TABLESPOON:	return 67.6280454;
			case CUBIC_INCH:	return 61.0237441;
			case FLUID_OUNCE:	return 33.8140227;
			case CUP:			return 4.22675284;
			case BOTTLE_12_OZ:	return 2.817835225;
			case BOTTLE_22_OZ:	return 1.537001032;
			case BOTTLE_330_ML:	return 3.030303030;
			case BOTTLE_500_ML:	return 2.0;
			case BOTTLE_750_ML:	return 1.333333333;
			case US_PINT:		return 2.11337642;
			case IMPERIAL_PINT:	return 1.75975326;
			case QUART:			return 1.05668821;
			case GALLON:		return 0.264172052;
			case BARREL:		return 0.00852167911;
			default: 			return 1.0;
		}
	}
	
	static public final double TeaspoonToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 4.92892159;
			case LITER:			return 0.00492892159;
			case TEASPOON:		return 1.0;
			case TABLESPOON:	return 0.333333333;
			case CUBIC_INCH:	return 0.30078125;
			case FLUID_OUNCE:	return 0.166666667;
			case CUP:			return 0.0208333333;
			case BOTTLE_12_OZ:	return 0.0138888889;
			case BOTTLE_22_OZ:	return 0.0075757576;
			case BOTTLE_330_ML:	return 0.01493612603;
			case BOTTLE_500_ML:	return 0.00985784318;
			case BOTTLE_750_ML:	return 0.00657189545;
			case US_PINT:		return 0.0104166667;
			case IMPERIAL_PINT:	return 0.00867368584;
			case QUART:			return 0.00520833333;
			case GALLON:		return 0.00130208333;
			case BARREL:		return 4.20026882e-5;
			default: 			return 1.0;
		}
	}

	static public final double TablespoonToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 14.7867648;
			case LITER:			return 0.0147867648;
			case TEASPOON:		return 3.0;
			case TABLESPOON:	return 1.0;
			case CUBIC_INCH:	return 0.90234375;
			case FLUID_OUNCE:	return 0.5;
			case CUP:			return 0.0625;
			case BOTTLE_12_OZ:	return 0.041666666667;
			case BOTTLE_22_OZ:	return 0.022727272727;
			case BOTTLE_330_ML:	return 0.0448083781818;
			case BOTTLE_500_ML:	return 0.0295735296;
			case BOTTLE_750_ML:	return 0.0197156864;
			case US_PINT:		return 0.03125;
			case IMPERIAL_PINT:	return 0.0260210575;
			case QUART:			return 0.015625;
			case GALLON:		return 0.00390625;
			case BARREL:		return 0.000126008065;
			default: 			return 1.0;
		}
	}

	static public final double CubicInchToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 16.387064;
			case LITER:			return 0.016387064;
			case TEASPOON:		return 3.32467532;
			case TABLESPOON:	return 1.10822511;
			case CUBIC_INCH:	return 1.0;
			case FLUID_OUNCE:	return 0.554112554;
			case CUP:			return 0.0692640693;
			case BOTTLE_12_OZ:	return 0.046176046167;
			case BOTTLE_22_OZ:	return 0.025186934273;
			case BOTTLE_330_ML:	return 0.04965776969697;
			case BOTTLE_500_ML:	return 0.032774128;
			case BOTTLE_750_ML:	return 0.021849418667;
			case US_PINT:		return 0.0346320346;
			case IMPERIAL_PINT:	return 0.0288371893;
			case QUART:			return 0.0173160173;
			case GALLON:		return 0.00432900433;
			case BARREL:		return 0.000139645301;
			default: 			return 1.0;
		}
	}

	static public final double FluidOunceToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 29.5735296;
			case LITER:			return 0.0295735296;
			case TEASPOON:		return 6.0;
			case TABLESPOON:	return 2.0;
			case CUBIC_INCH:	return 1.8046875;
			case FLUID_OUNCE:	return 1.0;
			case CUP:			return 0.125;
			case BOTTLE_12_OZ:	return 0.083333333333;
			case BOTTLE_22_OZ:	return 0.045454545455;
			case BOTTLE_330_ML:	return 0.08961675636364;
			case BOTTLE_500_ML:	return 0.0591470592;
			case BOTTLE_750_ML:	return 0.0394313728;
			case US_PINT:		return 0.0625;
			case IMPERIAL_PINT:	return 0.052042115 ;
			case QUART:			return 0.03125;
			case GALLON:		return 0.0078125;
			case BARREL:		return 0.000252016129;
			default: 			return 1.0;
		}
	}

	static public final double CupToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 236.588237;
			case LITER:			return 0.236588236;
			case TEASPOON:		return 48.0;
			case TABLESPOON:	return 16.0;
			case CUBIC_INCH:	return 14.4375;
			case FLUID_OUNCE:	return 8.0;
			case CUP:			return 1.0;
			case BOTTLE_12_OZ:	return 0.66666666667;
			case BOTTLE_22_OZ:	return 0.36363636364;
			case BOTTLE_330_ML:	return 0.716934051515;
			case BOTTLE_500_ML:	return 0.473176474;
			case BOTTLE_750_ML:	return 0.31545098267;
			case US_PINT:		return 0.5;
			case IMPERIAL_PINT:	return 0.41633692;
			case QUART:			return 0.25;
			case GALLON:		return 0.0625;
			case BARREL:		return 0.00201612903;
			default: 			return 1.0;
		}
	}

	static public final double Bottle12OzToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 354.8823552;
			case LITER:			return 0.3548823552;
			case TEASPOON:		return 72.0;
			case TABLESPOON:	return 24.0;
			case CUBIC_INCH:	return 21.65625;
			case FLUID_OUNCE:	return 12.0;
			case CUP:			return 1.5;
			case BOTTLE_12_OZ:	return 1.0;
			case BOTTLE_22_OZ:	return 0.5454545455;
			case BOTTLE_330_ML:	return 1.075401076364;
			case BOTTLE_500_ML:	return 0.7097647104;
			case BOTTLE_750_ML:	return 0.4731764736;
			case US_PINT:		return 0.75;
			case IMPERIAL_PINT:	return 0.62450538;
			case QUART:			return 0.375;
			case GALLON:		return 0.09375;
			case BARREL:		return 0.003024193548;
			default: 			return 1.0;
		}
	}

	static public final double Bottle22OzToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 650.6176512;
			case LITER:			return 0.6506176512;
			case TEASPOON:		return 132.0;
			case TABLESPOON:	return 44.0;
			case CUBIC_INCH:	return 39.703125;
			case FLUID_OUNCE:	return 22.0;
			case CUP:			return 2.75;
			case BOTTLE_12_OZ:	return 1.833333333326;
			case BOTTLE_22_OZ:	return 1.0;
			case BOTTLE_330_ML:	return 1.97156864;
			case BOTTLE_500_ML:	return 1.3012353024;
			case BOTTLE_750_ML:	return 0.8674902016;
			case US_PINT:		return 1.375;
			case IMPERIAL_PINT:	return 1.14492653;
			case QUART:			return 0.6875;
			case GALLON:		return 0.171875;
			case BARREL:		return 0.005544354838;
			default: 			return 1.0;
		}
	}

	static public final double Bottle330MlToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 330.0;
			case LITER:			return 0.33;
			case TEASPOON:		return 66.9517649;
			case TABLESPOON:	return 22.317255;
			case FLUID_OUNCE:	return 11.1586275;
			case CUP:			return 1.39482844;
			case BOTTLE_12_OZ:	return 0.929885625;
			case BOTTLE_22_OZ:	return 0.5072103409;
			case BOTTLE_330_ML:	return 1.0;
			case BOTTLE_500_ML:	return 0.66;
			case BOTTLE_750_ML:	return 0.44;
			case US_PINT:		return 0.697414218;
			case IMPERIAL_PINT:	return 0.580718575;
			case QUART:			return 0.348707109;
			case GALLON:		return 0.0871767773;
			case BARREL:		return 0.00281215411;
			default: 			return 1.0;
		}
	}
	
	static public final double Bottle500MlToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 500.0;
			case LITER:			return 0.5;
			case TEASPOON:		return 101.442068;
			case TABLESPOON:	return 33.8140227;
			case FLUID_OUNCE:	return 16.9070114;
			case CUP:			return 2.11337642;
			case BOTTLE_12_OZ:	return 1.408917616667;
			case BOTTLE_22_OZ:	return 0.76850051818;
			case BOTTLE_330_ML:	return 1.51515151515;
			case BOTTLE_500_ML:	return 1.0;
			case BOTTLE_750_ML:	return 0.666666666667;
			case US_PINT:		return 1.05668821;
			case IMPERIAL_PINT:	return 0.87987663;
			case QUART:			return 0.528344105;
			case GALLON:		return 0.132086026;
			case BARREL:		return 0.00426083955;
			default: 			return 1.0;
		}
	}
	
	static public final double Bottle750MlToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 750.0;
			case LITER:			return 0.75;
			case TEASPOON:		return 152.163102;
			case TABLESPOON:	return 50.72103405;
			case FLUID_OUNCE:	return 25.360517025;
			case CUP:			return 3.17006463;
			case BOTTLE_12_OZ:	return 2.1133764225;
			case BOTTLE_22_OZ:	return 1.1527507725;
			case BOTTLE_330_ML:	return 2.27272727273;
			case BOTTLE_500_ML:	return 1.5;
			case BOTTLE_750_ML:	return 1.0;
			case US_PINT:		return 1.585032315;
			case IMPERIAL_PINT:	return 1.319814945;
			case QUART:			return 0.7925161575;
			case GALLON:		return 0.198129039;
			case BARREL:		return 0.0063912593325;
			default: 			return 1.0;
		}
	}
	
	static public final double USPintToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 473.176473;
			case LITER:			return 0.473176473;
			case TEASPOON:		return 96.0;
			case TABLESPOON:	return 32.0;
			case CUBIC_INCH:	return 28.87500;
			case FLUID_OUNCE:	return 16.0;
			case CUP:			return 2.0;
			case BOTTLE_12_OZ:	return 1.33333333333;
			case BOTTLE_22_OZ:	return 0.72727272727;
			case BOTTLE_330_ML:	return 1.4338681;
			case BOTTLE_500_ML:	return 0.946352946;
			case BOTTLE_750_ML:	return 0.630901964;
			case US_PINT:		return 1.0;
			case IMPERIAL_PINT:	return 0.83267384;
			case QUART:			return 0.5;
			case GALLON:		return 0.125;
			case BARREL:		return 0.00403225806;
			default: 			return 1.0;
		}
	}

	static public final double ImperialPintToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 568.261485;
			case LITER:			return 0.568261485;
			case TEASPOON:		return 115.291241;
			case TABLESPOON:	return 38.4304135;
			case CUBIC_INCH:	return 34.6774434;
			case FLUID_OUNCE:	return 19.2152068;
			case CUP:			return 2.40190084;
			case BOTTLE_12_OZ:	return 1.60126723;
			case BOTTLE_22_OZ:	return 0.873418491;
			case BOTTLE_330_ML:	return 1.7220045;
			case BOTTLE_500_ML:	return 1.13652297;
			case BOTTLE_750_ML:	return 0.75768198;
			case US_PINT:		return 1.20095042;
			case IMPERIAL_PINT:	return 1.0;
			case QUART:			return 0.600475211;
			case GALLON:		return 0.150118803;
			case BARREL:		return 0.0048425420;
			default: 			return 1.0;
		}

	}
	
	static public final double QuartToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 946.352946;
			case LITER:			return 0.946352946;
			case TEASPOON:		return 192.0;
			case TABLESPOON:	return 64.0;
			case CUBIC_INCH:	return 57.75;
			case FLUID_OUNCE:	return 32.0;
			case CUP:			return 4.0;
			case BOTTLE_12_OZ:	return 2.66666666667;
			case BOTTLE_22_OZ:	return 1.45454545455;
			case BOTTLE_330_ML:	return 2.8677362;
			case BOTTLE_500_ML:	return 1.892705892;
			case BOTTLE_750_ML:	return 1.261803928;
			case US_PINT:		return 2.0;
			case IMPERIAL_PINT:	return 1.66534768;
			case QUART:			return 1.0;
			case GALLON:		return 0.25;
			case BARREL:		return 0.00806451613;
			default: 			return 1.0;
		}
	}

	static public final double GallonToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 3785.41178;
			case LITER:			return 3.78541178;
			case TEASPOON:		return 768.0;
			case TABLESPOON:	return 256.0;
			case CUBIC_INCH:	return 231.0;
			case FLUID_OUNCE:	return 128.0;
			case CUP:			return 16.0;
			case BOTTLE_12_OZ:	return 10.6666666667;
			case BOTTLE_22_OZ:	return 5.81818181818;
			case BOTTLE_330_ML:	return 11.47094478788;
			case BOTTLE_500_ML:	return 7.57082356;
			case BOTTLE_750_ML:	return 5.04721570667;
			case US_PINT:		return 8.0;
			case IMPERIAL_PINT:	return 6.66139072;
			case QUART:			return 4.0;
			case GALLON:		return 1.0;
			case BARREL:		return 0.0322580645;
			default: 			return 1.0;
		}
	}
	
	static public final double BarrelToUnit(Volume.Unit toType) {
		switch (toType) {
			case MILLILITER:	return 117347.765;
			case LITER:			return 117.347765;
			case TEASPOON:		return 23808.0;
			case TABLESPOON:	return 7936.0;
			case CUBIC_INCH:	return 7161.0;
			case FLUID_OUNCE:	return 3968.0;
			case CUP:			return 496.0;
			case BOTTLE_12_OZ:	return 330.6666666667;
			case BOTTLE_22_OZ:	return 180.3636363636;
			case BOTTLE_330_ML:	return 355.59928787879;
			case BOTTLE_500_ML:	return 234.69553;
			case BOTTLE_750_ML:	return 156.4636866667;
			case US_PINT:		return 248.0;
			case IMPERIAL_PINT:	return 206.503112;
			case QUART:			return 124.0;
			case GALLON:		return 31.0;
			case BARREL:		return 1.0;
			default: 			return 1.0;
		}
	}

}
