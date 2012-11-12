package com.chute.android.gallery.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Observable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.util.Log;

import com.chute.android.gallery.R;
import com.chute.sdk.v2.utils.Utils;

import darko.imagedownloader.BlockingFilterInputStream;
import darko.imagedownloader.ContentURLStreamHandlerFactory;
import darko.imagedownloader.FileCache;
import darko.imagedownloader.ImageLoader;

public class GalleryThreadPoolExecutor extends Observable {

	public static final String TAG = GalleryThreadPoolExecutor.class
			.getSimpleName();

	int poolSize = 3;
	int maxPoolSize = 3;
	long keepAliveTime = 10L;
	private final ThreadPoolExecutor threadPool;
	private boolean isDownloading = false;

	public boolean getIsDownloading() {
		return isDownloading;
	}

	public void setDownloading(boolean isDownloading) {
		this.isDownloading = isDownloading;
	}

	public FileCache getFileCache() {
		return fileCache;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	final PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(
			3, new QueueComparator());

	private final ImageLoader loader;
	private final FileCache fileCache;
	private final ContentURLStreamHandlerFactory streamFactory;

	public GalleryThreadPoolExecutor(final Context context) {
		threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize,
				keepAliveTime, TimeUnit.SECONDS, queue);
		loader = new ImageLoader(context, R.drawable.placeholder_image_large);
		loader.setDefaultBitmapSize(Utils.pixelsFromDp(context, 320));
		// loader.setOnlyFromCache(true);
		fileCache = new FileCache(context);
		streamFactory = new ContentURLStreamHandlerFactory(
				context.getContentResolver());
	}

	public void runTask(final String url) {
		threadPool.execute(new GalleryRunnable(url));
	}

	public void shutDown() {
		deleteObservers();
		threadPool.shutdown();
		loader.stopThread();
	}

	public ImageLoader getLoader() {
		return loader;
	}

	public class GalleryRunnable implements Runnable {

		private final long time;
		private final String url;

		public GalleryRunnable(final String url) {
			super();
			this.url = url;
			this.time = System.nanoTime();
		}

		public long getTime() {
			return time;
		}

		@Override
		public void run() {
			Log.d(TAG, "in runnable " + url);
			final File f = fileCache.getFile(url);
			Log.d(TAG, f.length() + " File size");
			if (f.exists() == false || f.length() == 0) {
				try {
					Log.d(TAG, "Downloading ");
					setDownloading(true);
					downloadFile(f, url);
				} catch (final MalformedURLException e) {
					Log.d(TAG, "", e);
				} catch (final IOException e) {
					Log.d(TAG, "", e);
				}
			}
			setChanged();
			notifyObservers(url);
		}

		public void downloadFile(final File f, final String url)
				throws MalformedURLException, IOException {
			final String protocol = darko.imagedownloader.Utils
					.getProtocol(url);
			final URLStreamHandler streamHandler = streamFactory
					.createURLStreamHandler(protocol);

			InputStream input = new URL(null, url, streamHandler)
					.openConnection().getInputStream();
			input = new BlockingFilterInputStream(input);

			final FileOutputStream fileOutput = new FileOutputStream(f);
			final byte[] buffer = new byte[512 * 512];
			int bufferLength = 0; // used to store a temporary size of the
			// buffer
			while ((bufferLength = input.read(buffer)) > 0) {
				// add the data in the buffer to the file in the file output
				// stream (the file on SDcard)
				fileOutput.write(buffer, 0, bufferLength);
			}
			fileOutput.flush();
			fileOutput.close();
		}
	}

	public void deleteCachedImage(String url) {
		if (fileCache.getFile(url).exists()) {
			fileCache.getFile(url).delete();
		}
		if (fileCache.getFile(url + "/960x960").exists()) {
			fileCache.getFile(url + "/960x960").delete();
		}
	}
}