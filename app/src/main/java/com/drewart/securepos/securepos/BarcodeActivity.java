package com.drewart.securepos.securepos;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.EnumMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

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
        //for closing this from NumDisplay
        BroadcastReceiver bc = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (action.equals("finish_barcode")) {
                    finish();
                }
            }
        };

        registerReceiver(bc, new IntentFilter("finish_activity"));


        generateBarCode();
        //R.id.imageViewBarCode


    }


    public void foo() {

      //  javax.security.cert.X509Certificate

    }
    public void generateBarCode(/*int cardNum*/) {
        // barcode data
        String barcode_data = "4111111111111111";

        // barcode image
        Bitmap bitmap = null;
        //ImageView iv = new ImageView(this);
        //LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
        //ImageView imageView = new ImageView(this);
        ImageView imageView = (ImageView)findViewById(R.id.imageViewBarCode);

        try {

            bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            imageView.setImageBitmap(bitmap);
            //imageView.refreshDrawableState();

        } catch (WriterException e) {
            e.printStackTrace();
        }

        //layout.addView(imageView);
    }

    /**************************************************************
     * getting from com.google.zxing.client.android.encode.QRCodeEncoder
     *
     * See the sites below
     * http://code.google.com/p/zxing/
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/EncodeActivity.java
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/QRCodeEncoder.java
     */

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public void switchToNumber() {
        Intent i = new Intent(getApplicationContext(), NumberDisplayActivity.class);
        startActivity(i);
    }

    public void completionPage() {
        Intent i = new Intent(getApplicationContext(), ConfirmationActivity.class);
        startActivity(i);
        finish();
    }





}

