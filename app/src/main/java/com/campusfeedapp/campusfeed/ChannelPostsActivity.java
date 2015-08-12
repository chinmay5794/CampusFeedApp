package com.campusfeedapp.campusfeed;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.campusfeedapp.campusfeed.Adapters.PostListAdapter;
import com.campusfeedapp.campusfeed.AsyncTasks.HTTPGetAsyncTask;
import com.campusfeedapp.campusfeed.CustomViews.RobotoTextView;
import com.campusfeedapp.campusfeed.DTO.PostItemDTO;
import com.campusfeedapp.campusfeed.Interfaces.OnHTTPCompleteListener;
import com.campusfeedapp.campusfeed.Utils.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

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
        Context context = getBaseContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = (View) inflater.inflate(R.layout.header_channel_desc,null);
        setupChannelDesc(v);
        mListView.addHeaderView(v);
        fetchPostsData();
    }

    private void setupChannelDesc(final View v){
        HTTPGetAsyncTask httpGetAsyncTask1 = new HTTPGetAsyncTask(this,true);
        String channelID = getIntent().getExtras().getString(Constants.Keys.CHANNEL_ID);
        httpGetAsyncTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.URL_GET_CHANNEL_DETAILS(channelID));
        httpGetAsyncTask1.setHTTPCompleteListener(new OnHTTPCompleteListener() {
            @Override
            public void onHTTPDataReceived(String result, String url) {
                RobotoTextView channelName = (RobotoTextView) v.findViewById(R.id.channel_name);
                RobotoTextView channelDesc = (RobotoTextView) v.findViewById(R.id.channel_desc);
                RobotoTextView channelAdmin = (RobotoTextView) v.findViewById(R.id.channel_admins);
                RobotoTextView followBtn = (RobotoTextView) v.findViewById(R.id.follow_btn);
                ImageView imageView = (ImageView) v.findViewById(R.id.channel_img);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    channelName.setText(jsonObject.getString(Constants.Keys.CHANNEL_NAME));
                    channelDesc.setText(jsonObject.getString(Constants.Keys.CHANNEL_DESCRIPTION));
                    Picasso.with(getBaseContext()).load(getIntent().getExtras().getString(Constants.Keys.CHANNEL_IMAGE_URL)).fit().centerCrop().into(imageView);
                    imageView.setImageAlpha(100);
                }
                catch (Exception e) {

                }
                //setChannelImg(relativeLayout);
            }
        });
    }

    private void setChannelImg(final RelativeLayout relativeLayout){
        Log.e("img url","is" + getIntent().getExtras().getString(Constants.Keys.CHANNEL_IMAGE_URL));
        Picasso.with(getBaseContext())
                .load(getIntent().getExtras().getString(Constants.Keys.CHANNEL_IMAGE_URL))
                .into(new Target() {
                    @Override
                    @TargetApi(16)
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            relativeLayout.setBackgroundDrawable(new BitmapDrawable(bitmap));
                        } else {
                            relativeLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        // use error drawable if desired
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // use placeholder drawable if desired
                    }
                });

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

