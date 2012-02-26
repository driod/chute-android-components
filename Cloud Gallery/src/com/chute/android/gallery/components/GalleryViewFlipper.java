package com.chute.android.gallery.components;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import com.chute.android.gallery.util.GalleryThreadPoolExecutor;
import com.chute.android.gallery.zoom.ImageZoomView;
import com.chute.android.gallery.zoom.PinchZoomListener.GestureEvent;
import com.chute.android.gallery.zoom.PinchZoomListener.OnMotionEventListener;
import com.chute.sdk.collections.GCAssetCollection;
import com.chute.sdk.model.GCAssetModel;
import com.chute.sdk.utils.GCUtils;

/**
 * @author DArkO.Grozdanovski
 * 
 */
public class GalleryViewFlipper extends AnimatedSwitcher {

    public static final String TAG = GalleryViewFlipper.class.getSimpleName();
    private int index;

    private GalleryCallback galleryCallback;
    private GCAssetCollection collection;
    private GalleryThreadPoolExecutor executor;
    private Handler handler;

    public enum PhotoChangeErrorType {
	NO_PREVIOUS_ITEM, NO_NEXT_ITEM, GENERAL_ERROR;
    }

    public interface GalleryCallback extends OnMotionEventListener {

	public void onPhotoChanged(int index, GCAssetModel asset);

	public void onPhotoChangeError(PhotoChangeErrorType error);
    }

    public GalleryViewFlipper(Context context) {
	super(context);
	init();
    }

    public GalleryViewFlipper(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    private void init() {
	final OnMotionEventListenerImplementation motionEventListener = new OnMotionEventListenerImplementation();

	final ImageZoomView viewOne = new ImageZoomView(getContext());
	viewOne.setOnMotionEventListener(motionEventListener);
	this.addView(viewOne);
	final ImageZoomView viewTwo = new ImageZoomView(getContext());
	viewTwo.setOnMotionEventListener(motionEventListener);
	this.addView(viewTwo);
	handler = new Handler();
	executor = new GalleryThreadPoolExecutor(getContext());
	executor.addObserver(new ObserverImplementation());
    }

    public void setGalleryCallback(GalleryCallback galleryCallback) {
	this.galleryCallback = galleryCallback;
    }

    public void setAssetCollection(GCAssetCollection collection, int index) {
	this.collection = collection;
	this.index = index;
	displayLargePhoto(collection.get(index).getUrl());
	triggerPhotoChangedCallback();
    }

    public void setAssetCollection(GCAssetCollection collection) {
	setAssetCollection(collection, 0);
    }

    public GCAssetCollection getAssetCollection() {
	return collection;
    }

    @Override
    public void showNext() {
	if (index + 1 >= collection.size()) {
	    triggerPhotoChangeError(PhotoChangeErrorType.NO_NEXT_ITEM);
	    return;
	}
	index++;
	super.showNext();
	displayLargePhoto(collection.get(index).getUrl());
	triggerPhotoChangedCallback();
    }

    @Override
    public void showPrevious() {
	if (index - 1 < 0) {
	    triggerPhotoChangeError(PhotoChangeErrorType.NO_PREVIOUS_ITEM);
	    return;
	}
	index--;
	super.showPrevious();
	displayLargePhoto(collection.get(index).getUrl());
	triggerPhotoChangedCallback();
    }

    private void triggerPhotoChangeError(PhotoChangeErrorType type) {
	if (galleryCallback != null) {
	    galleryCallback.onPhotoChangeError(type);
	}
    }

    private void triggerPhotoChangedCallback() {
	if (galleryCallback != null) {
	    galleryCallback.onPhotoChanged(index, collection.get(index));
	}
    }

    public GCAssetModel getSelectedItem() {
	return collection.get(index);
    }

    private void displayLargePhoto(String url) {
	Log.d(TAG, "in display large photo");
	displayCurrentPhoto(null);
	executor.runTask(GCUtils.getCustomSizePhotoURL(url, 960, 960));
    }

    public void displayCurrentPhoto(String url) {
	executor.getLoader().displayImage(url, getCurrentView());
    }

    public boolean isCurrentlySelectedPhoto(String url) {
	return url.contentEquals(GCUtils.getCustomSizePhotoURL(collection.get(index).getUrl(), 960,
		960));
    }

    public ImageZoomView getNextChild() {
	int displayedChild = getDisplayedChild();
	displayedChild = (getDisplayedChild() == 0) ? 1 : 0;
	return getChildAt(displayedChild);
    }

    private final class OnMotionEventListenerImplementation implements OnMotionEventListener {
	@Override
	public void triggered(GestureEvent event) {
	    if (galleryCallback != null) {
		galleryCallback.triggered(event);
	    }
	    Log.d(TAG, event.toString());
	    switch (event) {
	    case DOUBLE_TAP:
		getCurrentView().resetZoomState();
		break;
	    case SWIPE_TO_RIGHT:
		showPrevious();
		break;
	    case SWIPE_TO_LEFT:
		showNext();
		break;
	    default:
		break;
	    }
	}
    }

    @Override
    public ImageZoomView getCurrentView() {
	return (ImageZoomView) super.getCurrentView();
    }

    @Override
    public ImageZoomView getChildAt(int index) {
	return (ImageZoomView) super.getChildAt(index);
    }

    public void destroyGallery() {
	for (int i = 0; i < this.getChildCount(); i++) {
	    this.getChildAt(i).destroyView();
	}
	executor.shutDown();
    }

    private final class ObserverImplementation implements Observer {
	@Override
	public void update(Observable observable, final Object data) {
	    if (isCurrentlySelectedPhoto((String) data)) {

		handler.post(new Runnable() {

		    @Override
		    public void run() {
			displayCurrentPhoto((String) data);
		    }
		});

	    }
	}
    }

}
