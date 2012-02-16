package com.chute.android.comments.adapters;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chute.android.comments.R;
import com.chute.sdk.collections.GCCommentCollection;
import com.chute.sdk.model.GCCommentModel;
import com.chute.sdk.utils.GCDateUtil;
import com.darko.imagedownloader.ImageLoader;

public class CommentsAdapter extends BaseAdapter {
    private static final String TAG = CommentsAdapter.class.getSimpleName();
    private final LayoutInflater inflater;
    private final GCCommentCollection collection;
    private final ImageLoader loader;

    private final SimpleDateFormat sdf;

    public CommentsAdapter(Context context, final GCCommentCollection collection) {
	super();
	this.collection = collection;
	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	loader = ImageLoader.getLoader(context);
	sdf = new SimpleDateFormat("MMM dd, yyyy");
    }

    @Override
    public int getCount() {
	return collection.size();
    }

    @Override
    public GCCommentModel getItem(int position) {
	return collection.get(position);
    }

    public void changeData(GCCommentCollection collection) {
	this.collection.clear();
	this.collection.addAll(collection);
	notifyDataSetChanged();
    }

    public void addComment(GCCommentModel model) {
	this.collection.add(model);
	notifyDataSetChanged();
    }

    @Override
    public long getItemId(int arg0) {
	return 0;
    }

    public static class ViewHolder {
	public ImageView img;
	public TextView name;
	public TextView date;
	public TextView comment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View vi = convertView;
	ViewHolder holder;
	if (convertView == null) {
	    vi = inflater.inflate(R.layout.comments_adapter, null);
	    holder = new ViewHolder();
	    holder.img = (ImageView) vi.findViewById(R.id.imageViewThumb);
	    holder.name = (TextView) vi.findViewById(R.id.textViewName);
	    holder.date = (TextView) vi.findViewById(R.id.textViewDate);
	    holder.comment = (TextView) vi.findViewById(R.id.textViewComment);

	    vi.setTag(holder);
	} else {
	    holder = (ViewHolder) vi.getTag();
	}
	loader.displayImage(getItem(position).user.getAvatarURL(), holder.img);
	holder.name.setText(getItem(position).user.getName());

	try {
	    holder.date.setText(sdf.format(GCDateUtil.fromISODateString(getItem(position)
		    .getCreatedAt())));
	} catch (Exception e) {
	    Log.w(TAG, "", e);
	}
	holder.comment.setText(getItem(position).getComment());
	return vi;
    }
}
