package com.example.backup.extmedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.example.backup.constants.Constants;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;


//2890
public class ExternalMedia implements Constants {

	static ExternalMedia s_ext = null;
	Activity s_context;
	static String s_path;
	
	public void initConfig(Activity context) {
		this.s_context = context;
		s_path = Environment.getExternalStorageDirectory()
                			.getAbsolutePath() + "/Swipe'N'Ad";
		Log.d(TAG,"path:" + s_path);
	}
	
	public static ExternalMedia getInstance() {
		if(s_ext == null) {
			s_ext = new ExternalMedia();
		}
		return s_ext;
	}
	
	/*
	 * Check if media available to write or not
	 */
	public static boolean checkExternalMedia(){
	    boolean mExternalStorageAvailable = false;
	    boolean mExternalStorageWriteable = false;
	    String state = Environment.getExternalStorageState();

	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        // Can read and write the media
	        mExternalStorageAvailable = mExternalStorageWriteable = true;
	    } 
	    else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        // Can only read the media
	        mExternalStorageAvailable = true;
	        mExternalStorageWriteable = false;
	    } 
	    else {
	        // Can't read or write
	        mExternalStorageAvailable = mExternalStorageWriteable = false;
	    }   
	    
	    return (mExternalStorageAvailable && mExternalStorageWriteable);
	}

	public static boolean isFileExists(String FileName) {
		File file = new File(s_path, FileName);
		if (file.exists()) {
		  return true;
		}
		else {
			return false;
		}
	}
	
	public static Bitmap loadImage(String FileName) {
	   File file= new File(s_path , FileName);
	   BitmapFactory.Options options = new BitmapFactory.Options();
	   options.inSampleSize = 2;
	   final Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	   return b;
	}
	
	public void writeToSDFile(String FileName,String text){
	    File dir = new File (s_path);
	    dir.mkdirs();
	    File file = new File(dir, FileName);

	    try {
	        FileOutputStream f = new FileOutputStream(file);
	        PrintWriter pw = new PrintWriter(f);
	        pw.println(text);
	        pw.flush();
	        pw.close();
	        f.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return;
	}
	
	public String readFromSDFile(String FileName){
		String result = "";
		try {
	        InputStream inputStream = s_context.openFileInput(s_path + "/" + FileName);

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            result = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }
	    return result;
	}
	
}
