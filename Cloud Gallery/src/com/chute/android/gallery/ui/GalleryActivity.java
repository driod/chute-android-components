package com.chute.android.gallery.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.chute.android.gallery.R;
import com.chute.android.gallery.components.GalleryViewFlipper;
import com.chute.sdk.v2.api.album.GCAlbums;
import com.chute.sdk.v2.api.asset.GCAssets;
import com.chute.sdk.v2.model.AlbumModel;
import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.requests.ListResponseModel;
import com.chute.sdk.v2.model.requests.ResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class GalleryActivity extends Activity {

	protected static final String TAG = GalleryActivity.class.getSimpleName();
	private GalleryViewFlipper galleryView;
	private final String albumId = "1946";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_activity);
		galleryView = (GalleryViewFlipper) findViewById(R.id.galleryView);
		AlbumModel album = new AlbumModel();
		album.setId(albumId);
		// GCAlbums.get(getApplicationContext(), album, new
		// HttpCallback<ResponseModel<AlbumModel>>() {
		//
		// @Override
		// public void onSuccess(ResponseModel<AlbumModel> responseData) {
		// responseData.getData().getLinks().getAssets().getHref();
		// // galleryView.setAssetCollection(responseData);
		// }
		//
		// @Override
		// public void onHttpError(ResponseStatus responseCode) {
		// Log.d(TAG, "Http Error: " + responseCode.getStatusMessage());
		// }
		// }).executeAsync();

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
