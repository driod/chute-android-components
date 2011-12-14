package com.chute.android.tutorial.photoupload;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.asset.GCAssets;
import com.chute.sdk.api.asset.GCUploadProgressListener;
import com.chute.sdk.api.parcel.GCParcel;
import com.chute.sdk.collections.GCChuteCollection;
import com.chute.sdk.collections.GCLocalAssetCollection;
import com.chute.sdk.model.GCAccount;
import com.chute.sdk.model.GCChuteModel;
import com.chute.sdk.model.GCHttpRequestParameters;
import com.chute.sdk.model.GCLocalAssetModel;
import com.chute.sdk.parsers.GCCreateParcelsUploadsListParser;
import com.chute.sdk.parsers.base.GCStringResponse;

public class PhotoUploadActivity extends Activity {

    private static final String TAG = PhotoUploadActivity.class.getSimpleName();
    private ImageView imageThumb;
    private ProgressBar pb;
    private TextView fileText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	GCAccount.getInstance(this).setUsername("TestUser");
	// Token For testing purpouses
	GCAccount.getInstance(this).setPassword(
		"3115093a84aa3c4b696f9e5e0356826085e6c2810e55931ccd4d227ba70cfdbb");

	final Button startUpload = (Button) findViewById(R.id.buttonStartUpload);
	startUpload.setOnClickListener(new OnUploadClickListener());
	fileText = (TextView) findViewById(R.id.textView);

	pb = (ProgressBar) findViewById(R.id.progressBar);

	imageThumb = (ImageView) findViewById(R.id.imageView);
    }

    private final class OnUploadClickListener implements OnClickListener {
	@Override
	public void onClick(View v) {
	    GCLocalAssetModel asset = new GCLocalAssetModel();
	    try {
		asset.setFile(FileUtil.copyAsset(getApplicationContext(), "droid4.jpg"));
	    } catch (IOException e) {
		Log.w(TAG, "", e);
	    }
	    GCLocalAssetCollection assetCollection = new GCLocalAssetCollection();
	    assetCollection.add(asset);

	    GCChuteModel chuteModel = new GCChuteModel();
	    chuteModel.setId("1183"); // Test ID for a public chute called
				      // Upload Test
	    GCChuteCollection chuteCollection = new GCChuteCollection();
	    chuteCollection.add(chuteModel);

	    createParcel(assetCollection, chuteCollection);
	}
    }

    public void createParcel(GCLocalAssetCollection assets, GCChuteCollection chutes) {
	GCParcel.create(getApplicationContext(), assets, chutes,
		new GCCreateParcelsUploadsListParser(), new GCParcelCreateCallback())
		.executeAsync();
    }

    private final class GCParcelCreateCallback implements GCHttpCallback<GCLocalAssetCollection> {

	@Override
	public void onHttpException(GCHttpRequestParameters params, Throwable exception) {
	    Log.d(TAG, "Parcel Create http Error ", exception);
	}

	@Override
	public void onHttpError(int responseCode, String statusMessage) {
	}

	@Override
	public void onParserException(int responseCode, Throwable exception) {
	}

	@Override
	public void onSuccess(GCLocalAssetCollection responseData) {
	    try {
		GCAssets.upload(getApplicationContext(),
			new GCUploadProgressListenerImplementation(), new GCStringResponse(),
			new GCHttpUploadCallback(), responseData).executeAsync();
	    } catch (Exception e) {
		Log.w(TAG, "", e);
	    }
	    Log.e(TAG, responseData.toString());
	}
    }

    private final class GCUploadProgressListenerImplementation implements GCUploadProgressListener {

	private static final int DELAY = 1000;

	private Timer t;
	private long total;
	private long uploaded;

	class ProgressTask extends TimerTask {
	    @Override
	    public void run() {
		int percent = (int) ((uploaded * 100) / total);
		Log.e(TAG, "Current progress Text" + percent + "%");
		pb.setProgress(percent);
	    }
	}

	@Override
	public void onUploadStarted(String assetId, final String filepath, final Bitmap thumbnail) {
	    Log.d(TAG, "Upload started");
	    t = new Timer();
	    t.schedule(new ProgressTask(), DELAY, DELAY);
	    pb.setMax(100);
	    PhotoUploadActivity.this.runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    imageThumb.setImageBitmap(thumbnail);
		    fileText.setText(filepath);
		}
	    });
	}

	@Override
	public void onUploadFinished(String assetId, String filepath) {
	    Log.d(TAG, "Upload finished");
	    t.cancel();
	}

	@Override
	public void onProgress(long total, long uploaded) {
	    this.total = total;
	    this.uploaded = uploaded;
	    Log.d(TAG, "Content Size " + String.valueOf(total));
	    Log.d(TAG, "Current progress " + String.valueOf(uploaded));
	}
    }

    private class GCHttpUploadCallback implements GCHttpCallback<String> {
	@Override
	public void onSuccess(String responseData) {
	    Log.e(TAG, responseData);
	}

	@Override
	public void onHttpException(GCHttpRequestParameters params, Throwable exception) {
	    Log.d(TAG, "Upload callback Create http exception ", exception);
	}

	@Override
	public void onHttpError(int responseCode, String statusMessage) {
	    Log.d(TAG, "Upload callback http Error " + statusMessage + " Code " + responseCode);
	}

	@Override
	public void onParserException(int responseCode, Throwable exception) {
	    Log.d(TAG, "Upload callback Parser Exception  Code " + responseCode, exception);

	}
    }
}