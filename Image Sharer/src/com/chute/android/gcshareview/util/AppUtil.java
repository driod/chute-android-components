package com.chute.android.gcshareview.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.Log;

import com.chute.sdk.utils.GCConstants;
import com.chute.sdk.utils.GCUtils;
import com.chute.sdk.utils.MD5;

public class AppUtil {
    @SuppressWarnings("unused")
    private static final String TAG = AppUtil.class.getSimpleName();

    public static Typeface getTypefaceAllerRegular(Context context) {
	return Typeface.createFromAsset(context.getAssets(), "fonts/aller_rg-webfont.ttf");
    }

    public static Typeface getTypefaceAllerIttalic(Context context) {
	return Typeface.createFromAsset(context.getAssets(), "fonts/aller_lt-webfont.ttf");
    }

    public static Typeface getTypefaceHelveticaNeue(Context context) {
	return Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue-Medium.otf");
    }

    public static String getMobileSmallUrl(String urlNormal) {
	return GCUtils.getCustomSizePhotoURL(urlNormal, 960, 640);
    }

    public static String getThumbSmallUrl(String urlNormal) {
	return GCUtils.getCustomSizePhotoURL(urlNormal, 100, 100);
    }

    /*
     * isOnline - Check if there is a NetworkConnection
     * @return boolean
     */
    public static boolean isOnline(Context context) {
	ConnectivityManager cm = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	if (cm.getActiveNetworkInfo() != null) {
	    return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	} else {
	    return false;
	}
    }

    public static boolean isWifiConnected(Context context) {
	ConnectivityManager cm = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	if (cm.getActiveNetworkInfo() != null) {
	    NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

	    if (mWifi.isConnected()) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Gets the path.
     * 
     * @param context
     *            the context
     * @param uri
     *            the uri
     * @return the path
     */
    public static String getPath(Context context, Uri uri) throws NullPointerException {
	final String[] projection = { MediaColumns.DATA };
	final Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
	final int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	cursor.moveToFirst();
	return cursor.getString(column_index);
    }

    /**
     * Gets the temp file.
     * 
     * @param context
     *            the context
     * @return the temp file
     */
    public static File getTempFile(Context context) {
	final File path = getAppCacheDir(context);
	if (!path.exists()) {
	    path.mkdirs();
	}
	File f = new File(path, "temp_image.jpg");
	if (f.exists() == false) {
	    try {
		f.createNewFile();
	    } catch (IOException e) {
		Log.w(TAG, e.getMessage(), e);
	    }
	}
	return f;
    }

    public static boolean hasImageCaptureBug() {
	// list of known devices that have the bug
	ArrayList<String> devices = new ArrayList<String>();
	devices.add("android-devphone1/dream_devphone/dream");
	devices.add("generic/sdk/generic");
	devices.add("vodafone/vfpioneer/sapphire");
	devices.add("tmobile/kila/dream");
	devices.add("verizon/voles/sholes");
	devices.add("google_ion/google_ion/sapphire");
	devices.add("SEMC/X10i_1232-9897/X10i");

	return devices.contains(android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
		+ android.os.Build.DEVICE);
    }

    public static boolean isEmailValid(String email) {
	String regExpn = ".+@.+\\.[a-z]+";

	CharSequence inputStr = email;

	Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
	Matcher matcher = pattern.matcher(inputStr);

	if (matcher.matches()) {
	    return true;
	} else {
	    return false;
	}
    }

    private static String SDCARD_FOLDER_CACHE = Environment.getExternalStorageDirectory()
	    + "/Android/data/%s/files/";
    private static String SDCARD_FOLDER_CACHE_LARGE_PHOTOS = SDCARD_FOLDER_CACHE + "LargePhotos/";

    public static File getAppCacheDir(Context context) {
	return new File(String.format(SDCARD_FOLDER_CACHE, context.getPackageName()));
    }

    public static File getAppLargePhotoCache(Context context) {
	File file = new File(String.format(SDCARD_FOLDER_CACHE_LARGE_PHOTOS,
		context.getPackageName()));
	if (!file.exists()) {
	    file.mkdirs();
	}
	return file;
    }

    public static File getLargePhotoStoreFile(Context context, String url) throws Exception {
	return new File(getAppLargePhotoCache(context), "" + MD5.getStringMD5(url));
    }

    public static boolean checkIfExistsInLargePhotoCache(Context c, String fileUrl)
	    throws Exception {
	if (TextUtils.isEmpty(fileUrl)) {
	    return false;
	}
	if (GCConstants.DEBUG) {
	    Log.e(TAG, fileUrl);
	}
	return getLargePhotoStoreFile(c, fileUrl).exists();
    }

    public static String generateShareURLfromCode(String code) {
	return Constants.SHARED_ROLL_BASE_SHORTCUT_URL + code;
    }
}
