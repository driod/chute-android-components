package com.chute.android.gcgallery.views.dynamicgallery.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

import com.chute.android.gcgallery.R;

public class GalleryViewFlipper extends ViewFlipper {

    @SuppressWarnings("unused")
    private static final String TAG = GalleryViewFlipper.class.getSimpleName();

    public GalleryViewFlipper(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    private void init() {
    }

    public GalleryViewFlipper(Context context) {
	super(context);
	init();
    }

    @Override
    public void showNext() {
	this.setInAnimation(getContext(), R.anim.slide_in_left);
	this.setOutAnimation(getContext(), R.anim.slide_out_left);
	super.showNext();
    }

    @Override
    public void showPrevious() {
	this.setInAnimation(getContext(), R.anim.slide_in_right);
	this.setOutAnimation(getContext(), R.anim.slide_out_right);
	super.showPrevious();
    }
}
