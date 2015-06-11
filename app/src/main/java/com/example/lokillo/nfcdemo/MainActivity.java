package com.example.lokillo.nfcdemo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity {

    public static String TAG = "NFC";

    private TextView button;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter.getDefaultAdapter(getApplicationContext());


        button = (TextView)findViewById(R.id.click_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "text clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // check if device supports NFC
        if (isNfcSupported()) {
            onNfcSupported();
        } else  {
            onNfcNotSupported();
        }
    }

    private boolean isNfcSupported() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
    }

    private void onNfcSupported() {
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

    private void onNfcNotSupported() {
        Log.i(TAG, "NFC not supported");
        Toast.makeText(this, "NFC is not supported", Toast.LENGTH_SHORT).show();
        NfcAdapter nfcAdapt = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if (nfcAdapt != null)
            Toast.makeText(this, nfcAdapt.toString(), Toast.LENGTH_SHORT).show();

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


    private class NfcManager {



        public final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
        public final int REQUEST_CODE = 0;
        public final int STATE_DISABLED = 1;
        public final int STATE_ENABLEING = 2;
        public final int STATE_DISABLEING = 3;

        private  NfcManager() {

            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter intentFilters[] = new IntentFilter[] { tagDetected };
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, REQUEST_CODE,
                    new Intent(MainActivity.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            nfcAdapter.enableForegroundDispatch(MainActivity.this, pendingIntent, intentFilters, null);

        }




        /**
         * Writes an NdefMessage to a NFC tag.
         * @param msg the message to be written
         * @param tag the tag that stores the message
         * @return true if write was successful; false otherwise.
         */
        public boolean writeTag(NdefMessage msg, Tag tag) {
            int size = msg.getByteArrayLength();

            Ndef ndef = Ndef.get(tag);
            try {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    return false;
                }
                ndef.writeNdefMessage(msg);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
            return true;

        }

        public boolean readTag() {
            return false;
        }

        /**
         * @return true is NFC is enabled; false otherwise.
         */
        public boolean isNfcEnabled() {
            return nfcAdapter.isEnabled();
        }

        @Override
        public void onNewIntent(Intent intent) {

        }

    }


}
