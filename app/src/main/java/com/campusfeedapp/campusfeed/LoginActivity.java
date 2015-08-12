package com.campusfeedapp.campusfeed;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.campusfeedapp.campusfeed.AsyncTasks.HTTPPostAsyncTask;
import com.campusfeedapp.campusfeed.CustomViews.FloatLabeledEditText;
import com.campusfeedapp.campusfeed.CustomViews.RobotoTextView;
import com.campusfeedapp.campusfeed.Interfaces.OnHTTPCompleteListener;
import com.campusfeedapp.campusfeed.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity {

    RobotoTextView loginBtn;
    FloatLabeledEditText etUsername;
    FloatLabeledEditText etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (RobotoTextView) findViewById(R.id.submit_btn);
        etUsername = (FloatLabeledEditText) findViewById(R.id.field1);
        etPassword = (FloatLabeledEditText) findViewById(R.id.field2);
        final HTTPPostAsyncTask httpPostAsyncTask = new HTTPPostAsyncTask(LoginActivity.this,true);
        httpPostAsyncTask.setHTTPCompleteListener(new OnHTTPCompleteListener() {
            @Override
            public void onHTTPDataReceived(String result, String url) {
                try {
                    Constants.mAuthToken = new JSONObject(result).getString(Constants.Keys.AUTH_TOKEN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra(Constants.Keys.USER_ID,etUsername.getText().toString());
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Constants.Keys.USER_ID, etUsername.getText().toString());
                    jsonObject.put(Constants.Keys.PASSWORD, etPassword.getText().toString());
                } catch (Exception e){
                    e.printStackTrace();
                }
                httpPostAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.URL_LOGIN, jsonObject.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
