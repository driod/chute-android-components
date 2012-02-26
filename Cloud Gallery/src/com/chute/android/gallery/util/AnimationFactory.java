package com.chute.android.gallery.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

public class AnimationFactory {

    public static final String TAG = AnimationFactory.class.getSimpleName();

    public static Animation getSlideInLeftAnimation(final long duration) {
	final AnimationSet set = new AnimationSet(true);
	set.addAnimation(getTranslateAnimation(duration, 1.0f, 0.0f, 0.0f, 0.0f));
	set.addAnimation(getAlphaAnimationFadeIn(duration));
	return set;
    }

    public static Animation getSlideInRightAnimation(final long duration) {
	final AnimationSet set = new AnimationSet(true);
	set.addAnimation(getTranslateAnimation(duration, -1.0f, 0.0f, 0.0f, 0.0f));
	set.addAnimation(getAlphaAnimationFadeIn(duration));
	return set;
    }

    public static Animation getSlideOutRightAnimation(final long duration) {
	final AnimationSet set = new AnimationSet(true);
	set.addAnimation(getTranslateAnimation(duration, 0.0f, 1.0f, 0.0f, 0.0f));
	set.addAnimation(getAlphaAnimationFadeOut(duration));
	return set;
    }

    public static Animation getSlideOutLeftAnimation(final long duration) {
	final AnimationSet set = new AnimationSet(true);
	set.addAnimation(getTranslateAnimation(duration, 0.0f, -1.0f, 0.0f, 0.0f));
	set.addAnimation(getAlphaAnimationFadeOut(duration));
	return set;
    }

    public static Animation getAlphaAnimationFadeIn(long duration) {
	return getAlphaAnimation(duration, 0.0f, 1.0f);
    }

    public static Animation getAlphaAnimationFadeOut(long duration) {
	return getAlphaAnimation(duration, 1.0f, 0.0f);
    }

    public static Animation getAlphaAnimation(long duration, float from, float to) {
	final AlphaAnimation alpha = new AlphaAnimation(from, to);
	alpha.setDuration(duration);
	return alpha;
    }

    public static Animation getTranslateAnimation(long duration, float fromX, float toX,
	    float fromY, float toY) {
	final TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
		fromX, Animation.RELATIVE_TO_PARENT, toX, Animation.ABSOLUTE, fromY,
		Animation.ABSOLUTE, toY);
	translate.setDuration(duration);
	return translate;
    }
}
