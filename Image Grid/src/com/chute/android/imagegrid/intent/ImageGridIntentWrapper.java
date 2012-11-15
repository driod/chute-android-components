package com.chute.android.imagegrid.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.imagegrid.app.ImageGridActivity;

public class ImageGridIntentWrapper {

	@SuppressWarnings("unused")
	private static final String TAG = ImageGridIntentWrapper.class
			.getSimpleName();

	private static final String KEY_ALBUM_ID = "keyAlbumId";

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

	public void setAlbumId(String id) {
		intent.putExtra(KEY_ALBUM_ID, id);
	}

	public Intent getIntent() {
		return intent;
	}

	public String getAlbumId() {
		return intent.getExtras().getString(KEY_ALBUM_ID);
	}

	public void startActivity(Activity context) {
		context.startActivity(intent);
	}
}
