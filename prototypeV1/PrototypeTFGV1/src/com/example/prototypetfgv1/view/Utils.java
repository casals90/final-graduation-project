package com.example.prototypetfgv1.view;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class Utils {
	
	/* Function that convert bitmap to byte array */
	public static byte[] bitmapToByteArray(Bitmap b) {
		// Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
	}
	
	/* Function that convert byte array to bitmap */
	public static Bitmap byteArrayToBitmap(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0,data.length);
	}
	
	/* returns the bytesize of the give bitmap */
	public static int byteSizeOf(Bitmap bitmap) {
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	        return bitmap.getAllocationByteCount();
	    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
	        return bitmap.getByteCount();
	    } else {
	        return bitmap.getRowBytes() * bitmap.getHeight();
	    }
	}
	
	/* function that create a date */
	public static String getNowDate() {
		Date date = new Date(new Date().getTime());
		return String.valueOf(date);
	}
	
	/* Convert date to string */
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
}
