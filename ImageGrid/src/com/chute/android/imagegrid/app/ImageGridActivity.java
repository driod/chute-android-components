package com.chute.android.imagegrid.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.chute.android.imagegrid.R;
import com.chute.android.imagegrid.adapters.AssetCollectionAdapter;
import com.chute.android.imagegrid.intent.ImageGridIntentWrapper;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.model.GCChuteModel;
import com.chute.sdk.model.GCHttpRequestParameters;

public class ImageGridActivity extends Activity {

    public static final String TAG = ImageGridActivity.class.getSimpleName();
    private GridView grid;
    private AssetCollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.image_grid);

	grid = (GridView) findViewById(R.id.gridView);
	final GCChuteModel chute = new GCChuteModel();
	final ImageGridIntentWrapper wrapper = new ImageGridIntentWrapper(getIntent());
	chute.setId(wrapper.getChuteId());
	chute.assets(getApplicationContext(), new AssetCollectionCallback()).executeAsync();
    }

    // Callback which returns a collection of assets for a given chuteId
    private final class AssetCollectionCallback implements GCHttpCallback<GCChuteModel> {

	@Override
	public void onSuccess(GCChuteModel responseData) {
	    adapter = new AssetCollectionAdapter(ImageGridActivity.this,
		    responseData.assetCollection);
	    grid.setAdapter(adapter);
	}

	@Override
	public void onHttpException(GCHttpRequestParameters params, Throwable exception) {
	    Toast.makeText(getApplicationContext(), getString(R.string.http_exception),
		    Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onHttpError(int responseCode, String statusMessage) {
	    Toast.makeText(getApplicationContext(), getString(R.string.http_error),
		    Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onParserException(int responseCode, Throwable exception) {
	    Toast.makeText(getApplicationContext(), getString(R.string.parsing_exception),
		    Toast.LENGTH_SHORT).show();
	}
    }
}
