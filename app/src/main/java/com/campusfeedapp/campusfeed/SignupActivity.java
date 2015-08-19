package com.campusfeedapp.campusfeed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.campusfeedapp.campusfeed.CustomViews.FloatLabeledEditText;
import com.campusfeedapp.campusfeed.CustomViews.RobotoTextView;
import com.campusfeedapp.campusfeed.Utils.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;


public class SignupActivity extends ActionBarActivity {

    RobotoTextView signupBtn,loadImgBtn;
    FloatLabeledEditText firstNameEdt,lastNameEdt,branchEdt,phoneNumberEdt,emailEdt,idNumEdt,pswdEdt;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    String filepath;
    ByteArrayBody byteArrayBody;
    Spinner branchListSpinner;
    ArrayAdapter<String> arrAdptBranch;
    int branch_sel_pos=0;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstNameEdt=(FloatLabeledEditText)findViewById(R.id.field1_signup);
        lastNameEdt=(FloatLabeledEditText)findViewById(R.id.field2_signup);
        branchListSpinner=(Spinner)findViewById(R.id.field3_signup);
        //branchEdt=(FloatLabeledEditText)findViewById(R.id.field3_signup);
        phoneNumberEdt=(FloatLabeledEditText)findViewById(R.id.field4_signup);
        emailEdt=(FloatLabeledEditText)findViewById(R.id.field5_signup);
        idNumEdt=(FloatLabeledEditText)findViewById(R.id.field6_signup);
        pswdEdt=(FloatLabeledEditText)findViewById(R.id.field7_signup);
        arrAdptBranch=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        arrAdptBranch.addAll(getResources().getStringArray(R.array.branch_list));
        branchListSpinner.setAdapter(arrAdptBranch);
        branchListSpinner.setSelection(branch_sel_pos);
        signupBtn = (RobotoTextView)findViewById(R.id.submit_btn_signup);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNameEdt.getTextString().isEmpty()||lastNameEdt.getTextString().isEmpty()||
                        (branch_sel_pos==0)||phoneNumberEdt.getTextString().isEmpty()||
                        emailEdt.getTextString().isEmpty()||idNumEdt.getTextString().isEmpty()||
                        pswdEdt.getTextString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    new UploadFileToServer().execute();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        photo= BitmapFactory.decodeResource(getResources(),R.drawable.default_img);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        photo.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] byteData = byteArrayOutputStream.toByteArray();
        byteArrayBody = new ByteArrayBody(byteData, "image"); // second parameter is the name of the image (//TODO HOW DO I MAKE IT USE THE IMAGE FILENAME?)

        loadImgBtn = (RobotoTextView)findViewById(R.id.upload_btn_signup);
        loadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // ******** code for crop image
                galleryIntent.putExtra("crop", "true");
                galleryIntent.putExtra("aspectX", 0);
                galleryIntent.putExtra("aspectY", 0);
                galleryIntent.putExtra("outputX", 200);
                galleryIntent.putExtra("outputY", 200);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        branchListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch_sel_pos=parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    ImageView imgView = (ImageView) findViewById(R.id.upload_imgview);
                    Bitmap photo = extras2.getParcelable("data");
                    imgView.setImageBitmap(photo);
                    /*bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
                    byte [] byte_arr = stream.toByteArray();
                    String image_str = Base64.encodeBytes(byte_arr);
                    ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("image", image_str));*/
                    //updateByteArrayBody();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    photo.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
                    byte[] byteData = byteArrayOutputStream.toByteArray();
                    //String strData = Base64.encodeToString(data, Base64.DEFAULT); // I have no idea why Im doing this
                    byteArrayBody = new ByteArrayBody(byteData, "image"); // second parameter is the name of the image (//TODO HOW DO I MAKE IT USE THE IMAGE FILENAME?)

               /* Uri selectedImage = data.getData();

                File mf = new File(selectedImage.toString());
                filepath = mf.getAbsolutePath();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.upload_imgview);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));*/

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } /*catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }*/

    }

    private void updateByteArrayBody() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        photo.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] byteData = byteArrayOutputStream.toByteArray();
        byteArrayBody = new ByteArrayBody(byteData, "image"); // second parameter is the name of the image (//TODO HOW DO I MAKE IT USE THE IMAGE FILENAME?)
    }

    public class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        private  String TAG = getClass().getSimpleName().toString();
        private String signupResponse;
        long totalSize = 100;
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.URL_SIGNUP);

            try {
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
               // File sourceFile = new File(filepath);
               // builder.addPart(Constants.Keys.USERIMAGE, new FileBody(sourceFile));
               // updateByteArrayBody();
                builder.addPart(Constants.Keys.USERIMAGE,byteArrayBody);
                builder.addTextBody(Constants.Keys.USER_ID, idNumEdt.getText().toString());
                builder.addTextBody(Constants.Keys.FIRST_NAME,firstNameEdt.getText().toString());
                builder.addTextBody(Constants.Keys.LAST_NAME, lastNameEdt.getText().toString());
                builder.addTextBody(Constants.Keys.EMAIL_ID, emailEdt.getText().toString());
                builder.addTextBody(Constants.Keys.PASSWORD, pswdEdt.getText().toString());
                builder.addTextBody(Constants.Keys.BRANCH, branchListSpinner.getSelectedItem().toString());
                Log.e(TAG, branchListSpinner.getSelectedItem().toString());
                builder.addTextBody(Constants.Keys.PHONE, phoneNumberEdt.getText().toString());

                httppost.setEntity(builder.build());
                /*AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });*/
                /*MultipartEntityBuilder entity1 = MultipartEntityBuilder.create();
                //MultipartEntity entity1 = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName("UTF-8"));
                //File sourceFile = new File(filepath);

                // Adding file data to http body
                //entity.addPart(Constants.Keys.USERIMAGE, new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity1.addPart(Constants.Keys.USER_ID,new StringBody("userId"));
                entity1.addPart(Constants.Keys.FIRST_NAME, new StringBody("first"));
                entity1.addPart(Constants.Keys.LAST_NAME, new StringBody("last"));
                entity1.addPart(Constants.Keys.EMAIL_ID, new StringBody("email-id"));
                entity1.addPart(Constants.Keys.PASSWORD, new StringBody("pswd"));
                entity1.addPart("branch", new StringBody("Branch"));
                entity1.addPart("phone", new StringBody("phone number"));

               // totalSize = entity.getContentLength();
                httppost.setEntity(entity1.build());
                */

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    //Toast.makeText(getApplicationContext(),"Account Created",Toast.LENGTH_LONG).show();
                    signupResponse="Account Created";
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                    signupResponse=responseString;
                    //Toast.makeText(getApplicationContext(),responseString,Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                signupResponse=e.toString();
                //Toast.makeText(getApplicationContext(),"Error occured, sorry for the inconvenience",Toast.LENGTH_SHORT).show();
            }
            /*catch(ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }*/

            finish();
            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            Toast.makeText(getApplicationContext(),signupResponse,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }

    }


}
