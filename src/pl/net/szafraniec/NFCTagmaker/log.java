package pl.net.szafraniec.NFCTagmaker;

import android.util.Log;

public class log {
	static boolean appDebug = false;
	private static final String LOG_TAG = Config.LOG_TAG;

	public static void d(String message) {

		if ((appDebug) && (message != null)) {
			Log.d(LOG_TAG, message);
		}
	}

	public static void e(String message) {
		if ((appDebug) && (message != null)) {
			Log.e(LOG_TAG, message);
		}
	}

	public static void i(String message) {

		if ((appDebug) && (message != null)) {
			Log.i(LOG_TAG, message);
		}
	}

	public static void v(String message) {

		if ((appDebug) && (message != null)) {
			Log.v(LOG_TAG, message);
		}
	}

	public static void w(String message) {
		if ((appDebug) && (message != null)) {
			Log.w(LOG_TAG, message);
		}
	}
}
