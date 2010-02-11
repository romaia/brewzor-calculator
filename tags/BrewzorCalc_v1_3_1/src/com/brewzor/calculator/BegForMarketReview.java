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
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brewzor.calculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Closeable;

import com.brewzor.calculator.preferences.Preferences;

/**
 * Displays a dialog begging for a review in the market.
 */
class BegForMarketReview {
    private static final String ASSET_BEG_FOR_REVIEW = "beg_for_review";

    /**
     * callback to let the activity know when the user has accepted the EULA.
     */
    static interface OnBegAgreedTo {

        /**
         * Called when the user has accepted the eula and the dialog closes.
         */
        void onBegAgreedTo();
    }

    /**
     * Displays the EULA if necessary. This method should be called from the onCreate()
     * method of your main Activity.
     *
     * @param activity The Activity to finish if the user rejects the EULA.
     * @return Whether the user has agreed already.
     */
    static boolean show(final Activity activity, Context c) {
    	final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
    	if (!preferences.getBoolean(Preferences.GLOBAL_BEG_FOR_REVIEW, false)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.beg_for_review_title);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.beg_for_review_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    accept(preferences);
                    if (activity instanceof OnBegAgreedTo) {
                        ((OnBegAgreedTo) activity).onBegAgreedTo();
                    }
                }
            });
            builder.setMessage(readEula(activity));
            builder.create().show();
            return false;
        }
        return true;
    }

    private static void accept(SharedPreferences preferences) {
        preferences.edit().putBoolean(Preferences.GLOBAL_BEG_FOR_REVIEW, true).commit();
    }

    private static CharSequence readEula(Activity activity) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(activity.getAssets().open(ASSET_BEG_FOR_REVIEW)));
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = in.readLine()) != null) buffer.append(line).append('\n');
            return buffer;
        } catch (IOException e) {
            return "";
        } finally {
            closeStream(in);
        }
    }

    /**
     * Closes the specified stream.
     *
     * @param stream The stream to close.
     */
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}