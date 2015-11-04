package in.vshukla.thed;

import java.util.ArrayList;

/**
 * Created by neha on 4/11/15.
 */
public class Api {

    private static final String BASEURL = "http://example.com" + "/api";

    public ArrayList<Article> getArticleList(long timestamp) {
        return null;
    }

    public ArrayList<Article> getArticleList() {
        long tsLast = (System.currentTimeMillis() / 1000) - (7 * 24 * 60 * 60 );
        return this.getArticleList(tsLast);
    }

    public String getArticleText(String key) {
        return null;
    }
}
