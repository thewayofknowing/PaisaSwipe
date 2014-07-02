package com.example.backup.data;

import com.example.backup.extmedia.ExternalMedia;

import android.graphics.Bitmap;

public class Advertisement {

	private int s_id;
	private int s_type;
	private String s_adUrl;
	private int s_companyId;
	private char s_audience;
	private Bitmap s_bitmap;
	
	public Advertisement() {
		
	}
	
	public Advertisement(int id,int type, int companyId, String adUrl, char audience) {
		this.s_id = id;
		this.s_type = type;
		this.s_adUrl = adUrl;
		this.s_companyId = companyId;
		this.s_audience = audience;
		//s_bitmap = s_ext.loadImage(s_imageName);
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
	
	public char getAudience() {
		return this.s_audience;
	}
	
	public Bitmap getImage() {
		return s_bitmap;
	}
	
	public void setId(int id) {
		this.s_id = id;
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
	
	public void setAudience(char audience) {
		this.s_audience = audience;
	}
	
	public void setImage(Bitmap bitmap) {
		s_bitmap = bitmap;
	}
}
