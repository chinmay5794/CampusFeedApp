package com.campusfeedapp.campusfeed.Fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.campusfeedapp.campusfeed.CustomViews.AnimatedExpandableListView;
import com.campusfeedapp.campusfeed.CustomViews.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.campusfeedapp.campusfeed.AsyncTasks.HTTPGetAsyncTask;
import com.campusfeedapp.campusfeed.CustomViews.FontelloTextView;
import com.campusfeedapp.campusfeed.DTO.ChannelItemDTO;
import com.campusfeedapp.campusfeed.DTO.PostItemDTO;
import com.campusfeedapp.campusfeed.Interfaces.OnHTTPCompleteListener;
import com.campusfeedapp.campusfeed.R;
import com.campusfeedapp.campusfeed.CustomViews.RobotoTextView;
import com.campusfeedapp.campusfeed.Utils.Constants;
import com.squareup.picasso.Picasso;

/**
 * This is an example usage of the AnimatedExpandableListView class.
 *
 * It is an activity that holds a listview which is populated with 100 groups
 * where each group has from 1 to 100 children (so the first group will have one
 * child, the second will have two children and so on...).
 */
public class HomeFragment extends Fragment {

    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;

    public static final String TAG = HomeFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        listView = (AnimatedExpandableListView) v.findViewById(R.id.list_view);
        //listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        // Set indicator (arrow) to the right
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        //Log.v("width", width + "");
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50, r.getDisplayMetrics());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            listView.setIndicatorBounds(width - px, width);
        } else {
            listView.setIndicatorBoundsRelative(width - px, width);
        }

        return  v;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*List<GroupItem> items = new ArrayList<GroupItem>();

        // Populate our list with groups and it's children
        for (int i = 1; i < 100; i++) {
            GroupItem item = new GroupItem();

            item.title = "Expand this item " + i;

            for (int j = 0; j < i; j++) {
                ChildItem child = new ChildItem();
                child.title = "Expanded " + j;
                //child.hint = "Too awesome";

                item.items.add(child);
            }

            items.add(item);
        }*/
        /*HTTPGetAsyncTask httpGetAsyncTask = new HTTPGetAsyncTask(getActivity(),true);
        String userId = getActivity().getIntent().getExtras().getString(Constants.Keys.USER_ID);
        httpGetAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.URL_GET_FEED(userId));
        */
        adapter = new ExampleAdapter(getActivity().getBaseContext());
       // adapter.setData();
    }

    private static class GroupItem {
        ChannelItemDTO channelItemDTO;
        List<PostItemDTO> postItemList = new ArrayList<PostItemDTO>();
    }

    private static class ChildHolder {
        RobotoTextView title;
        RobotoTextView text;
        ImageView image;
        //TextView hint;
    }

    private static class GroupHolder {
        RobotoTextView title;
        ImageView image;
        FontelloTextView icon;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public PostItemDTO getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).postItemList.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition,
                                     boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            PostItemDTO postItemDTO = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.post_list_item, parent,
                        false);
                holder.title = (RobotoTextView) convertView.findViewById(R.id.list_item_google_cards_travel_title);
                holder.text = (RobotoTextView) convertView.findViewById(R.id.list_item_google_cards_travel_text);
                holder.image = (ImageView) convertView.findViewById(R.id.list_item_google_cards_travel_image);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(postItemDTO.getUserFullName());
            holder.text.setText(postItemDTO.getText());
            Picasso.with(getActivity().getBaseContext()).load(postItemDTO.imgUrl).centerCrop().fit().into(holder.image);
            //holder.hint.setText(item.hint);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).postItemList.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.expandable_list_item, parent,
                        false);
                holder.title = (RobotoTextView) convertView
                        .findViewById(R.id.title);
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.channelItemDTO.getChannelName());
            Picasso.with(getActivity().getBaseContext()).load(item.channelItemDTO.getImgUrl()).centerCrop().fit().into(holder.image);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
        // Required empty public constructor
    }
}
