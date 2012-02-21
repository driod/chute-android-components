package com.chute.android.socialgallery.model;

import java.util.Observable;

import android.content.Context;

import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.heart.GCHearts;
import com.chute.sdk.collections.GCHeartsMap;
import com.chute.sdk.model.GCHttpRequestParameters;
import com.chute.sdk.parsers.GCHeartsMapObjectParser;
import com.chute.sdk.utils.GCConstants;

public class HeartsSingleton extends Observable {

    public static final String TAG = HeartsSingleton.class.getSimpleName();
    private static HeartsSingleton instance;
    private final Context context;
    private GCHeartsMap responseData;

    private HeartsSingleton(Context context) {
	super();
	this.context = context;
	refreshHearts();
    }

    public static synchronized HeartsSingleton getInstance(Context context) {
	if (instance == null) {
	    instance = new HeartsSingleton(context.getApplicationContext());
	}
	return instance;
    }

    public void refreshHearts() {
	GCHearts.get(context, GCConstants.CURRENT_USER_ID, new GCHeartsMapObjectParser(),
		new HeartsMapCallback()).executeAsync();
    }

    public void getHearts() {
	if (responseData != null) {
	    setChanged();
	    notifyObservers(responseData);
	} else {
	    refreshHearts();
	}
    }

    private final class HeartsMapCallback implements GCHttpCallback<GCHeartsMap> {

	@Override
	public void onSuccess(GCHeartsMap responseData) {
	    HeartsSingleton.this.responseData = responseData;
	    setChanged();
	    notifyObservers(HeartsSingleton.this.responseData);
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
}
