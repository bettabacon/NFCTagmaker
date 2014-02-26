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
 * along with NFCKey; if not, write to the Free Software
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

import java.nio.charset.Charset;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
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
	private boolean card_write;

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

	private String getMifareType(Tag tag) {
		String type = "Unknown";
		for (String tech : tag.getTechList()) {
			if (tech.equals(MifareClassic.class.getName())) {
				MifareClassic mifareTag = MifareClassic.get(tag);
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
				MifareUltralight mifareUlTag = MifareUltralight.get(tag);
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
						getString(R.string.readHEX), Toast.LENGTH_LONG).show();
				card_write = false;
			}
		});
		Button write = (Button) findViewById(R.id.write);
		write.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.writeHEX), Toast.LENGTH_LONG).show();
				card_write = true;
			}
		});

	}

	@Override
	public void onNewIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String typ_karty = getMifareType(tag);
			Toast.makeText(getApplicationContext(),
					getString(R.string.card_type) + typ_karty,
					Toast.LENGTH_LONG).show();
			if ((typ_karty == "Ultralight") || (typ_karty == "Ultralight C")) {
				EditText et00 = (EditText) findViewById(R.id.editText00);
				EditText et01 = (EditText) findViewById(R.id.editText01);
				EditText et02 = (EditText) findViewById(R.id.editText02);
				EditText et03 = (EditText) findViewById(R.id.editText03);
				EditText et04 = (EditText) findViewById(R.id.editText04);
				EditText et05 = (EditText) findViewById(R.id.editText05);
				EditText et06 = (EditText) findViewById(R.id.editText06);
				EditText et07 = (EditText) findViewById(R.id.editText07);
				EditText et08 = (EditText) findViewById(R.id.editText08);
				EditText et09 = (EditText) findViewById(R.id.editText09);
				EditText et0A = (EditText) findViewById(R.id.editText0A);
				EditText et0B = (EditText) findViewById(R.id.editText0B);
				EditText et0C = (EditText) findViewById(R.id.editText0C);
				EditText et0D = (EditText) findViewById(R.id.editText0D);
				EditText et0E = (EditText) findViewById(R.id.editText0E);
				EditText et0F = (EditText) findViewById(R.id.editText0F);

				if (!card_write) {
					
					byte[] buffer = readpage(tag, 0);
					et00.setText(bytesToHex(buffer));
					buffer = readpage(tag, 1);
					et01.setText(bytesToHex(buffer));
					buffer = readpage(tag, 2);
					et02.setText(bytesToHex(buffer));
					buffer = readpage(tag, 3);
					et03.setText(bytesToHex(buffer));
					buffer = readpage(tag, 4);
					et04.setText(bytesToHex(buffer));
					buffer = readpage(tag, 5);
					et05.setText(bytesToHex(buffer));
					buffer = readpage(tag, 6);
					et06.setText(bytesToHex(buffer));
					buffer = readpage(tag, 7);
					et07.setText(bytesToHex(buffer));
					buffer = readpage(tag, 8);
					et08.setText(bytesToHex(buffer));
					buffer = readpage(tag, 9);
					et09.setText(bytesToHex(buffer));
					buffer = readpage(tag, 10);
					et0A.setText(bytesToHex(buffer));
					buffer = readpage(tag, 11);
					et0B.setText(bytesToHex(buffer));
					buffer = readpage(tag, 12);
					et0C.setText(bytesToHex(buffer));
					buffer = readpage(tag, 13);
					et0D.setText(bytesToHex(buffer));
					buffer = readpage(tag, 14);
					et0E.setText(bytesToHex(buffer));
					buffer = readpage(tag, 15);
					et0F.setText(bytesToHex(buffer));
				} else {
					byte[] buffer00 = hexStringToByteArray(et00.getText()
							.toString());
					byte[] buffer01 = hexStringToByteArray(et01.getText()
							.toString());
					byte[] buffer02 = hexStringToByteArray(et02.getText()
							.toString());
					byte[] buffer03 = hexStringToByteArray(et03.getText()
							.toString());
					byte[] buffer04 = hexStringToByteArray(et04.getText()
							.toString());
					byte[] buffer05 = hexStringToByteArray(et05.getText()
							.toString());
					byte[] buffer06 = hexStringToByteArray(et06.getText()
							.toString());
					byte[] buffer07 = hexStringToByteArray(et07.getText()
							.toString());
					byte[] buffer08 = hexStringToByteArray(et08.getText()
							.toString());
					byte[] buffer09 = hexStringToByteArray(et09.getText()
							.toString());
					byte[] buffer0A = hexStringToByteArray(et0A.getText()
							.toString());
					byte[] buffer0B = hexStringToByteArray(et0B.getText()
							.toString());
					byte[] buffer0C = hexStringToByteArray(et0C.getText()
							.toString());
					byte[] buffer0D = hexStringToByteArray(et0D.getText()
							.toString());
					byte[] buffer0E = hexStringToByteArray(et0E.getText()
							.toString());
					byte[] buffer0F = hexStringToByteArray(et0F.getText()
							.toString());
					CheckBox cb00 = (CheckBox) findViewById(R.id.checkBox00);
					CheckBox cb01 = (CheckBox) findViewById(R.id.checkBox01);
					CheckBox cb02 = (CheckBox) findViewById(R.id.checkBox02);
					CheckBox cb03 = (CheckBox) findViewById(R.id.checkBox03);
					CheckBox cb04 = (CheckBox) findViewById(R.id.checkBox04);
					CheckBox cb05 = (CheckBox) findViewById(R.id.checkBox05);
					CheckBox cb06 = (CheckBox) findViewById(R.id.checkBox06);
					CheckBox cb07 = (CheckBox) findViewById(R.id.checkBox07);
					CheckBox cb08 = (CheckBox) findViewById(R.id.checkBox08);
					CheckBox cb09 = (CheckBox) findViewById(R.id.checkBox09);
					CheckBox cb0A = (CheckBox) findViewById(R.id.checkBox0A);
					CheckBox cb0B = (CheckBox) findViewById(R.id.checkBox0B);
					CheckBox cb0C = (CheckBox) findViewById(R.id.checkBox0C);
					CheckBox cb0D = (CheckBox) findViewById(R.id.checkBox0D);
					CheckBox cb0E = (CheckBox) findViewById(R.id.checkBox0E);
					CheckBox cb0F = (CheckBox) findViewById(R.id.checkBox0F);
					if (cb00.isChecked()) {
						writeTag(tag, 0, buffer00);
					}
					if (cb01.isChecked()) {
						writeTag(tag, 1, buffer01);
					}
					if (cb02.isChecked()) {
						writeTag(tag, 2, buffer02);
					}
					if (cb03.isChecked()) {
						writeTag(tag, 3, buffer03);
					}
					if (cb04.isChecked()) {
						writeTag(tag, 4, buffer04);
					}
					if (cb05.isChecked()) {
						writeTag(tag, 5, buffer05);
					}
					if (cb06.isChecked()) {
						writeTag(tag, 6, buffer06);
					}
					if (cb07.isChecked()) {
						writeTag(tag, 7, buffer07);
					}
					if (cb08.isChecked()) {
						writeTag(tag, 8, buffer08);
					}
					if (cb09.isChecked()) {
						writeTag(tag, 9, buffer09);
					}
					if (cb0A.isChecked()) {
						writeTag(tag, 10, buffer0A);
					}
					if (cb0B.isChecked()) {
						writeTag(tag, 11, buffer0B);
					}
					if (cb0C.isChecked()) {
						writeTag(tag, 12, buffer0C);
					}
					if (cb0D.isChecked()) {
						writeTag(tag, 13, buffer0D);
					}
					if (cb0E.isChecked()) {
						writeTag(tag, 14, buffer0E);
					}
					if (cb0F.isChecked()) {
						writeTag(tag, 15, buffer0F);
					}
				}
				Toast.makeText(getApplicationContext(),
						getString(R.string.done), Toast.LENGTH_SHORT).show();
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(100);

			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.card_not_supported),
						Toast.LENGTH_LONG).show();
			}
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
		} catch (Exception e) {
			Log.e(TAG, "Exception while reading MifareUltralight message...", e);
			Toast.makeText(getApplicationContext(), "Error:" + e,
					Toast.LENGTH_SHORT).show();
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
				} catch (Exception e) {
					Log.e(TAG, "Error closing tag...", e);
					Toast.makeText(getApplicationContext(), "Error:" + e,
							Toast.LENGTH_SHORT).show();
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
		} catch (Exception e) {
			Log.e(TAG, "IOException while reading MifareUltralight message...",
					e);
			Toast.makeText(getApplicationContext(), "Error:" + e,
					Toast.LENGTH_SHORT).show();
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
				} catch (Exception e) {
					Log.e(TAG, "Error closing tag...", e);
					Toast.makeText(getApplicationContext(), "Error:" + e,
							Toast.LENGTH_SHORT).show();
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
		} catch (Exception e) {
			Log.e(TAG, "Exception while writing MifareUltralight...", e);
			Toast.makeText(getApplicationContext(), "Error:" + e,
					Toast.LENGTH_SHORT).show();
		} finally {
			try {
				ultralight.close();
			} catch (Exception e) {
				Log.e(TAG, "Exception while closing MifareUltralight...", e);
				Toast.makeText(getApplicationContext(), "Error:" + e,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
