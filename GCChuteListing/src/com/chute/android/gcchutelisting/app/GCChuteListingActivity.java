package com.chute.android.gcchutelisting.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.chute.android.gcchutelisting.R;
import com.chute.android.gcchutelisting.adapters.GCChuteListingAdapter;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.user.GCUser;
import com.chute.sdk.collections.GCChuteCollection;
import com.chute.sdk.model.GCHttpRequestParameters;
import com.chute.sdk.utils.GCConstants;

public class GCChuteListingActivity extends Activity {

    @SuppressWarnings("unused")
    private static final String TAG = GCChuteListingActivity.class.getSimpleName();
    private GCChuteListingAdapter adapter;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.chute_list);
	listView = (ListView) findViewById(R.id.listView);
	GCUser.userChutes(getApplicationContext(), GCConstants.CURRENT_USER_ID,
		new GCChuteCollectionCallback()).executeAsync();

    }

    private final class GCChuteCollectionCallback implements GCHttpCallback<GCChuteCollection> {

	@Override
	public void onSuccess(GCChuteCollection responseData) {
	    adapter = new GCChuteListingAdapter(GCChuteListingActivity.this, responseData);
	    listView.setAdapter(adapter);
	}

	@Override
	public void onHttpException(GCHttpRequestParameters params, Throwable exception) {
	    Toast.makeText(getApplicationContext(), getString(R.string.http_exception),
		    Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onHttpError(int responseCode, String statusMessage) {
	    Toast.makeText(getApplicationContext(), getString(R.string.http_error),
		    Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onParserException(int responseCode, Throwable exception) {
	    Toast.makeText(getApplicationContext(), getString(R.string.parsing_exception),
		    Toast.LENGTH_SHORT).show();
	}
    }
}
