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
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Random;

import com.brewzor.calculator.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;

// based on code from http://androidblogger.blogspot.com/2009/12/how-to-improve-your-application-crash.html
public class ErrorReporter implements Thread.UncaughtExceptionHandler {

	static private final String EOL = "\n";
	
	String VersionName;
	String PackageName;
	String FilePath;
	String PhoneModel;
	String AndroidVersion;
	String Board;
	String Brand;
	String Device;
	String Display;
	String FingerPrint;
	String Host; 
	String ID;
	String Manufacturer;
	String Model;
	String Product;
	String Tags;
	long Time;
	String Type;
	String User; 

	private Thread.UncaughtExceptionHandler PreviousHandler;
	private static ErrorReporter S_mInstance;
	private Context CurContext;

	public void Init(Context context) {
		PreviousHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);  
		getEnvironmentInfo(context);
		CurContext = context;
	}

	public long getAvailableInternalMemorySize() { 
		File path = Environment.getDataDirectory(); 
		StatFs stat = new StatFs(path.getPath()); 
		long blockSize = stat.getBlockSize(); 
		long availableBlocks = stat.getAvailableBlocks(); 
		return availableBlocks * blockSize; 
	} 

	public long getTotalInternalMemorySize() { 
		File path = Environment.getDataDirectory(); 
		StatFs stat = new StatFs(path.getPath()); 
		long blockSize = stat.getBlockSize(); 
		long totalBlocks = stat.getBlockCount(); 
		return totalBlocks * blockSize; 
	} 

	void getEnvironmentInfo(Context context)
	{
		PackageManager pm = context.getPackageManager();
		try
		{
			PackageInfo pi;

			// populate environment info variables
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			VersionName = pi.versionName;
			PackageName = pi.packageName;
			FilePath = context.getFilesDir().getAbsolutePath();
			PhoneModel = android.os.Build.MODEL;
			AndroidVersion = android.os.Build.VERSION.RELEASE;
			Board = android.os.Build.BOARD;			
			Brand  = android.os.Build.BRAND;
			Device  = android.os.Build.DEVICE;
			Display = android.os.Build.DISPLAY;
			FingerPrint = android.os.Build.FINGERPRINT;
			Host = android.os.Build.HOST;
			ID = android.os.Build.ID;
			Model = android.os.Build.MODEL;
			Product = android.os.Build.PRODUCT;
			Tags = android.os.Build.TAGS;
			Time = android.os.Build.TIME;
			Type = android.os.Build.TYPE;
			User = android.os.Build.USER;

		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public String CreateInformationString()
	{
		String ReturnVal = "";
		ReturnVal += "Version : " + VersionName + EOL;
		ReturnVal += "Package : " + PackageName + EOL;
		ReturnVal += "FilePath : " + FilePath + EOL;
		ReturnVal += "Phone Model" + PhoneModel + EOL;
		ReturnVal += "Android Version : " + AndroidVersion + EOL;
		ReturnVal += "Board : " + Board + EOL;
		ReturnVal += "Brand : " + Brand + EOL;
		ReturnVal += "Device : " + Device + EOL;
		ReturnVal += "Display : " + Display + EOL;
		ReturnVal += "Finger Print : " + FingerPrint + EOL;
		ReturnVal += "Host : " + Host + EOL;
		ReturnVal += "ID : " + ID + EOL;
		ReturnVal += "Model : " + Model + EOL;
		ReturnVal += "Product : " + Product + EOL;
		ReturnVal += "Tags : " + Tags + EOL;
		ReturnVal += "Time : " + Time + EOL;
		ReturnVal += "Type : " + Type + EOL;
		ReturnVal += "User : " + User + EOL;
		ReturnVal += "Total Internal memory : " + getTotalInternalMemorySize() + EOL;
		ReturnVal += "Available Internal memory : " + getAvailableInternalMemorySize() + EOL;

		return ReturnVal;
	}

	public void uncaughtException(Thread t, Throwable e)
	{
		String Report = "";
		Date CurDate = new Date();
		Report += "Error Report collected on : " + CurDate.toString() + EOL + EOL;
		Report += "Information :" + EOL;
		Report += "==============" + EOL + EOL;
		Report += CreateInformationString() + EOL + EOL;
		Report += "Stack :" + EOL;
		Report += "=======" + EOL;
		
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		String stacktrace = result.toString();
		Report += stacktrace + EOL;

		// If the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		Throwable cause = e.getCause();
		if (cause != null) {
			Report += "Cause :" + EOL;
			Report += "=======" + EOL;
			while (cause != null)
			{
				cause.printStackTrace(printWriter);
				Report += result.toString();
				cause = cause.getCause();
			}
		}
		printWriter.close();
		Report += "****  End of current Report ***" + EOL;
		SaveAsFile(Report);
		PreviousHandler.uncaughtException(t, e);
	}

	public void deleteAllReports() {
		String[] ErrorFileList = GetErrorFileList();
		for (String curString : ErrorFileList) {
			File curFile = new File(FilePath + "/" + curString);
			curFile.delete();
		}
	}
	
	public static ErrorReporter getInstance()
	{
		if (S_mInstance == null)
			S_mInstance = new ErrorReporter();
		return S_mInstance;
	}

	private void SendErrorMail(Context _context, String ErrorContent)
	{
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		String subject = _context.getResources().getString(R.string.crash_report_mail_subject);
		String body = _context.getResources().getString(R.string.crash_report_mail_body) +
		EOL + EOL +
		ErrorContent +
		EOL + EOL;
		sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"james@brewzor.com"});
		sendIntent.putExtra(Intent.EXTRA_TEXT, body);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		sendIntent.setType("message/rfc822");
		_context.startActivity(Intent.createChooser(sendIntent, "Send Email Via:"));
	}
	
	private void SaveAsFile(String ErrorContent)
	{
		try
		{
			Random generator = new Random();
			int random = generator.nextInt(99999);
			String FileName = "stack-" + random + ".stacktrace";
			FileOutputStream trace = CurContext.openFileOutput( FileName, Context.MODE_PRIVATE);
			trace.write(ErrorContent.getBytes());
			trace.close();
		}
		catch(IOException ioe) {
			// ...
		}
	}

	private String[] GetErrorFileList()
	{
		File dir = new File(FilePath + "/");
		// Try to create the files folder if it doesn't exist
		dir.mkdir();
		// Filter for ".stacktrace" files
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".stacktrace");
			}
		};
		return dir.list(filter);
	}
	
	public boolean bIsThereAnyErrorFile()
	{
		return GetErrorFileList().length > 0;
	}
	
	public void CheckErrorAndSendMail(Context _context)
	{
		try
		{
			if (bIsThereAnyErrorFile())
			{
				String WholeErrorText = "";
				String[] ErrorFileList = GetErrorFileList();
				int curIndex = 0;
				// We limit the number of crash reports to send ( in order not to be too slow )
				final int MaxSendMail = 2;
				for (String curString : ErrorFileList)
				{
					if (curIndex++ <= MaxSendMail)
					{
						WholeErrorText+="New Trace collected :" + EOL;
						WholeErrorText+="===================== " + EOL;
						String filePath = FilePath + "/" + curString;
						BufferedReader input =  new BufferedReader(new FileReader(filePath));
						String line;
						while ((line = input.readLine()) != null)
						{
							WholeErrorText += line + EOL;
						}
						input.close();
					}

					// DELETE FILES !!!!
					File curFile = new File( FilePath + "/" + curString);
					curFile.delete();
				}

				SendErrorMail(_context , WholeErrorText);
			
				if (bIsThereAnyErrorFile())
				{
					// clear up any remaining reports
					deleteAllReports();
				}			

			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
