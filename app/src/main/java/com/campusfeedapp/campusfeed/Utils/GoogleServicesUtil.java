package com.campusfeedapp.campusfeed.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class GoogleServicesUtil {

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "GCMDemo";

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	public static boolean checkPlayServices(Activity activity) {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(activity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				Toast.makeText(activity.getApplicationContext(),
						"Your device doesn't suppport play service",
						Toast.LENGTH_LONG);
				// activity.finish();
			}
			return false;
		}
		return true;
	}

	public static String getRegistrationId(Activity activity, Context context) {
		final SharedPreferences prefs = getGCMPreferences(activity, context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		Log.i("this is regristratio id", "this is regrestratio id " + registrationId);
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		/**
		 * Check if app was updated; if so, it must clear the registration ID
		 * since the existing regID is not guaranteed to work with the new app
		 * version.
		 */
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			/** should never happen */
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private static SharedPreferences getGCMPreferences(Activity activity,
			Context context) {
		/**
		 * This sample app persists the registration ID in shared preferences,
		 * but how you store the regID in your app is up to you.
		 */
		return activity.getSharedPreferences("GMO_AUTH", Context.MODE_PRIVATE);
	}

	public static void sendRegistrationIdToBackend(Activity activity,
			Context context, String regId) {
		//new SendGCMTask(activity, context).execute(regId);
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	public static void storeRegistrationId(Activity activity, Context context,
			String regId) {
		final SharedPreferences prefs = getGCMPreferences(activity, context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}
}
