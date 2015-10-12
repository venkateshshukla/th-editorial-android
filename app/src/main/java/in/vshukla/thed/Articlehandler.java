package in.vshukla.thed;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by neha on 12/10/15.
 */
public class Articlehandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ArticleManager";
    private static final String TABLE_NAME = "articles";
    private static final String KEY_KEY = "key";
    private static final String KEY_AUTHOR = "auhtor";
    private static final String KEY_TITLE = "title";
    public  Articlehandler(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate (SQLiteDatabase db)
    {
        String CREATE_ARTICLES_TABLE = "create table" + TABLE_NAME + "(" + KEY_KEY +
                "primary integer key" + KEY_AUTHOR + "TEXT"+
                KEY_TITLE + "TEXT"+ ")";
        db.execSQL(CREATE_ARTICLES_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int olderVersion,int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(db);
    }
    void addArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KEY, article.getKey());
        values.put(KEY_AUTHOR, article.getAuthor());
        values.put(KEY_TITLE, article.getTitle());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
}
