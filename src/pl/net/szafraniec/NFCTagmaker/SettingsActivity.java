package pl.net.szafraniec.NFCTagmaker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

public class SettingsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		EditText uri = (EditText) findViewById(R.id.uri);
		uri.setText(NFCTagmakerSettings.uri);
		EditText name = (EditText) findViewById(R.id.name);
		name.setText(NFCTagmakerSettings.name);
		EditText phone = (EditText) findViewById(R.id.phone);
		phone.setText(NFCTagmakerSettings.phone);
		EditText email = (EditText) findViewById(R.id.email);
		email.setText(NFCTagmakerSettings.email);
		EditText web = (EditText) findViewById(R.id.web);
		web.setText(NFCTagmakerSettings.web);

	}

	@Override
	protected void onStop() {
		super.onStop();
		EditText uri = (EditText) findViewById(R.id.uri);
		NFCTagmakerSettings.uri = uri.getText().toString();
		EditText name = (EditText) findViewById(R.id.name);
		NFCTagmakerSettings.name = name.getText().toString();
		EditText phone = (EditText) findViewById(R.id.phone);
		NFCTagmakerSettings.phone = phone.getText().toString();
		EditText email = (EditText) findViewById(R.id.email);
		NFCTagmakerSettings.email = email.getText().toString();
		EditText web = (EditText) findViewById(R.id.web);
		NFCTagmakerSettings.web = web.getText().toString();
		SharedPreferences settings = getSharedPreferences(
				NFCTagmakerSettings.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("uri", NFCTagmakerSettings.uri);
		editor.putString("name", NFCTagmakerSettings.name);
		editor.putString("phone", NFCTagmakerSettings.phone);
		editor.putString("web", NFCTagmakerSettings.web);
		editor.putString("email", NFCTagmakerSettings.email);
		editor.commit();
	}

}
