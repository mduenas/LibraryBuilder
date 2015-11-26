package com.markduenas.librarybuilder.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;


public class Scanner {

    // MC40 Constants
    // Let's define some intent strings
    // This intent string contains the source of the data as a string
    public static final String SOURCE_TAG = "com.motorolasolutions.emdk.datawedge.source";
    // This intent string contains the barcode symbology as a string
    public static final String LABEL_TYPE_TAG = "com.motorolasolutions.emdk.datawedge.label_type";
    // This intent string contains the barcode data as a byte array list
    public static final String DECODE_DATA_TAG = "com.motorolasolutions.emdk.datawedge.decode_data";

    // This intent string contains the captured data as a string
    // (in the case of MSR this data string contains a concatenation of the track data)
    public static final String DATA_STRING_TAG = "com.motorolasolutions.emdk.datawedge.data_string";

    // Let's define the MSR intent strings (in case we want to use these in the future)
    public static final String MSR_DATA_TAG = "com.motorolasolutions.emdk.datawedge.msr_data";
    public static final String MSR_TRACK1_TAG = "com.motorolasolutions.emdk.datawedge.msr_track1";
    public static final String MSR_TRACK2_TAG = "com.motorolasolutions.emdk.datawedge.msr_track2";
    public static final String MSR_TRACK3_TAG = "com.motorolasolutions.emdk.datawedge.msr_track3";
    public static final String MSR_TRACK1_STATUS_TAG = "com.motorolasolutions.emdk.datawedge.msr_track1_status";
    public static final String MSR_TRACK2_STATUS_TAG = "com.motorolasolutions.emdk.datawedge.msr_track2_status";
    public static final String MSR_TRACK3_STATUS_TAG = "com.motorolasolutions.emdk.datawedge.msr_track3_status";

    // Let's define the API intent strings for the soft scan trigger
    public static final String ACTION_SOFTSCANTRIGGER = "com.motorolasolutions.emdk.datawedge.api.ACTION_SOFTSCANTRIGGER";
    public static final String EXTRA_PARAM = "com.motorolasolutions.emdk.datawedge.api.EXTRA_PARAMETER";
    public static final String DWAPI_START_SCANNING = "START_SCANNING";
    public static final String DWAPI_STOP_SCANNING = "STOP_SCANNING";
    public static final String DWAPI_TOGGLE_SCANNING = "TOGGLE_SCANNING";

    // public static String ourIntentAction = "com.motorolasolutions.emdk.buildingreports.scanseries.RECVR";
    public static String ourIntentAction = "com.motorolasolutions.emdk.buildingreports.scanseries.BROADCAST";
    public static String ourIntentCategory = "android.intent.category.DEFAULT";

    // MC40 Constants End

    public Scanner() {

    }

    public static boolean isScannerApiAvailable() {

        return false;
    }

    public static boolean isCameraScannerAvailable(Context context) {

        return (CommonUtils.isIntentAvailable(context, "com.google.zxing.client.android.SCAN"));
    }

    public static boolean isScannerAvailable(Context context) {

        return CommonUtils.isIntentAvailable(context, "com.motorolasolutions.emdk.buildingreports.scanseries.BROADCAST");
    }

    public static void acquireCameraScan(Activity context) {

        // zxing - Zebra crossing scanner
//		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//		context.startActivityForResult(intent, SSConstants.REQUEST_CODE_SCANNER);
        IntentIntegrator intentIntegrator = new IntentIntegrator(context);
        intentIntegrator.initiateScan();

        // This uses the quickmark sdk to capture a scan which is included in the project
        // com.buildingreports.ScanSeries.scan.qm
        // it returns the same tag as the zxing scanner so swapping in the above code will work just as well
        // Intent intent = new Intent(context, CaptureActivity.class);
        // context.startActivityForResult(intent, SSConstants.REQUEST_CODE_SCANNER);
    }

    public static boolean isMC40() {

        if (Build.MODEL.contains("MC40N0"))
            return true;

        return false;
    }

    public static boolean isTC55() {

        if (Build.MODEL.contains("TC55"))
            return true;

        return false;
    }

    public static boolean isXM5() {

        if (Build.MODEL.contains("XM5"))
            return true;

        return false;
    }

    public static boolean isARCWELDER() {

        if (Build.MODEL.contains("App Runtime for Chrome"))
            return true;

        return false;
    }

    public static boolean supportsDelayScan() {

        // add each scanner here and whether its supported as boolean value
        if (Scanner.isMC40() || Scanner.isTC55() || Scanner.isXM5())
            return true;
        return false;
    }

    public static void vibrateIfPreference(Context context, boolean preference) {
        if (preference) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
        }
    }
}
