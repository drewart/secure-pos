package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Andreas on 5/1/2015.
 */
public class BarcodeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcodedisplay);

        Button mNumberDisplayButton = (Button)findViewById(R.id.number_display_button);
        mNumberDisplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToNumber();
            }
        });

        Button mFinishActivity = (Button)findViewById(R.id.deactivateButton);
        mFinishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completionPage();
            }
        });
    }

    public void switchToNumber() {
        Intent i = new Intent(getApplicationContext(), NumberDisplayActivity.class);
        startActivity(i);
    }

    public void completionPage() {
        Intent i = new Intent(getApplicationContext(), ConfirmationActivity.class);
        startActivity(i);
    }
}

