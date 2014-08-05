package com.example.backup.constants;

public interface Constants {
	public static final String myPreferences = "MyPreferences";
	public static final String PACKAGE_NAME = "com.example.backup";
	
	public static final int REQ_CREATE_PATTERN = 1;
	public static final int REQ_ENTER_PATTERN = 2;
	public static final String APP_LOCK_TYPE = "App Lock Type";
	public static final String IMPRESSION_CURRENT_INDEX = "current impression index";
	public static final String LOCK_PATTERN = "Lock Pattern";
	
	public static final String SCREENLOCK_ACTIVATED = "ScreenLock_Activated";
	public static final String APPLOCK_ACTIVATED = "AppLock_Activated";
	public final String ACTIVATED_LIST = "App_Activated_List";
	public final String LOCKED_LIST = "App_locked_list";
	public final String DEACTIVATED_LIST = "App_DeActivated_List"; 
	public final int ROUND_RADIUS = 16;
	
	public final String USER_ID = "User ID";
	public final String USER_BALANCE = "User Balance";
	public final String LOGIN_TYPE = "Login type";
	public final String USER_EMAIL = "User Email";
	public final String USER_NAME = "User Name";
	public final String USER_PASSWORD = "User Password";
	public final String USER_PINCODE = "User Pincode";
	public final String USER_GENDER = "User Gender";
	
	
	public static final String OPEN_SANS_BOLD = "fonts/OpenSans-Bold.ttf";
	public static final String OPEN_SANS_SEMIBOLD = "fonts/OpenSans-Semibold.ttf";
	public static final String OPEN_SANS_REGULAR = "fonts/OpenSans-Regular.ttf";
	
	public final static String TAG = "PaisaSwipe";
	
	public static final int SWIPE_THRESHOLD = 49;
    public static final int SWIPE_VELOCITY_THRESHOLD = 49;
   
    public static final int interval = 10000;
    public static final short gridSize = 3;
    public static final String GAME_CONGRATULATIONS = "Congratulations!!!\nYou have successfully solved the Puzzle";
    public static final String app_ad_filename = "AppAd.txt";
    
    public static final String server_url = "http://54.187.181.173/start_up/";
    public static final String post_stats_page = "insert_stat";
    public static final String fetch_ads = "get_ads";
    public static final String signup_page = "signup_android";
    
    public static int[] mIcon = new int[]{
    		 com.example.backup.R.drawable.game,
    		 com.example.backup.R.drawable.profile,
			 com.example.backup.R.drawable.wallet,
			 com.example.backup.R.drawable.settings,
			 com.example.backup.R.drawable.share,
			 com.example.backup.R.drawable.feedback,
			 com.example.backup.R.drawable.faq,
			 com.example.backup.R.drawable.logout,
			 };
    
    public static String[] SYSTEM_APPS = new String[] {
    	"Calendar","Camera","Chrome","Clock","Drive","Hangouts","Gmail","Gallery","Messaging","Settings","Google Play Store","Drive","Google+","YouTube"
    };
    
    public static String[] PROVIDERS = {
    	"Vodafone",
    	"Airtel",
    	"Idea",
    	"Reliance",
    	"Aircel",
    	"Tata"
    };
    
    public static String[] STATES = {
    	"Andaman and Nicobar","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chandigarh","Chattisgarh",
    	"Dadra and Nagar Haveli","Daman and Diu","Delhi","Goa","Gujarat","Haryana","Himachal Pradesh","Jammu and Kashmir","Jharkhand",
    	"Karnataka","Kerala","Lakshadweep Islands","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram",
    	"Nagaland","Odisha","Puducherry","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttar Pradesh","Uttarkhand","West Bengal"
    };
    
    public static String[] mContents = {"Game","Profile","Wallet","Settings","Share","Feedback","FAQ","LogOut"};
    
}
