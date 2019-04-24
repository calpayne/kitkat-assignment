package com.kitkat.group.clubs.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;

import com.kitkat.group.clubs.MainActivity;

/**
 * Created by SHAKTHI on 24/04/2019.
 */

public class subTask {
    public NfcAdapter Resume(Context context, NfcAdapter nfcAdapter,Intent intent )
    {
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch((Activity) context, pendingIntent, null, null);
        return nfcAdapter;
    }
    public NfcAdapter Pause(Context context,NfcAdapter nfcAdapter)
    {
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        nfcAdapter.disableForegroundDispatch((Activity) context);
        return  nfcAdapter;
    }
}
