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

        Button mBarcodeButton = (Button)findViewById(R.id.barcode_button);
        mBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToBarcode();
            }
        });
    }

    public void switchToBarcode() {
        Intent i = new Intent(getApplicationContext(), BarcodeActivity.class);
        startActivity(i);
    }

}
