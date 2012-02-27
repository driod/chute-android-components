package com.chute.android.gcchutelisting.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.chute.android.gcchutelisting.R;
import com.chute.android.gcchutelisting.adapters.GalleryListingAdapter;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.user.GCUser;
import com.chute.sdk.collections.GCChuteCollection;
import com.chute.sdk.model.GCHttpRequestParameters;
import com.chute.sdk.utils.GCConstants;

public class GalleryListingActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = GalleryListingActivity.class
			.getSimpleName();
	private GalleryListingAdapter adapter;
	public ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chute_list);
		listView = (ListView) findViewById(R.id.albumList);
		GCUser.userChutes(getApplicationContext(), GCConstants.CURRENT_USER_ID,
				new GCChuteCollectionCallback()).executeAsync();

	}

	private final class GCChuteCollectionCallback implements
			GCHttpCallback<GCChuteCollection> {

		@Override
		public void onSuccess(GCChuteCollection responseData) {
			adapter = new GalleryListingAdapter(GalleryListingActivity.this,
					responseData);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new ListItemClickListener());
		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.http_exception), Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.http_error), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.parsing_exception), Toast.LENGTH_SHORT)
					.show();
		}
	}

	private final class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			finish();
		}

	}
}
