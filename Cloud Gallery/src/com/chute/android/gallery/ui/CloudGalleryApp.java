package com.chute.android.gallery.ui;

import android.app.Application;
import android.content.Context;

import com.chute.android.gallery.R;
import com.chute.sdk.v2.model.AccountStore;
import com.chute.sdk.v2.utils.Utils;

import darko.imagedownloader.ImageLoader;

public class CloudGalleryApp extends Application {

	@SuppressWarnings("unused")
	private static final String TAG = CloudGalleryApp.class.getSimpleName();

	private static ImageLoader createImageLoader(Context context) {
		final ImageLoader imageLoader = new ImageLoader(context,
				R.drawable.placeholder_image_small);
		imageLoader.setDefaultBitmapSize(Utils.pixelsFromDp(context, 75));
		return imageLoader;
	}

	private ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		AccountStore.setAppId(getApplicationContext(),
				"46b7c778447e18ee5865a83f4202f42a2f85283c47ef24541366509235d8eccf");
		mImageLoader = createImageLoader(getApplicationContext());
	}

	@Override
	public Object getSystemService(String name) {
		if (ImageLoader.IMAGE_LOADER_SERVICE.equals(name)) {
			return mImageLoader;
		}
		return super.getSystemService(name);
	}
}
