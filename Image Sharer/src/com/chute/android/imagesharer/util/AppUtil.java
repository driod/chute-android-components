package com.chute.android.imagesharer.util;


public class AppUtil {
    @SuppressWarnings("unused")
    private static final String TAG = AppUtil.class.getSimpleName();

    public static String generateShareURLfromCode(String code) {
	return Constants.SHARE_URL + code;
    }
}
