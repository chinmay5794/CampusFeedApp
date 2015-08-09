package com.campusfeedapp.campusfeed;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.campusfeedapp.campusfeed.AsyncTasks.HTTPGetAsyncTask;
import com.campusfeedapp.campusfeed.Interfaces.OnHTTPCompleteListener;
import com.campusfeedapp.campusfeed.Utils.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class DiscoverChannelsFragment extends Fragment {

    public static final String TAG = DiscoverChannelsFragment.class.getSimpleName();
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;


    public static DiscoverChannelsFragment newInstance() {
        return new DiscoverChannelsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    List<ChannelItemDTO> allChannelList;
    ChannelListAdapter channelListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tabbed, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getChildFragmentManager());

        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        allChannelList = new ArrayList<>();
        channelListAdapter = new ChannelListAdapter(allChannelList,getActivity().getBaseContext());

        HTTPGetAsyncTask httpGetAsyncTask = new HTTPGetAsyncTask(getActivity(),true);
        httpGetAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.URL_GET_ALL_CHANNELS+"?limit=10&offset=0");
        httpGetAsyncTask.setHTTPCompleteListener(new OnHTTPCompleteListener() {
            @Override
            public void onHTTPDataReceived(String result, String url) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constants.Keys.ALL_CHANNELS);
                    for(int i=0;i<jsonArray.length();i++){
                        ChannelItemDTO channelItemDTO = new Gson().fromJson(jsonArray.get(i).toString(), ChannelItemDTO.class);
                        channelListAdapter.mChannelList.add(channelItemDTO);
                    }
                    channelListAdapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return v;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TabbedContentFragment(channelListAdapter);
            Bundle args = new Bundle();
            args.putInt(TabbedContentFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }

    public static class TabbedContentFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        ChannelListAdapter channelListAdapter;

        public TabbedContentFragment(ChannelListAdapter channelListAdapter) {
            this.channelListAdapter = channelListAdapter;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed_content,
                    container, false);

            ListView channelListView = (ListView) rootView.findViewById(R.id.list_view);
            channelListView.setAdapter(channelListAdapter);

            final HTTPGetAsyncTask httpGetAsyncTask = new HTTPGetAsyncTask(getActivity().getBaseContext(),true);
            httpGetAsyncTask.setHTTPCompleteListener(new OnHTTPCompleteListener() {
                @Override
                public void onHTTPDataReceived(String result, String url) {
                    
                    Intent intent = new Intent(getActivity(),ChannelPostsActivity.class);

                }
            });

            channelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String channelID = channelListAdapter.mChannelList.get(position).getChannelID();
                    httpGetAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Constants.URL_GET_POSTS_OF_CHANNEL(channelID,String.valueOf(10),String.valueOf(0)));
                }
            });

            return rootView;
        }
    }

}
