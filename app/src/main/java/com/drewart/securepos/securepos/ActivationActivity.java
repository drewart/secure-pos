package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Andreas on 5/1/2015.
 */
public class ActivationActivity extends Activity {

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

        try {
            SharedPreferences sharedPref = getSharedPreferences(
                    getString(R.string.share_pref_file_name), Context.MODE_PRIVATE);

            String privateKeyStr = sharedPref.getString("secure.private-key","");
            String publicKeyStr = sharedPref.getString("secure.public-key", "");
            if (TextUtils.isEmpty(privateKeyStr) || TextUtils.isEmpty(publicKeyStr)) {
                pinView.setError("Security Key Not Found");
            }
            Secure secure = new Secure();
            secure.LoadFromBase64String(privateKeyStr, publicKeyStr);

            String pinRaw = pinView.getText().toString();

            if (TextUtils.isEmpty(pinRaw)) {
                pinView.setError(getString(R.string.error_field_required));
                return;
            }

            String pinEncrypt = secure.Encrypt(pinRaw);

            String localPin = sharedPref.getString("secure.pin","");

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

            Log.e("Error Activation 1", e.toString());
            pinView.setError("Security Key Store Issues");

        }

        // clear the pin before moving to next app
        pinView.getText().clear();

        Intent i = new Intent(getApplicationContext(), BarcodeActivity.class);
        startActivity(i);
    }

}


