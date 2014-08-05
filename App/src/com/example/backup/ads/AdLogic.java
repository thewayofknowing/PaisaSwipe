package com.example.backup.ads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.example.backup.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.backup.constants.Constants;
import com.example.backup.data.Advertisement;
import com.example.backup.db.DataBaseHelper;

public class AdLogic implements Constants {

	private static Bitmap bitmap = null;;
	private static AdLogic s_adLogic = null;
	private static Context s_context;
	private static DataBaseHelper db;
	private static List<Advertisement> s_adList;
	private static Advertisement[] sortList;
	private static int s_totalImpressions = 0;
	private static int s_currentIndex;
	private static String s_directory;
	private static SharedPreferences s_sharedPref;
	private static Editor s_editor;
	
	public static AdLogic getInstance(Context context) {
		if (s_adLogic == null) {
			s_context = context;
			s_adLogic = new AdLogic();
			db = new DataBaseHelper(context);
			s_directory = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/";
			s_sharedPref = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
			s_editor = s_sharedPref.edit();
			s_currentIndex = s_sharedPref.getInt(IMPRESSION_CURRENT_INDEX,0);
			runCirculationAlgo();
			String order = "";
			for(int i=0;i<sortList.length;i++) {
				order += sortList[i].getId() + ":";
			}
			Log.d(TAG,order);
		}
		return s_adLogic;		
	}
	
	public class CustomComparator implements Comparator<Advertisement> {
	    @Override
	    public int compare(Advertisement a1, Advertisement a2) {
            return a1.getImpressionCount() > a2.getImpressionCount() ? 1 : (a1.getImpressionCount() < a2.getImpressionCount() ? -1 : 0);
	    }
	}
	
	public static class PairInt {
		public int first;
		public int second;
		public static PairInt make_pair(int start,int end) {
			PairInt element = new PairInt();
			element.first = start;
			element.second = end;
			return element;
		}
	}
	
	static int findstart(Advertisement[] ads,int pos) {
		for(int i=pos;i<ads.length;i++) {
			if(ads[i] == null)
				return i;
		}
		return -1;
	}
	
	static int findend(Advertisement[] ads,int pos) {
		for(int i=pos-1;i>=0;i--) {
			if(ads[i] == null)
				return i;
		}
		return -1;
	}
	
	public static void runCirculationAlgo() {
		 s_adList = new ArrayList<Advertisement>();
		 s_adList.addAll(db.getAllAds());
	     for(Advertisement element: s_adList) {
	    	 s_totalImpressions += element.getImpressionCount();
	     }
	     sortList = new Advertisement[s_totalImpressions];
	     for(int i=0;i<sortList.length;i++) {
	    	 sortList[i] = null;
	     }
	     Collections.sort(s_adList, s_adLogic.new CustomComparator());
	     Queue<PairInt> q = new LinkedList<PairInt>();
	     
	     for(int i=0;i<s_adList.size();i++) {
	    	q.clear();
	 		int start = findstart(sortList, 0);
	 		int end = findend(sortList, sortList.length);
	 		Advertisement currentAd = s_adList.get(i);
	 		sortList[start] = currentAd;
	 		sortList[end] = currentAd;
	 		q.add(PairInt.make_pair(start, end));
	 		int count = currentAd.getImpressionCount() - 2;
	 		while(count>0) {
	 			PairInt tuple = q.element();
	 			int mid = (tuple.first + tuple.second)/2;
	 			if(sortList[mid]!=null) {
	 				int oldmid = mid;
	 				mid = findstart(sortList,mid);
	 			    if(mid == -1) mid = findend(sortList,oldmid);
	 			}
	 			sortList[mid] = currentAd;
	 			q.remove();
	 			q.add(PairInt.make_pair(tuple.first,mid));
	 			q.add(PairInt.make_pair(mid,tuple.second));
	 			count--;
	 		} 		
	 	}   
	}
	
	public Advertisement getAdvertisement() {
		
		/*
		 DisplayMetrics metrics = new DisplayMetrics();
	     context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	     int dstWidth = (int)( metrics.widthPixels * metrics.density);
	     int dstHeight =(int)( metrics.heightPixels * metrics.density);
	     */
	    Advertisement result = new Advertisement();
	     if(s_currentIndex<sortList.length) {
	    	 try {
		    	File file = new File(s_directory + sortList[s_currentIndex].getImageName());
				bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
	    		sortList[s_currentIndex].setImage(bitmap);
	    		s_editor.putInt(IMPRESSION_CURRENT_INDEX, s_currentIndex+1);
				return sortList[s_currentIndex++];
			} catch (FileNotFoundException e) {
				 bitmap = BitmapFactory.decodeResource(s_context.getResources(), R.drawable.poster);
				 e.printStackTrace();
			}
	     }
	     else {
	    	 result = new Advertisement(-1, 0, "", -1, "", 'a', BitmapFactory.decodeResource(s_context.getResources(), R.drawable.poster));
	    	 return result;
	     }
	     return result;
	}
}
