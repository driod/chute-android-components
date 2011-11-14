package com.chute.android.comments.util.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.comments.app.CommentsActivity;

public class CommentsActivityIntentWrapper {
    private static final String EXTRA_KEY_CHUTE_ID = "chuteId";
    private static final String EXTRA_KEY_CHUTE_NAME = "chuteName";
    private static final String EXTRA_KEY_ASSET_ID = "assetId";

    @SuppressWarnings("unused")
    private static final String TAG = CommentsActivityIntentWrapper.class.getSimpleName();

    private final Intent intent;

    public CommentsActivityIntentWrapper(Intent intent) {
	super();
	this.intent = intent;
    }

    public CommentsActivityIntentWrapper(Context packageContext, Class<?> cls) {
	super();
	intent = new Intent(packageContext, cls);
    }

    public CommentsActivityIntentWrapper(Context packageContext) {
	super();
	intent = new Intent(packageContext, CommentsActivity.class);
    }

    public void setChuteId(String id) {
	intent.putExtra(EXTRA_KEY_CHUTE_ID, id);
    }

    public Intent getIntent() {
	return intent;
    }

    public String getChuteId() {
	return intent.getExtras().getString(EXTRA_KEY_CHUTE_ID);
    }

    public void setAssetId(String id) {
	intent.putExtra(EXTRA_KEY_ASSET_ID, id);
    }

    public String getAssetId() {
	return intent.getExtras().getString(EXTRA_KEY_ASSET_ID);
    }

    public void setChuteName(String name) {
	intent.putExtra(EXTRA_KEY_CHUTE_NAME, name);
    }

    public String getChuteName() {
	return intent.getExtras().getString(EXTRA_KEY_CHUTE_NAME);
    }

    public void startActivityForResult(Activity context, int code) {
	context.startActivityForResult(intent, code);
    }
}
