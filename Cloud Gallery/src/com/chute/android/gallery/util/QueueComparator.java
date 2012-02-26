package com.chute.android.gallery.util;

import java.util.Comparator;

import com.chute.android.gallery.util.GalleryThreadPoolExecutor.GalleryRunnable;

class QueueComparator<T extends GalleryRunnable> implements Comparator<T> {

    public static final String TAG = QueueComparator.class.getSimpleName();

    @Override
    public int compare(T object1, T object2) {
	if (object1.getTime() == object2.getTime()) {
	    return 0;
	}
	if (object1.getTime() < object2.getTime()) {
	    return 1;
	}
	return -1;
    }
}
