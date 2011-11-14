package com.chute.android.multiimagepicker.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.chute.android.multiimagepicker.R;
import com.chute.android.multiimagepicker.intent.ChoosePhotosActivityIntentWrapper;
import com.chute.sdk.collections.GCLocalAssetCollection;
import com.chute.sdk.model.GCLocalAssetModel;

public class MainActivity extends Activity implements OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	Button selectPhotos = (Button) findViewById(R.id.btnSelectPhotos);
	selectPhotos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
	ChoosePhotosActivityIntentWrapper wrapper = new ChoosePhotosActivityIntentWrapper(this);
	wrapper.startActivityForResult(this,
		ChoosePhotosActivityIntentWrapper.ACTIVITY_FOR_RESULT_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (requestCode == ChoosePhotosActivityIntentWrapper.ACTIVITY_FOR_RESULT_KEY) {
	    if (resultCode == RESULT_OK) {
		ChoosePhotosActivityIntentWrapper wrapper = new ChoosePhotosActivityIntentWrapper(
			data);
		GCLocalAssetCollection localAssetCollection = makeGCLocalAssetCollection(wrapper
			.getAssetPathList());
		int photosSelected = localAssetCollection.size();
		Toast.makeText(getApplicationContext(), photosSelected + " photos selected!",
			Toast.LENGTH_SHORT).show();
	    } else {
		Toast.makeText(getApplicationContext(), "No photos selected", Toast.LENGTH_SHORT)
			.show();
	    }
	}
    }

    public GCLocalAssetCollection makeGCLocalAssetCollection(ArrayList<String> gridSelectedFilePath) {
	GCLocalAssetCollection collection = new GCLocalAssetCollection();
	GCLocalAssetModel model = new GCLocalAssetModel();
	for (String filePath : gridSelectedFilePath) {
	    model.setFile(filePath);
	    collection.add(model);
	}
	return collection;
    }
}
