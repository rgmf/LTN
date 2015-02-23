package es.rgmf.ltn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.inputmethod.InputMethodManager;

public class Utilities {
	/**
	 * Hide the software keyboard in Android.
	 * 
	 * @param activity The Activity where the method is calling.
	 */
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	/**
	 * Copy source to dest.
	 * 
	 * @param source the file to copy.
	 * @param dest the destination folder where the file will be copied.
	 * @throws IOException
	 * @return the file copied.
	 */
	public static String copyFileUsingFileChannels(File source, File dest)
			throws IOException {
		/*
		InputStream in = new FileInputStream(source);

        OutputStream out = new FileOutputStream(dest);

        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        */
		FileChannel inputChannel = null;
	    FileChannel outputChannel = null;
	    String nameFileCopied;
	    try {
	        inputChannel = new FileInputStream(source).getChannel();
	        nameFileCopied = dest + "/" + source.getName();
	        outputChannel = new FileOutputStream(nameFileCopied).getChannel();
	        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
	    } finally {
	        inputChannel.close();
	        outputChannel.close();
	    }
	    
	    return nameFileCopied;
	}
	
	/**
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}

	/**
	 * It loads an image and return the bitmap effciently (see {@link https://developer.android.com/training/displaying-bitmaps/load-bitmap.html}.
	 * 
	 * @param selectedImagePath
	 * @param i
	 * @param j
	 * @return
	 */
	public static Bitmap loadBitmapEfficiently(String imagePath, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inJustDecodeBounds = true;
    	//BitmapFactory.decodeResource(getResources(), R.id.student_photo_input, options);
    	BitmapFactory.decodeFile(imagePath, options);
    	
    	// Calculate inSampleSize
        options.inSampleSize = Utilities.calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap photoBitmap = BitmapFactory.decodeFile(imagePath, options);
        
        return photoBitmap;
	}
	
	/**
	 * Get Drawable by name.
	 * 
	 * @param context The context of the application.
	 * @param name The name of the Drawable.
	 * @return the Drawable.
	 */
	public static Drawable getDrawable(Context context, String name) {
		Resources resources = context.getResources();
		final int resourceId = resources.getIdentifier(name, "drawable", 
		   context.getPackageName());
		return resources.getDrawable(resourceId);
	}
}
