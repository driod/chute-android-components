package com.chute.android.socialgallery.util.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.socialgallery.app.SocialGalleryActivity;

public class SocialGalleryActivityIntentWrapper {

	public static final String TAG = SocialGalleryActivityIntentWrapper.class
			.getSimpleName();

	private static final String EXTRA_KEY_CHUTE_ID = "chuteId";
	private static final String EXTRA_KEY_CHUTE_NAME = "chuteName";
	private static final String EXTRA_KEY_CHUTE_SHORTCUT = "chuteShortcut";

	private final Intent intent;

	public SocialGalleryActivityIntentWrapper(Intent intent) {
		super();
		this.intent = intent;
	}

	public SocialGalleryActivityIntentWrapper(Context packageContext,
			Class<?> cls) {
		super();
		intent = new Intent(packageContext, cls);
	}

	public SocialGalleryActivityIntentWrapper(Context packageContext) {
		super();
		intent = new Intent(packageContext, SocialGalleryActivity.class);
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

	public void setChuteName(String name) {
		intent.putExtra(EXTRA_KEY_CHUTE_NAME, name);
	}

	public String getChuteName() {
		return intent.getExtras().getString(EXTRA_KEY_CHUTE_NAME);
	}

	public void setChuteShortcut(String shortcut) {
		intent.putExtra(EXTRA_KEY_CHUTE_SHORTCUT, shortcut);
	}

	public String getChuteShortcut() {
		return intent.getExtras().getString(EXTRA_KEY_CHUTE_SHORTCUT);
	}

	public void startActivity(Activity context) {
		context.startActivity(intent);
	}
}
