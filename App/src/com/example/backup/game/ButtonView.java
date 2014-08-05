package com.example.backup.game;

import com.example.backup.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ButtonView extends FrameLayout
{
	private View contentView;
	private Context context;
	public RelativeLayout btnMenu;
	public static TextView timer;
	
	public ButtonView(final PuzzleActivity context, View contentView)
	{
		super(context);
		this.context = context;
		this.contentView = contentView;
		contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(contentView);
		
		RelativeLayout overlay = new RelativeLayout(context);
		overlay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(overlay);
		
		LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		btnMenu = (RelativeLayout) inf.inflate(R.layout.game_bottom_bar, null);
		timer = (TextView) btnMenu.findViewById(R.id.time);
		btnMenu.setClickable(true);
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		//layoutParams.setMargins(0, 1, 0, 2);
		btnMenu.setLayoutParams(layoutParams);
		((ImageView) btnMenu.findViewById(R.id.preview_button)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,Preview.class);
				context.startActivity(intent);
			}
				
		});

		overlay.addView(btnMenu);
		context.startService(new Intent(context,Timer.class));
		
	}
	
	public static class Timer extends Service {

		public static int i;
		private static Handler myHandler;
		private static Runnable r;
	    private LocalBroadcastManager broadcaster;
	    static final public String SWIPENAD = "SwipeNad";   

	    public Timer() {
	    	super();
		}
	    
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
		
		@Override
		public void onCreate() {
			i = 0;
			myHandler = new Handler();
		    broadcaster = LocalBroadcastManager.getInstance(this);
			super.onCreate();
		}
		
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			r = new Runnable() {
				
				@Override
				public void run() {
					String minutes = (i/60) + "";
					if (minutes.length() == 1) {
						minutes = "0" + minutes;
					}
					String seconds = (i%60) + "";
					if (seconds.length() == 1) {
						seconds = "0" + seconds;
					}
					sendResult(minutes + ":" + seconds);
					i++;
					//timer.setText((i/60) + ":" + (i%60));
					myHandler.postDelayed(r, 1000);
				}
			};
			myHandler.post(r);
			return super.onStartCommand(intent, flags, startId);
		}
		
		public static void stopTimer() {
			myHandler.removeCallbacks(r);
		}
		
		public static void startTimer() {
			myHandler.post(r);
		}
		
		public void sendResult(String message) {
			Intent intent = new Intent(SWIPENAD);
		    if(message != null)
		        intent.putExtra("time", message);
		    broadcaster.sendBroadcast(intent);
		}
		
		@Override
		public void onDestroy() {
			myHandler.removeCallbacks(r);
			myHandler = null;
			super.onDestroy();
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int canvasWidth = MeasureSpec.getSize(widthMeasureSpec);
		int canvasHeight = (int) (MeasureSpec.getSize(heightMeasureSpec)/12) ;
		btnMenu.getLayoutParams().height = canvasHeight;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		btnMenu.getLayoutParams().width = display.getWidth();
		btnMenu.requestLayout();
		
	}

	public View getContentView()
	{
		return contentView;
	}
}
