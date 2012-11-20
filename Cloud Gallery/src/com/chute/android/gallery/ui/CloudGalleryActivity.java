package com.chute.android.gallery.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chute.android.gallery.R;

public class CloudGalleryActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	@SuppressWarnings("unused")
	private static final String TAG = CloudGalleryActivity.class
			.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button showList = (Button) findViewById(R.id.btnShowList);
		showList.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		final Intent intent = new Intent(getApplicationContext(),
				GalleryActivity.class);
		startActivity(intent);
	}
}