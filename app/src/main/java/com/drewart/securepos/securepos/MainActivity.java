package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {



    private AddUser mAuthTask = null;
    private String name;
    private String email;
    private String phone;
    private String bank;
    private String pin;
    private String pin2;
    private InputStream is=null;
    private String result=null;
    private String line=null;
    private int code;
    private Spinner bankListSpinner;
    private EditText nameView = null;
    private EditText emailView = null;
    private EditText phoneView = null;
    private EditText pinView = null;
    private EditText rePinView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: maybe download from server and have a local cache
        addBankList();

       nameView    = (EditText) findViewById(R.id.editTextName);
       emailView   = (EditText) findViewById(R.id.editTextEmail);
       phoneView   = (EditText) findViewById(R.id.editTextPhone);
       pinView      = (EditText) findViewById(R.id.editTextPin);
       rePinView    = (EditText) findViewById(R.id.editTextPin2);

       bankListSpinner     = (Spinner) findViewById(R.id.spinnerBank);

        Button mSignUpButton = (Button)findViewById(R.id.signUp_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name    = nameView.getText().toString();
                email   = emailView.getText().toString();
                phone   = phoneView.getText().toString();
                bank    = bankListSpinner.getSelectedItem().toString();
                // todo encrypt here
                pin     = pinView.getText().toString();
                pin2    = rePinView.getText().toString();


                if (validate())
                    signUp();
            }
        });
    }

    // gantlet of field validation
    protected boolean validate() {
        // TODO: add logic
        // Check for a valid password, if the user entered one.
       /* if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        */
        boolean valid = true;
        View focusView = null;


        if (TextUtils.isEmpty(name)) {
            nameView.setError(getString(R.string.error_field_required));
            focusView = nameView;
            valid = false;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneView.setError(getString(R.string.error_field_required));
            focusView = phoneView;
            valid = false;
        }

        if (phone.replaceAll("\\D+","").length() < 9) {
            phoneView.setError(getString(R.string.phone_digit_length));
            focusView = phoneView;
            valid = false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            valid = false;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            valid = false;
        }

        // check if pin length is too short
        if (pin.length() < 4) {
            pinView.setError(getString(R.string.pin_error_length));
            focusView = pinView;
            valid = false;
        }
        else if (!pin.equals(pin2)) {
            rePinView.setError(getString(R.string.pin_error_match));
            focusView = rePinView;
            valid = false;
        }

        if (!valid)
            focusView.requestFocus();

        return true;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        boolean valid = true;
        // not ideal regex no sub domain email
        valid =  email.contains("@") && email.matches("[\\w\\.-_]+@[\\w-]+\\.\\w+");
        return valid;
    }


    protected void addBankList() {
        bankListSpinner = (Spinner) findViewById(R.id.spinnerBank);
        List<String> list = new ArrayList<String>();
        list.add("Wells Fargo");
        list.add("Bank of America");
        list.add("US Bank");
        list.add("Other");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankListSpinner.setAdapter(dataAdapter);

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
        nameValuePairs.add(new BasicNameValuePair("bank", bank));
        // encrypted
        nameValuePairs.add(new BasicNameValuePair("pin", pin));
        String json;
        try {
            json = new JSONStringer()
                    .object().key("name").value(name)
                    .key("email").value(email)
                    .key("phone").value(phone)
                    .key("bank").value(bank)
                    .key("pin").value(pin)
                    .endObject().toString();
           /* json.append("{");
            json.append("name:'").append(name).append("'");
            json.append("email:'").append(email).append("'");
            json.append("phone:'").append(phone).append("'");
            json.append("bank:'").append(bank).append("'");
            json.append("pin:'").append(pin).append("'");
            json.append("}");*/
        } catch (JSONException je)
        {
            //error
            Toast.makeText(getApplicationContext(), je.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e("Fail 1", je.toString());
        }

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
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Settings.RegistrationUrl);
                httppost.setHeader("public-key","0123456789");
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

                    SharedPreferences sharedPref = getSharedPreferences(
                            getString(R.string.share_pref_file_name), Context.MODE_PRIVATE);

                    SharedPreferences.Editor prefEditor = sharedPref.edit();

                    prefEditor.putBoolean("registered",true);
                    prefEditor.apply();

                    Intent i = new Intent(getApplicationContext(), ActivationActivity.class);
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

