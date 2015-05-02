package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Deep Thought on 02/05/2015.
 */
public class ConfirmationActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmationpage);

        Button closeApp = (Button) findViewById(R.id.closeapp_button);
        closeApp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                finish();
                System.exit(0);
            }

        });
    }
}
