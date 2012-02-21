package com.chute.android.comments.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.comment.GCComments;
import com.chute.sdk.collections.GCCommentCollection;
import com.chute.sdk.model.GCCommentModel;
import com.chute.sdk.model.GCHttpRequestParameters;
import com.chute.sdk.parsers.GCCommentListObjectParser;
import com.chute.sdk.parsers.GCCommentSingleObjectParser;

public class PhotoCommentsActivity extends Activity {
    @SuppressWarnings("unused")
    private static final String TAG = PhotoCommentsActivity.class.getSimpleName();
    private ListView listView;
    private TextView titleView;
    private EditText comment;
    private PhotoCommentsActivityIntentWrapper wrapper;
    private PhotoCommentsAdapter adapter;

    private int commentAddedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.comments_activity);
	listView = (ListView) findViewById(R.id.listView);
	listView.setCacheColorHint(Color.TRANSPARENT);
	listView.setDivider(null);
	listView.setDividerHeight(25);
	adapter = new PhotoCommentsAdapter(this, new GCCommentCollection());
	listView.setAdapter(adapter);
	titleView = (TextView) findViewById(R.id.titleView);

	wrapper = new PhotoCommentsActivityIntentWrapper(getIntent());

	GCComments.get(getApplicationContext(), wrapper.getChuteId(), wrapper.getAssetId(),
		new GCCommentListObjectParser(), new CommentCollectionCallback()).executeAsync();
	titleView.setText(wrapper.getChuteName());

	comment = (EditText) findViewById(R.id.editTextComment);
	Button save = (Button) findViewById(R.id.buttonSave);
	save.setOnClickListener(new OnSaveClickListener());

    }

    private final class OnSaveClickListener implements OnClickListener {
	@Override
	public void onClick(View v) {
	    String comment = PhotoCommentsActivity.this.comment.getText().toString();
	    if (TextUtils.isEmpty(comment)) {
		Toast.makeText(getApplicationContext(), R.string.toast_enter_comment,
			Toast.LENGTH_SHORT).show();
		return;
	    }
	    GCComments.add(getApplicationContext(), wrapper.getChuteId(), wrapper.getAssetId(),
		    comment, new GCCommentSingleObjectParser(), new CommentsAddCallback())
		    .executeAsync();
	    PhotoCommentsActivity.this.comment.getText().clear();
	}
    }

    private final class CommentsAddCallback implements GCHttpCallback<GCCommentModel> {
	@Override
	public void onSuccess(GCCommentModel responseData) {
	    adapter.addComment(responseData);
	    Toast.makeText(getApplicationContext(), R.string.toast_comment_added,
		    Toast.LENGTH_SHORT).show();
	    commentAddedCount++;
	}

	@Override
	public void onHttpException(GCHttpRequestParameters params, Throwable exception) {
	}

	@Override
	public void onHttpError(int responseCode, String statusMessage) {
	}

	@Override
	public void onParserException(int responseCode, Throwable exception) {
	}
    }

    private final class CommentCollectionCallback implements GCHttpCallback<GCCommentCollection> {
	@Override
	public void onSuccess(GCCommentCollection responseData) {
	    adapter.changeData(responseData);
	}

	@Override
	public void onHttpException(GCHttpRequestParameters params, Throwable exception) {
	    Toast.makeText(getApplicationContext(), "Failed to get comments, Connection error",
		    Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onHttpError(int responseCode, String statusMessage) {
	    Toast.makeText(getApplicationContext(),
		    "Failed to get comments, server error " + responseCode + " " + statusMessage,
		    Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onParserException(int responseCode, Throwable exception) {
	    Toast.makeText(getApplicationContext(), "Failed to get comments, parser error",
		    Toast.LENGTH_SHORT).show();
	}
    }

    @Override
    public void onBackPressed() {
	MainActivityIntentWrapper wrapper = new MainActivityIntentWrapper(new Intent());
	wrapper.setExtraComments(commentAddedCount);
	setResult(RESULT_OK, wrapper.getIntent());
	super.onBackPressed();
    }

}