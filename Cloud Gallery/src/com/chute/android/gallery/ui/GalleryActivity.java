package com.chute.android.gallery.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.chute.android.gallery.R;
import com.chute.android.gallery.components.GalleryViewFlipper;
import com.chute.sdk.v2.api.asset.GCAssets;
import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.requests.ListResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class GalleryActivity extends Activity {

	protected static final String TAG = GalleryActivity.class.getSimpleName();
	private GalleryViewFlipper galleryView;
	@SuppressWarnings("unused")
	private final String albumId = "1946";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_activity);
		galleryView = (GalleryViewFlipper) findViewById(R.id.galleryView);
		GCAssets.all(getApplicationContext(),
				new HttpCallback<ListResponseModel<AssetModel>>() {

					@Override
					public void onSuccess(
							ListResponseModel<AssetModel> responseData) {
						galleryView.setAssetCollection(responseData.getData());
					}

					@Override
					public void onHttpError(ResponseStatus responseCode) {
                      Log.d("debug", "http error = " + responseCode.getStatusMessage());
					}

				}).executeAsync();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		galleryView.destroyGallery();
	}

}
