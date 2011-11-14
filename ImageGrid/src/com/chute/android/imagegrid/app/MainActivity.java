package com.chute.android.imagegrid.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chute.android.imagegrid.R;
import com.chute.android.imagegrid.intent.ImageGridIntentWrapper;
import com.chute.sdk.model.GCAccount;

public class MainActivity extends Activity implements OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	GCAccount account = GCAccount.getInstance(getApplicationContext());
	account.setPassword("46b7c778447e18ee5865a83f4202f42a2f85283c47ef24541366509235d8eccf");

	Button showAssetsButton = (Button) findViewById(R.id.btnShowAssets);
	showAssetsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
	ImageGridIntentWrapper wrapper = new ImageGridIntentWrapper(this);
	wrapper.setChuteID("688");
	wrapper.startActivity(this);
    }

}