package com.chute.android.gcgallery.views.dynamicgallery.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.chute.android.gcgallery.views.dynamicgallery.DynamicZoomControl;
import com.chute.android.gcgallery.views.dynamicgallery.ImageZoomView;
import com.chute.android.gcgallery.views.dynamicgallery.LongPressZoomListener;
import com.chute.android.gcgallery.views.dynamicgallery.LongPressZoomListener.DoubleTapGestureDetector;
import com.chute.android.gcgallery.views.dynamicgallery.LongPressZoomListener.MyGestureDetector;
import com.chute.android.gcgallery.views.dynamicgallery.LongPressZoomListener.ReadyListener;
import com.chute.sdk.collections.GCAssetCollection;
import com.chute.sdk.model.GCAssetModel;

public class GalleryWidget extends FrameLayout {
    private static final int PROGRESS_BAR_SIZE = 80;

    @SuppressWarnings("unused")
    private static final String TAG = GalleryWidget.class.getSimpleName();

    public interface OnPhotoChangedCallback {
	public void onChanged();

	public void onGestureTriggered(int direction);
    }

    private int currentShownAsset = ListView.INVALID_POSITION;

    private ImageZoomView imageZoomViewOne;
    private ImageZoomView imageZoomViewTwo;
    /** Zoom control */
    private DynamicZoomControl mZoomControlOne;
    private LongPressZoomListener mZoomListenerOne;
    private DynamicZoomControl mZoomControlTwo;
    private LongPressZoomListener mZoomListenerTwo;

    private ReadyListenerImpl readyListener;

    private GalleryViewFlipper flipper;

    private ProgressBar pb;

    private GalleryLoaderStack galleryLoaderStack;
    private FadeViewAnimationController fadeAnimationController;

    private DisplayImageTask task;

    private GCAssetCollection collection = new GCAssetCollection();

    private OnPhotoChangedCallback photoChangedCallback;

    private Bitmap placeholder;

    public GalleryWidget(Context context) {
	super(context);
	init();
    }

    public GalleryWidget(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	init();
    }

