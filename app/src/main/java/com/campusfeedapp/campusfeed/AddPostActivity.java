package com.campusfeedapp.campusfeed;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.campusfeedapp.campusfeed.CustomViews.RobotoTextView;
import com.campusfeedapp.campusfeed.Utils.Constants;

import org.json.JSONObject;


public class AddPostActivity extends ActionBarActivity implements View.OnClickListener {

    CheckBox anonChkBox;
    LinearLayout radioGroupAuth;
    RadioButton rbChannelName;
    RadioButton rbAuthName;
    RobotoTextView submitBtn;
    EditText etChannelDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        anonChkBox = (CheckBox)findViewById(R.id.chkbox_add_anon);
        radioGroupAuth = (LinearLayout) findViewById(R.id.radio_auth);
        rbAuthName = (RadioButton) findViewById(R.id.rb_author_name);
        rbChannelName = (RadioButton) findViewById(R.id.rb_channel_name);
        submitBtn = (RobotoTextView) findViewById(R.id.submit_btn_addpost);
        etChannelDesc = (EditText) findViewById(R.id.et_channel_description);

        anonChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    radioGroupAuth.setVisibility(View.GONE);
                }
                else{
                    radioGroupAuth.setVisibility(View.VISIBLE);
                }
            }
        });

        submitBtn.setOnClickListener(this);
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
        if(v.getId()==R.id.submit_btn_addpost &&
                ( rbChannelName.isChecked() || rbAuthName.isChecked()) ) {
            JSONObject jsonObject = new JSONObject();
            String userId = getSharedPreferences(Constants.SharedPrefs.USER_CREDENTIALS, Context.MODE_PRIVATE)
                            .getString(Constants.Keys.USER_ID, "");
            try {
                jsonObject.put(Constants.Keys.CHANNEL_ID, getIntent().getExtras().getString(Constants.Keys.CHANNEL_ID));
                jsonObject.put(Constants.Keys.USER_ID,userId);
                if(rbChannelName.isChecked()){
                    jsonObject.put(Constants.Keys.POST_BY,"channel");
                }
                else {
                    jsonObject.put(Constants.Keys.POST_BY,"user");
                }
                jsonObject.put(Constants.Keys.POST_TEXT,etChannelDesc.getText().toString());
                if (anonChkBox.isChecked()){
                    jsonObject.put(Constants.Keys.IS_ANONYMOUS,"true");
                }
                else {
                    jsonObject.put(Constants.Keys.IS_ANONYMOUS,"false");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
