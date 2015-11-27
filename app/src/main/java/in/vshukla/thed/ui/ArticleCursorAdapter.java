package in.vshukla.thed.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import hugo.weaving.DebugLog;
import in.vshukla.thed.R;
import in.vshukla.thed.db.DbContract.DbEntry;

/**
 * Created by neha on 25/11/15.
 */
public class ArticleCursorAdapter extends CursorAdapter {

    @DebugLog
    public ArticleCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.article_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTitle, tvDate, tvAuthor, tvKind, tvKey;
        String title, date, author, kind, key;

        tvTitle = (TextView) view.findViewById(R.id.tv_list_title);
        tvDate = (TextView) view.findViewById(R.id.tv_list_date);
        tvAuthor = (TextView) view.findViewById(R.id.tv_list_author);
        tvKind = (TextView) view.findViewById(R.id.tv_list_kind);
        tvKey = (TextView) view.findViewById(R.id.tv_list_key);

        title = cursor.getString(cursor.getColumnIndexOrThrow(DbEntry.COL_TITLE));
        date = cursor.getString(cursor.getColumnIndexOrThrow(DbEntry.COL_PDATE));
        author = cursor.getString(cursor.getColumnIndexOrThrow(DbEntry.COL_AUTHOR));
        kind = cursor.getString(cursor.getColumnIndexOrThrow(DbEntry.COL_KIND));
        key = cursor.getString(cursor.getColumnIndexOrThrow(DbEntry.COL_KEY));

        tvTitle.setText(title);
        tvDate.setText(date);
        tvAuthor.setText(author);
        tvKind.setText(kind);
        tvKey.setText(key);
    }
}
