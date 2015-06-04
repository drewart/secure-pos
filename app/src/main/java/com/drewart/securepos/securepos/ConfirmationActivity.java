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

    }

    // prevents users from going back through the app by disabling it
    @Override
    public void onBackPressed(){
        finish();
    }
}
