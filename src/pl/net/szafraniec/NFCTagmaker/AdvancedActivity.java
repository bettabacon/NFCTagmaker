package pl.net.szafraniec.NFCTagmaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdvancedActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced);

		Button ul = (Button) findViewById(R.id.ultralight);
		ul.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				Intent intent = new Intent(getApplicationContext(),
						UltralightHEXEDIT.class);
				startActivity(intent);
			}
		});

		Button clone = (Button) findViewById(R.id.clone);
		clone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				Intent intent = new Intent(getApplicationContext(),
						CloneReadActivity.class);
				startActivity(intent);
			}
		});

	}
}
