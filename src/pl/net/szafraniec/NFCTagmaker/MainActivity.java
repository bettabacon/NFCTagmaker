/**
 * Copyright (C) 2014 Mateusz Szafraniec
 * This file is part of NFCKey.
 *
 * NFCKey is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * NFCKey is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NFCKey; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Ten plik jest czÄ™Ĺ›ciÄ… NFCKey.
 *
 * NFCKey jest wolnym oprogramowaniem; moĹĽesz go rozprowadzaÄ‡ dalej
 * i/lub modyfikowaÄ‡ na warunkach Powszechnej Licencji Publicznej GNU,
 * wydanej przez FundacjÄ™ Wolnego Oprogramowania - wedĹ‚ug wersji 2 tej
 * Licencji lub (wedĹ‚ug twojego wyboru) ktĂłrejĹ› z pĂłĹşniejszych wersji.
 *
 * Niniejszy program rozpowszechniany jest z nadziejÄ…, iĹĽ bÄ™dzie on
 * uĹĽyteczny - jednak BEZ JAKIEJKOLWIEK GWARANCJI, nawet domyĹ›lnej
 * gwarancji PRZYDATNOĹšCI HANDLOWEJ albo PRZYDATNOĹšCI DO OKREĹšLONYCH
 * ZASTOSOWAĹ�. W celu uzyskania bliĹĽszych informacji siÄ™gnij do
 * Powszechnej Licencji Publicznej GNU.
 *
 * Z pewnoĹ›ciÄ… wraz z niniejszym programem otrzymaĹ‚eĹ› teĹĽ egzemplarz
 * Powszechnej Licencji Publicznej GNU (GNU General Public License);
 * jeĹ›li nie - napisz do Free Software Foundation, Inc., 59 Temple
 * Place, Fifth Floor, Boston, MA  02110-1301  USA
 */
package pl.net.szafraniec.NFCTagmaker;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	final public int ABOUT = 0;
	public static String version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		NfcAdapter Nfc = NfcAdapter.getDefaultAdapter(this);
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
		SharedPreferences settings = getSharedPreferences(
				NFCTagmakerSettings.PREFS_NAME, 0);
		NFCTagmakerSettings.uri = settings.getString("uri",
				getString(R.string.defaultUri));
		NFCTagmakerSettings.phone = settings.getString("phone",
				getString(R.string.defaultPhone));
		NFCTagmakerSettings.name = settings.getString("name",
				getString(R.string.defaultName));
		Button x = (Button) findViewById(R.id.quit);
		x.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				finish();
			}
		});

		Button clone = (Button) findViewById(R.id.clone);
		clone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				Intent intent = new Intent(getApplicationContext(),
						CloneReadActivity.class);
				startActivity(intent);
				finish();
			}
		});

		Button wu = (Button) findViewById(R.id.Writeuri);
		wu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				NdefRecord ndef_records = NdefRecord
						.createUri(NFCTagmakerSettings.uri);
				NFCTagmakerSettings.nfc_payload = new NdefMessage(ndef_records);
				Intent intent = new Intent(getApplicationContext(),
						WriteNFCActivity.class);
				startActivity(intent);
			}
		});

		Button wp = (Button) findViewById(R.id.Writephone);
		wp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				NdefRecord[] ndef_name = new NdefRecord[1];
				String[] uri = new String[] { NFCTagmakerSettings.phone, "1234567890" };
				ndef_name[0] = createNdefSmartPosterRecord(
						NFCTagmakerSettings.name, uri);
				NFCTagmakerSettings.nfc_payload = new NdefMessage(ndef_name);
				Intent intent = new Intent(getApplicationContext(),
						WriteNFCActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.ABOUT:
			AboutDialog about = new AboutDialog(this);
			about.setTitle(getString(R.string.About));
			about.show();
			break;
		case R.id.donate:
			Intent intent = new Intent(getApplicationContext(),
					DonateActivity.class);
			startActivity(intent);
			break;
		case R.id.settings:
			Intent settings = new Intent(getApplicationContext(),
					SettingsActivity.class);
			startActivity(settings);
			break;
		}
		return true;
	}

	// Returns a text record according to the propositions given by the NFC
	// Forum
	protected static NdefRecord createNdefTextRecord(String text) {
		String lang = "en";
		byte[] textBytes = text.getBytes();
		byte[] langBytes = null;
		try {
			langBytes = lang.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(MainActivity.class.getSimpleName(), e.getLocalizedMessage());
		}
		int langLength = langBytes.length;
		int textLength = textBytes.length;
		byte[] payload = new byte[1 + langLength + textLength];
		// Sets status byte
		payload[0] = (byte) langLength;
		// Copies langbytes and textbytes into payload
		System.arraycopy(langBytes, 0, payload, 1, langLength);
		System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
				new byte[0], payload);
	}

	// Creates a URI record according to the propositions given by the NFC Forum
	protected static NdefRecord createNdefUriRecord(String address) {
		byte[] uriField = address.getBytes(Charset.forName("UTF-8"));
		byte[] payload = new byte[uriField.length + 1];
		byte idCode = 0x00;
		payload[0] = idCode;
		System.arraycopy(uriField, 0, payload, 1, uriField.length);
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI,
				new byte[0], payload);
	}

	protected static NdefRecord createNdefPhoneRecord(String address) {
		byte[] uriField = address.getBytes(Charset.forName("UTF-8"));
		byte[] payload = new byte[uriField.length + 1];
		byte idCode = 0x05;
		payload[0] = idCode;
		System.arraycopy(uriField, 0, payload, 1, uriField.length);
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI,
				new byte[0], payload);
	}

	// Creates a smartposter record according to the propositions given by the
	// NFC Forum

	private static NdefRecord createNdefSmartPosterRecord(String text,
			String[] uri) {
		NdefRecord[] records = new NdefRecord[1 + uri.length];
		records[0] = createNdefTextRecord(text);
		for (int i = 1; i < records.length; i++)
			// records[i] = createNdefUriRecord(uri[i - 1]) ;
			records[i] = createNdefPhoneRecord(uri[i - 1]);
		NdefMessage nm = new NdefMessage(records);
		NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_SMART_POSTER, new byte[0], nm.toByteArray());
		return record;
	}

}
