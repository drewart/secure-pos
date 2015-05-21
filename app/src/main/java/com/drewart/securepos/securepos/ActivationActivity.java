package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Andreas on 5/1/2015.
 */
public class ActivationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        final EditText editText = (EditText)findViewById(R.id.userPin);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.getText().clear();
            }
        });

        Button mActivateButton = (Button)findViewById(R.id.activation_button);
        mActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activate();
            }
        });
    }

    public void activate() {
        Intent i = new Intent(getApplicationContext(), BarcodeActivity.class);
        startActivity(i);
        //closes activity immediately; prevents user from accidentally reactivating
        finish();
    }

}


