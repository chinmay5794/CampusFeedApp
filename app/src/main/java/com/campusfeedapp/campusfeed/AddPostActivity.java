package com.campusfeedapp.campusfeed;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.campusfeedapp.campusfeed.CustomViews.RobotoTextView;
import com.campusfeedapp.campusfeed.Utils.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;


public class AddPostActivity extends ActionBarActivity implements View.OnClickListener {

    RobotoTextView submitBtn;
    EditText etPostText;
    RobotoTextView uploadPicBtn;
    private static int RESULT_LOAD_IMG = 1;
    String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        submitBtn = (RobotoTextView) findViewById(R.id.submit_btn_addpost);
        uploadPicBtn = (RobotoTextView) findViewById(R.id.upload_btn_addpost);
        etPostText = (EditText) findViewById(R.id.et_post_text);

        submitBtn.setOnClickListener(this);
        uploadPicBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submit_btn_addpost && !etPostText.getText().toString().isEmpty()) {
            new PostDataSender().execute();
        }
        else {
            Toast.makeText(AddPostActivity.this,"Please enter post text.",Toast.LENGTH_SHORT).show();
        }

        if(v.getId()==R.id.upload_btn_addpost){
            uploadPicBtn.setEnabled(false);
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.upload_imgview_addpost);
                imageView.setImageBitmap(bitmap);

                String[] projection = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                filepath = cursor.getString(columnIndex); // returns null
                cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
                uploadPicBtn.setEnabled(true);
            }
        }


    }

    public class PostDataSender extends AsyncTask<Void, Integer, String> {

        private  String TAG = getClass().getSimpleName().toString();
        long totalSize = 100;
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        private String uploadFile() {
            String responseString = null;

            String userId = getSharedPreferences(Constants.SharedPrefs.USER_CREDENTIALS, Context.MODE_PRIVATE)
                    .getString(Constants.Keys.USER_ID, "");
            String channelId = getIntent().getExtras().getString(Constants.Keys.CHANNEL_ID);

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.URL_CREATE_POST(channelId));

            httppost.setHeader("token", Constants.mAuthToken);


            try {

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                if(!uploadPicBtn.isEnabled()) {
                    builder.addBinaryBody("post_img", new File(filepath));
                }
                builder.addTextBody(Constants.Keys.USER_ID, userId);
                builder.addTextBody(Constants.Keys.CHANNEL_ID, channelId);
                builder.addTextBody(Constants.Keys.POST_TEXT,etPostText.getText().toString());
                builder.addTextBody(Constants.Keys.POST_BY, "channel");
                builder.addTextBody(Constants.Keys.IS_ANONYMOUS,"False");

                httppost.setEntity(builder.build());

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            Toast.makeText(AddPostActivity.this,"sent",Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }

    }


}
