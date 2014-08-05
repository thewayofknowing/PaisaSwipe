package com.example.backup.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;

public class Advertisement {

	private int s_id;
	private int s_type;
	private String  s_imageName;
	private String s_adUrl;
	private int s_totalImpressions;
	private int s_companyId;
	private char s_audience;
	private Bitmap s_bitmap;
	
	public Advertisement() {
		s_id = 0;
		s_type = 1;
		s_companyId = 2;
		s_bitmap = null;
	}
	
	public Advertisement(int id,int type, String image_name, int companyId, String adUrl, char audience, Bitmap bitmap) {
		this.s_id = id;
		this.s_type = type;
		this.s_imageName = image_name;
		this.s_adUrl = adUrl;
		this.s_companyId = companyId;
		this.s_audience = audience;
		this.s_bitmap = bitmap;
	}
	
	/*
	 * Getters and Setters
	 */
	
	public int getId() {
		return this.s_id;
	}
	
	public int getType() {
		return this.s_type;
	}
	
	public int getCompanyId() {
		return this.s_companyId;
	}
	
	public String getURL() {
		return this.s_adUrl;
	}
	
	public int getImpressionCount() {
		return this.s_totalImpressions;
	}
	
	public String getImageName() {
		return this.s_imageName;
	}
	
	public char getAudience() {
		return this.s_audience;
	}
	
	public Bitmap getImage() {
		return s_bitmap;
	}
	
	public void setId(int id) {
		this.s_id = id;
	}
	
	public void setImageName(String name) {
		this.s_imageName = name;
	}
	
	public void setType(int type) {
		this.s_type = type;
	}
	
	public void setCompanyId(int companyId) {
		this.s_companyId = companyId;
	}
	
	public void setURL(String url) {
		this.s_adUrl = url;
	}
	
	public void setImpressionCount(int count) {
		this.s_totalImpressions = count;
	}
	
	public void setAudience(char audience) {
		this.s_audience = audience;
	}
	
	public void setImage(Bitmap bitmap) {
		s_bitmap = bitmap;
	}
	
	public void writeImage(Context context) {
		if (s_bitmap!=null) {
			
		}
		else {
	        File file = new File( context.getFilesDir().getAbsolutePath()  + "/" + s_imageName);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(file);
				// Writing the bitmap to the output stream
			    s_bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			    fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		}
	}
}
