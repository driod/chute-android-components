package com.chute.android.imagegrid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chute.android.imagegrid.R;
import com.chute.sdk.collections.GCAssetCollection;
import com.chute.sdk.model.GCAssetModel;
import com.chute.sdk.utils.GCUtils;
import com.darko.imagedownloader.ImageLoader;

public class AssetCollectionAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = AssetCollectionAdapter.class.getSimpleName();
    private static LayoutInflater inflater = null;
    private final GCAssetCollection collection;
    public ImageLoader imageLoader;

    public AssetCollectionAdapter(Context context, final GCAssetCollection collection) {
	this.collection = collection;
	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	imageLoader = ImageLoader.getLoader(context);
    }

    @Override
    public int getCount() {
	return collection.size();
    }

    @Override
    public GCAssetModel getItem(int position) {
	return collection.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    public static class ViewHolder {
	public ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	View vi = convertView;
	ViewHolder holder;
	if (convertView == null) {
	    vi = inflater.inflate(R.layout.adapter_item, null);
	    holder = new ViewHolder();
	    holder.image = (ImageView) vi.findViewById(R.id.imageViewThumb);
	    vi.setTag(holder);
	} else {
	    holder = (ViewHolder) vi.getTag();
	}
	imageLoader.displayImage(
		GCUtils.getCustomSizePhotoURL(collection.get(position).getUrl(), 100, 100),
		holder.image);
	return vi;
    }
    

}
