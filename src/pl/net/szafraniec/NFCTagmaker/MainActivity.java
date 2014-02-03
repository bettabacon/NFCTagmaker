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


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import pl.net.szafraniec.NFCTagmaker.AboutDialog;
import pl.net.szafraniec.NFCTagmaker.R;
import android.provider.Settings;
import android.content.pm.PackageManager.NameNotFoundException;

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
//			finish();
		}
		if (Nfc != null) {
			if (Nfc.isEnabled() != true) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.EnabeNFCFirst), Toast.LENGTH_LONG)
						.show();
//				startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
			}
		}
		SharedPreferences settings = getSharedPreferences(
				NFCTagmakerSettings.PREFS_NAME, 0);
		NFCTagmakerSettings.uri = settings.getString("uri", getString(R.string.defaultUri));
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

		Button wu = (Button) findViewById(R.id.Writeurl);
		wu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				NdefRecord ndef_records = NdefRecord.createUri(NFCTagmakerSettings.uri);
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
				NdefRecord ndef_phone = NdefRecord.createUri("tel:"+NFCTagmakerSettings.phone);
		        NdefRecord ndef_uri = NdefRecord.createUri(NFCTagmakerSettings.uri);
				NFCTagmakerSettings.nfc_payload = new NdefMessage(ndef_phone,ndef_uri);
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

}
