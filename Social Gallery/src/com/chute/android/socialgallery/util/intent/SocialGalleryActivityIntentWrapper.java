package com.chute.android.socialgallery.util.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.socialgallery.app.SocialGalleryActivity;

public class SocialGalleryActivityIntentWrapper {

	public static final String TAG = SocialGalleryActivityIntentWrapper.class
			.getSimpleName();

	private static final String EXTRA_KEY_ALBUM_ID = "albumId";
	private static final String EXTRA_KEY_ALBUM_NAME = "albumName";
	private static final String EXTRA_KEY_ALBUM_SHORTCUT = "albumShortcut";

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

	public void setAlbumId(String id) {
		intent.putExtra(EXTRA_KEY_ALBUM_ID, id);
	}

	public Intent getIntent() {
		return intent;
	}

	public String getAlbumId() {
		return intent.getExtras().getString(EXTRA_KEY_ALBUM_ID);
	}

	public void setAlbumName(String name) {
		intent.putExtra(EXTRA_KEY_ALBUM_NAME, name);
	}

	public String getAlbumName() {
		return intent.getExtras().getString(EXTRA_KEY_ALBUM_NAME);
	}

	public void setAlbumShortcut(String shortcut) {
		intent.putExtra(EXTRA_KEY_ALBUM_SHORTCUT, shortcut);
	}

	public String getAlbumShortcut() {
		return intent.getExtras().getString(EXTRA_KEY_ALBUM_SHORTCUT);
	}

	public void startActivity(Activity context) {
		context.startActivity(intent);
	}
}
