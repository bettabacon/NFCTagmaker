/**
 * Copyright (C) 2014 Mateusz Szafraniec
 * This file is part of NFCTagMaker.
 *
 * NFCTagMaker is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * NFCTagMaker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NFCTagMaker; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Ten plik jest częścią NFCTagMaker.
 *
 * NFCTagMaker jest wolnym oprogramowaniem; możesz go rozprowadzać dalej
 * i/lub modyfikować na warunkach Powszechnej Licencji Publicznej GNU,
 * wydanej przez Fundację Wolnego Oprogramowania - według wersji 2 tej
 * Licencji lub (według twojego wyboru) którejś z późniejszych wersji.
 *
 * Niniejszy program rozpowszechniany jest z nadzieją, iż będzie on
 * użyteczny - jednak BEZ JAKIEJKOLWIEK GWARANCJI, nawet domyślnej
 * gwarancji PRZYDATNOŚCI HANDLOWEJ albo PRZYDATNOŚCI DO OKREŚLONYCH
 * ZASTOSOWAŃ. W celu uzyskania bliższych informacji sięgnij do
 * Powszechnej Licencji Publicznej GNU.
 *
 * Z pewnością wraz z niniejszym programem otrzymałeś też egzemplarz
 * Powszechnej Licencji Publicznej GNU (GNU General Public License);
 * jeśli nie - napisz do Free Software Foundation, Inc., 59 Temple
 * Place, Fifth Floor, Boston, MA  02110-1301  USA
 */
