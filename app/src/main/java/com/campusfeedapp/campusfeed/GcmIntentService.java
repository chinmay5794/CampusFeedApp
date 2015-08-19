package com.campusfeedapp.campusfeed;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by chinmay on 17/8/15.
 */
public class GcmIntentService extends GcmListenerService {

    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GCM";
    private NotificationManager mNotificationManager;
    private static Bundle extras;


    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

    }

    public GcmIntentService(){
        super();
    }


    /*@Override
    protected void onHandleIntent(Intent intent) {

        extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        *//**
         * The getMessageType() intent parameter must be the intent you received
         * in your BroadcastReceiver.
         *//*

        Log.i(TAG, "intent coming");
        String messageType = gcm.getMessageType(intent);

        if(!extras.isEmpty()){
            String string = "Bundle{";
            for (String key : extras.keySet()) {
                string += " " + key + " => " + extras.get(key) + ";";
            }
            string += " }Bundle";
            Log.e(TAG,"extras not empty");
            Log.e("Dumping Intent ",string);
        }

        boolean isOnForeground = false;
        try {
            //isOnForeground = ActivityManagerUtil.isOnForeground(getApplicationContext());
            isOnForeground = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        *//** Release the wake lock provided by the WakefulBroadcastReceiver. *//*
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }*/
}
