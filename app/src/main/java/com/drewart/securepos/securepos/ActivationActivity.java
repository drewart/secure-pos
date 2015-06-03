package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Andreas on 5/1/2015.
 */
public class ActivationActivity extends Activity {

    private ActivateCard mAuthTask = null;
    private InputStream is=null;
    private String line=null;
    private String result=null;
    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        final EditText pinView = (EditText)findViewById(R.id.userPin);
        pinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinView.getText().clear();
            }
        });

        Button mActivateButton = (Button)findViewById(R.id.activation_button);
        mActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activate();
            }
        });

        pinView.requestFocus();

    }

    public void activate() {

        // TODO validate pin
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.share_pref_file_name), Context.MODE_PRIVATE);

        if(sharedPref.getString("email", null) != null) {
            final EditText pinView = (EditText)findViewById(R.id.userPin);
            final EditText amountView = (EditText)findViewById(R.id.userLimit);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email",sharedPref.getString("email", null)));
            // encrypted// TODO encrypt pin w/ public key
            nameValuePairs.add(new BasicNameValuePair("pin", pinView.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("amount", amountView.getText().toString()));


            mAuthTask = new ActivateCard(nameValuePairs);
            mAuthTask.execute((Void) null);

        } else {
            Log.e("Fail 1", "No email in shared pref");
        }




    }


    public class ActivateCard extends AsyncTask<Void, Void, Boolean> {

        private final ArrayList<NameValuePair> nameValuePairs;

        ActivateCard (ArrayList<NameValuePair> nvp) {
            nameValuePairs = nvp;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Settings.ActivationUrl);
                //httppost.setHeader("public-key","0123456789");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            }
            catch(Exception e)
            {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            try
            {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection success ");
            }
            catch(Exception e)
            {
                Log.e("Fail 2", e.toString());
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            try
            {
                JSONObject json_data = new JSONObject(result);
                code=(json_data.getInt("code"));

                String message = json_data.getString("message");



                Log.e("code: " , String.valueOf(code));

                if(code==1)
                {
                    Toast.makeText(getBaseContext(), "Activated Successfully",
                            Toast.LENGTH_SHORT).show();



                    Intent i = new Intent(getApplicationContext(), BarcodeActivity.class);
                    startActivity(i);

                    finish();
                }
                else
                {
                    Toast.makeText(getBaseContext(), message,
                            Toast.LENGTH_LONG).show();
                }
            }
            catch(Exception e)
            {
                Log.e("Fail 3", e.toString());
            }
        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }

    }

}


