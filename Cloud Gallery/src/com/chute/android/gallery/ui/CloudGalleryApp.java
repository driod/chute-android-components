package com.chute.android.gallery.ui;

import android.app.Application;
import android.content.Context;

import com.chute.android.gallery.R;
import com.chute.sdk.model.GCAccountStore;
import com.chute.sdk.utils.GCUtils;
import com.darko.imagedownloader.ImageLoader;

public class CloudGalleryApp extends Application {

    @SuppressWarnings("unused")
    private static final String TAG = CloudGalleryApp.class.getSimpleName();

    private static ImageLoader createImageLoader(Context context) {
	final ImageLoader imageLoader = new ImageLoader(context, R.drawable.placeholder_image_small);
	imageLoader.setDefaultBitmapSize(GCUtils.pixelsFromDp(context, 75));
	return imageLoader;
    }

    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
	super.onCreate();
	GCAccountStore.setAppId(getApplicationContext(), "4f15d1f138ecef6af9000004");
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
