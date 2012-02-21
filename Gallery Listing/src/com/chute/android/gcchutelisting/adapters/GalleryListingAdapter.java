package com.chute.android.gcchutelisting.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chute.android.gcchutelisting.R;
import com.chute.sdk.collections.GCChuteCollection;
import com.chute.sdk.model.GCChuteModel;
import com.darko.imagedownloader.ImageLoader;

public class GalleryListingAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = GalleryListingAdapter.class.getSimpleName();
    private static LayoutInflater inflater = null;
    private final GCChuteCollection chuteCollection;
    private final ImageLoader loader;

    public GalleryListingAdapter(Activity context, final GCChuteCollection chuteCollection) {
	this.chuteCollection = chuteCollection;
	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	loader = ImageLoader.getLoader(context);
    }

    @Override
    public int getCount() {
	return chuteCollection.size();
    }

    @Override
    public GCChuteModel getItem(int position) {
	return chuteCollection.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    public static class ViewHolder {
	public TextView name;
	public ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View vi = convertView;
	ViewHolder holder;
	if (convertView == null) {
	    vi = inflater.inflate(R.layout.adapter_item, null);
	    holder = new ViewHolder();
	    holder.name = (TextView) vi.findViewById(R.id.txt_album_name);
        holder.image = (ImageView) vi.findViewById(R.id.album_cover);
	    vi.setTag(holder);
	} else {
	    holder = (ViewHolder) vi.getTag();
	}
	holder.name.setText(getItem(position).getName());
	loader.displayImage(getItem(position).getRecentThumbnailURL(), holder.image);
	vi.setTag(R.layout.adapter_item, position);
	return vi;
    }

}
