package com.example.prototypetfgv2.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.CurrentAlbum;
import com.example.prototypetfgv2.model.User;
import com.parse.ParseUser;

public class Utils {
	
	//Function that convert bitmap to byte array
	public static byte[] bitmapToByteArray(Bitmap b) {
		// Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        b.compress(Bitmap.CompressFormat.JPEG,25, stream);
        return stream.toByteArray();
	}
	
	//Function that convert byte array to bitmap 
	public static Bitmap byteArrayToBitmap(byte[] data) {
		Log.v("prototypev1","utils byteArraytobitmap"+data.length);
		return BitmapFactory.decodeByteArray(data,0,data.length);
	}
	
	//function that create a date 
	public static String getNowDate() {
		Date date = new Date(new Date().getTime());
		return String.valueOf(date);
	}
	
	//Convert date to string 
	public static String dateToString(Date date) {
		return String.valueOf(date);
	}
	
	public static boolean isUserUpdate(String updatedAt) {
		if(getNowDate().compareTo(updatedAt) == 0)
			return true;
		return false;
		
	}
	
	public void typeOfScreen(Activity activity) {
		//Determine screen size
	    if((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {     
	        Toast.makeText(activity, "Large screen",Toast.LENGTH_LONG).show();

	    }
	    else if((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {     
	        Toast.makeText(activity, "Normal sized screen" , Toast.LENGTH_LONG).show();

	    } 
	    else if((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {     
	        Toast.makeText(activity, "Small sized screen" , Toast.LENGTH_LONG).show();
	    }
	    else {
	        Toast.makeText(activity, "Screen size is neither large, normal or small" , Toast.LENGTH_LONG).show();
	    }	
	    //Determine density
	    DisplayMetrics metrics = new DisplayMetrics();
	    activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;
        if(density==DisplayMetrics.DENSITY_HIGH) {
            Toast.makeText(activity, "DENSITY_HIGH... Density is " + String.valueOf(density),  Toast.LENGTH_LONG).show();
        }
        else if(density==DisplayMetrics.DENSITY_MEDIUM) {
            Toast.makeText(activity, "DENSITY_MEDIUM... Density is " + String.valueOf(density),  Toast.LENGTH_LONG).show();
        }
        else if(density==DisplayMetrics.DENSITY_LOW) {
            Toast.makeText(activity, "DENSITY_LOW... Density is " + String.valueOf(density),  Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(activity, "Density is neither HIGH, MEDIUM OR LOW.  Density is " + String.valueOf(density),  Toast.LENGTH_LONG).show();
        }
	}
	
	public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	
	public static void cleanBackStack(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	}
	
	public static JSONArray removeElementToJsonArray(JSONArray array,String id) {
		int position = getElementPosition(array, id);
		
		ArrayList<String> list = new ArrayList<String>();     
		int len = array.length();
		
		for (int i=0;i<len;i++){ 
			list.add(array.optString(i));
		} 
		//Remove the element from arraylist
		list.remove(position);
		//Recreate JSON Array
		return new JSONArray(list);
	}
	
	public static int getElementPosition(JSONArray array,String id) {
		for(int i = 0; i < array.length(); i++) {
			if(id.compareTo(array.optString(i)) == 0)
				return i;	
		}
		return -1;
	}
	
	public static JSONArray arrayListStringToJsonArray(ArrayList<String> array) {
		JSONArray jArray = new JSONArray();
		for(int i = 0; i < array.size(); i++) {
			jArray.put(array.get(i));
		}
		return jArray;
	}
	
	public static boolean isElementExist(JSONArray array,String id) {
		for(int i = 0; i < array.length(); i++) {
			try {
				if(id.compareTo(array.getString(i)) == 0)
					return true;
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public static String[] arrayListStringToStringArray(ArrayList<String> arr) {
		String[] newArray = new String[arr.size()];
		newArray = arr.toArray(newArray);
		return newArray;
	}
	
	public static ArrayList<String> arrayStringToArrayList(String[] arr) {
		ArrayList<String> newArray = new ArrayList<String>( Arrays.asList(arr));
		return newArray;
	}
	
	public static boolean stringIsInArrayList(ArrayList<String> arr,String s) {
		for(int i = 0; i < arr.size(); i++) {
			//Log.v("prototypev1","array values "+arr.get(i));
			if(arr.get(i).compareTo(s) == 0)
				return true;
		}
		return false;
	}
	
	public static ArrayList<String> jsonArrayToArrayListString(JSONArray arr) {
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < arr.length(); i++) {
			try {
				list.add(arr.getString(i));
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		return list;
	}
	
	public static List<User> deleteUsersInList(List<User> users,ArrayList<String> ids) {
		for(int i = 0; i < ids.size();i ++) {
			for(User u : users) {
				if(u.getId().compareTo(ids.get(i))== 0)
					users.remove(u);
			}
		}
		return users;
	}
	
	public static List<String> jsonArrayToListString(JSONArray arr) {
		if(arr != null) {
			ArrayList<String> list = new ArrayList<String>();
			try {
				for(int i = 0; i < arr.length(); i++) {
					list.add(arr.getString(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}
		return null;
		
	}
	
	public static int getPositionCurrentAlbum(ArrayList<CurrentAlbum> albums) {
		JSONObject currentAlbum = ParseUser.getCurrentUser().getJSONObject("currentAlbum");
		if(currentAlbum != null) {
			try {
				String id = currentAlbum.getString("id");
				for(int i = 0; i < albums.size(); i++) {
					if(albums.get(i).getId().compareTo(id) == 0)
						return i;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return -1;
			}
		}
		return -1;
	}
	
	public static int getRandomInt(int n) {
		Random r = new Random();
		return r.nextInt(n);
	}
	
	/*
     * getting screen width
     
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
 
        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }*/
	
	public static ArrayList<Integer> getMetrics(Activity activity) {
		DisplayMetrics metrics =  activity.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		ArrayList<Integer> size = new ArrayList<Integer>();
		size.add(width);
		size.add(height);
		return size;
	}
	
	public static int monthNameToInt(String month) {
		MonthName monthName = new MonthName();
		return monthName.getMonthNumber(month);
	}
	
	public static String newDateFormatFromCreatedAt(String createdAt) {
		String[] separated = createdAt.split(" ");
		//Month number
		int m = Utils.monthNameToInt(separated[1]);
		return separated[2]+"/"+m+"/"+separated[5]+" "+separated[3];
	}
	
	public static String substracDates(String current,String createdAt) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
		Date currentDate = null;
		Date createdAtDate = null;
		try {
			currentDate = simpleDateFormat.parse(current);
		} catch (ParseException e) {
			//Log.v("prototypev1","peta date current");
			e.printStackTrace();
		}
		try {
			createdAtDate = simpleDateFormat.parse(createdAt);
		} catch (ParseException e) {
			//Log.v("prototypev1","peta date createdAt");
			e.printStackTrace();
		}
		long different = currentDate.getTime() - createdAtDate.getTime();
		//Log.v("prototypev1","---------------------------------- ");
		//Log.v("prototypev1","diff "+different);
		long seconds = different / 1000;
		if(seconds >= 60 ) {
			//Log.v("prototypev1","seconds "+seconds);
			long minutes = seconds / 60;
			if(minutes >= 60 ) {
				//Log.v("prototypev1","minutes "+minutes);
				long hours = minutes / 60;
				if(hours >= 24) {
					//Log.v("prototypev1","hours "+hours);
					long days = hours / 24;
					//Log.v("prototypev1","days "+days);
					//Log.v("prototypev1","return days");
					return days+":d";
				}
				else {
					//Log.v("prototypev1","return hours");
					return hours+":h";
				}
			}
			else {
				//Log.v("prototypev1","return minutes");
				return minutes+":m";
			}
		}
		//Return seconds
		else {
			//Log.v("prototypev1","return seconds");
			return seconds+":s";
		}	
	}
	
	public static ArrayList<String> getAlbumsId(ArrayList<Album> albums) {
		ArrayList<String> ids = new ArrayList<String>();
		for(int i = 0; i < albums.size(); i++) {
			ids.add(albums.get(i).getId());
		}
		return ids;
	}
}

	
	
	
	
	
	
	