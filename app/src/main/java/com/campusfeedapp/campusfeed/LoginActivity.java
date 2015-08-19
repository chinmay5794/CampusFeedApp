package com.campusfeedapp.campusfeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.campusfeedapp.campusfeed.AsyncTasks.HTTPPostAsyncTask;
import com.campusfeedapp.campusfeed.CustomViews.FloatLabeledEditText;
import com.campusfeedapp.campusfeed.CustomViews.RobotoTextView;
import com.campusfeedapp.campusfeed.Interfaces.OnHTTPCompleteListener;
import com.campusfeedapp.campusfeed.Utils.Constants;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Password;


import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    Validator validator;
    HTTPPostAsyncTask httpPostAsyncTask;
    RobotoTextView loginBtn;

    FloatLabeledEditText etUserId;
    FloatLabeledEditText etPassword;

    TextView signupTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkAndRestore();

        loginBtn = (RobotoTextView) findViewById(R.id.submit_btn);
        etUserId = (FloatLabeledEditText) findViewById(R.id.field1);
        etPassword = (FloatLabeledEditText) findViewById(R.id.field2);

        loginBtn.setOnClickListener(this);
        signupTxt=(TextView)findViewById(R.id.signup_submit);
        signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkAndRestore(){
        SharedPreferences mPrefs = getSharedPreferences(Constants.SharedPrefs.USER_CREDENTIALS,Context.MODE_PRIVATE);
        boolean loggedIn = mPrefs.getBoolean(Constants.Keys.IS_LOGGED_IN,false);
        if(loggedIn){
            String userId = mPrefs.getString(Constants.Keys.USER_ID,"");
            Constants.mAuthToken = mPrefs.getString(Constants.Keys.AUTH_TOKEN,"");
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra(Constants.Keys.USER_ID,userId);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    private void saveToSharedPrefs(JSONObject jResponse){
        SharedPreferences mPrefs = getSharedPreferences(Constants.SharedPrefs.USER_CREDENTIALS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        try {
            editor.putString(Constants.Keys.FIRST_NAME, jResponse.getString(Constants.Keys.FIRST_NAME));
            editor.putString(Constants.Keys.LAST_NAME, jResponse.getString(Constants.Keys.LAST_NAME));
            editor.putString(Constants.Keys.AUTH_TOKEN, jResponse.getString(Constants.Keys.AUTH_TOKEN));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(Constants.Keys.USER_ID, etUserId.getText().toString());
        editor.putBoolean(Constants.Keys.IS_LOGGED_IN, true);
        editor.commit();
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

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.submit_btn) {

            httpPostAsyncTask = new HTTPPostAsyncTask(LoginActivity.this, true);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Constants.Keys.USER_ID, etUserId.getText().toString());
                jsonObject.put(Constants.Keys.PASSWORD, etPassword.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpPostAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.URL_LOGIN, jsonObject.toString());

            httpPostAsyncTask.setHTTPCompleteListener(new OnHTTPCompleteListener() {
                @Override
                public void onHTTPDataReceived(String result, String url) {
                    if (!result.contentEquals("")) {
                        try {
                            Constants.mAuthToken = new JSONObject(result).getString(Constants.Keys.AUTH_TOKEN);
                            saveToSharedPrefs(new JSONObject(result));
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(Constants.Keys.USER_ID, etUserId.getText().toString());
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginBtn.setEnabled(true);
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                        loginBtn.setEnabled(true);
                    }
                }
            });
        }
    }
}
