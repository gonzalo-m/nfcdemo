package com.example.lokillo.nfcdemo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
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


public class MainActivity extends Activity {

    static final String TAG = "NFC";
    private boolean mLockTag;

    static final int REQUEST_CODE = 0;

    private TextView button;
    private NfcManager nfcManager;

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

        nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);

        // check if device supports NFC
        if (nfcManager != null) {
            onNfcSupported();
        } else  {
            onNfcNotSupported();
        }
    }

//    private boolean isNfcSupported() {
//        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
//    }

    private void onNfcSupported() {
        Log.i(TAG, "NFC is supported");
        Toast.makeText(this, "NFC is supported", Toast.LENGTH_SHORT).show();

        // ask user to enable NFC, if not enabled
        if(!nfcManager.getDefaultAdapter().isEnabled()) {

            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }

    }

    public boolean readTag() {
        return false;
    }

    /**
     * Writes an NDEF Message to a NFC tag.
     * @param msg the message to be written
     * @param tag the tag that stores the message
     * @return true if the write was successful; false otherwise.
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


    /**
     *
     * @param id the id to be stored in the NDEF Message
     * @return an NDEF Message from the given parameter
     */
    public NdefMessage toNdefMessage(int id) {
        String msg = String.valueOf(id);
        byte[] msgBytes = msg.getBytes();
        NdefRecord msgRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "Alarm.com".getBytes(),
                new byte[]{}, msgBytes);
        return new NdefMessage(msgRecord);
    }

    @Override
    public void onNewIntent(Intent intent) {

        if (intent.getAction() == NfcAdapter.ACTION_TAG_DISCOVERED) {
            Toast.makeText(MainActivity.this, "debug", Toast.LENGTH_SHORT).show();
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Log.d(TAG, detectedTag.toString());
            boolean succ = writeTag(toNdefMessage(77), detectedTag);
            if (succ) {
                Toast.makeText(MainActivity.this, "tag was successfully written", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void onNfcNotSupported() {
        Log.i(TAG, "NFC not supported");
        Toast.makeText(this, "NFC is not supported", Toast.LENGTH_SHORT).show();

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

    private void enableTagWrite() {
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter intentFilters[] = new IntentFilter[] { tagDetected };
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, REQUEST_CODE,
                new Intent(MainActivity.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_UPDATE_CURRENT);

        nfcManager.getDefaultAdapter().enableForegroundDispatch(MainActivity.this, pendingIntent, intentFilters, null);
    }
    @Override
    public void onResume() {
        super.onResume();
        enableTagWrite();

    }

    // https://kayrnt.wordpress.com/2012/12/03/read-write-lock-tags-in-your-android-app-androiddev/


}
