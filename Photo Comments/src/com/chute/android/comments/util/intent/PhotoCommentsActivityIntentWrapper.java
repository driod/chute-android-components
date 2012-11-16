package com.chute.android.comments.util.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.comments.app.PhotoCommentsActivity;

public class PhotoCommentsActivityIntentWrapper {
	private static final String EXTRA_KEY_ALBUM_ID = "albumId";
	private static final String EXTRA_KEY_ASSET_ID = "assetId";
	private static final String EXTRA_KEY_ALBUM_NAME = "albumName";

	@SuppressWarnings("unused")
	private static final String TAG = PhotoCommentsActivityIntentWrapper.class
			.getSimpleName();

	private final Intent intent;

	public PhotoCommentsActivityIntentWrapper(Intent intent) {
		super();
		this.intent = intent;
	}

	public PhotoCommentsActivityIntentWrapper(Context packageContext,
			Class<?> cls) {
		super();
		intent = new Intent(packageContext, cls);
	}

	public PhotoCommentsActivityIntentWrapper(Context packageContext) {
		super();
		intent = new Intent(packageContext, PhotoCommentsActivity.class);
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

	public void setAssetId(String id) {
		intent.putExtra(EXTRA_KEY_ASSET_ID, id);
	}

	public String getAssetId() {
		return intent.getExtras().getString(EXTRA_KEY_ASSET_ID);
	}

	public void setAlbumName(String name) {
		intent.putExtra(EXTRA_KEY_ALBUM_NAME, name);
	}

	public String getAlbumName() {
		return intent.getExtras().getString(EXTRA_KEY_ALBUM_NAME);
	}

	public void startActivityForResult(Activity context, int code) {
		context.startActivityForResult(intent, code);
	}
}
