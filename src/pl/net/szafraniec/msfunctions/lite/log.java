package pl.net.szafraniec.msfunctions.lite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

public class log {
    static void appendLog(String text) {
        final File logFile = new File(
                Environment.getExternalStorageDirectory(), LOG_TAG + ".log"); //$NON-NLS-1$
        final Time now = new Time();
        now.setToNow();
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            }
            catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            // BufferedWriter for performance, true to set append to file flag
            final BufferedWriter buf = new BufferedWriter(new FileWriter(
                    logFile, true));
            buf.append(now.format2445() + ";" + text); //$NON-NLS-1$
            buf.newLine();
            buf.flush();
            buf.close();
        }
        catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void d(String message) {

        if ((appDebug) && (message != null)) {
            Log.d(LOG_TAG, message);
            if (fullDebug) {
                appendLog("DEBUG;" + message); //$NON-NLS-1$
            }
        }
    }

    public static void e(String message) {
        if ((appDebug) && (message != null)) {
            Log.e(LOG_TAG, message);
            if (fullDebug) {
                appendLog("ERROR;" + message); //$NON-NLS-1$
            }
        }
    }

    public static void i(String message) {

        if ((appDebug) && (message != null)) {
            Log.i(LOG_TAG, message);
            if (fullDebug) {
                appendLog("INFO;" + message); //$NON-NLS-1$
            }
        }
    }

    public static void setLogTag(String logTag) {
        log.LOG_TAG = logTag;
    }

    public static void v(String message) {

        if ((appDebug) && (message != null)) {
            Log.v(LOG_TAG, message);
            if (fullDebug) {
                appendLog("VERBOSE;" + message); //$NON-NLS-1$
            }
        }
    }

    public static void w(String message) {
        if ((appDebug) && (message != null)) {
            Log.w(LOG_TAG, message);
            if (fullDebug) {
                appendLog("WARNING;" + message); //$NON-NLS-1$
            }
        }
    }

    public static boolean appDebug = false;

    public static boolean fullDebug = false;

    public static String LOG_TAG = ""; //$NON-NLS-1$
}
