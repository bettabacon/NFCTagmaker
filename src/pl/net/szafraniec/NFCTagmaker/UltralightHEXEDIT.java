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
import android.widget.EditText;
import android.widget.Toast;

public class UltraLightWriteActivity extends Activity {
private boolean typ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ultralight);
		Button read = (Button) findViewById(R.id.read);
		read.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View self) {
				Toast.makeText(getApplicationContext(),
						"place tag against your device to read HEX values",
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

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
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
	public void onNewIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			//int success = 0;
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			//Ndef ndef = Ndef.get(tag);
			//writeTag(tag);
			EditText et1 = (EditText) findViewById(R.id.editText1);
			EditText et2 = (EditText) findViewById(R.id.editText2);
			EditText et3 = (EditText) findViewById(R.id.editText3);
			EditText et4 = (EditText) findViewById(R.id.editText4);
			EditText et5 = (EditText) findViewById(R.id.editText5);
			EditText et6 = (EditText) findViewById(R.id.editText6);
			EditText et7 = (EditText) findViewById(R.id.editText7);
			EditText et8 = (EditText) findViewById(R.id.editText8);
			EditText et9 = (EditText) findViewById(R.id.editText9);
			
			if (!typ) {
			byte [] buffer =readpage(tag,3);
			et1.setText(bytesToHex(buffer));
			buffer =readpage(tag,4);
			et2.setText(bytesToHex(buffer));
			buffer =readpage(tag,5);
			et3.setText(bytesToHex(buffer));
			buffer =readpage(tag,6);
			et4.setText(bytesToHex(buffer));
			buffer =readpage(tag,7);
			et5.setText(bytesToHex(buffer));
			buffer =readpage(tag,8);
			et6.setText(bytesToHex(buffer));
			buffer =readpage(tag,9);
			et7.setText(bytesToHex(buffer));
			buffer =readpage(tag,10);
			et8.setText(bytesToHex(buffer));
			buffer =readpage(tag,11);
			et9.setText(bytesToHex(buffer));
			}
			else {
			byte [] buffer1 = hexStringToByteArray(et1.getText().toString());
			byte [] buffer2 = hexStringToByteArray(et2.getText().toString());
			byte [] buffer3 = hexStringToByteArray(et3.getText().toString());
			byte [] buffer4 = hexStringToByteArray(et4.getText().toString());
			byte [] buffer5 = hexStringToByteArray(et5.getText().toString());
			byte [] buffer6 = hexStringToByteArray(et6.getText().toString());
			byte [] buffer7 = hexStringToByteArray(et7.getText().toString());
			byte [] buffer8 = hexStringToByteArray(et8.getText().toString());
			byte [] buffer9 = hexStringToByteArray(et9.getText().toString());
			writeTag(tag,3,buffer1);
			writeTag(tag,4,buffer2);
			writeTag(tag,5,buffer3);
			writeTag(tag,6,buffer4);
			writeTag(tag,7,buffer5);
			writeTag(tag,8,buffer6);
			writeTag(tag,9,buffer7);
			writeTag(tag,10,buffer8);
			writeTag(tag,11,buffer9);
			}
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(100);
			Toast.makeText(getApplicationContext(),
					"Done!",
					Toast.LENGTH_LONG).show();
			//finish();
		}
	}
	
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
    public void writeTag(Tag tag,int page, byte[] data) {
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            //ultralight.writePage(4, "abcd".getBytes(Charset.forName("US-ASCII")));
            //ultralight.writePage(5, "efgh".getBytes(Charset.forName("US-ASCII")));
            //ultralight.writePage(6, "ijkl".getBytes(Charset.forName("US-ASCII")));
            //ultralight.writePage(7, "mnop".getBytes(Charset.forName("US-ASCII")));
            ultralight.writePage(page, data);
        } catch (IOException e) {
            Log.e(TAG, "IOException while closing MifareUltralight...", e);
        } finally {
            try {
                ultralight.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing MifareUltralight...", e);
            }
        }
    }
    
    private static final String TAG = pl.net.szafraniec.NFCTagmaker.UltraLightWriteActivity.class.getSimpleName();



    public String readTag(Tag tag) {
        MifareUltralight mifare = MifareUltralight.get(tag);
        try {
            mifare.connect();
            byte[] payload = mifare.readPages(4);
            return new String(payload, Charset.forName("US-ASCII"));
        } catch (IOException e) {
            Log.e(TAG, "IOException while writing MifareUltralight message...", e);
        } finally {
            if (mifare != null) {
               try {
                   mifare.close();
               }
               catch (IOException e) {
                   Log.e(TAG, "Error closing tag...", e);
               }
            }
        }
        return null;
     
        
    }
    
    


    public byte[] readpage(Tag tag, int page) {
        MifareUltralight mifare = MifareUltralight.get(tag);
        try {
            mifare.connect();
            byte[] buffer = mifare.readPages(page);
            byte[] buffer2 = Arrays.copyOf(buffer, 4);             
            return buffer2;
        } catch (IOException e) {
            Log.e(TAG, "IOException while writing MifareUltralight message...", e);
        } finally {
            if (mifare != null) {
               try {
                   mifare.close();
               }
               catch (IOException e) {
                   Log.e(TAG, "Error closing tag...", e);
               }
            }
        }
        return (byte[]) null;
        
        
    }

}
