package com.campusfeedapp.campusfeed.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.campusfeedapp.campusfeed.DTO.ChannelItemDTO;
import com.campusfeedapp.campusfeed.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chinmay on 8/8/15.
 */
public class ChannelListAdapter extends BaseAdapter {
    public List<ChannelItemDTO> mChannelList;
    private LayoutInflater mInflater;
    private Context context;

    public ChannelListAdapter(List<ChannelItemDTO> channelItemDTOList,Context context){
        mChannelList = channelItemDTOList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }
    @Override
    public int getCount() {
        return mChannelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mChannelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.channel_list_item, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.drag_and_drop_travel_text);
            holder.image = (ImageView) convertView.findViewById(R.id.drag_and_drop_travel_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String itemName = mChannelList.get(position).getChannelName();
        Log.e("apple",itemName + " chao");
        Picasso.with(context).load(mChannelList.get(position).getImgUrl()).centerCrop().fit().into(holder.image);

        holder.text.setText(itemName);

        return convertView;
    }

    private static class ViewHolder {
        public /*Roboto*/TextView text;
        public ImageView image;
    }
}
