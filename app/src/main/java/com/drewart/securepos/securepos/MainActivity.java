package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
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


public class MainActivity extends Activity {

    private AddUser mAuthTask = null;
    private String name;
    private String email;
    private String phone;
    private String bank;
    private InputStream is=null;
    private String result=null;
    private String line=null;
    private int code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       final EditText e_name    = (EditText) findViewById(R.id.editTextName);
       final EditText e_email   = (EditText) findViewById(R.id.editTextEmail);
       final EditText e_phone   = (EditText) findViewById(R.id.editTextPhone);
       //final EditText e_bank    = (EditText) findViewById(R.id.bankOption);

        Button mSignUpButton = (Button)findViewById(R.id.signUp_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name    = e_name.getText().toString();
                email   = e_email.getText().toString();
                phone   = e_phone.getText().toString();
                //bank    = e_bank.getText().toString();
                signUp();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void signUp() {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("name",name));
        nameValuePairs.add(new BasicNameValuePair("email",email));
        nameValuePairs.add(new BasicNameValuePair("phone",phone));
        //nameValuePairs.add(new BasicNameValuePair("bank",bank));


        mAuthTask = new AddUser(nameValuePairs);
        mAuthTask.execute((Void) null);
    }

    public class AddUser extends AsyncTask<Void, Void, Boolean> {

        private final ArrayList<NameValuePair> nameValuePairs;

        AddUser (ArrayList<NameValuePair> nvp) {
            nameValuePairs = nvp;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try
            {
//                final String url = "https://students.washington.edu/andreas5/insert.php";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://students.washington.edu/andreas5/insert.php");
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
//            mAuthTask = null;
//            showProgress(false);
//
            try
            {
                JSONObject json_data = new JSONObject(result);
                code=(json_data.getInt("code"));

                Log.e("code: " , String.valueOf(code));

                if(code==1)
                {
                    Toast.makeText(getBaseContext(), "Inserted Successfully",
                            Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Sorry, Try Again",
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

