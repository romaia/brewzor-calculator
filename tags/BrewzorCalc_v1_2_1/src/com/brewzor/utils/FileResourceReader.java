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
package com.brewzor.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;

public class FileResourceReader extends Activity {
	
	/**
	 * Closes the specified stream.
	 *
	 * @param activity The originating activity.
	 */
		public static CharSequence readResource(String filename, Activity activity) {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(activity.getAssets().open(filename)));
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

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

	}
