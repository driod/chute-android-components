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
import com.chute.android.gallery.components.GalleryViewFlipper.GalleryCallback;
import com.chute.android.gallery.components.GalleryViewFlipper.PhotoChangeErrorType;
import com.chute.android.gallery.zoom.PinchZoomListener.GestureEvent;
import com.chute.android.imagesharer.intent.ShareActivityIntentWrapper;
import com.chute.android.socialgallery.R;
import com.chute.android.socialgallery.util.Constants;
import com.chute.android.socialgallery.util.intent.SocialGalleryActivityIntentWrapper;
import com.chute.android.socialgallery.util.view.HeartCheckbox;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.chute.GCChutes;
import com.chute.sdk.collections.GCAssetCollection;
import com.chute.sdk.model.GCAssetModel;
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
		gallery.setGalleryCallback(new NewGalleryCallback());
		comments = (ImageButton) findViewById(R.id.btnComment);
		comments.setOnClickListener(new CommentsClickListener());
		share = (ImageButton) findViewById(R.id.btnShare);
		share.setOnClickListener(new ShareClickListener());
		heart = (HeartCheckbox) findViewById(R.id.btnHeart);

		GCChutes.Resources.assets(getApplicationContext(),
				socialWrapper.getChuteId(), new AssetCollectionCallback())
				.executeAsync();

	}

	private final class AssetCollectionCallback implements
			GCHttpCallback<GCAssetCollection> {

		@Override
		public void onSuccess(GCAssetCollection responseData) {
			if (responseData.size() > 0) {
				gallery.setAssetCollection(responseData);
			} else {
				Toast.makeText(
						getApplicationContext(),
						getApplicationContext().getResources().getString(
								R.string.no_photos_in_this_chute),
						Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {
			Toast.makeText(getApplicationContext(), R.string.http_exception,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {
			Toast.makeText(getApplicationContext(), R.string.http_error,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {
			Toast.makeText(getApplicationContext(), R.string.parsing_exception,
					Toast.LENGTH_SHORT).show();
		}

	}

	final class CommentsClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (gallery.getAssetCollection() != null) {
				PhotoCommentsActivityIntentWrapper wrapper = new PhotoCommentsActivityIntentWrapper(
						SocialGalleryActivity.this);
				wrapper.setChuteId(socialWrapper.getChuteId());
				wrapper.setAssetId(gallery.getSelectedItem().getId());
				wrapper.setChuteName(socialWrapper.getChuteName());
				wrapper.startActivityForResult(SocialGalleryActivity.this,
						Constants.ACTIVITY_FOR_RESULT_KEY);
			} else {
				Toast.makeText(
						getApplicationContext(),
						getApplicationContext().getResources().getString(
								R.string.no_photos_in_this_chute),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private final class ShareClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (gallery.getAssetCollection() != null) {
				ShareActivityIntentWrapper wrapper = new ShareActivityIntentWrapper(
						SocialGalleryActivity.this);
				wrapper.setChuteId(socialWrapper.getChuteId());
				wrapper.setChuteName(socialWrapper.getChuteName());
				wrapper.setChuteShortcut(socialWrapper.getChuteShortcut());
				wrapper.setAssetShareUrl(gallery.getSelectedItem()
						.getShareUrl());
				wrapper.startActivity(SocialGalleryActivity.this);
			} else {
				Toast.makeText(
						getApplicationContext(),
						getApplicationContext().getResources().getString(
								R.string.no_photos_in_this_chute),
						Toast.LENGTH_SHORT).show();

			}
		}

	}

	private final class NewGalleryCallback implements GalleryCallback {

		@Override
		public void triggered(GestureEvent event) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPhotoChanged(int index, GCAssetModel asset) {
			heart.markHeartByAssetId(asset.getId());
		}

		@Override
		public void onPhotoChangeError(PhotoChangeErrorType error) {
			// TODO Auto-generated method stub

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
			Toast.makeText(
					getApplicationContext(),
					wrapper.getExtraComments()
							+ getApplicationContext().getResources().getString(
									R.string.comments_added),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(
					getApplicationContext(),
					getApplicationContext().getResources().getString(
							R.string.no_comments_added), Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		heart.deleteObservers();
		gallery.destroyGallery();
	}

}