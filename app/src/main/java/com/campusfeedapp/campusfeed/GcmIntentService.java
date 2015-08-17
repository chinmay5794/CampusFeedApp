package com.campusfeedapp.campusfeed;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by chinmay on 17/8/15.
 */
public class GcmIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GCM";
    private NotificationManager mNotificationManager;
    private static Bundle extras;


    public GcmIntentService(){
        super("GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        /**
         * The getMessageType() intent parameter must be the intent you received
         * in your BroadcastReceiver.
         */

        Log.i(TAG, "intent coming");
        String messageType = gcm.getMessageType(intent);

        boolean isOnForeground = false;
        try {
            //isOnForeground = ActivityManagerUtil.isOnForeground(getApplicationContext());
            isOnForeground = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        /** Release the wake lock provided by the WakefulBroadcastReceiver. */
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
