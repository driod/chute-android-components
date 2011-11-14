package com.chute.android.gcgallery.views.dynamicgallery.components;

// Task for the queue
public class PhotoToLoad {
    public String url;
    public Direction direction;

    public PhotoToLoad(String url, Direction direction) {
	this.url = url;
	this.direction = direction;
    }
}