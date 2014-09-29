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

import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WriteNFCActivity extends Activity {

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

    @Override
    protected void onCreate(Bundle sis) {
        super.onCreate(sis);
        setContentView(R.layout.activity_write_nfc);

        setResult(0);
        final Button b = (Button) findViewById(R.id.cancel_nfc_write_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                nfc_disable();
                finish();
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        final String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            int success = 0;
            final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            final Ndef ndef = Ndef.get(tag);
            final int payload_length = Config.nfc_payload.getByteArrayLength();
            if (ndef != null) {
                try {
                    final int tag_size = ndef.getMaxSize();
                    if (tag_size >= payload_length) {
                        ndef.connect();
                        ndef.writeNdefMessage(Config.nfc_payload);
                        ndef.close();
                        success = 1;
                    }
                    else {
                        Toast.makeText(
                                getApplicationContext(),
                                "NFC Tag size is too small\r\nMessage size: "
                                        + payload_length + " Tag size: "
                                        + tag_size, Toast.LENGTH_LONG).show();
                    }
                }
                catch (final IOException e) {
                    e.printStackTrace();
                    log.e("IOExceptionWrite");
                    Toast.makeText(getApplicationContext(), "IOExceptionWrite",
                            Toast.LENGTH_SHORT).show();

                }
                catch (final NullPointerException e) {
                    e.printStackTrace();
                    log.e("NullPointerWrite");
                    Toast.makeText(getApplicationContext(), "NullPointerWrite",
                            Toast.LENGTH_SHORT).show();

                }
                catch (final FormatException e) {
                    e.printStackTrace();
                    log.e("FormatExceptionWrite");
                    Toast.makeText(getApplicationContext(),
                            "FormatExceptionWrite", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                final NdefFormatable format = NdefFormatable.get(tag);
                final MifareClassic mifareTag = MifareClassic.get(tag);
                final int tag_size = mifareTag.getSize();
                if (format != null) {
                    try {
                        Toast.makeText(getApplicationContext(),
                                "Format: " + tag_size, Toast.LENGTH_SHORT)
                                .show();
                        format.connect();
                        format.format(Config.nfc_payload);
                        format.close();
                        success = 1;
                    }
                    catch (final IOException e) {
                        e.printStackTrace();
                        log.e("IOExceptionFormat");
                        Toast.makeText(getApplicationContext(),
                                "IOExceptionFormat", Toast.LENGTH_SHORT).show();

                    }
                    catch (final NullPointerException e) {
                        e.printStackTrace();
                        log.e("NullPointerFormat");
                        Toast.makeText(getApplicationContext(),
                                "NullPointerFormat", Toast.LENGTH_SHORT).show();

                    }
                    catch (final FormatException e) {
                        e.printStackTrace();
                        log.e("FormatExceptionFormat");
                        Toast.makeText(getApplicationContext(),
                                "FormatExceptionFormat", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
            setResult(success);
            if (success == 1) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.Success), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.Failed), Toast.LENGTH_SHORT).show();
            }
            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(100);
            finish();
        }
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
