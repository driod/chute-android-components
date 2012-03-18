package com.chute.android.imagesharer.util;

import com.chute.android.imagesharer.R;

import android.content.Context;
import android.content.Intent;


public class IntentUtil {
    @SuppressWarnings("unused")
    private static final String TAG = IntentUtil.class.getSimpleName();

    private IntentUtil() {
    }

    public static void sendEmail(Context context, String to, String subject, String body) {
	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	emailIntent.putExtra(Intent.EXTRA_TEXT, body);
	emailIntent.setType("plain/text");
	context.startActivity(Intent.createChooser(emailIntent,
		context.getString(R.string.share_email_intent_chooser_title)));

    }
}
