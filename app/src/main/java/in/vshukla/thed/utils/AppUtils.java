package in.vshukla.thed.utils;

import android.os.Environment;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static in.vshukla.thed.utils.AppConstants.EMPTY_STR;
import static in.vshukla.thed.utils.AppConstants.MILLIS_IN_DAY;
import static in.vshukla.thed.utils.AppConstants.MILLIS_IN_HOUR;
import static in.vshukla.thed.utils.AppConstants.MILLIS_IN_MINUTE;
import static in.vshukla.thed.utils.AppConstants.MILLIS_IN_WEEK;

/**
 * Utility methods for the App.
 *
 * Created by venkatesh on 29/11/16.
 */

public class AppUtils {

    private static final String TAG = "AppUtils";

    public static final DateFormat[] DATE_FORMAT_ARR = new DateFormat[]{
            new SimpleDateFormat(AppConstants.REC_DATE_FMT_1, Locale.US),
            new SimpleDateFormat(AppConstants.REC_DATE_FMT_2, Locale.US)
    };

    /**
     * Get a pretty string giving information about the time difference between now and the given time string.
     * 5w : Number of weeks past
     * 4d : Number of days past
     * 3h : Number of hours past
     * 2m : Number of minutes past
     *
     * @param dateStr Format [yyyy-MM-dd HH:mm:ss.S]
     * @return
     */
    public static String getDateDiffString(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return EMPTY_STR;
        }
        Date date = null;
        for (DateFormat df : DATE_FORMAT_ARR) {
            date = parseDateStrQuietly(dateStr, df);
            if (date != null) {
                break;
            }
        }

        if (date == null) {
            Log.e(TAG, "No matching format found for date : " + dateStr);
            return EMPTY_STR;
        }

        return getDateDiffString(date);
    }

    public static String getDateDiffString(Long timestamp) {
        Date date = new Date(timestamp);
        return getDateDiffString(date);
    }

    private static String getDateDiffString(Date date) {

        Date now = new Date();
        long diff = now.getTime() - date.getTime();
        if (diff > MILLIS_IN_WEEK) {
            return (diff / MILLIS_IN_WEEK) + "w";
        }
        if (diff > MILLIS_IN_DAY) {
            return (diff / MILLIS_IN_DAY) + "d";
        }
        if (diff > MILLIS_IN_HOUR) {
            return (diff / MILLIS_IN_HOUR) + "h";
        }
        if (diff > MILLIS_IN_MINUTE) {
            return (diff / MILLIS_IN_MINUTE) + "m";
        }
        return EMPTY_STR;
    }


    /**
     * Try to parse the given date string with the given date format.
     * In case the string is not in correct format, return null.
     *
     * @param dateStr
     * @param df
     * @return
     */
    private static Date parseDateStrQuietly(String dateStr, DateFormat df) {
        Date date = null;
        if (df == null || dateStr == null || dateStr.trim().isEmpty()) {
            return date;
        }

        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            Log.w(TAG, "Unable to parse the date string : " + dateStr);
        }
        return date;
    }

    /**
     * Checks if external storage is available for read and write
     *
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available to at least read
     *
     * @return
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
