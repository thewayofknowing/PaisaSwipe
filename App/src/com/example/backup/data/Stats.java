package com.example.backup.data;

public class Stats {

	private int s_adId;
	private int s_userId = 0;
	private int s_type = 0;
	private int s_companyId = 2;
	private String s_appeared_at;
	private String s_swiped_at = "";
	private String s_clicked_at = "";
	private int s_completion_time = 0;
	private int s_previews = -1;
	
	/*
	 * Getters and Setters
	 */
	
	public int getAdId() {
		return this.s_adId;
	}
	
	public int getUserId() {
		return this.s_userId;
	}
	
	public int getType() {
		return this.s_type;
	}
	
	public int getCompanyId() {
		return this.s_companyId;
	}
	
	public String getAppearedAtTime() {
		return this.s_appeared_at;
	}
	
	public String getSwipedAtTime() {
		return this.s_swiped_at;
	}
	
	public String getClickedAtTime() {
		return this.s_clicked_at;
	}
	
	public int getCompletionTime() {
		return this.s_completion_time;
	}
	
	public int getPreviews() {
		return this.s_previews;
	}
	
	public void setAdId(int id) {
		this.s_adId = id;
	}

	public void setUserId(int userId) {
		this.s_userId = userId;
	}
	
	public void setType(int type) {
		this.s_type = type;
	}
	
	public void setCompanyId(int companyId) {
		this.s_companyId = companyId;
	}
	
	public void setAppearedAtTime(String time) {
		this.s_appeared_at = time;
	}
	
	public void setSwipedAtTime(String time) {
		this.s_swiped_at = time;
	}
	
	public void setClickedAtTime(String time) {
		this.s_clicked_at = time;
	}
	
	public void setCompletionTime(int seconds) {
		this.s_completion_time = seconds;
	}
	
	public void setPreviews(int previews) {
		this.s_previews = previews;
	}
	
}
