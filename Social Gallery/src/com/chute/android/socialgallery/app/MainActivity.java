package com.chute.android.socialgallery.app;

import com.chute.android.socialgallery.R;
import com.chute.android.socialgallery.util.Constants;
import com.chute.android.socialgallery.util.intent.SocialGalleryActivityIntentWrapper;
import com.chute.sdk.model.GCAccountStore;

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
		
		GCAccountStore account = GCAccountStore.getInstance(getApplicationContext());
		account.setPassword("46b7c778447e18ee5865a83f4202f42a2f85283c47ef24541366509235d8eccf");
		
		findViewById(R.id.btnStart).setOnClickListener(new OnStartClickListener());
	}
	
	private final class OnStartClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			SocialGalleryActivityIntentWrapper wrapper = new SocialGalleryActivityIntentWrapper(MainActivity.this);
			wrapper.setChuteId(Constants.CHUTE_ID);
			wrapper.setChuteName(Constants.CHUTE_NAME);
			wrapper.setChuteShortcut(Constants.CHUTE_SHORTCUT);
			wrapper.startActivity(MainActivity.this);
		}
		
	}
	
}
