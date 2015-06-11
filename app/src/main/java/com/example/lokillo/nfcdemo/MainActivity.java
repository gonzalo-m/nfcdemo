package com.example.lokillo.nfcdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    public static String TAG = "NFC";

    private TextView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (TextView)findViewById(R.id.click_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "text clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // check if device supports NFC
        if (isNFCSupported()) {
            continueWithNFC();
        } else  {
            stopNFC();
        }
    }

    private boolean isNFCSupported() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
    }

    private void continueWithNFC() {
        Log.i(TAG, "NFC is supported");
        Toast.makeText(this, "NFC is supported", Toast.LENGTH_SHORT).show();
        // add NFC setting to UI

        // ask user to enable NFC, if not enabled
        NfcAdapter nfcAdapt = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if(!nfcAdapt.isEnabled()) {
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }

        Toast.makeText(this, nfcAdapt.toString(), Toast.LENGTH_SHORT).show();

    }

    private void stopNFC() {
        Log.i(TAG, "NFC not supported");
        Toast.makeText(this, "NFC is not supported", Toast.LENGTH_SHORT).show();
        NfcAdapter nfcAdapt = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if (nfcAdapt != null)
            Toast.makeText(this, nfcAdapt.toString(), Toast.LENGTH_SHORT).show();

    }

    private void askUserToEnableNFC() {

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
