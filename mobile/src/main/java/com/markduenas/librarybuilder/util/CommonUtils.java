package com.markduenas.librarybuilder.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * The Class CommonUtils.
 *
 * @author markduenas
 */
public class CommonUtils {

    public static String convertStreamToString(InputStream is) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        is.close();

        return sb.toString();
    }

    public static String formatDate(Context context, String timeToFormat) {

        int flags = 0;
        //flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
        flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;
        return formatDateTime(context, timeToFormat, flags);
    }

    public static String formatDateTime(Context context, String timeToFormat) {

        int flags = 0;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
        flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;
        return formatDateTime(context, timeToFormat, flags);
    }

    public static String formatTime(Context context, String timeToFormat) {

        int flags = 0;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
//        flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
//        flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
//        flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;
        return formatDateTime(context, timeToFormat, flags);
    }

    public static String formatDateTime(Context context, String timeToFormat, int flags) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                long when = date.getTime();
                //finalDateTime = android.text.format.DateUtils.formatDateTime(context, when + TimeZone.getDefault().getOffset(when), flags);
                // since we are not storing in actual UTC, don't worry about the time zone offset
                finalDateTime = android.text.format.DateUtils.formatDateTime(context, when, flags);
            }
        }
        return finalDateTime;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date parseTimeFromString(String date) {

        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm:ss");
        // need convert the above date to milliseconds in long value
        Date newDate = null;
        try {

            newDate = myFormat.parse(date);

        } catch (ParseException e) {

            // always return a date
            Log.e("CommonUtils", e.getMessage() + "");
            newDate = new Date();
        }
        return newDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date parseTimeFromString(String date, String pattern) {

        SimpleDateFormat myFormat = new SimpleDateFormat(pattern);
        // need convert the above date to milliseconds in long value
        Date newDate = null;
        try {

            newDate = myFormat.parse(date);

        } catch (ParseException e) {

            // always return a date
            Log.e("CommonUtils", e.getMessage() + "");
            newDate = new Date();
        }
        return newDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date parseDateFromString(String date) {

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // need convert the above date to milliseconds in long value
        Date newDate = null;
        try {

            newDate = myFormat.parse(date);

        } catch (ParseException e) {

            // always return a date
            Log.e("CommonUtils", e.getMessage() + "");
            newDate = new Date();
        }
        return newDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static String parseDateFromStringToUTC(String date, String pattern) {

        SimpleDateFormat myFormat = new SimpleDateFormat(pattern);
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date newDate = null;
        String utcDate = "";
        try {

            newDate = myFormat.parse(date);
            utcDate = iso8601Format.format(newDate);

        } catch (ParseException e) {

            // always return a date
            Log.e("CommonUtils", e.getMessage() + "");
        }
        return utcDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date parseDateFromString(String date, String pattern) {

//        Format dateFormat = android.text.format.DateFormat.getDateFormat(context);
//        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
        SimpleDateFormat myFormat = new SimpleDateFormat(pattern);
        // need convert the above date to milliseconds in long value
        Date newDate = null;
        try {

            newDate = myFormat.parse(date);

        } catch (ParseException e) {

            // always return a date
            Log.e("CommonUtils", e.getMessage() + "");
            newDate = new Date();
        }
        return newDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date parseDateFromString(String date, String format, String timeZone) {

        SimpleDateFormat myFormat = new SimpleDateFormat(format);
        myFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        // need convert the above date to milliseconds in long value
        Date newDate = null;
        try {

            newDate = myFormat.parse(date);

        } catch (ParseException e) {

            // always return a date
            Log.e("CommonUtils", e.getMessage() + "");
            newDate = new Date();
        }
        return newDate;
    }

    public static void playUriRingtone(Context context, String uriString) {

        if (!uriString.equals("not found")) {
            Log.d("playUriRingtone", uriString);

            try {
                Uri notificationUri = Uri.parse(uriString);
                final Ringtone r = RingtoneManager.getRingtone(context, notificationUri);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            while(true) {
                                if (r != null) {
                                    r.play();
                                }
                                sleep(1000);
                                r.stop();
                                break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();
            } catch (Exception e) {
                Log.d("playUriRingtone", e.getMessage() + "");
            }
        }
    }

    public static String percentEncode(String stringToEncode) {

//        String encodedString = stringToEncode.replace("%", "%26#37;");
//        encodedString = encodedString.replace("&", "%26amp;");
//        encodedString = encodedString.replace("\"", "%26quot;");
//        encodedString = encodedString.replace("'", "%26#39;");
//        encodedString = encodedString.replace(">", "%26gt;");
//        encodedString = encodedString.replace("<", "%26lt;");
        String encodedString;
        if (stringToEncode != null && stringToEncode.length() > 0) {

            encodedString = stringToEncode.replace("%", "%26#37;");
            encodedString = encodedString.replace("&", "%26amp;");
            encodedString = encodedString.replace("\"", "%26quot;");
            encodedString = encodedString.replace("'", "%26#39;");
            encodedString = encodedString.replace(">", "%26gt;");
            encodedString = encodedString.replace("<", "%26lt;");

        } else {

            encodedString = stringToEncode;
        }
        return encodedString;

        // from iOS ScanSeries
//        NSString *retVal = [[[[[[inputString stringByReplacingOccurrencesOfString:@"%" withString:@"%26#37;"]
//        stringByReplacingOccurrencesOfString: @"&" withString: @"%26amp;"]
//        stringByReplacingOccurrencesOfString: @"\"" withString: @"%26quot;"]
//        stringByReplacingOccurrencesOfString: @"'" withString: @"%26#39;"]
//        stringByReplacingOccurrencesOfString: @">" withString: @"%26gt;"]
//        stringByReplacingOccurrencesOfString: @"<" withString: @"%26lt;"];
    }

    public static String percentDecode(String stringToDecode) {

        String decodedString;
        if (stringToDecode != null && stringToDecode.length() > 0) {

            decodedString = stringToDecode.replace("%26#37", "%");
            decodedString = decodedString.replace("%26amp;", "&");
            decodedString = decodedString.replace("&quot;", "\"");
            decodedString = decodedString.replace("&#39;", "'");
            decodedString = decodedString.replace("&gt;", ">");
            decodedString = decodedString.replace("&lt;", "<");

        } else {

            decodedString = stringToDecode;
        }
        return decodedString;
    }

//    public static void fetchCloudToken(Context context, String username, String password, Handler.Callback callback, boolean isDev) {
//
//        FetchCloudTokenAsyncTask fetchCloudTokenAsyncTask = new FetchCloudTokenAsyncTask(context, username, callback);
//        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//        String url = SSConstants.CLOUD_BASE_URL + SSConstants.CLOUD_BACKUP_URL + SSConstants.CLOUD_GET_TOKEN;
//        try {
//            url = String.format(url, encode(username, "UTF-8"), encode(password, "UTF-8"), android_id);
//        } catch (UnsupportedEncodingException e) {
//            Log.e("fetchCloudToken", e.getMessage() + "");
//        }
//        // add the development server flag to the URL
//        if (isDev) {
//            url += "&dev=1";
//        }
//        fetchCloudTokenAsyncTask.execute(url);
//    }
//
//    public static void validateCloudToken(Context context, String userid, String cloudToken, Handler.Callback callback) {
//
//        String checkTokenUrl = SSConstants.CLOUD_BASE_URL + "/brcbackup/token.php?a=validateToken&userID=%s&usertoken=%s";
//        ValidateCloudTokenAsyncTask validateCloudTokenAsyncTask = new ValidateCloudTokenAsyncTask(context, userid, cloudToken, callback);
//        validateCloudTokenAsyncTask.execute(String.format(checkTokenUrl, userid, cloudToken));
//    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isLong(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static long getTimeStamp() {

        //Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        return new Date().getTime() / 1000;
    }

    public static String getUTCTimeStamp() {

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDate = iso8601Format.format(new Date());
        return newDate;
    }

    public static String formatDateTimeToUTC(Context context, Date date) {

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDate = iso8601Format.format(date);
        return newDate;
    }

    public static String formatDate(Context context, Date date) {

        String formattedDate = "";
        //formattedDate = android.text.format.DateFormat.getDateFormat(context).format(date);
        // this doesn't use the system configured date format
        DateFormat df = DateFormat.getDateInstance();
        formattedDate = df.format(date);
        //formattedDate = DateUtils.formatDateTime(context, date.getTime(), DateUtils.FORMAT_SHOW_DATE);
        return formattedDate;
    }

    public static String formatDate(Context context, Date date, String stringFormat) {

        String formattedDate = "";
        SimpleDateFormat myFormat = new SimpleDateFormat(stringFormat);
        formattedDate = myFormat.format(date);
        //formattedDate = DateUtils.formatDateTime(context, date.getTime(), DateUtils.FORMAT_SHOW_DATE);
        return formattedDate;
    }

    public static String formatTime(Context context, Date date) {

        String formattedDate = "";
        formattedDate += android.text.format.DateFormat.getTimeFormat(context).format(date);
        // this doesn't use the system configured time
        // formattedDate = DateUtils.formatDateTime(context, date.getTime(), android.text.format.DateUtils.FORMAT_SHOW_DATE | android.text.format.DateUtils.FORMAT_SHOW_TIME);
        return formattedDate;
    }

    public static String formatDateTime(Context context, Date date) {

        String formattedDate = "";
        formattedDate = android.text.format.DateFormat.getDateFormat(context).format(date);
        formattedDate += " ";
        formattedDate += android.text.format.DateFormat.getTimeFormat(context).format(date);
        // this doesn't use the system configured time
        // formattedDate = DateUtils.formatDateTime(context, date.getTime(), android.text.format.DateUtils.FORMAT_SHOW_DATE | android.text.format.DateUtils.FORMAT_SHOW_TIME);
        return formattedDate;
    }

    public static void hideKeyboard(Activity activity, View v) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideKeyboard(Activity activity) {

        View view = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {

            imm.showSoftInput(activity.findViewById(android.R.id.content), InputMethodManager.SHOW_IMPLICIT);
        }
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showKeyboard(Activity activity, View v) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (v != null) {

            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        } else {

            imm.showSoftInput(activity.findViewById(android.R.id.content), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void toggleKeyboard(Activity activity, View v) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null) {
            return activeInfo.isConnected();
        }
        return false;
    }

    /**
     * Gets the stack trace string.
     *
     * @param e the e
     * @return the stack trace string
     */
    public static String getStackTraceString(Exception e) {
        String stackTrace = "";
        for (StackTraceElement l : e.getStackTrace())
            stackTrace += l.toString();
        return stackTrace;
    }

    /**
     * Make long toast.
     *
     * @param ctx     the ctx
     * @param message the message
     */
    public static void makeLongToast(Context ctx, String message) {

        if (ctx != null && message != null) {

            Toast toast = Toast.makeText(ctx, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 50);
            toast.show();
        }
    }

    public static void makeShortToast(Context ctx, String message) {

        if (ctx != null && message != null) {

            Toast toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 50);
            toast.show();
        }
    }

    /**
     * Given either a Spannable String or a regular String and a token, apply the given CharacterStyle to the span between the tokens, and also remove tokens.
     * <p/>
     * For example, {@code setSpanBetweenTokens("Hello ##world##!", "##",
     *new ForegroundColorSpan(0xFFFF0000));} will return a CharSequence {@code "Hello world!"} with {@code world} in red.
     *
     * @param text  The text, with the tokens, to adjust.
     * @param token The token string; there should be at least two instances of token in text.
     * @param cs    The style to apply to the CharSequence. WARNING: You cannot send the same two instances of this parameter, otherwise the second call will remove the original span.
     * @return A Spannable CharSequence with the new style applied.
     * @see //http://developer.android.com/reference/android/text/style/CharacterStyle.html
     */
    public static CharSequence setSpanBetweenTokens(CharSequence text, String token, CharacterStyle... cs) {
        // Start and end refer to the points where the span will apply
        int tokenLen = token.length();
        int start = text.toString().indexOf(token) + tokenLen;
        int end = text.toString().indexOf(token, start);

        if (start > -1 && end > -1) {
            // Copy the spannable string to a mutable spannable string
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            for (CharacterStyle c : cs)
                ssb.setSpan(c, start, end, 0);

            // Delete the tokens before and after the span
            ssb.delete(end, end + tokenLen);
            ssb.delete(start - tokenLen, start);

            text = ssb;
        }

        return text;
    }

    /**
     * Indicates whether the specified action can be used as an intent. This method queries the package manager for installed packages that can respond to an intent with the specified action. If no suitable package is found, this method
     * returns false.
     *
     * @param context The application's environment.
     * @param action  The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {

//        final PackageManager packageManager = context.getPackageManager();
//        final Intent intent = new Intent(action);
//        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        return (list.size() > 0 && list.get(0).serviceInfo != null);

        final Intent intent = new Intent(action);
        final PackageManager packageManager =  context.getPackageManager();
        List<ResolveInfo> list =packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }

    public static boolean hasSystemSharedLibraryInstalled(Context ctx, String libraryName) {

        boolean hasLibraryInstalled = false;
        if (!TextUtils.isEmpty(libraryName)) {
            String[] installedLibraries = ctx.getPackageManager().getSystemSharedLibraryNames();
            if (installedLibraries != null) {
                for (String s : installedLibraries) {
                    if (libraryName.equals(s)) {
                        hasLibraryInstalled = true;
                        break;
                    }
                }
            }
        }
        return hasLibraryInstalled;
    }

//    public static void startListHelper(Context ctx, String title, ArrayList<String> subList, int requestCode, String currentValue) {
//
//        startListHelper(ctx, title, subList, requestCode, "", 0, currentValue);
//    }

//    public static void startListHelper(Context ctx, String title, ArrayList<String> subList, int requestCode, String tag, int linkId, String currentValue) {
//
//        // fire up the ListHelperActivity
//        Intent startListHelper = new Intent(ctx, ListHelperActivity.class);
//        startListHelper.putStringArrayListExtra(ListHelperActivity.EXTRA_LISTHELPERARRAY, subList);
//        startListHelper.putExtra(ListHelperActivity.EXTRA_LISTHELPERTITLE, title);
//        if (!tag.isEmpty())
//            startListHelper.putExtra(ListHelperActivity.EXTRA_LISTHELPERTAG, tag);
//        if (linkId > 0)
//            startListHelper.putExtra(ListHelperActivity.EXTRA_LISTHELPERLINKID, linkId);
//        if (currentValue != null && !currentValue.isEmpty())
//            startListHelper.putExtra(ListHelperActivity.EXTRA_LISTHELPERSELECTEDVALUE, currentValue);
//        // this is dangerous, must always be an activity
//        ((Activity) ctx).startActivityForResult(startListHelper, requestCode);
//    }

//    public static void startListHelperIndexed(Context ctx, String title, ArrayList<String> subList, int requestCode, String tag, int linkId, String currentValue) {
//
//        // fire up the ListHelperActivity
//        Intent startListHelper = new Intent(ctx, ListHelperWithIndexActivity.class);
//        startListHelper.putStringArrayListExtra(ListHelperWithIndexActivity.EXTRA_LISTHELPERARRAY, subList);
//        startListHelper.putExtra(ListHelperWithIndexActivity.EXTRA_LISTHELPERTITLE, title);
//        if (!tag.isEmpty())
//            startListHelper.putExtra(ListHelperWithIndexActivity.EXTRA_LISTHELPERTAG, tag);
//        if (linkId > 0)
//            startListHelper.putExtra(ListHelperWithIndexActivity.EXTRA_LISTHELPERLINKID, linkId);
//        if (currentValue != null && !currentValue.isEmpty())
//            startListHelper.putExtra(ListHelperActivity.EXTRA_LISTHELPERSELECTEDVALUE, currentValue);
//        // this is dangerous, must always be an activity
//        ((Activity) ctx).startActivityForResult(startListHelper, requestCode);
//    }

    public static byte[] my_short_to_bb_le(short myShort) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(myShort).array();
    }

    public static short my_bb_to_short_le(byte[] byteBarray) {
        return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public static byte[] my_int_to_bb_le(int myInteger) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
    }

    public static int my_bb_to_int_le(byte[] byteBarray) {
        return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static byte[] my_int_to_bb_be(int myInteger) {
        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(myInteger).array();
    }

    public static int my_bb_to_int_be(byte[] byteBarray) {
        return ByteBuffer.wrap(byteBarray).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.

    /**
     * Download url.
     *
     * @param urlString the url string
     * @return the input stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static InputStream downloadUrl(String urlString) throws IOException {

        String USER_AGENT = "Mozilla/5.0";
        InputStream stream = null;
        URL url = new URL(urlString);
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            //add request header
            conn.setRequestProperty("User-Agent", USER_AGENT);
            //conn.setDoInput(true);
            // Starts the query
            conn.connect();
            stream = conn.getInputStream();
        } catch (Exception ex) {
            Log.e("downloadUrl", ex.getMessage() + "");
        }

        return stream;
    }

    public static InputStream downloadUrl(String urlString, String headerPropertyName, String headerPropertyValue) throws IOException {

        String USER_AGENT = "Mozilla/5.0";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setRequestProperty(headerPropertyName, headerPropertyValue);
        //add request header
        conn.setRequestProperty("User-Agent", USER_AGENT);
        //conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

//    public static HttpResponse post(String url, HashMap<String, String> postParameters) {
//
//        try {
//
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(url);
//            SimpleMultipartEntity simpleMultipartEntity = new SimpleMultipartEntity();
//            Iterator<String> it = postParameters.keySet().iterator();
//            String k, v;
//            while (it.hasNext()) {
//
//                k = it.next();
//                v = postParameters.get(k);
//                // add to the multipart entity here
//                simpleMultipartEntity.addPart(k, v);
//            }
//            httppost.setEntity(simpleMultipartEntity);
//            HttpResponse response = httpclient.execute(httppost);
//            return response;
//        } catch (Exception e) {
//            // show error
//            Log.e("CommonUtils.post", e.getMessage() + "");
//        }
//        return null;
//    }

//    public static HttpResponse postWithHeader(String url, HashMap<String, String> postParameters, String headerName, String headerValue) {
//
//        try {
//
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(url);
//            SimpleMultipartEntity simpleMultipartEntity = new SimpleMultipartEntity();
//            Iterator<String> it = postParameters.keySet().iterator();
//            String k, v;
//            while (it.hasNext()) {
//
//                k = it.next();
//                v = postParameters.get(k);
//                // add to the multipart entity here
//                simpleMultipartEntity.addPart(k, v);
//            }
//            httppost.setEntity(simpleMultipartEntity);
//            httppost.addHeader(headerName, headerValue);
//            HttpResponse response = httpclient.execute(httppost);
//            return response;
//        } catch (Exception e) {
//            // show error
//            Log.e("CommonUtils.post", e.getMessage() + "");
//        }
//        return null;
//    }


//    public static HttpResponse postFile(String url, String fileName, File file, HashMap<String, String> postParameters) {
//
//        try {
//
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(url);
//            SimpleMultipartEntity simpleMultipartEntity = new SimpleMultipartEntity();
//            //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            Iterator<String> it = postParameters.keySet().iterator();
//            String k, v;
//            while (it.hasNext()) {
//
//                k = it.next();
//                v = postParameters.get(k);
//                //nameValuePairs.add(new BasicNameValuePair(k, v));
//                // add to the multipart entity here
//                simpleMultipartEntity.addPart(k, v);
//            }
//            // add the file to the multipart entity here
//            simpleMultipartEntity.addPart("userfile", fileName, new FileInputStream(file));
//            httppost.setEntity(simpleMultipartEntity);
//            HttpResponse response = httpclient.execute(httppost);
//            //Do something with response...
//            return response;
//        } catch (Exception e) {
//            // show error
//            Log.e("CommonUtils.postFile", e.getMessage() + "");
//        }
//        return null;
//    }

//    public static void login(Context context) {
//
//        // session expired or something, try logging in again
//        Intent loginIntent = new Intent(context, LoginActivity.class);
//        if (context instanceof Activity) {
//            ((Activity) context).startActivityForResult(loginIntent, SSConstants.REQUEST_CODE_LOGIN);
//        } else {
//            Log.d("CommonUtils", "login not in context of activity...");
//        }
//    }
}
