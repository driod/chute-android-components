/*
 * Copyright (c) 2010, Sony Ericsson Mobile Communication AB. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this 
 *      list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the Sony Ericsson Mobile Communication AB nor the names
 *      of its contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.chute.android.gcgallery.views.dynamicgallery;

import android.content.Context;
import android.graphics.PointF;
import android.os.Vibrator;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

/** Listener for controlling zoom state through touch events */
public class LongPressZoomListener implements View.OnTouchListener {

    /**
     * Enum defining listener modes. Before the view is touched the listener is
     * in the UNDEFINED mode. Once touch starts it can enter either one of the
     * other two modes: If the user scrolls over the view the listener will
     * enter PAN mode, if the user lets his finger rest and makes a longpress
     * the listener will enter ZOOM mode.
     */
    private enum Mode {
	UNDEFINED, PAN, ZOOM, PINCH, SWIPE
    }

    /** Time of tactile feedback vibration when entering zoom mode */
    private static final long VIBRATE_TIME = 50;

    private static final String TAG = "LongPressZOOM";

    /** Current listener mode */
    private Mode mMode = Mode.UNDEFINED;

    /** Zoom control to manipulate */
    private DynamicZoomControl mZoomControl;

    /** X-coordinate of previously handled touch event */
    private float mX;

    /** Y-coordinate of previously handled touch event */
    private float mY;

    /** X-coordinate of latest down event */
    private float mDownX;

    /** Y-coordinate of latest down event */
    private float mDownY;

    /** Velocity tracker for touch events */
    private VelocityTracker mVelocityTracker;

    /** Distance touch can wander before we think it's scrolling */
    private final int mScaledTouchSlop;

    /** Duration in ms before a press turns into a long press */
    private final int mLongPressTimeout;

    /** Vibrator for tactile feedback */
    private final Vibrator mVibrator;

    /** Maximum velocity for fling */
    private final int mScaledMaximumFlingVelocity;

    public interface ReadyListener {
	public void ready(int result);
    }

    private ReadyListener readyListener;
    private final GestureDetector gestureDetector;
    private final GestureDetector doubleTapGestureDetector;

    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    float prevDist = 1f;

    /**
     * Creates a new instance
     * 
     * @param context
     *            Application context
     */
    public LongPressZoomListener(Context context) {
	mLongPressTimeout = ViewConfiguration.getLongPressTimeout();
	mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	mScaledMaximumFlingVelocity = ViewConfiguration.get(context)
		.getScaledMaximumFlingVelocity();
	mVibrator = (Vibrator) context.getSystemService("vibrator");
	gestureDetector = new GestureDetector(new MyGestureDetector());

	doubleTapGestureDetector = new GestureDetector(new DoubleTapGestureDetector());
    }

    public void addReadyListener(ReadyListener readyListener) {
	this.readyListener = readyListener;
    }

    /**
     * Sets the zoom control to manipulate
     * 
     * @param control
     *            Zoom control
     */
    public void setZoomControl(DynamicZoomControl control) {
	mZoomControl = control;
    }

    /** Runnable that enters zoom mode */
    private final Runnable mLongPressRunnable = new Runnable() {
	@Override
	public void run() {
	    mMode = Mode.ZOOM;
	    Log.d(TAG, "mode=ZOOM");
	    mVibrator.vibrate(VIBRATE_TIME);

	}
    };

