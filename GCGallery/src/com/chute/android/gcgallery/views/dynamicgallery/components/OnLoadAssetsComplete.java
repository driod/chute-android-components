package com.chute.android.gcgallery.views.dynamicgallery.components;

import com.chute.sdk.collections.GCAssetCollection;

public interface OnLoadAssetsComplete {
    public void onSuccess(GCAssetCollection assets);
}