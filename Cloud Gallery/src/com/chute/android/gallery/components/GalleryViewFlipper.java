package com.chute.android.gallery.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ViewFlipper;

import com.chute.android.gallery.R;
import com.chute.android.gallery.zoom.ImageZoomView;
import com.chute.android.gallery.zoom.PinchZoomListener.GestureEvent;
import com.chute.android.gallery.zoom.PinchZoomListener.OnMotionEventListener;
import com.chute.sdk.collections.GCAssetCollection;
import com.chute.sdk.utils.GCUtils;
import com.darko.imagedownloader.ImageLoader;

/**
 * @author DArkO.Grozdanovski
 * 
 */
public class GalleryViewFlipper extends ViewFlipper {

    public static final String TAG = GalleryViewFlipper.class.getSimpleName();
    private ImageLoader fullPhotoLoader;
    private int index;

    private OnMotionEventListener motionListener;
    private GCAssetCollection collection;

    public GalleryViewFlipper(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    private void init() {
	fullPhotoLoader = new ImageLoader(getContext(), R.drawable.placeholder_image_large, true, 0);
	fullPhotoLoader.setDefaultImageSize(GCUtils.pixelsFromDp(getContext(), 300));

	final ImageZoomView viewOne = new ImageZoomView(getContext());
	viewOne.setOnMotionEventListener(new OnMotionEventListenerImplementation());
	this.addView(viewOne);

	final ImageZoomView viewTwo = new ImageZoomView(getContext());
	viewTwo.setOnMotionEventListener(new OnMotionEventListenerImplementation());
	this.addView(viewTwo);
    }

    public GalleryViewFlipper(Context context) {
	super(context);
	init();
    }

    public void setMotionListener(OnMotionEventListener motionListener) {
	this.motionListener = motionListener;
    }

    public void setAssetCollection(GCAssetCollection collection, int index) {
	this.collection = collection;
	this.index = index;
	fullPhotoLoader.displayImage(
		GCUtils.getCustomSizePhotoURL(collection.get(index).getUrl(), 960, 960),
		getCurrentView());
    }

    public void setAssetCollection(GCAssetCollection collection) {
	setAssetCollection(collection, 0);
    }

    @Override
    public void showNext() {
	this.setInAnimation(getContext(), R.anim.slide_in_left);
	this.setOutAnimation(getContext(), R.anim.slide_out_left);
	if (index + 1 < collection.size()) {
	    index++;
	    displayLargePhoto(collection.get(index).getUrl(), getNextChild());
	    super.showNext();
	}
    }

    public void displayLargePhoto(String url, ImageZoomView view) {
	fullPhotoLoader.displayImage(GCUtils.getCustomSizePhotoURL(url, 960, 960), view);
    }

    @Override
    public void showPrevious() {
	this.setInAnimation(getContext(), R.anim.slide_in_right);
	this.setOutAnimation(getContext(), R.anim.slide_out_right);
	if (index - 1 >= 0) {
	    index--;
	    displayLargePhoto(collection.get(index).getUrl(), getNextChild());
	    super.showPrevious();
	}
    }

    public ImageZoomView getNextChild() {
	int displayedChild = getDisplayedChild();
	if (displayedChild == 0) {
	    displayedChild = 1;
	} else {
	    displayedChild = 0;
	}
	return getChildAt(displayedChild);
    }

    private final class OnMotionEventListenerImplementation implements OnMotionEventListener {
	@Override
	public void triggered(GestureEvent event) {
	    if (motionListener != null) {
		motionListener.triggered(event);
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
    }

}
