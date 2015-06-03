package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
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

        // TODO validate pin

        // clear the pin before moving to next app
        pinView.getText().clear();

        Intent i = new Intent(getApplicationContext(), BarcodeActivity.class);
        startActivity(i);
    }

}


