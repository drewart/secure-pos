package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Andreas on 5/1/2015.
 */
public class NumberDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numberdisplay);

        // simply makes it close itself
        Button mBarcodeButton = (Button)findViewById(R.id.barcode_button);
        mBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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


    //want to also close barcode from here for backward navigation
    //possible logic 1: make this page close itself, then "finish" in barcode
    //possible logic 2: "finish" barcode first from here, then close this page
    //problems: "singleInstance" prevents user from activating multiple times in one opening
    //      can't call "finish" or "completionPage" functions directly: static in nonstatic context
    //
    public void completionPage() {
        Intent i = new Intent(getApplicationContext(), ConfirmationActivity.class);
        startActivity(i);
        Intent intent = new Intent("finish_barcode");
        sendBroadcast(intent);
        //BarcodeActivity.finish();
        finish();

        //BarcodeActivity.completionPage();
    }

}
