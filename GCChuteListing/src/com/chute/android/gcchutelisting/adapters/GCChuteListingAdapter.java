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

public class GCChuteListingAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = GCChuteListingAdapter.class.getSimpleName();
    private static LayoutInflater inflater = null;
    private final GCChuteCollection chuteCollection;
    private final ImageLoader loader;

    public GCChuteListingAdapter(Activity context, final GCChuteCollection chuteCollection) {
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
	public TextView membersCount;
	public TextView photoCount;
	public ImageView thumb;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View vi = convertView;
	ViewHolder holder;
	if (convertView == null) {
	    vi = inflater.inflate(R.layout.adapter_item, null);
	    holder = new ViewHolder();
	    holder.thumb = (ImageView) vi.findViewById(R.id.imageViewThumb);
	    holder.name = (TextView) vi.findViewById(R.id.textViewName);
	    holder.photoCount = (TextView) vi.findViewById(R.id.textViewPhotoCount);
	    holder.membersCount = (TextView) vi.findViewById(R.id.textViewMemberCount);

	    vi.setTag(holder);
	} else {
	    holder = (ViewHolder) vi.getTag();
	}
	loader.displayImage(getItem(position).getRecentThumbnailURL(), holder.thumb);
	holder.membersCount.setText(getItem(position).getMembersCount());
	holder.name.setText(getItem(position).getName());
	holder.photoCount.setText(getItem(position).getAssetsCount());
	vi.setTag(R.layout.adapter_item, position);
	return vi;
    }

}
