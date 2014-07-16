package com.example.backup.constants;

import java.util.HashMap;


public interface Constants {
	public static final String myPreferences = "MyPreferences";
	
	public static final int REQ_CREATE_PATTERN = 1;
	public static final int REQ_ENTER_PATTERN = 2;
	
	public static final String SCREENLOCK_ACTIVATED = "ScreenLock_Activated";
	public static final String APPLOCK_ACTIVATED = "AppLock_Activated";
	public final String ACTIVATED_LIST = "App_Activated_List";
	public final String LOCKED_LIST = "App_locked_list";
	public final String DEACTIVATED_LIST = "App_DeActivated_List"; 
	public final int ROUND_RADIUS = 54;
	
	public final static String TAG = "PaisaSwipe";
	
	public static final int SWIPE_THRESHOLD = 49;
    public static final int SWIPE_VELOCITY_THRESHOLD = 49;
   
    public static final int interval = 10000;
    public static final short gridSize = 3;
    public static final String app_ad_filename = "AppAd.txt";
    
    public static final String server_url = "http://54.187.181.173/start_up/";
    public static final String post_stats_page = "postStats";
    public static final String signup_page = "signup_android";
    
    public static int[] mIcon = new int[]{
    		 com.example.backup.R.drawable.game,
			 com.example.backup.R.drawable.wallet,
			 com.example.backup.R.drawable.setting,
			 com.example.backup.R.drawable.feedback,
			 };
    
    public static String[] mContents = {"Game","Wallet","Settings","Feedback"};
    
}
