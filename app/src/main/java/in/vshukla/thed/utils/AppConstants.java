package in.vshukla.thed.utils;

/**
 * Constants to be used across the application.
 *
 * Created by venkatesh on 26/11/16.
 */

public interface AppConstants {
    static final String SERVER_BASEURL = "http://example.com/api/";
    static final String REC_DATE_FMT_1 = "yyyy-MM-dd HH:mm:ss.S";
    static final String REC_DATE_FMT_2 = "EEE, d MMM yyyy HH:mm:ss Z";
    static final long MILLIS_IN_MINUTE = 1000L * 60;
    static final long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;
    static final long MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;
    static final long MILLIS_IN_WEEK = MILLIS_IN_DAY * 7;
    static final String EMPTY_STR = "";

}
