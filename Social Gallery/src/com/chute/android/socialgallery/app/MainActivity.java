package com.chute.android.socialgallery.app;

import com.chute.android.socialgallery.R;
import com.chute.android.socialgallery.util.Constants;
import com.chute.android.socialgallery.util.intent.SocialGalleryActivityIntentWrapper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	public static final String TAG = MainActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		findViewById(R.id.btnStart).setOnClickListener(new OnStartClickListener());
	}
	
	private final class OnStartClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			SocialGalleryActivityIntentWrapper wrapper = new SocialGalleryActivityIntentWrapper(MainActivity.this);
			wrapper.setAssetId(Constants.ASSET_ID);
			wrapper.setChuteId(Constants.CHUTE_ID);
			wrapper.setChuteName(Constants.CHUTE_NAME);
			wrapper.setChuteShortcut(Constants.CHUTE_SHORTCUT);
			wrapper.startActivity(MainActivity.this);
		}
		
	}
	
}
