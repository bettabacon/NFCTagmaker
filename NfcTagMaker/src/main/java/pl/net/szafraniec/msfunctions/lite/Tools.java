package pl.net.szafraniec.msfunctions.lite;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.provider.Settings;

public class Tools extends Activity {

    /**
     * @param bytes
     *            Input bytes
     * @return output as hex string
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes != null) {
            final char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                final int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }
        else {
            return null;
        }
    }

    /**
     * @param ctx
     *            Context
     * @return true if in debug mode
     */
    public static boolean CheckDebuggable(Context ctx) {
        boolean debuggable = false;
        try {
            final PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            final Signature signatures[] = pinfo.signatures;

            final CertificateFactory cf = CertificateFactory
                    .getInstance("X.509"); //$NON-NLS-1$

            for (final Signature signature : signatures) {
                final ByteArrayInputStream stream = new ByteArrayInputStream(
                        signature.toByteArray());
                final X509Certificate cert = (X509Certificate) cf
                        .generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }
        }
        catch (final NameNotFoundException e) {
            // debuggable variable will remain false
        }
        catch (final CertificateException e) {
            // debuggable variable will remain false
        }
        return debuggable;
    }

    

    /**
     * @param number
     * @return int[]
     */
    public static int[] decToBin(int number) {
        final int[] array = new int[8];
        int k = array.length - 1;

        while (number != 0) {
            array[k--] = number & 1;
            number >>>= 1;
        }
        return array;
    }

  
    /**
     * @param context
     *            Context
     * @return int versionCode
     */
    public static int getAppBuild(Context context) {
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (final NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e); //$NON-NLS-1$
        }
    }

    /**
     * @param context
     *            Context
     * @return String versionName
     */
    public static String getAppVersion(Context context) {
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        }
        catch (final NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e); //$NON-NLS-1$
        }
    }

     /**
     * get md5 device id
     *
     * @param context
     *            context
     * @return md5 device id
     */
    public static final String getMd5DeviceId(Context context) {
        final String android_id = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        final String deviceId = md5(android_id).toUpperCase();
        return deviceId;
    }

    public static int getNewsVersion(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(
                PREF_NEWS_VERSION, 0);
        return prefs.getInt(PREF_NEWS_VERSION, 0);
    }

    public static int getRunCount(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(
                PREF_RUNCOUNT, 0);
        return prefs.getInt(PREF_RUNCOUNT, 0);
    }

    /**
     * @param s
     *            string in hex format
     * @return byte[]
     */
    public static byte[] hexStringToByteArray(String s) {
        if (s != null) {
            final int len = s.length();
            final byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                        .digit(s.charAt(i + 1), 16));
            }
            return data;
        }
        else {
            return null;
        }
    }

    /**
     * @param targetPackage
     *            package to check
     * @param context
     * @return true if exists
     */
    public static boolean isPackageExisted(String targetPackage, Context context) {
        final PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        }
        catch (final NameNotFoundException e) {
            return false;
        }
        return true;
    }

     /**
     * Calculate md5 from string
     *
     * @param s
     *            String
     * @return md5 string
     */
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            final MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            final byte messageDigest[] = digest.digest();

            // Create Hex String
            final StringBuffer hexString = new StringBuffer();
            for (final byte element : messageDigest) {
                String h = Integer.toHexString(0xFF & element);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();

        }
        catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param string
     *            to check
     * @return true if string is not null or empty
     */
    public static boolean notNullOrEmpty(String string) {
        if (string == null) {
            return false;
        }
        if (string.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * @param string
     *            to check
     * @return true if string is null or empty
     */
    public static boolean nullOrEmpty(String string) {
        return (!(notNullOrEmpty(string)));
    }

       
    /**
     * @param id
     *            resource id of raw text file
     * @param mContext
     * @return string
     */
    public static String readRawTextFile(int id, Context mContext) {
        final InputStream inputStream = mContext.getResources()
                .openRawResource(id);
        final InputStreamReader in = new InputStreamReader(inputStream);
        final BufferedReader buf = new BufferedReader(in);
        String line;
        final StringBuilder text = new StringBuilder();
        try {
            while ((line = buf.readLine()) != null) {
                text.append(line);
            }
        }
        catch (final IOException e) {
            return null;
        }
        return text.toString();
    }

    public static void setNewsVersion(Context context, int version) {
        final SharedPreferences.Editor prefs = context.getSharedPreferences(
                PREF_NEWS_VERSION, 0).edit();
        prefs.putInt(PREF_NEWS_VERSION, version);
        prefs.commit();
    }

    public static void setRunCount(Context context, int RunCount) {
        final SharedPreferences.Editor prefs = context.getSharedPreferences(
                PREF_RUNCOUNT, 0).edit();
        prefs.putInt(PREF_RUNCOUNT, RunCount);
        prefs.commit();
    }

    /**
     * @param str
     *            string to convert to integer (with round from float)
     * @return integer
     */
    public static int stringToInt(String str) {
        final float f = Float.parseFloat(str);
        if (f > 0) {
            return Math.round(f);
        }
        return Integer.parseInt(str);

    }

    /**
     * @param string
     *            string to convert to integer
     * @return integer
     */
    public static int strToInt(String string) {
        return Integer.parseInt(string.replaceAll("[\\D]", "")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray(); //$NON-NLS-1$

    private static final X500Principal DEBUG_DN = new X500Principal(
            "CN=Android Debug,O=Android,C=US"); //$NON-NLS-1$

    public static final String PREF_RUNCOUNT = "run_count"; //$NON-NLS-1$

    public static final String PREF_NEWS_VERSION = "version"; //$NON-NLS-1$

    public Tools() {
    }
}
