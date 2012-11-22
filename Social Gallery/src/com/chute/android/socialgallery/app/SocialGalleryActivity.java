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
import com.chute.android.imagesharer.intent.ShareActivityIntentWrapper;
import com.chute.android.socialgallery.R;
import com.chute.android.socialgallery.util.Constants;
import com.chute.android.socialgallery.util.intent.SocialGalleryActivityIntentWrapper;
import com.chute.sdk.v2.api.album.GCAlbums;
import com.chute.sdk.v2.model.AlbumModel;
import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.requests.ListResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class SocialGalleryActivity extends Activity {

	private ImageButton comments;
	private ImageButton share;
	private GalleryViewFlipper gallery;
	private SocialGalleryActivityIntentWrapper wrapper;
	private AlbumModel album = new AlbumModel();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_gallery_activity);

		wrapper = new SocialGalleryActivityIntentWrapper(getIntent());

		gallery = (GalleryViewFlipper) findViewById(R.id.galleryId);
		comments = (ImageButton) findViewById(R.id.btnComment);
		comments.setOnClickListener(new CommentsClickListener());
		share = (ImageButton) findViewById(R.id.btnShare);
		share.setOnClickListener(new ShareClickListener());

		album.setId(wrapper.getAlbumId());
		GCAlbums.Assets.all(getApplicationContext(), album,
				new AlbumAssetsCallback()).executeAsync();

	}

	private final class AlbumAssetsCallback implements
			HttpCallback<ListResponseModel<AssetModel>> {

		@Override
		public void onSuccess(ListResponseModel<AssetModel> responseData) {
			if (responseData.getData().size() > 0) {
				gallery.setAssetCollection(responseData.getData());
			} else {
				Toast.makeText(
						getApplicationContext(),
						getApplicationContext().getResources().getString(
								R.string.no_photos_in_this_album),
						Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		public void onHttpError(ResponseStatus responseCode) {

		}

	}

	final class CommentsClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (gallery.getAssetCollection() != null) {
				PhotoCommentsActivityIntentWrapper commentsWrapper = new PhotoCommentsActivityIntentWrapper(
						SocialGalleryActivity.this);
				commentsWrapper.setAlbumId(wrapper.getAlbumId());
				commentsWrapper.setAssetId(gallery.getSelectedItem().getId());
				commentsWrapper.setAlbumName(wrapper.getAlbumName());
				commentsWrapper.startActivityForResult(
						SocialGalleryActivity.this,
						Constants.ACTIVITY_FOR_RESULT_KEY);
			} else {
				Toast.makeText(
						getApplicationContext(),
						getApplicationContext().getResources().getString(
								R.string.no_photos_in_this_album),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private final class ShareClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (gallery.getAssetCollection() != null) {
				ShareActivityIntentWrapper shareWrapper = new ShareActivityIntentWrapper(
						SocialGalleryActivity.this);
				shareWrapper.setAlbumId(wrapper.getAlbumId());
				shareWrapper.setAlbumName(wrapper.getAlbumName());
				shareWrapper.setAlbumShortcut(wrapper.getAlbumShortcut());
				shareWrapper.setAssetShareUrl(gallery.getSelectedItem()
						.getUrl());
				shareWrapper.startActivity(SocialGalleryActivity.this);
			} else {
				Toast.makeText(
						getApplicationContext(),
						getApplicationContext().getResources().getString(
								R.string.no_photos_in_this_album),
						Toast.LENGTH_SHORT).show();

			}
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
		gallery.destroyGallery();
	}

}