    public GalleryWidget(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    private void init() {
	this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	flipper = new GalleryViewFlipper(getContext());
	flipper.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	readyListener = new ReadyListenerImpl();
	imageZoomViewOne = new ImageZoomView(getContext());
	mZoomControlOne = new DynamicZoomControl();
	mZoomListenerOne = new LongPressZoomListener(getContext());
	mZoomListenerOne.setZoomControl(mZoomControlOne);
	mZoomListenerOne.addReadyListener(readyListener);

	// INIT ImageZoomViewTWO
	imageZoomViewTwo = new ImageZoomView(getContext());
	mZoomControlTwo = new DynamicZoomControl();
	mZoomListenerTwo = new LongPressZoomListener(getContext());
	mZoomListenerTwo.setZoomControl(mZoomControlTwo);
	mZoomListenerTwo.addReadyListener(readyListener);

	flipper.addView(imageZoomViewOne);
	flipper.addView(imageZoomViewTwo);
	this.addView(flipper);

	pb = new ProgressBar(getContext());
	LayoutParams layoutParams = new LayoutParams(GalleryWidget.PROGRESS_BAR_SIZE,
		GalleryWidget.PROGRESS_BAR_SIZE);
	layoutParams.gravity = Gravity.CENTER;
	pb.setLayoutParams(layoutParams);
	this.addView(pb);

	galleryLoaderStack = new GalleryLoaderStack(getContext(), new OnImageDownloadedImpl());
	fadeAnimationController = FadeViewAnimationController.getInstance();
    }

    public void resetZoomState() {
	mZoomControlOne.getZoomState().setPanX(0.5f);
	mZoomControlOne.getZoomState().setPanY(0.5f);
	mZoomControlOne.getZoomState().setZoom(1f);
	mZoomControlOne.getZoomState().notifyObservers();

	mZoomControlTwo.getZoomState().setPanX(0.5f);
	mZoomControlTwo.getZoomState().setPanY(0.5f);
	mZoomControlTwo.getZoomState().setZoom(1f);
	mZoomControlTwo.getZoomState().notifyObservers();
    }

    public GalleryLoaderStack getGalleryLoaderStack() {
	return galleryLoaderStack;
    }

    public FadeViewAnimationController getFadeAnimationController() {
	return fadeAnimationController;
    }

    public GCAssetCollection getCollection() {
	return collection;
    }

    public void setCollection(final GCAssetCollection collection) {
	setCollection(collection, currentShownAsset);
    }

    public int getCurrentShownAssetPosition() {
	return currentShownAsset;
    }

    public GCAssetModel getCurrentShownAsset() {
	return collection.get(currentShownAsset);
    }

    public OnPhotoChangedCallback getPhotoChangedCallback() {
	return photoChangedCallback;
    }

    public void setPhotoChangedCallback(OnPhotoChangedCallback photoChangedCallback) {
	this.photoChangedCallback = photoChangedCallback;
    }

    public void setCollection(final GCAssetCollection collection, int initPosition) {
	this.collection = collection;
	currentShownAsset = initPosition;
	imageZoomViewOne.setZoomState(mZoomControlOne.getZoomState());
	imageZoomViewOne.setOnTouchListener(mZoomListenerOne);
	mZoomControlOne.setAspectQuotient(imageZoomViewOne.getAspectQuotient());
	imageZoomViewTwo.setZoomState(mZoomControlTwo.getZoomState());
	imageZoomViewTwo.setOnTouchListener(mZoomListenerTwo);
	mZoomControlTwo.setAspectQuotient(imageZoomViewTwo.getAspectQuotient());

	displayAssetOnCurrentPosition();
    }

    private void displayAssetOnCurrentPosition() {
	if (currentShownAsset == ListView.INVALID_POSITION) {
	    currentShownAsset = 0;
	}

	if (task == null || task.getStatus() == Status.FINISHED) {
	    task = new DisplayImageTask(currentShownAsset, Direction.STATIONARY);
	    task.execute();
	}
    }

    public void displayAssetOnPosition(int position) {
	currentShownAsset = position;
	displayAssetOnCurrentPosition();
    }

    private final class OnImageDownloadedImpl implements OnImageDownloaded {
	@Override
	public void setImageToView(PhotoToLoad photoToLoad) {
	    new DispayDownloadedPhotoTask(photoToLoad).execute();
	}
    }

    private final class ReadyListenerImpl implements ReadyListener {
	@Override
	public void ready(int result) {
	    switch (result) {
	    case MyGestureDetector.READY_SWIPE_TO_LEFT:
		if (task == null || task.getStatus() == Status.FINISHED) {
		    task = new DisplayImageTask(currentShownAsset, Direction.FORWARD);
		    task.execute();
		}
		break;
	    case MyGestureDetector.READY_SWIPE_TO_RIGHT:
		if (task == null || task.getStatus() == Status.FINISHED) {
		    task = new DisplayImageTask(currentShownAsset, Direction.BACKWARDS);
		    task.execute();
		}
		break;
	    case MyGestureDetector.READY_SINGLE_TAP_UP:
		fadeAnimationController.setActive();
		break;
	    case DoubleTapGestureDetector.READY_DOUBLE_TAP_UP:
		resetZoomState();
		fadeAnimationController.setActive();
		break;
	    default:
		break;
	    }
	    if (photoChangedCallback != null) {
		photoChangedCallback.onGestureTriggered(result);
	    }
	}
    }

    private class DisplayImageTask extends AsyncTask<Void, Void, Boolean> {

	@Override
	protected void onProgressUpdate(Void... values) {
	    pb.setVisibility(View.VISIBLE);
	    fadeAnimationController.setActive();
	    super.onProgressUpdate(values);
	}

	private int position;
	private final Direction direction;

	public DisplayImageTask(int position, Direction direction) {
	    this.position = position;
	    this.direction = direction;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
	    try {
		if (direction == Direction.FORWARD) {
		    position++;
		} else if (direction == Direction.BACKWARDS) {
		    position--;
		} else if (direction == Direction.STATIONARY) {
		    publishProgress();
		    galleryLoaderStack.queuePhoto(collection.get(position).getUrl(),
			    Direction.STATIONARY);

		    try {
			galleryLoaderStack.queuePhoto(collection.get(position + 1).getUrl(),
				Direction.DUMMY);
		    } catch (Exception e) {
			// Do nothing
		    }

		    try {
			galleryLoaderStack.queuePhoto(collection.get(position - 1).getUrl(),
				Direction.DUMMY);
		    } catch (Exception e) {
			// Do nothing
		    }
		    return true;
		}
		if (position >= 0 && position < collection.size()) {
		    galleryLoaderStack.queuePhoto(collection.get(position).getUrl(), direction);
		    publishProgress();

		    try {
			if (currentShownAsset < position) {
			    galleryLoaderStack.queuePhoto(collection.get(position + 1).getUrl(),
				    Direction.DUMMY);
			} else {
			    galleryLoaderStack.queuePhoto(collection.get(position - 1).getUrl(),
				    Direction.DUMMY);
			}
		    } catch (Exception e) {
			// Do nothing
		    }
		    currentShownAsset = position;
		    return true;
		}
	    } catch (Exception e) {
		Log.w(TAG, "", e);
	    }
	    return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    if (result == false) {
		pb.setVisibility(View.GONE);
	    }
	    super.onPostExecute(result);
	}
    }

    private class DispayDownloadedPhotoTask extends AsyncTask<Void, Void, Bitmap> {

	private final PhotoToLoad photoToLoad;

	public DispayDownloadedPhotoTask(PhotoToLoad photoToLoad) {
	    this.photoToLoad = photoToLoad;
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
	    return decodeBitmap(photoToLoad.url);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
	    pb.setVisibility(View.GONE);
	    if (result == null) {
		return;
	    }
	    switch (photoToLoad.direction) {
	    case STATIONARY:
		((ImageZoomView) flipper.getChildAt(flipper.getDisplayedChild())).setImage(result);
		break;
	    case FORWARD:
		((ImageZoomView) flipper.getChildAt(flipper.getDisplayedChild() == 0 ? 1 : 0))
			.setImage(result);
		flipper.showNext();
		break;
	    case BACKWARDS:
		((ImageZoomView) flipper.getChildAt(flipper.getDisplayedChild() == 0 ? 1 : 0))
			.setImage(result);
		flipper.showPrevious();
		break;
	    }

	    fadeAnimationController.setActive();
	    resetZoomState();
	    if (GalleryWidget.this.photoChangedCallback != null) {
		photoChangedCallback.onChanged();
	    }
	    super.onPostExecute(result);
	}
    }

    public Bitmap decodeBitmap(String fileUrl) {
	// Log.e("File URL IMAGE FULLSCREEN", fileUrl);
	// URL myFileUrl = null;
	if (TextUtils.isEmpty(fileUrl)) {
	    return getPlaceholder();
	}
	Bitmap bmp;
	try {
	    bmp = BitmapFactory.decodeFile(AppUtil.getLargePhotoStoreFile(getContext(), fileUrl)
		    .getPath());
	    if (bmp != null) {
		return bmp;
	    }
	} catch (Exception e1) {
	    e1.printStackTrace();
	} catch (OutOfMemoryError e) {
	    System.gc();
	}

	return getPlaceholder();
    }

    public Bitmap getPlaceholder() {
	if (placeholder == null) {
	    placeholder = BitmapFactory.decodeResource(getContext().getResources(),
		    R.drawable.placeholder_image_large);
	}
	return placeholder;
    }

    public void recyclePlaceholder() {
	if (placeholder != null) {
	    placeholder.recycle();
	}
    }

    @Override
    protected void finalize() throws Throwable {
	Log.e(TAG, "Finalizing");
	try {
	    if (imageZoomViewOne.getImage() != null) {
		imageZoomViewOne.getImage().recycle();
		imageZoomViewOne.setOnTouchListener(null);
		mZoomControlOne.getZoomState().deleteObservers();
	    }
	    if (imageZoomViewTwo.getImage() != null) {
		imageZoomViewTwo.getImage().recycle();
		imageZoomViewTwo.setOnTouchListener(null);
		mZoomControlTwo.getZoomState().deleteObservers();
	    }
	    try {
		galleryLoaderStack.stopThread();
	    } catch (Exception e) {
		Log.w(TAG, "", e);
	    }
	    FadeViewAnimationController.getInstance().clearList();
	} catch (Exception e) {
	    Log.w(TAG, "", e);
	}
	super.finalize();
    }
}
