package com.chute.android.imagegrid.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.imagegrid.app.ImageGridActivity;

public class ImageGridIntentWrapper {

    @SuppressWarnings("unused")
    private static final String TAG = ImageGridIntentWrapper.class.getSimpleName();

    private static final String KEY_CHUTE_ID = "keyChuteId";

    private final Intent intent;

    public ImageGridIntentWrapper(Intent intent) {
	super();
	this.intent = intent;
    }

    public ImageGridIntentWrapper(Context packageContext, Class<?> cls) {
	super();
	intent = new Intent(packageContext, cls);
    }

    public ImageGridIntentWrapper(Context packageContext) {
	super();
	intent = new Intent(packageContext, ImageGridActivity.class);
    }

    public void setChuteID(String id) {
	intent.putExtra(KEY_CHUTE_ID, id);
    }

    public Intent getIntent() {
	return intent;
    }

    public String getChuteId() {
	return intent.getExtras().getString(KEY_CHUTE_ID);
    }

    public void startActivity(Activity context) {
	context.startActivity(intent);
    }
}
