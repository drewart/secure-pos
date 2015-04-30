package com.drewart.securepos.securepos;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.*;


public class MainActivity extends ActionBarActivity {

    private Spinner bankListSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addBankList();
    }


    protected void addBankList() {
        bankListSpinner = (Spinner) findViewById(R.id.bankList);
        List<String> list = new ArrayList<String>();
        list.add("Bank of Drew");
        list.add("Bank of Jason");
        list.add("Bank of Andreas");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankListSpinner.setAdapter(dataAdapter);

    }

    protected void getValues() {

        bankListSpinner =  (Spinner) findViewById(R.id.bankList);
        String bankValue = String.valueOf(bankListSpinner.getSelectedItem());

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
}
