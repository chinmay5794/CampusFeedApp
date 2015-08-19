package com.campusfeedapp.campusfeed;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by chinmay on 17/8/15.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("this is inside Gcm", "this is inside Gcm " + bundle2string(intent.getExtras()));
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        //** Start the service, keeping the device awake while it is launching. *//*
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }
}
