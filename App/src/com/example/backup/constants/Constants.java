package com.example.backup.constants;

import java.util.HashMap;


public interface Constants {
	public static final String myPreferences = "MyPreferences";
	public static final String STATUS = "Status";
	public final String ACTIVATED_LIST = "App_Activated_List";
	public final String DEACTIVATED_LIST = "App_DeActivated_List"; 
	public final static String TAG = "PaisaSwipe";
	public static final int SWIPE_THRESHOLD = 49;
    public static final int SWIPE_VELOCITY_THRESHOLD = 49;
    public static final int interval = 10000;
    public static final short gridSize = 3;
    public static final String app_ad_filename = "AppAd.txt";
    public static final String server_url = "http://54.187.181.173/start_up/";
    public static final String post_stats_page = "postStats";
    public static final String signup_page = "app_signup";
    
    public static int[] mIcon = new int[]{
    		 com.example.backup.R.drawable.game,
			 com.example.backup.R.drawable.wallet,
			 com.example.backup.R.drawable.setting,
			 com.example.backup.R.drawable.feedback,
			 };
    
    public static String[] mContents = {"Game","Wallet","Settings","Feedback"};
    
}
