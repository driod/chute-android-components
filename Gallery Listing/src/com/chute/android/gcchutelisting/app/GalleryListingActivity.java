package com.chute.android.gcchutelisting.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.chute.android.gcchutelisting.R;
import com.chute.android.gcchutelisting.adapters.GalleryListingAdapter;
import com.chute.sdk.v2.api.album.GCAlbums;
import com.chute.sdk.v2.model.AlbumModel;
import com.chute.sdk.v2.model.requests.ListResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class GalleryListingActivity extends Activity {

	private static final String TAG = GalleryListingActivity.class
			.getSimpleName();
	private GalleryListingAdapter adapter;
	public ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_list);
		listView = (ListView) findViewById(R.id.albumList);
		GCAlbums.list(getApplicationContext(), new AlbumsCallback())
				.executeAsync();

	}

	private final class AlbumsCallback implements
			HttpCallback<ListResponseModel<AlbumModel>> {

		@Override
		public void onSuccess(ListResponseModel<AlbumModel> responseData) {
			adapter = new GalleryListingAdapter(GalleryListingActivity.this,
					responseData.getData());
			listView.setAdapter(adapter);

		}

		@Override
		public void onHttpError(ResponseStatus responseCode) {
			Log.d(TAG, "Http Error: " + responseCode.getStatusCode() + " "
					+ responseCode.getStatusMessage());
		}

	}

}
