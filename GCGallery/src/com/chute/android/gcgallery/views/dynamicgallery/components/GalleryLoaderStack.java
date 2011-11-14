package com.chute.android.gcgallery.views.dynamicgallery.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.darko.imagedownloader.BitmapContentHandler;

public class GalleryLoaderStack {
    @SuppressWarnings("unused")
    private static final String TAG = GalleryLoaderStack.class.getSimpleName();

    PhotosQueue photosQueue = new PhotosQueue();
    PhotosLoader photoLoaderThread = new PhotosLoader();

    private final OnImageDownloaded onImageDownloaded;

    private final Context context;
    private final Handler handler;

    private final BitmapContentHandler bitmapHandler;

    public GalleryLoaderStack(Context context, OnImageDownloaded onImageDownloaded) {
	this.context = context;
	handler = new Handler(context.getMainLooper());
	this.onImageDownloaded = onImageDownloaded;
	photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
	bitmapHandler = new BitmapContentHandler();
	bitmapHandler.setRequiredSize(320);
    }

    public void queuePhoto(String url, Direction direction) {
	// This ImageView may be used for other images before. So there may be
	// some old tasks in the queue. We need to discard them.
	PhotoToLoad p = new PhotoToLoad(AppUtil.getMobileSmallUrl(url), direction);
	synchronized (photosQueue.photosToLoad) {
	    if (direction == Direction.DUMMY) {
		photosQueue.photosToLoad.add(0, p);
	    } else {
		photosQueue.photosToLoad.push(p);
	    }
	    photosQueue.photosToLoad.notifyAll();
	}

	// start thread if it's not started yet
	if (photoLoaderThread.getState() == Thread.State.NEW) {
	    photoLoaderThread.start();
	}
    }

    class PhotosLoader extends Thread {
	@Override
	public void run() {
	    try {
		while (true) {
		    // thread waits until there are any images to load in the
		    // queue
		    if (photosQueue.photosToLoad.size() == 0) {
			synchronized (photosQueue.photosToLoad) {
			    photosQueue.photosToLoad.wait();
			}
		    }
		    if (photosQueue.photosToLoad.size() != 0) {
			PhotoToLoad photoToLoad;
			synchronized (photosQueue.photosToLoad) {
			    photoToLoad = photosQueue.photosToLoad.pop();
			}

			try {
			    if (!AppUtil.checkIfExistsInLargePhotoCache(context, photoToLoad.url)) {
				downloadFile(photoToLoad.url);
			    }

			    if (photoToLoad.direction != Direction.DUMMY) {
				BitmapDisplayer bd = new BitmapDisplayer(photoToLoad);
				handler.post(bd);
			    }
			} catch (Exception e) {
			    Log.w(TAG, "", e);
			}
		    }
		    if (Thread.interrupted()) {
			break;
		    }
		}
	    } catch (InterruptedException e) {
		// allow thread to exit
	    }
	}
    }

    public void stopThread() {
	photoLoaderThread.interrupt();
    }

    // stores list of photos to download
    class PhotosQueue {
	private final Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {

	private final PhotoToLoad photoToLoad;

	public BitmapDisplayer(PhotoToLoad photoToLoad) {
	    this.photoToLoad = photoToLoad;
	}

	@Override
	public void run() {
	    onImageDownloaded.setImageToView(photoToLoad);
	}
    }

    public boolean downloadFile(String fileUrl) {
	// Log.e("File URL IMAGE FULLSCREEN", fileUrl);
	if (fileUrl.contentEquals("")) {
	    return false;
	}
	File file;
	try {
	    file = AppUtil.getLargePhotoStoreFile(context, fileUrl);
	} catch (Exception e1) {
	    Log.w(TAG, "", e1);
	    return false;
	}
	if (file != null && file.exists()) {
	    if (file.length() > 0) {
		return true;
	    } else {
		file.delete();
	    }
	}

	try {
	    Bitmap bmp = bitmapHandler.getContent(new URL(fileUrl).openConnection());
	    if (bmp != null) {
		file.createNewFile();
		OutputStream outStream = new FileOutputStream(file);
		bmp.compress(Bitmap.CompressFormat.PNG, 90, outStream);
		bmp.recycle();
		return true;
	    }
	} catch (IOException e) {
	    Log.w(TAG, "", e);
	} catch (NullPointerException e) {
	    Log.w(TAG, "", e);
	} catch (OutOfMemoryError e) {
	    Log.w(TAG, "", e);
	}
	if (file != null && file.exists() && file.length() > 0) {
	    file.delete();
	}
	return false;
    }

}
