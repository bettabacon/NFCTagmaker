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
	}

	@Override
	protected void onStop() {
		super.onStop();
		EditText url = (EditText) findViewById(R.id.uri);
		NFCTagmakerSettings.uri = url.getText().toString();
		SharedPreferences settings = getSharedPreferences(
				NFCTagmakerSettings.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("uri", NFCTagmakerSettings.uri);
		editor.commit();
	}

}
