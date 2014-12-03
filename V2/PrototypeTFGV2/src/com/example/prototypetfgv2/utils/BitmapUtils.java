package com.example.prototypetfgv2.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.DisplayMetrics;

public class BitmapUtils {
	
	public static Bitmap decodeFileForDisplay(File f,Activity activity){
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);
	        DisplayMetrics metrics =  activity.getResources().getDisplayMetrics();

	        int scaleW =  o.outWidth / metrics.widthPixels;
	        int scaleH =  o.outHeight / metrics.heightPixels;
	        int scale = Math.max(scaleW,scaleH);
	      
	        //Find the correct scale value. It should be the power of 2.
	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize=scale;
	        Bitmap scaledPhoto = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	        try {
	            ExifInterface exif = new ExifInterface(f.getAbsolutePath());
	            int rotation = exifToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL));
	            if (rotation > 0)
	                scaledPhoto = convertBitmapToCorrectOrientation(scaledPhoto, rotation);

	        } catch (IOException e1) {
	            e1.printStackTrace();
	        }
	        return scaledPhoto;
	    } catch (FileNotFoundException e) {}
	    	return null;
	}

	public static Bitmap convertBitmapToCorrectOrientation(Bitmap photo,int rotation) {
	    int width = photo.getWidth();
	    int height = photo.getHeight();
	    Matrix matrix = new Matrix();
	    matrix.preRotate(rotation);
	    return Bitmap.createBitmap(photo,0,0, width, height, matrix, false);
	}
	
	public static int exifToDegrees(int exifOrientation) {        
	    if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)  
	    	return 90; 
	    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) 
	    	return 180; 
	    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
	    	return 270;            
	    return 0;    
	}
	
	/*public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
	    int targetWidth = 50;
	    int targetHeight = 50;
	    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
	                        targetHeight,Bitmap.Config.RGB_565);

	    Canvas canvas = new Canvas(targetBitmap);
	    Path path = new Path();
	    path.addCircle(((float) targetWidth - 1) / 2,
	        ((float) targetHeight - 1) / 2,
	        (Math.min(((float) targetWidth), 
	        ((float) targetHeight)) / 2),
	        Path.Direction.CCW);

	    canvas.clipPath(path);
	    Bitmap sourceBitmap = scaleBitmapImage;
	    canvas.drawBitmap(sourceBitmap, 
	        new Rect(0, 0, sourceBitmap.getWidth(),
	        sourceBitmap.getHeight()), 
	        new Rect(0, 0, targetWidth, targetHeight), null);
	    return targetBitmap;
	}*/
}
