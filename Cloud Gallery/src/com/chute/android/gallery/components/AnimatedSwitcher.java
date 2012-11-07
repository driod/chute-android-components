package com.chute.android.gallery.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewSwitcher;

import com.chute.android.gallery.util.AnimationFactory;

public class AnimatedSwitcher extends ViewSwitcher {

	private static final int DEFAULT_DURATION = 150;
	public static final String TAG = AnimatedSwitcher.class.getSimpleName();

	public AnimatedSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AnimatedSwitcher(Context context) {
		super(context);
	}

	@Override
	public void showNext() {
		this.setInAnimation(AnimationFactory
				.getSlideInLeftAnimation(DEFAULT_DURATION));
		this.setOutAnimation(AnimationFactory
				.getSlideOutLeftAnimation(DEFAULT_DURATION));
		super.showNext();
	}

	@Override
	public void showPrevious() {
		this.setInAnimation(AnimationFactory
				.getSlideInRightAnimation(DEFAULT_DURATION));
		this.setOutAnimation(AnimationFactory
				.getSlideOutRightAnimation(DEFAULT_DURATION));
		super.showPrevious();
	}

}
