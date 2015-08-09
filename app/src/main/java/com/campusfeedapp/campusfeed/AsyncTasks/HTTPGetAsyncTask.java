package com.campusfeedapp.campusfeed.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.campusfeedapp.campusfeed.Interfaces.OnHTTPCompleteListener;
import com.campusfeedapp.campusfeed.Utils.Constants;
import com.campusfeedapp.campusfeed.Utils.NetworkUtil;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by chinmay on 18/6/15.
 */

//<1,2,3> => inputType of <doInBackground,setProgressUpdate,onPostExecute>

//Conventions : - params[0] = URL ,params[1] = JSON String if exists
public class HTTPGetAsyncTask extends AsyncTask<String,String,String> {

    private String TAG = "HTTPGet";
    private Context context;
    JSONObject jResponse;
    private String url;
    private boolean setLoader;
    private OnHTTPCompleteListener listener;

    public HTTPGetAsyncTask(Context context, boolean setLoader){
        this.context = context;
        this.setLoader = setLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(setLoader){
            //show loader
        }
    }

    @Override
    protected String doInBackground(String... params) {

        url = params[0];

        if(NetworkUtil.isNetworkAvailable(context)) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);

                httpGet.setHeader("token", Constants.mAuthToken);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    Log.i(TAG + "URL: " + params[0], "Successfully sent GET data");
                } else {
                    Log.i(TAG + "URL: " + params[0], "HTTP Status code is " +
                            String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                }

                //-- Retrieve the response from the server
                String line = "";
                StringBuilder result = new StringBuilder();
                InputStream is = httpResponse.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e(TAG + "URL: " + params[0], "Unsupported Encoding Exception");
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.e(TAG + "URL: " + params[0], "Client Protocol Exception");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG + "URL: " + params[0], "IOException");
            }
        }
        else{
            Log.w(TAG, "No internet connection");
        }
        return null;

    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        try {
            if (response != null) {
                Log.e(TAG + "response:", response);
                listener.onHTTPDataReceived(response, url);
            } else {
                Log.w(TAG, "Null response");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!NetworkUtil.isNetworkAvailable(context)){
            Toast.makeText(context,"No internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    public void setHTTPCompleteListener(OnHTTPCompleteListener listener){
        this.listener = listener;
    }
}
