package com.campusfeedapp.campusfeed;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.campusfeedapp.campusfeed.R;


public class DrawerAdapter extends BaseAdapter {

    private List<String> mDrawerItems;
    private LayoutInflater mInflater;

    public DrawerAdapter(Context context,List<String> items) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDrawerItems = items;
    }

    @Override
    public int getCount() {
        return mDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //@Override
    //public long getItemId(int position) {
    //    return mDrawerItems.get(position).getTag();
    //}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_list_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String itemName = mDrawerItems.get(position);

        holder.title.setText(itemName);

        return convertView;
    }

    private static class ViewHolder {
        public /*Roboto*/TextView title;
    }
}
