package com.chute.android.comments.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chute.android.comments.R;
import com.chute.sdk.v2.model.CommentModel;
import com.chute.sdk.v2.utils.DateUtil;

import darko.imagedownloader.ImageLoader;

public class PhotoCommentsAdapter extends BaseAdapter {
	private static final String TAG = PhotoCommentsAdapter.class
			.getSimpleName();
	private final LayoutInflater inflater;
	private final List<CommentModel> collection;
	private final ImageLoader loader;

	private final SimpleDateFormat sdf;

	public PhotoCommentsAdapter(Context context,
			final List<CommentModel> collection) {
		super();
		this.collection = collection;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		loader = ImageLoader.getLoader(context);
		sdf = new SimpleDateFormat("MMM dd, yyyy");
	}

	@Override
	public int getCount() {
		return collection.size();
	}

	@Override
	public CommentModel getItem(int position) {
		return collection.get(position);
	}

	public void changeData(List<CommentModel> collection) {
		this.collection.clear();
		this.collection.addAll(collection);
		notifyDataSetChanged();
	}

	public void addComment(CommentModel model) {
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
		// loader.displayImage(getItem(position).user.getAvatarURL(),
		// holder.img);
		// holder.name.setText(getItem(position).user.getName());

		try {
			holder.date.setText(sdf.format(DateUtil.fromISODateString(getItem(
					position).getCreatedAt())));
		} catch (Exception e) {
			Log.w(TAG, "", e);
		}
		holder.comment.setText(getItem(position).getCommentText());
		return vi;
	}
}
