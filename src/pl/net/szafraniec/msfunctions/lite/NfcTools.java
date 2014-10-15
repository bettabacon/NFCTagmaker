package pl.net.szafraniec.msfunctions.lite;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;

@SuppressLint("NewApi")
public class NfcTools {
    public static NdefRecord createNdefMySmartPosterRecord(String text,
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

    public static NdefRecord createNdefRecord(String content, byte idCode) {
        final byte[] uriField = content.getBytes(Charset.forName("UTF-8"));
        final byte[] payload = new byte[uriField.length + 1];
        payload[0] = idCode;
        System.arraycopy(uriField, 0, payload, 1, uriField.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI,
                new byte[0], payload);
    }

    public static NdefRecord createNdefSmartPosterRecord(String text,
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

    public static NdefRecord createNdefTelRecord(String phone) {
        return createNdefRecord(phone, (byte) 0x05);
    }

    public static NdefRecord createNdefTextRecord(String text) {
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

    public static NdefRecord createNdefUriRecord(String uri) {
        return createNdefRecord(uri, (byte) 0x00);
    }

    @SuppressLint("NewApi")
    public static String getMifareType(Tag tag) {
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

    public static NdefMessage ndefVcard(String name, String phone,
            String email, String web) {
        String vcardcontent = "BEGIN:VCARD\r\nVERSION:2.1\r\nN:" + name
                + "\r\n";

        if (phone.length() > 3) {
            vcardcontent = vcardcontent + "TEL;CELL:" + phone + "\r\n";
        }

        if (email.length() > 6) {
            vcardcontent = vcardcontent + "EMAIL;INTERNET:" + email + "\r\n";
        }

        if (web.length() > 6) {
            vcardcontent = vcardcontent + "URL:" + web + "\r\n";
        }

        vcardcontent = vcardcontent + "END:VCARD\r\n";
        final NdefRecord ndef_records = NdefRecord.createMime("text/x-vCard",
                vcardcontent.getBytes());
        return new NdefMessage(ndef_records);
    }

    static public void nfc_disable(Context context, Activity activity) {
        final NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        adapter.disableForegroundDispatch(activity);
    }

    @SuppressWarnings("rawtypes")
    public static void nfc_enable(Context context, Activity activity, Class cls) {
        // Register for any NFC event (only while we're in the foreground)

        final NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        final PendingIntent pending_intent = PendingIntent.getActivity(context,
                0, new Intent(context, cls)
        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        adapter.enableForegroundDispatch(activity, pending_intent, null, null);
    }

}
