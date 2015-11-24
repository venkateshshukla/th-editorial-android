package in.vshukla.thed.db;

import android.provider.BaseColumns;

/**
 * Created by neha on 21/11/15.
 */
public final class DbContract {
    public DbContract() {}

    public static abstract class DbEntry implements BaseColumns {
        public static final String TABLE_NAME = "opinions";
        public static final String COL_KEY = "key";
        public static final String COL_AUTHOR = "author";
        public static final String COL_KIND = "kind";
        public static final String COL_TITLE = "title";
        public static final String COL_TIMESTAMP = "timestamp";
        public static final String COL_PDATE = "print_date";
        public static final String COL_TEXT = "text";
    }

}
