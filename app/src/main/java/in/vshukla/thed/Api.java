package in.vshukla.thed;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by neha on 4/11/15.
 */
public class Api {

    public static final String TAG = "API";
    private static final String BASEURL = "http://example.com" + "/api";

    public ArrayList<Article> getArticleList(long timestamp) {
        Log.d(TAG, "Getting articles after timestamp : " + String.valueOf(timestamp));
        return null;
    }

    public ArrayList<Article> getArticleList() {
        Log.d(TAG, "Getting articles from past 7 days.");
        long tsLast = (System.currentTimeMillis() / 1000) - (7 * 24 * 60 * 60 );
        return this.getArticleList(tsLast);
    }

    public String getArticleText(String key) {
        Log.d(TAG, "Getting articles associated with key : " + key);
        return null;
    }
}
