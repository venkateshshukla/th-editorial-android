package in.vshukla.thed.utils;

/**
 * Constants to be used across the application.
 *
 * Created by venkatesh on 26/11/16.
 */

public interface AppConstants {
    String SERVER_BASEURL = "http://th-opinion.appspot.com/api/";
    String REC_DATE_FMT_1 = "yyyy-MM-dd HH:mm:ss.S";
    String REC_DATE_FMT_2 = "EEE, d MMM yyyy HH:mm:ss Z";
    long MILLIS_IN_MINUTE = 1000L * 60;
    long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;
    long MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;
    long MILLIS_IN_WEEK = MILLIS_IN_DAY * 7;
    String EMPTY_STR = "";
    String DB_FEED_NEWSFEED = "api/v1/newsfeeds";
    String EXTRAS_ARTICLE = "extras_article";
}