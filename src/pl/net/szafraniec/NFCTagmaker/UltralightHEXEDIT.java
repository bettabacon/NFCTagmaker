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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import pl.net.szafraniec.msfunctions.NfcTools;
import pl.net.szafraniec.msfunctions.Tools;
import pl.net.szafraniec.msfunctions.log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class UltralightHEXEDIT extends Activity {
    private boolean card_write;
    private static String errortext;
    private static final int PICK_FILE = 1;
    private static final int SAVE_NEW_FILE = 2;
    private static File defaultFile = new File(
            Environment.getExternalStorageDirectory(), "nfctag.bin");

    public void exportTag(File file) throws IOException {

        final EditText et00 = (EditText) findViewById(R.id.editText00);
        final EditText et01 = (EditText) findViewById(R.id.editText01);
        final EditText et02 = (EditText) findViewById(R.id.editText02);
        final EditText et03 = (EditText) findViewById(R.id.editText03);
        final EditText et04 = (EditText) findViewById(R.id.editText04);
        final EditText et05 = (EditText) findViewById(R.id.editText05);
        final EditText et06 = (EditText) findViewById(R.id.editText06);
        final EditText et07 = (EditText) findViewById(R.id.editText07);
        final EditText et08 = (EditText) findViewById(R.id.editText08);
        final EditText et09 = (EditText) findViewById(R.id.editText09);
        final EditText et0A = (EditText) findViewById(R.id.editText0A);
        final EditText et0B = (EditText) findViewById(R.id.editText0B);
        final EditText et0C = (EditText) findViewById(R.id.editText0C);
        final EditText et0D = (EditText) findViewById(R.id.editText0D);
        final EditText et0E = (EditText) findViewById(R.id.editText0E);
        final EditText et0F = (EditText) findViewById(R.id.editText0F);
        final byte[] buffer00 = Tools.hexStringToByteArray(et00.getText().toString());
        final byte[] buffer01 = Tools.hexStringToByteArray(et01.getText().toString());
        final byte[] buffer02 = Tools.hexStringToByteArray(et02.getText().toString());
        final byte[] buffer03 = Tools.hexStringToByteArray(et03.getText().toString());
        final byte[] buffer04 = Tools.hexStringToByteArray(et04.getText().toString());
        final byte[] buffer05 = Tools.hexStringToByteArray(et05.getText().toString());
        final byte[] buffer06 = Tools.hexStringToByteArray(et06.getText().toString());
        final byte[] buffer07 = Tools.hexStringToByteArray(et07.getText().toString());
        final byte[] buffer08 = Tools.hexStringToByteArray(et08.getText().toString());
        final byte[] buffer09 = Tools.hexStringToByteArray(et09.getText().toString());
        final byte[] buffer0A = Tools.hexStringToByteArray(et0A.getText().toString());
        final byte[] buffer0B = Tools.hexStringToByteArray(et0B.getText().toString());
        final byte[] buffer0C = Tools.hexStringToByteArray(et0C.getText().toString());
        final byte[] buffer0D = Tools.hexStringToByteArray(et0D.getText().toString());
        final byte[] buffer0E = Tools.hexStringToByteArray(et0E.getText().toString());
        final byte[] buffer0F = Tools.hexStringToByteArray(et0F.getText().toString());
        FileOutputStream fos;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.write(buffer00);
            fos.write(buffer01);
            fos.write(buffer02);
            fos.write(buffer03);
            fos.write(buffer04);
            fos.write(buffer05);
            fos.write(buffer06);
            fos.write(buffer07);
            fos.write(buffer08);
            fos.write(buffer09);
            fos.write(buffer0A);
            fos.write(buffer0B);
            fos.write(buffer0C);
            fos.write(buffer0D);
            fos.write(buffer0E);
            fos.write(buffer0F);
            fos.flush();
            fos.close();
            Toast.makeText(getApplicationContext(), getString(R.string.done),
                    Toast.LENGTH_SHORT).show();

        }
        catch (final IOException e) {
            log.w("IOException while writing file");
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "IOException while writing file" + e, Toast.LENGTH_SHORT)
                    .show();
            // return false;
        }

        // return true;
    }
    
    public void importTag(File file) throws IOException {
        final EditText et00 = (EditText) findViewById(R.id.editText00);
        final EditText et01 = (EditText) findViewById(R.id.editText01);
        final EditText et02 = (EditText) findViewById(R.id.editText02);
        final EditText et03 = (EditText) findViewById(R.id.editText03);
        final EditText et04 = (EditText) findViewById(R.id.editText04);
        final EditText et05 = (EditText) findViewById(R.id.editText05);
        final EditText et06 = (EditText) findViewById(R.id.editText06);
        final EditText et07 = (EditText) findViewById(R.id.editText07);
        final EditText et08 = (EditText) findViewById(R.id.editText08);
        final EditText et09 = (EditText) findViewById(R.id.editText09);
        final EditText et0A = (EditText) findViewById(R.id.editText0A);
        final EditText et0B = (EditText) findViewById(R.id.editText0B);
        final EditText et0C = (EditText) findViewById(R.id.editText0C);
        final EditText et0D = (EditText) findViewById(R.id.editText0D);
        final EditText et0E = (EditText) findViewById(R.id.editText0E);
        final EditText et0F = (EditText) findViewById(R.id.editText0F);
        FileInputStream fis;

        try {
            fis = new FileInputStream(file);

            final byte[] buffer = new byte[64];
            byte[] buffer2 = new byte[4];
            fis.read(buffer, 0, 64);
            fis.close();
            buffer2 = Arrays.copyOfRange(buffer, 0, 4);
            et00.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 4, 8);
            et01.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 8, 12);
            et02.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 12, 16);
            et03.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 16, 20);
            et04.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 20, 24);
            et05.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 24, 28);
            et06.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 28, 32);
            et07.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 32, 36);
            et08.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 36, 40);
            et09.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 40, 44);
            et0A.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 44, 48);
            et0B.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 48, 52);
            et0C.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 52, 56);
            et0D.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 56, 60);
            et0E.setText(Tools.bytesToHex(buffer2));
            buffer2 = Arrays.copyOfRange(buffer, 60, 64);
            et0F.setText(Tools.bytesToHex(buffer2));
            Toast.makeText(getApplicationContext(), getString(R.string.done),
                    Toast.LENGTH_SHORT).show();
        }
        catch (final FileNotFoundException e) {
            log.w("FileNotFound");
            Toast.makeText(getApplicationContext(),
                    getString(R.string.FileNotFound) + file.toString(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        catch (final IOException ee) {
            log.w("IOException" + ee);
            Toast.makeText(getApplicationContext(),
                    "IOException" + ee + " " + file.toString(),
                    Toast.LENGTH_LONG).show();
            ee.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FILE: {
                if (resultCode == RESULT_OK && data != null
                        && data.getData() != null) {
                    final String theFilePath = data.getData().getPath();
                    final File file = new File(theFilePath);
                    try {
                        importTag(file);
                    }
                    catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
                break;
            case SAVE_NEW_FILE: {
                if (resultCode == RESULT_OK && data != null
                        && data.getData() != null) {
                    final String theFilePath = data.getData().getPath();
                    final File file = new File(theFilePath);
                    try {
                        exportTag(file);
                    }
                    catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ultralight);
        final Button read = (Button) findViewById(R.id.read);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.readHEX), Toast.LENGTH_LONG).show();
                card_write = false;
            }
        });
        final Button write = (Button) findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.writeHEX), Toast.LENGTH_LONG).show();
                card_write = true;
            }
        });

        final Button export = (Button) findViewById(R.id.export_button);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                final Intent intent = new Intent(
                        "org.openintents.action.PICK_FILE");
                intent.putExtra(Intent.EXTRA_TITLE, "A Custom Title"); // optional
                try {
                    startActivityForResult(intent, SAVE_NEW_FILE);
                }
                catch (final RuntimeException rr) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.OIFile), Toast.LENGTH_LONG)
                            .show();
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.SavingTo)
                                    + defaultFile.toString(), Toast.LENGTH_LONG)
                            .show();
                    try {
                        exportTag(defaultFile);
                    }
                    catch (final IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        final Button import_button = (Button) findViewById(R.id.import_button);
        import_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View self) {
                try {
                    final Intent intent = new Intent(
                            "org.openintents.action.PICK_FILE");
                    startActivityForResult(intent, PICK_FILE);
                }
                catch (final RuntimeException rr) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.OIFile), Toast.LENGTH_LONG)
                            .show();

                    try {
                        importTag(defaultFile);
                    }
                    catch (final IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onNewIntent(Intent intent) {
        final String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            final String typ_karty = NfcTools.getMifareType(tag);
            Toast.makeText(getApplicationContext(),
                    getString(R.string.card_type) + typ_karty,
                    Toast.LENGTH_LONG).show();
            if ((typ_karty == "Ultralight") || (typ_karty == "Ultralight C")) {
                final EditText et00 = (EditText) findViewById(R.id.editText00);
                final EditText et01 = (EditText) findViewById(R.id.editText01);
                final EditText et02 = (EditText) findViewById(R.id.editText02);
                final EditText et03 = (EditText) findViewById(R.id.editText03);
                final EditText et04 = (EditText) findViewById(R.id.editText04);
                final EditText et05 = (EditText) findViewById(R.id.editText05);
                final EditText et06 = (EditText) findViewById(R.id.editText06);
                final EditText et07 = (EditText) findViewById(R.id.editText07);
                final EditText et08 = (EditText) findViewById(R.id.editText08);
                final EditText et09 = (EditText) findViewById(R.id.editText09);
                final EditText et0A = (EditText) findViewById(R.id.editText0A);
                final EditText et0B = (EditText) findViewById(R.id.editText0B);
                final EditText et0C = (EditText) findViewById(R.id.editText0C);
                final EditText et0D = (EditText) findViewById(R.id.editText0D);
                final EditText et0E = (EditText) findViewById(R.id.editText0E);
                final EditText et0F = (EditText) findViewById(R.id.editText0F);

                if (!card_write) {

                    byte[] buffer = readpage(tag, 0);
                    et00.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 1);
                    et01.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 2);
                    et02.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 3);
                    et03.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 4);
                    et04.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 5);
                    et05.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 6);
                    et06.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 7);
                    et07.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 8);
                    et08.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 9);
                    et09.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 10);
                    et0A.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 11);
                    et0B.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 12);
                    et0C.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 13);
                    et0D.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 14);
                    et0E.setText(Tools.bytesToHex(buffer));
                    buffer = readpage(tag, 15);
                    et0F.setText(Tools.bytesToHex(buffer));
                }
                else {
                    final byte[] buffer00 = Tools.hexStringToByteArray(et00.getText()
                            .toString());
                    final byte[] buffer01 = Tools.hexStringToByteArray(et01.getText()
                            .toString());
                    final byte[] buffer02 = Tools.hexStringToByteArray(et02.getText()
                            .toString());
                    final byte[] buffer03 = Tools.hexStringToByteArray(et03.getText()
                            .toString());
                    final byte[] buffer04 = Tools.hexStringToByteArray(et04.getText()
                            .toString());
                    final byte[] buffer05 = Tools.hexStringToByteArray(et05.getText()
                            .toString());
                    final byte[] buffer06 = Tools.hexStringToByteArray(et06.getText()
                            .toString());
                    final byte[] buffer07 = Tools.hexStringToByteArray(et07.getText()
                            .toString());
                    final byte[] buffer08 = Tools.hexStringToByteArray(et08.getText()
                            .toString());
                    final byte[] buffer09 = Tools.hexStringToByteArray(et09.getText()
                            .toString());
                    final byte[] buffer0A = Tools.hexStringToByteArray(et0A.getText()
                            .toString());
                    final byte[] buffer0B = Tools.hexStringToByteArray(et0B.getText()
                            .toString());
                    final byte[] buffer0C = Tools.hexStringToByteArray(et0C.getText()
                            .toString());
                    final byte[] buffer0D = Tools.hexStringToByteArray(et0D.getText()
                            .toString());
                    final byte[] buffer0E = Tools.hexStringToByteArray(et0E.getText()
                            .toString());
                    final byte[] buffer0F = Tools.hexStringToByteArray(et0F.getText()
                            .toString());
                    final CheckBox cb00 = (CheckBox) findViewById(R.id.checkBox00);
                    final CheckBox cb01 = (CheckBox) findViewById(R.id.checkBox01);
                    final CheckBox cb02 = (CheckBox) findViewById(R.id.checkBox02);
                    final CheckBox cb03 = (CheckBox) findViewById(R.id.checkBox03);
                    final CheckBox cb04 = (CheckBox) findViewById(R.id.checkBox04);
                    final CheckBox cb05 = (CheckBox) findViewById(R.id.checkBox05);
                    final CheckBox cb06 = (CheckBox) findViewById(R.id.checkBox06);
                    final CheckBox cb07 = (CheckBox) findViewById(R.id.checkBox07);
                    final CheckBox cb08 = (CheckBox) findViewById(R.id.checkBox08);
                    final CheckBox cb09 = (CheckBox) findViewById(R.id.checkBox09);
                    final CheckBox cb0A = (CheckBox) findViewById(R.id.checkBox0A);
                    final CheckBox cb0B = (CheckBox) findViewById(R.id.checkBox0B);
                    final CheckBox cb0C = (CheckBox) findViewById(R.id.checkBox0C);
                    final CheckBox cb0D = (CheckBox) findViewById(R.id.checkBox0D);
                    final CheckBox cb0E = (CheckBox) findViewById(R.id.checkBox0E);
                    final CheckBox cb0F = (CheckBox) findViewById(R.id.checkBox0F);
                    if (cb00.isChecked()) {
                        writeUltralightTagByPage(tag, 0, buffer00);
                    }
                    if (cb01.isChecked()) {
                        writeUltralightTagByPage(tag, 1, buffer01);
                    }
                    if (cb02.isChecked()) {
                        writeUltralightTagByPage(tag, 2, buffer02);
                    }
                    if (cb03.isChecked()) {
                        writeUltralightTagByPage(tag, 3, buffer03);
                    }
                    if (cb04.isChecked()) {
                        writeUltralightTagByPage(tag, 4, buffer04);
                    }
                    if (cb05.isChecked()) {
                        writeUltralightTagByPage(tag, 5, buffer05);
                    }
                    if (cb06.isChecked()) {
                        writeUltralightTagByPage(tag, 6, buffer06);
                    }
                    if (cb07.isChecked()) {
                        writeUltralightTagByPage(tag, 7, buffer07);
                    }
                    if (cb08.isChecked()) {
                        writeUltralightTagByPage(tag, 8, buffer08);
                    }
                    if (cb09.isChecked()) {
                        writeUltralightTagByPage(tag, 9, buffer09);
                    }
                    if (cb0A.isChecked()) {
                        writeUltralightTagByPage(tag, 10, buffer0A);
                    }
                    if (cb0B.isChecked()) {
                        writeUltralightTagByPage(tag, 11, buffer0B);
                    }
                    if (cb0C.isChecked()) {
                        writeUltralightTagByPage(tag, 12, buffer0C);
                    }
                    if (cb0D.isChecked()) {
                        writeUltralightTagByPage(tag, 13, buffer0D);
                    }
                    if (cb0E.isChecked()) {
                        writeUltralightTagByPage(tag, 14, buffer0E);
                    }
                    if (cb0F.isChecked()) {
                        writeUltralightTagByPage(tag, 15, buffer0F);
                    }
                }
                Toast.makeText(getApplicationContext(),
                        getString(R.string.done), Toast.LENGTH_SHORT).show();
                final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(100);

            }
            else {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.card_not_supported),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcTools.nfc_disable(this,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcTools.nfc_enable(this,this,getClass());
    }

    public byte[] readpage(Tag tag, int page) {
        final MifareUltralight mifare = MifareUltralight.get(tag);
        try {
            mifare.connect();
            final byte[] buffer = mifare.readPages(page);
            final byte[] buffer2 = Arrays.copyOf(buffer, 4);
            return buffer2;
        }
        catch (final Exception e) {
            errortext = getString(R.string.exReadingPage) + page + " " + e;
            log.e(errortext);
            Toast.makeText(getApplicationContext(), errortext,
                    Toast.LENGTH_SHORT).show();
        }
        finally {
            if (mifare != null) {
                try {
                    mifare.close();
                }
                catch (final Exception e) {
                    errortext = getString(R.string.exClosingTag) + " " + e;
                    log.e(errortext);
                    Toast.makeText(getApplicationContext(), errortext,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        return null;
    }

    public void writeUltralightTagByPage(Tag tag, int page, byte[] data) {
        final MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            ultralight.writePage(page, data);
        }
        catch (final Exception e) {
            errortext = getString(R.string.exWritingPage) + page + " " + e;
            log.e(errortext);
            Toast.makeText(getApplicationContext(), errortext,
                    Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                ultralight.close();
            }
            catch (final Exception e) {
                errortext = getString(R.string.exClosingTag) + " " + e;
                log.e(errortext);
                Toast.makeText(getApplicationContext(), errortext,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
