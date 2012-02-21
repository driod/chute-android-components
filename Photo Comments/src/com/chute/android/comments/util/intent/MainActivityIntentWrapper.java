package com.chute.android.comments.util.intent;

import android.content.Context;
import android.content.Intent;

import com.chute.android.comments.app.PhotoCommentsActivity;

public class MainActivityIntentWrapper {
    private static final String EXTRA_NEW_COMMENTS_ADDED_COUNT = "extraNewCommentsAdded";

    @SuppressWarnings("unused")
    private static final String TAG = MainActivityIntentWrapper.class.getSimpleName();

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
	intent = new Intent(packageContext, PhotoCommentsActivity.class);
    }

    public Intent getIntent() {
	return intent;
    }

    public void setExtraComments(int newComments) {
	intent.putExtra(EXTRA_NEW_COMMENTS_ADDED_COUNT, newComments);
    }

    public int getExtraComments() {
	return intent.getExtras().getInt(EXTRA_NEW_COMMENTS_ADDED_COUNT);
    }
}
