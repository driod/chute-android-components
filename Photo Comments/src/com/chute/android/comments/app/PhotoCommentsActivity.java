package com.chute.android.comments.app;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chute.android.comments.R;
import com.chute.android.comments.adapters.PhotoCommentsAdapter;
import com.chute.android.comments.util.intent.PhotoCommentsActivityIntentWrapper;
import com.chute.android.comments.util.intent.MainActivityIntentWrapper;
import com.chute.sdk.v2.api.comment.GCComments;
import com.chute.sdk.v2.model.AlbumModel;
import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.CommentModel;
import com.chute.sdk.v2.model.requests.ListResponseModel;
import com.chute.sdk.v2.model.requests.ResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class PhotoCommentsActivity extends Activity {
	private static final String TAG = PhotoCommentsActivity.class
			.getSimpleName();
	private ListView listView;
	private TextView titleView;
	private EditText comment;
	private PhotoCommentsActivityIntentWrapper wrapper;
	private PhotoCommentsAdapter adapter;
	private List<CommentModel> commentList;
	private AlbumModel album = new AlbumModel();
	private AssetModel asset = new AssetModel();

	private int commentAddedCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments_activity);
		listView = (ListView) findViewById(R.id.listView);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDivider(null);
		listView.setDividerHeight(25);
		adapter = new PhotoCommentsAdapter(this, commentList);
		listView.setAdapter(adapter);
		titleView = (TextView) findViewById(R.id.titleView);

		wrapper = new PhotoCommentsActivityIntentWrapper(getIntent());
		album.setId(wrapper.getAlbumId());
		asset.setId(wrapper.getAssetId());

		GCComments.get(getApplicationContext(), album, asset,
				new CommentsCallback()).executeAsync();
		titleView.setText(wrapper.getAlbumName());

		comment = (EditText) findViewById(R.id.editTextComment);
		Button save = (Button) findViewById(R.id.buttonSave);
		save.setOnClickListener(new OnSaveClickListener());

	}

	private final class OnSaveClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String comment = PhotoCommentsActivity.this.comment.getText()
					.toString();
			if (TextUtils.isEmpty(comment)) {
				Toast.makeText(getApplicationContext(),
						R.string.toast_enter_comment, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			GCComments.create(getApplicationContext(), album, asset,
					new CommentsAddCallback()).executeAsync();
			PhotoCommentsActivity.this.comment.getText().clear();
		}
	}

	private final class CommentsAddCallback implements
			HttpCallback<ResponseModel<CommentModel>> {

		@Override
		public void onSuccess(ResponseModel<CommentModel> responseData) {
			adapter.addComment(responseData.getData());
			Toast.makeText(getApplicationContext(),
					R.string.toast_comment_added, Toast.LENGTH_SHORT).show();
			commentAddedCount++;

		}

		@Override
		public void onHttpError(ResponseStatus responseCode) {
			Log.d(TAG, "Http Error: " + responseCode.getStatusMessage());
			Toast.makeText(getApplicationContext(),
					R.string.comments_http_error, Toast.LENGTH_SHORT).show();

		}

	}

	private final class CommentsCallback implements
			HttpCallback<ListResponseModel<CommentModel>> {

		@Override
		public void onSuccess(ListResponseModel<CommentModel> responseData) {
			adapter.changeData(responseData.getData());
		}

		@Override
		public void onHttpError(ResponseStatus responseCode) {
			Log.d(TAG, "Http Error: " + responseCode.getStatusMessage());
			Toast.makeText(getApplicationContext(),
					R.string.comments_http_error, Toast.LENGTH_SHORT).show();

		}

	}

	@Override
	public void onBackPressed() {
		MainActivityIntentWrapper wrapper = new MainActivityIntentWrapper(
				new Intent());
		wrapper.setExtraComments(commentAddedCount);
		setResult(RESULT_OK, wrapper.getIntent());
		super.onBackPressed();
	}

}