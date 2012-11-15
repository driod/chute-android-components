package com.chute.android.multiimagepicker.app;

import android.app.Application;
import android.content.Context;
import android.util.TypedValue;

import com.chute.android.multiimagepicker.R;
import com.chute.sdk.v2.model.AccountStore;

import darko.imagedownloader.ImageLoader;

public class MultiImagePickerApp extends Application {

	@SuppressWarnings("unused")
	private static final String TAG = MultiImagePickerApp.class.getSimpleName();

	private static ImageLoader createImageLoader(Context context) {
		ImageLoader imageLoader = new ImageLoader(context,
				R.drawable.placeholder_image_small);
		imageLoader.setDefaultBitmapSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources()
						.getDisplayMetrics()));
		return imageLoader;
	}

	private ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		AccountStore
				.setAppId(getApplicationContext(),
						"46b7c778447e18ee5865a83f4202f42a2f85283c47ef24541366509235d8eccf");
		mImageLoader = createImageLoader(this);
	}

	@Override
	public Object getSystemService(String name) {
		if (ImageLoader.IMAGE_LOADER_SERVICE.equals(name)) {
			return mImageLoader;
		}
		return super.getSystemService(name);
	}

}
