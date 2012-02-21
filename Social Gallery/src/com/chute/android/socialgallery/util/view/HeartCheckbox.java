package com.chute.android.socialgallery.util.view;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chute.android.socialgallery.R;
import com.chute.android.socialgallery.model.HeartsSingleton;
import com.chute.sdk.api.heart.GCHearts;
import com.chute.sdk.collections.GCHeartsMap;
import com.chute.sdk.parsers.base.GCStringResponse;

public class HeartCheckbox extends CheckBox {

    private GCHeartsMap hearts;
    private HeartsObserver heartsObserver;

    public HeartCheckbox(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    public HeartCheckbox(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	init();
    }

    public HeartCheckbox(Context context) {
	super(context);
	init();
    }

    public static final String TAG = HeartCheckbox.class.getSimpleName();
    private String assetId;

    private void init() {
	if (isInEditMode() == false) {
	    this.setBackgroundDrawable(null);
	}
	this.setButtonDrawable(R.drawable.heart_selector);
	this.setOnCheckedChangeListener(new OnHeartChangedListener());
	final HeartsSingleton hearts = HeartsSingleton.getInstance(getContext());
	heartsObserver = new HeartsObserver();
	hearts.addObserver(heartsObserver);
	hearts.getHearts();
    }

    public void deleteObservers() {
	try {
	    HeartsSingleton.getInstance(getContext()).deleteObserver(heartsObserver);
	} catch (Exception e) {
	    Log.w(TAG, "", e);
	}
    }

    public void markHeartByAssetId(String assetId) {
	this.assetId = assetId;
	Log.d(TAG, "assetId " + assetId);
	if (hearts == null) {
	    return;
	}
	if (hearts.get(Integer.parseInt(assetId)) != null) {
	    this.setChecked(true);
	} else {
	    this.setChecked(false);
	}

    }

    private final class OnHeartChangedListener implements OnCheckedChangeListener {
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	    Log.d(TAG, "isChecked " + isChecked);
	    GCHearts.set(getContext(), assetId, isChecked, new GCStringResponse(), null)
		    .executeAsync();
	    if (hearts == null) {
		return;
	    }
	    int id = Integer.parseInt(assetId);
	    if (isChecked) {
		hearts.append(id, true);
	    } else {
		hearts.delete(id);
	    }
	}
    }

    private final class HeartsObserver implements Observer {
	@Override
	public void update(Observable observable, Object data) {
	    HeartCheckbox.this.hearts = (GCHeartsMap) data;
	}
    }
}
