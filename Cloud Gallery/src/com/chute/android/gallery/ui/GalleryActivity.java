package com.chute.android.gallery.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.chute.android.gallery.R;
import com.chute.android.gallery.components.GalleryViewFlipper;

public class GalleryActivity extends Activity {

    protected static final String TAG = GalleryActivity.class.getSimpleName();
    private GalleryViewFlipper galleryView;
    private final String chuteId = "1946";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.gallery_activity);
	galleryView = (GalleryViewFlipper) findViewById(R.id.galleryView);
//	GCChutes.Resources.assets(getApplicationContext(), chuteId,
//		new GCHttpCallback<GCAssetCollection>() {
//
//		    @Override
//		    public void onSuccess(GCAssetCollection responseData) {
//			galleryView.setAssetCollection(responseData);
//		    }
//
//		    @Override
//		    public void onParserException(int responseCode, Throwable exception) {
//		    	Toast.makeText(getApplicationContext(), R.string.parsing_exception, Toast.LENGTH_SHORT).show();
//
//		    }
//
//		    @Override
//		    public void onHttpException(GCHttpRequestParameters params, Throwable exception) {
//		    	Toast.makeText(getApplicationContext(), R.string.http_exception, Toast.LENGTH_SHORT).show();
//		    }
//
//		    @Override
//		    public void onHttpError(int responseCode, String statusMessage) {
//		    	Toast.makeText(getApplicationContext(), R.string.http_error, Toast.LENGTH_SHORT).show();
//		    }
//		}).executeAsync();
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	galleryView.destroyGallery();
    }

}