    // implements View.OnTouchListener
    @Override
    public boolean onTouch(View v, MotionEvent event) {
	WrapMotionEvent mMotionevent = WrapMotionEvent.wrap(event);
	// dumpEvent(mMotionevent);
	if (mZoomControl.getZoomState().getZoom() == 1.0f) {
	    if (gestureDetector.onTouchEvent(event)) {
		mMode = Mode.SWIPE;
		v.removeCallbacks(mLongPressRunnable);
		// return true;
	    }
	}
	doubleTapGestureDetector.onTouchEvent(event);
	/* if(mMode==Mode.PAN && mZoomControl.getZoomState().getZoom()==1.0f){
		 return true;
	 }*/
	final int action = event.getAction();
	final float x = event.getX();
	final float y = event.getY();

	if (mVelocityTracker == null) {
	    mVelocityTracker = VelocityTracker.obtain();
	}
	mVelocityTracker.addMovement(event);

	switch (action & MotionEvent.ACTION_MASK) {
	case MotionEvent.ACTION_DOWN:

	    mZoomControl.stopFling();
	    v.postDelayed(mLongPressRunnable, mLongPressTimeout);
	    mDownX = x;
	    mDownY = y;
	    mX = x;
	    mY = y;

	    break;
	case MotionEvent.ACTION_POINTER_DOWN:
	    v.removeCallbacks(mLongPressRunnable);
	    oldDist = spacing(mMotionevent);
	    Log.d(TAG, "oldDist=" + oldDist);
	    if (oldDist > 10f) {
		prevDist = oldDist;
		// midPoint(mid, mMotionevent);
		mMode = Mode.PINCH;
		Log.d(TAG, "mode=PINCH");
	    }
	    break;
	case MotionEvent.ACTION_MOVE: {
	    final float dx = (x - mX) / v.getWidth();
	    final float dy = (y - mY) / v.getHeight();

	    if (mMode == Mode.ZOOM) {
		mZoomControl.zoom((float) Math.pow(20, -dy), mDownX / v.getWidth(),
			mDownY / v.getHeight());
	    } else if (mMode == Mode.PINCH) {
		float newDist = spacing(mMotionevent);
		mZoomControl.zoom((newDist) / prevDist, mDownX / v.getWidth(),
			mDownY / v.getHeight());
		prevDist = newDist;
		Log.i(TAG, String.valueOf(newDist / oldDist));
		/* mZoomControl.zoom((float)Math.pow(20, -dy), mDownX / v.getWidth(), mDownY
		      / v.getHeight());*/
	    } else if (mMode == Mode.PAN && mZoomControl.getZoomState().getZoom() != 1.0f) {
		mZoomControl.pan(-dx, -dy);
	    } else {
		if (mZoomControl.getZoomState().getZoom() == 1.0f) {
		    return true;
		}
		final float scrollX = mDownX - x;
		final float scrollY = mDownY - y;

		final float dist = (float) Math.sqrt(scrollX * scrollX + scrollY * scrollY);

		if (dist >= mScaledTouchSlop) {
		    v.removeCallbacks(mLongPressRunnable);
		    mMode = Mode.PAN;
		}
	    }

	    mX = x;
	    mY = y;
	    break;
	}

	case MotionEvent.ACTION_UP:
	    if (mMode == Mode.PAN) {
		mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
		mZoomControl.startFling(-mVelocityTracker.getXVelocity() / v.getWidth(),
			-mVelocityTracker.getYVelocity() / v.getHeight());
	    } else {
		mZoomControl.startFling(0, 0);
	    }
	    mVelocityTracker.recycle();
	    mVelocityTracker = null;
	    v.removeCallbacks(mLongPressRunnable);
	    mMode = Mode.UNDEFINED;
	    break;
	case MotionEvent.ACTION_POINTER_UP:
	    mZoomControl.startFling(0, 0);
	    mVelocityTracker.recycle();
	    mVelocityTracker = null;
	    v.removeCallbacks(mLongPressRunnable);
	    mMode = Mode.UNDEFINED;
	    Log.d(TAG, " mMode = Mode.UNDEFINED;");
	    break;
	default:
	    mVelocityTracker.recycle();
	    mVelocityTracker = null;
	    v.removeCallbacks(mLongPressRunnable);
	    mMode = Mode.UNDEFINED;
	    break;

	}

	return true;
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(WrapMotionEvent event) {
	// ...
	String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP",
		"7?", "8?", "9?" };
	StringBuilder sb = new StringBuilder();
	int action = event.getAction();
	int actionCode = action & MotionEvent.ACTION_MASK;
	sb.append("event ACTION_").append(names[actionCode]);
	if (actionCode == MotionEvent.ACTION_POINTER_DOWN
		|| actionCode == MotionEvent.ACTION_POINTER_UP) {
	    sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
	    sb.append(")");
	}
	sb.append("[");
	for (int i = 0; i < event.getPointerCount(); i++) {
	    sb.append("#").append(i);
	    sb.append("(pid ").append(event.getPointerId(i));
	    sb.append(")=").append((int) event.getX(i));
	    sb.append(",").append((int) event.getY(i));
	    if (i + 1 < event.getPointerCount()) {
		sb.append(";");
	    }
	}
	sb.append("]");
	Log.d(TAG, sb.toString());
    }

    /** Determine the space between the first two fingers */
    private float spacing(WrapMotionEvent event) {
	// ...
	float x = event.getX(0) - event.getX(1);
	float y = event.getY(0) - event.getY(1);
	return FloatMath.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers */
    private PointF midPoint(PointF point, WrapMotionEvent event) {
	// ...
	float x = event.getX(0) + event.getX(1);
	float y = event.getY(0) + event.getY(1);
	point.set(x / 2, y / 2);
	return point;
    }

    private static final int SWIPE_MIN_DISTANCE = 60;
    private static final int SWIPE_MAX_OFF_PATH = 180;
    private static final int SWIPE_THRESHOLD_VELOCITY = 130;

    public class MyGestureDetector extends SimpleOnGestureListener {
	public static final int READY_SWIPE_TO_TOP = 6;
	public static final int READY_SWIPE_TO_BOTTOM = 5;
	public static final int READY_SINGLE_TAP_UP = 3;
	public static final int READY_SWIPE_TO_RIGHT = 2;
	public static final int READY_SWIPE_TO_LEFT = 1;

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	    try {

		// right to left swipe
		if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
			&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
			return false;
		    }
		    if (readyListener != null) {
			readyListener.ready(READY_SWIPE_TO_LEFT);
		    }
		    return true;
		} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
			&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
			return false;
		    }
		    if (readyListener != null) {
			readyListener.ready(READY_SWIPE_TO_RIGHT);
		    }
		    return true;
		} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
			&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
		    if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH) {
			return false;
		    }
		    if (readyListener != null) {
			readyListener.ready(READY_SWIPE_TO_BOTTOM);
		    }
		    return true;
		} else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
			&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
		    if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH) {
			return false;
		    }
		    if (readyListener != null) {
			readyListener.ready(READY_SWIPE_TO_TOP);
		    }
		    return true;
		}

	    } catch (Exception e) {
		// nothing
	    }
	    return false;

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
	    if (readyListener != null) {
		readyListener.ready(READY_SINGLE_TAP_UP);
	    }
	    return true;
	}
    }

    public class DoubleTapGestureDetector extends SimpleOnGestureListener {
	public static final int READY_DOUBLE_TAP_UP = 4;

	@Override
	public boolean onDoubleTap(MotionEvent e) {
	    if (readyListener != null) {
		readyListener.ready(READY_DOUBLE_TAP_UP);
	    }
	    return true;
	}
    }

}
