package com.chute.android.gallery.ui;

import android.app.Activity;
import android.os.Bundle;

import com.chute.android.gallery.R;
import com.chute.android.gallery.components.GalleryViewFlipper;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.chute.GCChutes;
import com.chute.sdk.collections.GCAssetCollection;
import com.chute.sdk.model.GCHttpRequestParameters;

public class GalleryActivity extends Activity {

    protected static final String TAG = GalleryActivity.class.getSimpleName();
    private GalleryViewFlipper galleryView;
    private final String chuteId = "1946";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.gallery_activity);
	galleryView = (GalleryViewFlipper) findViewById(R.id.galleryView);
	GCChutes.Resources.assets(getApplicationContext(), chuteId,
		new GCHttpCallback<GCAssetCollection>() {

		    @Override
		    public void onSuccess(GCAssetCollection responseData) {
			galleryView.setAssetCollection(responseData);
		    }

		    @Override
		    public void onParserException(int responseCode, Throwable exception) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void onHttpException(GCHttpRequestParameters params, Throwable exception) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void onHttpError(int responseCode, String statusMessage) {
			// TODO Auto-generated method stub

		    }
		}).executeAsync();
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	galleryView.destroyGallery();
    }

}
