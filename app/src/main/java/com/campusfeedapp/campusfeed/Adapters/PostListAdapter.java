package com.campusfeedapp.campusfeed.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.campusfeedapp.campusfeed.DTO.PostItemDTO;
import com.campusfeedapp.campusfeed.ImageDisplayActivity;
import com.campusfeedapp.campusfeed.R;
import com.campusfeedapp.campusfeed.CustomViews.RobotoTextView;
import com.campusfeedapp.campusfeed.Utils.Constants;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.post_list_item, parent, false);
			holder = new ViewHolder();
			holder.text = (RobotoTextView) convertView.findViewById(R.id.list_item_google_cards_travel_text);
			holder.image = (ImageView) convertView.findViewById(R.id.list_item_google_cards_travel_image);
			holder.createdTime = (RobotoTextView) convertView.findViewById(R.id.list_item_created_time_post);
			holder.views = (RobotoTextView) convertView.findViewById(R.id.list_item_views_post);
			holder.imageWrapper = (RelativeLayout) convertView.findViewById(R.id.main_image_layout_post);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String postText = mPostList.get(position).getText();
		String postTime = mPostList.get(position).getTime();
		String postViews = mPostList.get(position).getNumViews();
		if(mPostList.get(position).getImgUrl().isEmpty()){
			holder.image.setVisibility(View.GONE);
			holder.imageWrapper.setVisibility(View.GONE);
		}
		else {
			Picasso.with(context).load(mPostList.get(position).getImgUrl()).centerCrop().fit().into(holder.image);
		}
		holder.text.setText(postText);
		holder.createdTime.setText(postTime);
		holder.views.setText(context.getApplicationContext().getString(R.string.eye) + " " + postViews);

		holder.image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ImageDisplayActivity.class);
				intent.putExtra("img_url",mPostList.get(position).getImgUrl());
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	private static class ViewHolder {
		public RobotoTextView text;
		public RobotoTextView createdTime;
		public RobotoTextView views;
		public RelativeLayout imageWrapper;
		public ImageView image;
	}
}