package pl.net.szafraniec.NFCTagmaker;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final X500Principal DEBUG_DN = new X500Principal(
            "CN=Android Debug,O=Android,C=US"); //$NON-NLS-1$

    public static String version;

    private static NdefRecord createNdefMySmartPosterRecord(String text,
            String[] uri, byte[] type) {
        final NdefRecord[] records = new NdefRecord[1 + uri.length];
        records[0] = createNdefTextRecord(text);
        for (int i = 1; i < records.length; i++) {
            records[i] = createNdefRecord(uri[i - 1], type[i - 1]);
        }
        final NdefMessage nm = new NdefMessage(records);
        final NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_SMART_POSTER, new byte[0], nm.toByteArray());
        return record;
    }

    protected static NdefRecord createNdefRecord(String address, byte idCode) {
        final byte[] uriField = address.getBytes(Charset.forName("UTF-8"));
        final byte[] payload = new byte[uriField.length + 1];
        payload[0] = idCode;
        System.arraycopy(uriField, 0, payload, 1, uriField.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI,
                new byte[0], payload);
    }

    private static NdefRecord createNdefSmartPosterRecord(String text,
            String[] uri) {
        final NdefRecord[] records = new NdefRecord[1 + uri.length];
        records[0] = createNdefTextRecord(text);
        for (int i = 1; i < records.length; i++) {
            records[i] = createNdefTelRecord(uri[i - 1]);
        }
        final NdefMessage nm = new NdefMessage(records);
        final NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_SMART_POSTER, new byte[0], nm.toByteArray());
        return record;
    }

    protected static NdefRecord createNdefTelRecord(String phone) {
        return createNdefRecord(phone, (byte) 0x05);
    }

    protected static NdefRecord createNdefTextRecord(String text) {
        final String lang = "en";
        final byte[] textBytes = text.getBytes();
        byte[] langBytes = null;
        try {
            langBytes = lang.getBytes("UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            log.e(e.getLocalizedMessage());
        }
        final int langLength = langBytes.length;
        final int textLength = textBytes.length;
        final byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
                new byte[0], payload);
    }

    protected static NdefRecord createNdefUriRecord(String phone) {
        return createNdefRecord(phone, (byte) 0x00);
    }

    public static int getRunCount(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(
                Config.PREF_RUNCOUNT, 0);
        return prefs.getInt(Config.PREF_RUNCOUNT, 0);
    }

    public static void setRunCount(Context context, int RunCount) {
        final SharedPreferences.Editor prefs = context.getSharedPreferences(
                Config.PREF_RUNCOUNT, 0).edit();
        prefs.putInt(Config.PREF_RUNCOUNT, RunCount);
        prefs.commit();
    }

    final public int ABOUT = 0;

    private boolean CheckDebuggable(Context ctx) {
        boolean debuggable = false;

        try {
            final PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            final Signature signatures[] = pinfo.signatures;

            final CertificateFactory cf = CertificateFactory
                    .getInstance("X.509"); //$NON-NLS-1$

            for (final Signature signature : signatures) {
                final ByteArrayInputStream stream = new ByteArrayInputStream(
                        signature.toByteArray());
                final X509Certificate cert = (X509Certificate) cf
                        .generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }
        }
        catch (final NameNotFoundException e) {
            // debuggable variable will remain false
        }
        catch (final CertificateException e) {
            // debuggable variable will remain false
        }
        return debuggable;
    }

    private String getHex(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            final int b = bytes[i] & 0xff;
            if (b < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String getMifareType(Tag tag) {
        String type = "Unknown";
        for (final String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                final MifareClassic mifareTag = MifareClassic.get(tag);
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                final MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
            }
        }
        return type;
    }

    private void nfc_disable() {
        final NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        adapter.disableForegroundDispatch(this);
    }

    private void nfc_enable() {
        // Register for any NFC event (only while we're in the foreground)

        final NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        final PendingIntent pending_intent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        adapter.enableForegroundDispatch(this, pending_intent, null, null);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log.appDebug = CheckDebuggable(this);
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        }
        catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
        final NfcAdapter Nfc = NfcAdapter.getDefaultAdapter(this);
        if (Nfc == null) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.CantFindNFCAdapter), Toast.LENGTH_LONG)
                    .show();
        }
        if (Nfc != null) {
            if (Nfc.isEnabled() != true) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.EnabeNFCFirst), Toast.LENGTH_LONG)
                        .show();
                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            }
        }
        int RunCount = getRunCount(this);
        if (RunCount == 4) {
            showDialog(1);
        }
        if (RunCount < 6) {
            RunCount = RunCount + 1;
            setRunCount(this, RunCount);
        }
        final SharedPreferences settings = getSharedPreferences(
                Config.PREFS_NAME, 0);
        Config.uri = settings.getString("uri", getString(R.string.defaultUri));
        Config.phone = settings.getString("phone",
                getString(R.string.defaultPhone));
        Config.name = settings.getString("name",
                getString(R.string.defaultName));
        Config.email = settings.getString("email",
                getString(R.string.defaultEmail));
        Config.web = settings.getString("web", getString(R.string.defaultWeb));

        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (action.equalsIgnoreCase(Intent.ACTION_SEND)
                && intent.hasExtra(Intent.EXTRA_TEXT)) {
            final String s = intent.getStringExtra(Intent.EXTRA_TEXT);
            Config.uri = s;
            final SharedPreferences.Editor editor = settings.edit();
            editor.putString("uri", Config.uri);
            editor.commit();
        }
        final Button x = (Button) findViewById(R.id.quit);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                finish();
            }
        });

        final Button advanced = (Button) findViewById(R.id.advanced);
        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                final Intent intent = new Intent(getApplicationContext(),
                        AdvancedActivity.class);
                startActivity(intent);
            }
        });

        final Button wu = (Button) findViewById(R.id.Writeuri);
        wu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                if (Config.uri.length() != 0) {
                    final NdefRecord ndef_records = NdefRecord
                            .createUri(Config.uri);
                    Config.nfc_payload = new NdefMessage(ndef_records);
                    final Intent intent = new Intent(getApplicationContext(),
                            WriteNFCActivity.class);
                    startActivity(intent);
                }
                else {
                    final Intent settings = new Intent(getApplicationContext(),
                            SettingsActivity.class);
                    startActivity(settings);
                }
            }
        });

        final Button wp = (Button) findViewById(R.id.Writephone);
        wp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                if ((Config.phone.length() != 0) && (Config.name.length() != 0)) {
                    final NdefRecord[] ndef_name = new NdefRecord[1];
                    final String[] uri = new String[] { Config.phone };
                    ndef_name[0] = createNdefSmartPosterRecord(Config.name, uri);
                    Config.nfc_payload = new NdefMessage(ndef_name);
                    final Intent intent = new Intent(getApplicationContext(),
                            WriteNFCActivity.class);
                    startActivity(intent);
                }
                else {
                    final Intent settings = new Intent(getApplicationContext(),
                            SettingsActivity.class);
                    startActivity(settings);
                }
            }
        });

        final Button sp = (Button) findViewById(R.id.Writesp);
        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                final NdefRecord[] ndef_name = new NdefRecord[1];
                int ile = 0;
                int gdzie = 0;

                if (Config.phone.length() > 3) {
                    ile = ile + 1;
                }

                if (Config.uri.length() > 3) {
                    ile = ile + 1;
                }

                if (Config.email.length() > 6) {
                    ile = ile + 1;
                }
                if (Config.web.length() > 6) {
                    ile = ile + 1;
                }

                final String[] uri = new String[ile];
                final byte[] type = new byte[ile];

                if (Config.phone.length() > 3) {
                    uri[gdzie] = Config.phone;
                    type[gdzie] = 0x05;
                    gdzie = gdzie + 1;
                }

                if (Config.uri.length() > 3) {
                    uri[gdzie] = Config.uri;
                    type[gdzie] = 0x00;
                    gdzie = gdzie + 1;
                }

                if (Config.email.length() > 6) {
                    uri[gdzie] = Config.email;
                    type[gdzie] = 0x06;
                    gdzie = gdzie + 1;
                }
                if (Config.web.length() > 6) {
                    uri[gdzie] = Config.web;
                    type[gdzie] = 0x00;
                    gdzie = gdzie + 1;
                }
                ndef_name[0] = createNdefMySmartPosterRecord(Config.name, uri,
                        type);
                Config.nfc_payload = new NdefMessage(ndef_name);
                final Intent intent = new Intent(getApplicationContext(),
                        WriteNFCActivity.class);
                startActivity(intent);
            }
        });

        final Button wvc = (Button) findViewById(R.id.WritevCard);
        wvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                String vcardcontent = "BEGIN:VCARD\r\nVERSION:2.1\r\nN:"
                        + Config.name + "\r\n";

                if (Config.phone.length() > 3) {
                    vcardcontent = vcardcontent + "TEL;CELL:" + Config.phone
                            + "\r\n";
                }

                if (Config.email.length() > 6) {
                    vcardcontent = vcardcontent + "EMAIL;INTERNET:"
                            + Config.email + "\r\n";
                }

                if (Config.web.length() > 6) {
                    vcardcontent = vcardcontent + "URL:" + Config.web + "\r\n";
                }

                vcardcontent = vcardcontent + "END:VCARD\r\n";
                final NdefRecord ndef_records = NdefRecord.createMime(
                        "text/x-vCard", vcardcontent.getBytes());
                Config.nfc_payload = new NdefMessage(ndef_records);
                final Intent intent = new Intent(getApplicationContext(),
                        WriteNFCActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // We have only one dialog.
        Dialog dialog;
        AlertDialog.Builder builder;
        switch (id) {
            case 1:
                builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.rate_text))
                        .setTitle(
                                getResources().getString(
                                        R.string.menu_item_rate))
                        .setCancelable(false)
                        .setPositiveButton(
                                getResources().getString(
                                        R.string.menu_item_rate),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int id) {
                                        // Do something here
                                        final Intent GooglePlayIntent = new Intent(
                                                Intent.ACTION_VIEW,
                                                Config.URI_APP_LINK);
                                        startActivity(GooglePlayIntent);
                                    }
                                })
                        .setNegativeButton(
                                getResources().getString(
                                        android.R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int id) {
                                        // Do something here
                                    }
                                });

                dialog = builder.create();
                break;
            default:
                dialog = null;
        }
        return dialog;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        final String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            final String typ_karty = getMifareType(tag);
            Toast.makeText(getApplicationContext(),
                    getString(R.string.card_type) + typ_karty,
                    Toast.LENGTH_LONG).show();
            final Ndef ndef = Ndef.get(tag);
            Toast.makeText(getApplicationContext(),
                    "ID:" + getHex(tag.getId()), Toast.LENGTH_LONG).show();
            if (ndef != null) {
                Toast.makeText(getApplicationContext(),
                        "Type:" + ndef.getType() + "size:" + ndef.getMaxSize(),
                        Toast.LENGTH_LONG).show();
            }
            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(100);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.ABOUT:
                final AboutDialog about = new AboutDialog(this);
                about.setTitle(getString(R.string.About));
                about.show();
                break;
            case R.id.menu_item_rate:
                showDialog(1);
                return true;
            case R.id.settings:
                final Intent settings = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                startActivity(settings);
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfc_disable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfc_enable();
    }
}
