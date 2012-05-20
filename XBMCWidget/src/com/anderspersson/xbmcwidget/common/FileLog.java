package com.anderspersson.xbmcwidget.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.util.Log;

public class FileLog {
	public static void appendLog(String text)
	{       
	   File logFile = new File("sdcard/log.txt");
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(new Date().toString() + " " + text);
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      Log.w("FileLog", "Unable to log", e);
	      e.printStackTrace();
	   }
	}
}
