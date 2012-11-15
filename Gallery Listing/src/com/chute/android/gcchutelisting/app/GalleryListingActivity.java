package com.chute.android.gcchutelisting.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.chute.android.gcchutelisting.R;
import com.chute.android.gcchutelisting.adapters.GalleryListingAdapter;
import com.chute.sdk.v2.api.user.GCUsers;
import com.chute.sdk.v2.model.AlbumModel;
import com.chute.sdk.v2.model.UserModel;
import com.chute.sdk.v2.model.requests.ListResponseModel;
import com.chute.sdk.v2.utils.Constants;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class GalleryListingActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = GalleryListingActivity.class
			.getSimpleName();
	private GalleryListingAdapter adapter;
	public ListView listView;
	private UserModel user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chute_list);
		listView = (ListView) findViewById(R.id.albumList);
		user = new UserModel();
		user.setId(Constants.CURRENT_USER_ID);
		GCUsers.getAlbums(getApplicationContext(), user, new UserAlbumsCallback()).executeAsync();

	}

	private final class UserAlbumsCallback implements
			HttpCallback<ListResponseModel<AlbumModel>> {

		@Override
		public void onSuccess(ListResponseModel<AlbumModel> responseData) {
			adapter = new GalleryListingAdapter(GalleryListingActivity.this,
					responseData.getData());
			listView.setAdapter(adapter);
			
		}

		@Override
		public void onHttpError(ResponseStatus responseCode) {
			Log.d(TAG, "Http Error: " + responseCode.getStatusCode() + " " + responseCode.getStatusMessage());
		}
		
	}


}
