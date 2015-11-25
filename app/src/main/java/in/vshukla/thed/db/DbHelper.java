package in.vshukla.thed.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import in.vshukla.thed.db.DbContract.DbEntry;

/**
 * Created by neha on 21/11/15.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Opinions.db";

    private static final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY," +
            "%s STRING UNIQUE," +
            "%s STRING," +
            "%s STRING," +
            "%s STRING," +
            "%s STRING," +
            "%s STRING," +
            "%s STRING)",
            DbEntry.TABLE_NAME,
            DbEntry._ID,
            DbEntry.COL_KEY,
            DbEntry.COL_AUTHOR,
            DbEntry.COL_KIND,
            DbEntry.COL_PDATE,
            DbEntry.COL_TEXT,
            DbEntry.COL_TIMESTAMP,
            DbEntry.COL_TITLE);

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + DbEntry.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
