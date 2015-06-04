package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.ImageButton;

/**
 * Created by Andreas on 5/1/2015.
 */
public class ActivationActivity extends Activity {

    private ActivateCard mAuthTask = null;
    private InputStream is=null;
    private String line=null;
    private String result=null;
    private int code;

    EditText pinView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        pinView = (EditText)findViewById(R.id.userPin);
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

        ImageButton button = (ImageButton) findViewById(R.id.resetButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });


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

                String cc = json_data.getString("cc");



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
                    Toast.makeText(getBaseContext(), cc,
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
        protected void pinEncryption() {
            try {
                SharedPreferences sharedPref = getSharedPreferences(
                        getString(R.string.share_pref_file_name), Context.MODE_PRIVATE);

                String privateKeyStr = sharedPref.getString("secure.private-key", "");
                String publicKeyStr = sharedPref.getString("secure.public-key", "");
                if (TextUtils.isEmpty(privateKeyStr) || TextUtils.isEmpty(publicKeyStr)) {
                    pinView.setError("Security Key Not Found");
                }
                Secure secure = new Secure();

                try {
                    secure.LoadFromBase64String(privateKeyStr, publicKeyStr);
                } catch (Exception e) {
                    Log.e("Error converting", e.toString());
                }
                String pinRaw = pinView.getText().toString();

                if (TextUtils.isEmpty(pinRaw)) {
                    pinView.setError(getString(R.string.error_field_required));
                    return;
                }

                try {
                    String pinEncrypt = secure.Encrypt(pinRaw);
                    String localPin = sharedPref.getString("secure.pin", "");

                    // TODO replace with server call validation

                    if (TextUtils.isEmpty(localPin)) {
                        pinView.setError("local pin not found");
                        return;
                    } // verify local machine
                    else if (!pinEncrypt.equals(localPin)) {
                        pinView.setError(getString(R.string.pin_error_match));
                        return;
                    }
                } catch (Exception e) {
                    Log.e("Error encrypting pin", e.toString());
                }

            } catch (Exception e) {

                Log.e("Error Activation 1", e.toString());
                pinView.setError("Security Key Store Issues");

            }
        }

    }

}


