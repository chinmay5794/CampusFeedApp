package com.campusfeedapp.campusfeed.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.campusfeedapp.campusfeed.Adapters.ChannelListAdapter;
import com.campusfeedapp.campusfeed.AsyncTasks.HTTPGetAsyncTask;
import com.campusfeedapp.campusfeed.ChannelPostsActivity;
import com.campusfeedapp.campusfeed.DTO.ChannelItemDTO;
import com.campusfeedapp.campusfeed.Interfaces.OnHTTPCompleteListener;
import com.campusfeedapp.campusfeed.R;
import com.campusfeedapp.campusfeed.Utils.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyChannelsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyChannelsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyChannelsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = MyChannelsFragment.class.getSimpleName();

    ChannelListAdapter channelListAdapter;
    List<ChannelItemDTO> channelItemDTOList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyChannelsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyChannelsFragment newInstance() {
        return new MyChannelsFragment();
    }

    public MyChannelsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_my_channels, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        channelItemDTOList = new ArrayList<ChannelItemDTO>();
        channelListAdapter = new ChannelListAdapter(channelItemDTOList,getActivity().getBaseContext());
        listView.setAdapter(channelListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String channelID = channelListAdapter.mChannelList.get(position).getChannelID();
                String channelImgUrl = channelListAdapter.mChannelList.get(position).getImgUrl();
                Intent intent = new Intent(getActivity(),ChannelPostsActivity.class);
                intent.putExtra(Constants.Keys.CHANNEL_ID,channelID);
                intent.putExtra(Constants.Keys.CHANNEL_IMAGE_URL,channelImgUrl);
                startActivity(intent);
            }
        });
        fetchMyChannelsData();

        return rootView;
    }

    private void fetchMyChannelsData(){
        String userId = getActivity().getIntent().getExtras().getString(Constants.Keys.USER_ID);
        HTTPGetAsyncTask httpGetAsyncTask = new HTTPGetAsyncTask(getActivity().getBaseContext(),true);
        httpGetAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.URL_GET_MY_CHANNELS(userId));
        httpGetAsyncTask.setHTTPCompleteListener(new OnHTTPCompleteListener() {
            @Override
            public void onHTTPDataReceived(String result, String url) {
                Log.e("pk", url);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constants.Keys.MY_CHANNELS);
                    Log.e("apop",String.valueOf(jsonArray.length()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.e("pkpi", jsonArray.get(i).toString());
                        ChannelItemDTO channelItemDTO = new Gson().fromJson(jsonArray.get(i).toString(), ChannelItemDTO.class);
                        channelListAdapter.mChannelList.add(channelItemDTO);
                    }
                    channelListAdapter.notifyDataSetChanged();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
