package com.chute.android.socialgallery.app;

import android.app.Application;
import android.content.Context;

import com.chute.android.socialgallery.R;
import com.chute.sdk.v2.utils.Utils;

import darko.imagedownloader.ImageLoader;

public class SocialGalleryApp extends Application {

	public static final String TAG = SocialGalleryApp.class.getSimpleName();

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
