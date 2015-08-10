package com.campusfeedapp.campusfeed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chinmay on 8/8/15.
 */
public class PostListAdapter extends BaseAdapter {
	public List<PostItemDTO> mPostList;
	private LayoutInflater mInflater;
	private Context context;

	public PostListAdapter(List<PostItemDTO> postItemDTOList,Context context){
		mPostList = postItemDTOList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}
	@Override
	public int getCount() {
		return mPostList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPostList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.post_list_item, parent, false);
			holder = new ViewHolder();
			holder.text = (RobotoTextView) convertView.findViewById(R.id.list_item_google_cards_travel_text);
			holder.image = (ImageView) convertView.findViewById(R.id.list_item_google_cards_travel_image);
			holder.title = (RobotoTextView) convertView.findViewById(R.id.list_item_google_cards_travel_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String postText = mPostList.get(position).getText();
		String postAuth = mPostList.get(position).getUserFullName();
		Picasso.with(context).load(mPostList.get(position).getImgUrl()).centerCrop().fit().into(holder.image);
		holder.text.setText(postText);
		holder.title.setText(postAuth);

		return convertView;
	}

	private static class ViewHolder {
		public RobotoTextView text;
		public RobotoTextView title;
		public ImageView image;
	}
}
