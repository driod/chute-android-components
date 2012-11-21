package com.chute.android.imagegrid.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.chute.android.imagegrid.R;
import com.chute.android.imagegrid.adapters.AssetsAdapter;
import com.chute.android.imagegrid.intent.ImageGridIntentWrapper;
import com.chute.sdk.v2.api.album.GCAlbums;
import com.chute.sdk.v2.model.AlbumModel;
import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.requests.ListResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class ImageGridActivity extends Activity {

	public static final String TAG = ImageGridActivity.class.getSimpleName();
	private AlbumModel album;
	private GridView grid;
	private AssetsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_grid);

		grid = (GridView) findViewById(R.id.gridView);
		final ImageGridIntentWrapper wrapper = new ImageGridIntentWrapper(
				getIntent());
		album = new AlbumModel();
		album.setId(wrapper.getAlbumId());
		GCAlbums.Assets.all(getApplicationContext(), album,
				new AssetsCallback()).executeAsync();
	}

	private final class AssetsCallback implements
			HttpCallback<ListResponseModel<AssetModel>> {

		@Override
		public void onSuccess(ListResponseModel<AssetModel> responseData) {
			adapter = new AssetsAdapter(ImageGridActivity.this,
					responseData.getData());
			grid.setAdapter(adapter);
			grid.setOnItemClickListener(new GridClickedListener());
		}

		@Override
		public void onHttpError(ResponseStatus responseCode) {
			Toast.makeText(getApplicationContext(),
					responseCode.getStatusMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	private final class GridClickedListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			finish();
		}

	}
}
