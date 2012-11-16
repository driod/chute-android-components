package com.chute.android.imagesharer.intent;

import com.chute.android.imagesharer.app.ShareActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ShareActivityIntentWrapper {

	public static final String TAG = ShareActivityIntentWrapper.class
			.getSimpleName();

	private static final String EXTRA_ALBUM_SHORTCUT = "extra_album_shortcut";
	private static final String EXTRA_ASSET_SHARE_URL = "extra_asset_share_url";
	private static final String EXTRA_ALBUM_NAME = "key_album_name";
	private static final String EXTRA_ALBUM_ID = "albumId";

	private final Intent intent;

	public ShareActivityIntentWrapper(Intent intent) {
		super();
		this.intent = intent;
	}

	public ShareActivityIntentWrapper(Context packageContext, Class<?> cls) {
		super();
		intent = new Intent(packageContext, cls);
	}

	public ShareActivityIntentWrapper(Context packageContext) {
		super();
		intent = new Intent(packageContext, ShareActivity.class);
	}

	public void setAlbumId(String id) {
		intent.putExtra(EXTRA_ALBUM_ID, id);
	}

	public Intent getIntent() {
		return intent;
	}

	public String getAlbumId() {
		return intent.getExtras().getString(EXTRA_ALBUM_ID);
	}

	public void setAlbumName(String name) {
		intent.putExtra(EXTRA_ALBUM_NAME, name);
	}

	public String getAlbumName() {
		return intent.getExtras().getString(EXTRA_ALBUM_NAME);
	}

	public void setAlbumShortcut(String shortcut) {
		intent.putExtra(EXTRA_ALBUM_SHORTCUT, shortcut);
	}

	public String getAlbumShortcut() {
		return intent.getExtras().getString(EXTRA_ALBUM_SHORTCUT);
	}

	public void setAssetShareUrl(String url) {
		intent.putExtra(EXTRA_ASSET_SHARE_URL, url);
	}

	public String getAssetShareUrl() {
		return intent.getExtras().getString(EXTRA_ASSET_SHARE_URL);
	}

	public void startActivity(Activity context) {
		context.startActivity(intent);
	}
}
