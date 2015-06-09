package com.example.lokillo.nfcdemo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {

    public static String TAG = "NFC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    private void stopNFC() {
        Log.i(TAG, "NFC not supported");
        Toast.makeText(this, "NFC is not supported", Toast.LENGTH_SHORT).show();

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
