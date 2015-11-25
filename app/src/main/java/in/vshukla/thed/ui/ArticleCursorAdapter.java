package in.vshukla.thed.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import in.vshukla.thed.R;
import in.vshukla.thed.db.DbContract.DbEntry;

/**
 * Created by neha on 25/11/15.
 */
public class ArticleCursorAdapter extends CursorAdapter {

    private static final String TAG = "ArticleCursorAdapter";

    public ArticleCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        Log.d(TAG, "Created adapter");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.article_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTitle, tvDate, tvAuthor, tvKind;
        String title, date, author, kind;

        tvTitle = (TextView) view.findViewById(R.id.tv_list_title);
        tvDate = (TextView) view.findViewById(R.id.tv_list_date);
        tvAuthor = (TextView) view.findViewById(R.id.tv_list_author);
        tvKind = (TextView) view.findViewById(R.id.tv_list_kind);

        title = cursor.getString(cursor.getColumnIndexOrThrow(DbEntry.COL_TITLE));
        date = cursor.getString(cursor.getColumnIndexOrThrow(DbEntry.COL_PDATE));
        author = cursor.getString(cursor.getColumnIndexOrThrow(DbEntry.COL_AUTHOR));
        kind = cursor.getString(cursor.getColumnIndexOrThrow(DbEntry.COL_KIND));

        tvTitle.setText(title);
        tvDate.setText(date);
        tvAuthor.setText(author);
        tvKind.setText(kind);
    }
}
