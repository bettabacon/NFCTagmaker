package pl.net.szafraniec.NFCTagmaker;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class UltralightHEXEDIT extends Activity {
	private boolean typ;

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static final String TAG = pl.net.szafraniec.NFCTagmaker.UltralightHEXEDIT.class
			.getSimpleName();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	private void nfc_disable() {
		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
		adapter.disableForegroundDispatch(this);
	}

	private void nfc_enable() {
		// Register for any NFC event (only while we're in the foreground)

		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
		PendingIntent pending_intent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass())
						.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		adapter.enableForegroundDispatch(this, pending_intent, null, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ultralight);
		Button read = (Button) findViewById(R.id.read);
		read.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				Toast.makeText(getApplicationContext(),
						"place tag against your device to READ HEX values",
						Toast.LENGTH_LONG).show();
				typ = false;
			}
		});
		Button write = (Button) findViewById(R.id.write);
		write.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				Toast.makeText(getApplicationContext(),
						"place tag against your device to WRITE HEX values",
						Toast.LENGTH_LONG).show();
				typ = true;
			}
		});

	}

	@Override
	public void onNewIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			EditText et1 = (EditText) findViewById(R.id.editText1);
			EditText et2 = (EditText) findViewById(R.id.editText2);
			EditText et3 = (EditText) findViewById(R.id.editText3);
			EditText et4 = (EditText) findViewById(R.id.editText4);
			EditText et5 = (EditText) findViewById(R.id.editText5);
			EditText et6 = (EditText) findViewById(R.id.editText6);
			EditText et7 = (EditText) findViewById(R.id.editText7);
			EditText et8 = (EditText) findViewById(R.id.editText8);
			EditText et9 = (EditText) findViewById(R.id.editText9);
			EditText et10 = (EditText) findViewById(R.id.editText10);
			EditText et11 = (EditText) findViewById(R.id.editText11);
			EditText et12 = (EditText) findViewById(R.id.editText12);
			EditText et13 = (EditText) findViewById(R.id.editText13);

			if (!typ) {
				byte[] buffer = readpage(tag, 3);
				et1.setText(bytesToHex(buffer));
				buffer = readpage(tag, 4);
				et2.setText(bytesToHex(buffer));
				buffer = readpage(tag, 5);
				et3.setText(bytesToHex(buffer));
				buffer = readpage(tag, 6);
				et4.setText(bytesToHex(buffer));
				buffer = readpage(tag, 7);
				et5.setText(bytesToHex(buffer));
				buffer = readpage(tag, 8);
				et6.setText(bytesToHex(buffer));
				buffer = readpage(tag, 9);
				et7.setText(bytesToHex(buffer));
				buffer = readpage(tag, 10);
				et8.setText(bytesToHex(buffer));
				buffer = readpage(tag, 11);
				et9.setText(bytesToHex(buffer));
				buffer = readpage(tag, 12);
				et10.setText(bytesToHex(buffer));
				buffer = readpage(tag, 13);
				et11.setText(bytesToHex(buffer));
				buffer = readpage(tag, 14);
				et12.setText(bytesToHex(buffer));
				buffer = readpage(tag, 15);
				et13.setText(bytesToHex(buffer));
			} else {
				byte[] buffer1 = hexStringToByteArray(et1.getText().toString());
				byte[] buffer2 = hexStringToByteArray(et2.getText().toString());
				byte[] buffer3 = hexStringToByteArray(et3.getText().toString());
				byte[] buffer4 = hexStringToByteArray(et4.getText().toString());
				byte[] buffer5 = hexStringToByteArray(et5.getText().toString());
				byte[] buffer6 = hexStringToByteArray(et6.getText().toString());
				byte[] buffer7 = hexStringToByteArray(et7.getText().toString());
				byte[] buffer8 = hexStringToByteArray(et8.getText().toString());
				byte[] buffer9 = hexStringToByteArray(et9.getText().toString());
				byte[] buffer10 = hexStringToByteArray(et10.getText()
						.toString());
				byte[] buffer11 = hexStringToByteArray(et11.getText()
						.toString());
				byte[] buffer12 = hexStringToByteArray(et12.getText()
						.toString());
				byte[] buffer13 = hexStringToByteArray(et13.getText()
						.toString());
				CheckBox cb1 = (CheckBox) findViewById(R.id.checkBox1);
				CheckBox cb2 = (CheckBox) findViewById(R.id.checkBox2);
				CheckBox cb3 = (CheckBox) findViewById(R.id.checkBox3);
				CheckBox cb4 = (CheckBox) findViewById(R.id.checkBox4);
				CheckBox cb5 = (CheckBox) findViewById(R.id.checkBox5);
				CheckBox cb6 = (CheckBox) findViewById(R.id.checkBox6);
				CheckBox cb7 = (CheckBox) findViewById(R.id.checkBox7);
				CheckBox cb8 = (CheckBox) findViewById(R.id.checkBox8);
				CheckBox cb9 = (CheckBox) findViewById(R.id.checkBox9);
				CheckBox cb10 = (CheckBox) findViewById(R.id.checkBox10);
				CheckBox cb11 = (CheckBox) findViewById(R.id.checkBox11);
				CheckBox cb12 = (CheckBox) findViewById(R.id.checkBox12);
				CheckBox cb13 = (CheckBox) findViewById(R.id.checkBox13);
				if (cb1.isChecked()) {
					writeTag(tag, 3, buffer1);
				}
				if (cb2.isChecked()) {
					writeTag(tag, 4, buffer2);
				}
				if (cb3.isChecked()) {
					writeTag(tag, 5, buffer3);
				}
				if (cb4.isChecked()) {
					writeTag(tag, 6, buffer4);
				}
				if (cb5.isChecked()) {
					writeTag(tag, 7, buffer5);
				}
				if (cb6.isChecked()) {
					writeTag(tag, 8, buffer6);
				}
				if (cb7.isChecked()) {
					writeTag(tag, 9, buffer7);
				}
				if (cb8.isChecked()) {
					writeTag(tag, 10, buffer8);
				}
				if (cb9.isChecked()) {
					writeTag(tag, 11, buffer9);
				}
				if (cb10.isChecked()) {
					writeTag(tag, 12, buffer10);
				}
				if (cb11.isChecked()) {
					writeTag(tag, 13, buffer11);
				}
				if (cb12.isChecked()) {
					writeTag(tag, 14, buffer12);
				}
				if (cb13.isChecked()) {
					writeTag(tag, 15, buffer13);
				}
			}
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(100);
			Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT)
					.show();
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

	public byte[] readpage(Tag tag, int page) {
		MifareUltralight mifare = MifareUltralight.get(tag);
		try {
			mifare.connect();
			byte[] buffer = mifare.readPages(page);
			byte[] buffer2 = Arrays.copyOf(buffer, 4);
			return buffer2;
		} catch (IOException e) {
			Log.e(TAG, "IOException while reading MifareUltralight message...",
					e);
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
				} catch (IOException e) {
					Log.e(TAG, "Error closing tag...", e);
				}
			}
		}
		return null;

	}

	public String readTag(Tag tag) {
		MifareUltralight mifare = MifareUltralight.get(tag);
		try {
			mifare.connect();
			byte[] payload = mifare.readPages(4);
			return new String(payload, Charset.forName("US-ASCII"));
		} catch (IOException e) {
			Log.e(TAG, "IOException while reading MifareUltralight message...",
					e);
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
				} catch (IOException e) {
					Log.e(TAG, "Error closing tag...", e);
				}
			}
		}
		return null;

	}

	public void writeTag(Tag tag, int page, byte[] data) {
		MifareUltralight ultralight = MifareUltralight.get(tag);
		try {
			ultralight.connect();
			ultralight.writePage(page, data);
		} catch (IOException e) {
			Log.e(TAG, "IOException while writing MifareUltralight...", e);
		} finally {
			try {
				ultralight.close();
			} catch (IOException e) {
				Log.e(TAG, "IOException while closing MifareUltralight...", e);
			}
		}
	}

}
