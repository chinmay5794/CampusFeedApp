package com.campusfeedapp.campusfeed.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chinmay on 14/6/15.
 */
public class NetworkUtil {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();
        return (networkInfo != null) && networkInfo.isConnected();

    }
}
