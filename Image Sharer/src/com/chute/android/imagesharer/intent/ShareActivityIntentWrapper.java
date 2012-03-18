package com.chute.android.imagesharer.intent;

import com.chute.android.imagesharer.app.ShareActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ShareActivityIntentWrapper {

	public static final String TAG = ShareActivityIntentWrapper.class
			.getSimpleName();

	private static final String EXTRA_CHUTE_SHORTCUT = "extra_chute_shortcut";
	private static final String EXTRA_ASSET_SHARE_URL = "extra_asset_share_url";
	private static final String EXTRA_CHUTE_NAME = "key_chute_name";
	private static final String EXTRA_CHUTE_ID = "chuteId";

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

	public void setChuteId(String id) {
		intent.putExtra(EXTRA_CHUTE_ID, id);
	}

	public Intent getIntent() {
		return intent;
	}

	public String getChuteId() {
		return intent.getExtras().getString(EXTRA_CHUTE_ID);
	}

	public void setChuteName(String name) {
		intent.putExtra(EXTRA_CHUTE_NAME, name);
	}

	public String getChuteName() {
		return intent.getExtras().getString(EXTRA_CHUTE_NAME);
	}

	public void setChuteShortcut(String shortcut) {
		intent.putExtra(EXTRA_CHUTE_SHORTCUT, shortcut);
	}

	public String getChuteShortcut() {
		return intent.getExtras().getString(EXTRA_CHUTE_SHORTCUT);
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
