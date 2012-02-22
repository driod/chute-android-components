package com.chute.android.socialgallery.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chute.android.comments.util.intent.MainActivityIntentWrapper;
import com.chute.android.comments.util.intent.PhotoCommentsActivityIntentWrapper;
import com.chute.android.gallery.components.GalleryViewFlipper;
import com.chute.android.gcshareview.intent.ShareActivityIntentWrapper;
import com.chute.android.socialgallery.R;
import com.chute.android.socialgallery.util.Constants;
import com.chute.android.socialgallery.util.intent.SocialGalleryActivityIntentWrapper;
import com.chute.android.socialgallery.util.view.HeartCheckbox;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.chute.GCChutes;
import com.chute.sdk.collections.GCAssetCollection;
import com.chute.sdk.model.GCHttpRequestParameters;

public class SocialGalleryActivity extends Activity {

	private ImageButton comments;
	private ImageButton share;
	private HeartCheckbox heart;
	private GalleryViewFlipper gallery;
	private SocialGalleryActivityIntentWrapper socialWrapper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_gallery_activity);
		
		socialWrapper = new SocialGalleryActivityIntentWrapper(getIntent());

		gallery = (GalleryViewFlipper) findViewById(R.id.galleryId);
		comments = (ImageButton) findViewById(R.id.btnComment);
		comments.setOnClickListener(new CommentsClickListener());
		share = (ImageButton) findViewById(R.id.btnShare);
		share.setOnClickListener(new ShareClickListener());
		heart = (HeartCheckbox) findViewById(R.id.btnHeart);
		heart.setOnClickListener(new HeartClickListener());

		GCChutes.Resources.assets(getApplicationContext(), Constants.CHUTE_ID,
				new AssetCollectionCallback()).executeAsync();

	}

	private final class AssetCollectionCallback implements
			GCHttpCallback<GCAssetCollection> {

		@Override
		public void onSuccess(GCAssetCollection responseData) {
			gallery.setAssetCollection(responseData);
		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {

		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {

		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {

		}

	}
	
    final class CommentsClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			PhotoCommentsActivityIntentWrapper wrapper = new PhotoCommentsActivityIntentWrapper(
					SocialGalleryActivity.this);
			wrapper.setChuteId(socialWrapper.getChuteId());
			wrapper.setAssetId(socialWrapper.getAssetId());
			wrapper.setChuteName(socialWrapper.getChuteName());
			wrapper.startActivityForResult(SocialGalleryActivity.this,
					Constants.ACTIVITY_FOR_RESULT_KEY);
		}

	}

	private final class ShareClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			ShareActivityIntentWrapper wrapper = new ShareActivityIntentWrapper(
					SocialGalleryActivity.this);
			wrapper.setChuteId(socialWrapper.getChuteId());
			wrapper.setChuteName(socialWrapper.getChuteName());
			wrapper.setChuteShortcut(socialWrapper.getChuteShortcut());
			wrapper.startActivity(SocialGalleryActivity.this);
		}

	}

	private final class HeartClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			heart.markHeartByAssetId(Constants.ASSET_ID);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode != Constants.ACTIVITY_FOR_RESULT_KEY
				|| resultCode != RESULT_OK) {
			return;
		}
		MainActivityIntentWrapper wrapper = new MainActivityIntentWrapper(data);
		if (wrapper.getExtraComments() > 0) {
			Toast.makeText(getApplicationContext(),
					wrapper.getExtraComments() + " Comments added!",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "No Comments added!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		gallery.destroyGallery();
	}

}