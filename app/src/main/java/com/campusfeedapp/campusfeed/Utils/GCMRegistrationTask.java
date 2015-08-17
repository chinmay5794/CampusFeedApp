package com.campusfeedapp.campusfeed.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;


import java.io.IOException;

public class GCMRegistrationTask extends AsyncTask<String, Void, String> {

	private Context context;
	private GoogleCloudMessaging gcm;
	private ProgressDialog dialog;
	private Activity activity;

	public GCMRegistrationTask(Activity activity, Context context) {
		this.activity = activity;
		this.context = context;
	}

	@Override
	protected String doInBackground(String... arg0) {
		String regId = "";
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			}
			regId = gcm.register(Constants.GCM_SENDER_ID);
		} catch (IOException ex) {
			/**
			 * If there is an error, don't just keep trying to register. Require
			 * the user to click a button again, or perform exponential
			 * back-off.
			 */
		}
		return regId;
	}

	@Override
	protected void onPreExecute() {
		//dialog = new ProgressDialog(activity);
		//dialog.setMessage("Regestring with GCM services...");
		//dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//dialog.show();
	}

	@Override
	protected void onPostExecute(String regId) {
		super.onPostExecute(regId);
		//if (dialog.isShowing()) {
		//	dialog.dismiss();
		//}
		//Log.i("this is regid ","this is regid "+regId);
		if (!regId.isEmpty()) {
			GoogleServicesUtil.sendRegistrationIdToBackend(activity, context,
					regId);
			GoogleServicesUtil.storeRegistrationId(activity, context, regId);
		} else {
			//ToastMakerUtil.display(context, "Error in getting registration id",
			//		Toast.LENGTH_LONG);
		}
	}

}
