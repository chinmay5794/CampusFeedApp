package com.campusfeedapp.campusfeed;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.Toast;

import com.campusfeedapp.campusfeed.AsyncTasks.HTTPGetAsyncTask;
import com.campusfeedapp.campusfeed.Interfaces.OnHTTPCompleteListener;
import com.campusfeedapp.campusfeed.R;
import com.campusfeedapp.campusfeed.AlphaForegroundColorSpan;
import com.campusfeedapp.campusfeed.KenBurnsView;
import com.campusfeedapp.campusfeed.Utils.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Please NOTE: In manifest, set theme for this class to
 * \@style/OverlayActionBar
 * @author MLADJO
 *
 */
public class ChannelPostsActivity extends Activity {

    private ListView mListView;
    private List<PostItemDTO> mPostList;
    private HTTPGetAsyncTask httpGetAsyncTask;
    private PostListAdapter postListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_channel_posts);
        mPostList = new ArrayList<PostItemDTO>();
        mListView = (ListView) findViewById(R.id.list_view);
        httpGetAsyncTask = new HTTPGetAsyncTask(this,true);
        postListAdapter = new PostListAdapter(mPostList,getBaseContext());
        mListView.setAdapter(postListAdapter);
        fetchPostsData();
    }

    private void fetchPostsData() {

        String channelID = getIntent().getExtras().getString(Constants.Keys.CHANNEL_ID);
        httpGetAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.URL_GET_POSTS_OF_CHANNEL(channelID,String.valueOf(10),String.valueOf(0)));
        httpGetAsyncTask.setHTTPCompleteListener(new OnHTTPCompleteListener() {
            @Override
            public void onHTTPDataReceived(String result, String url) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constants.Keys.CHANNEL_POSTS);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PostItemDTO postItemDTO = new Gson().fromJson(jsonArray.get(i).toString(), PostItemDTO.class);
                        postListAdapter.mPostList.add(postItemDTO);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                postListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

