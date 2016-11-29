package in.vshukla.thed.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * An article entity
 *
 * Created by venkatesh on 28/11/16.
 */

public class Article extends RealmObject {

    public static final String COL_KEY = "key";
    public static final String COL_AUTHOR = "author";
    public static final String COL_TITLE = "title";
    public static final String COL_BODY = "body";
    public static final String COL_KIND = "kind";
    public static final String COL_DATE = "date";
    public static final String COL_TIMESTAMP = "timestamp";

    @Required
    @PrimaryKey
    private String key;

    @Index
    private String  author;

    @Required
    @Index
    private String title;

    private String body;

    @Required
    private String kind;

    @Required
    private String date;

    private long timestamp;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
