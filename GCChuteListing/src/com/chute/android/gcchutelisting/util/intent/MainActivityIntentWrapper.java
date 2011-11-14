package com.chute.android.gcchutelisting.util.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.gcchutelisting.app.GCChuteListingActivity;

public class MainActivityIntentWrapper {

    @SuppressWarnings("unused")
    private static final String TAG = MainActivityIntentWrapper.class.getSimpleName();
    private static final String EXTRA_ITEM_CLICKED = "extra_item_clicked";

    private final Intent intent;

    public MainActivityIntentWrapper(Intent intent) {
	super();
	this.intent = intent;
    }

    public MainActivityIntentWrapper(Context packageContext, Class<?> cls) {
	super();
	intent = new Intent(packageContext, cls);
    }

    public MainActivityIntentWrapper(Context packageContext) {
	super();
	intent = new Intent(packageContext, GCChuteListingActivity.class);
    }

    public Intent getIntent() {
	return intent;
    }

    public void setExtraItemClicked(boolean itemClicked) {
	intent.putExtra(EXTRA_ITEM_CLICKED, itemClicked);
    }

    public Boolean getExtraItemClicked() {
	return (Boolean) intent.getExtras().get(EXTRA_ITEM_CLICKED);
    }

    public void startActivity(Activity context) {
	context.startActivity(intent);
    }
}
