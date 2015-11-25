package in.vshukla.thed;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import in.vshukla.thed.db.DbContract.DbEntry;
import in.vshukla.thed.db.DbHelper;
import in.vshukla.thed.ui.ArticleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String[] PROJECTION = new String[]{
            DbEntry._ID,
            DbEntry.COL_AUTHOR,
            DbEntry.COL_KIND,
            DbEntry.COL_PDATE,
            DbEntry.COL_TITLE
    };
    private static final String SORT_ORDER = DbEntry.COL_TIMESTAMP;

    private SQLiteDatabase articleDb;
    private ListView listView;
    private ArticleCursorAdapter articleCursorAdapter;
    private Cursor articleCursor;

    static final String PREF_TIMESTAMP = "latest_timestamp";

    private void setArticleDb(SQLiteDatabase db) {
        if (db == null) {
            Log.w(TAG, "Setting articleDb to null.");
        } else {
            Log.d(TAG, "articleDb initialised.");
        }
        articleDb = db;
        articleCursor = articleDb.query(DbEntry.TABLE_NAME, PROJECTION, null, null, null, null, SORT_ORDER);
        articleCursorAdapter = new ArticleCursorAdapter(this, articleCursor);
        if (articleCursorAdapter == null) {
            Log.e(TAG, "Received a null cursorAdapter.");
        } else {
            listView.setAdapter(articleCursorAdapter);
            Log.d(TAG, "Set cursorAdapter to listView.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_parent);
        new GetDatabaseTask().execute(this);
    }

    @Override
    protected void onDestroy() {
        if (articleCursor != null) {
            Log.d(TAG, "Closing cursor.");
            articleCursor.close();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Log.d(TAG, "action_settings pressed.");
                break;
            case R.id.action_refresh:
                Log.d(TAG, "action_refresh pressed.");
                getLatestNews();
                break;
            default:
                Log.w(TAG, "Unknown menu item pressed.");
        }

        return super.onOptionsItemSelected(item);
    }

    private void getLatestNews() {
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        long timestamp = prefs.getLong(PREF_TIMESTAMP, 0L);
        Response.ErrorListener apiErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };

        Response.Listener<JSONObject> apiResponseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Got API response : " + response.toString());
            }
        };

        Api api = new Api(this);
        try {
            api.getArticleList(timestamp, apiResponseListener, apiErrorListener);
        } catch (JSONException e) {
            Log.e(TAG, "Error fetching ArticleList.");
            e.printStackTrace();
        } finally {
            Log.d(TAG, "Done with latest news.");
        }
    }

    private class GetDatabaseTask extends AsyncTask<Context, Void, SQLiteDatabase> {
        private static final String TAG = "GetDatabaseTask";

        @Override
        protected SQLiteDatabase doInBackground(Context... params) {
            if (params.length != 0) {
                Log.d(TAG, "Taking the first context passed. Acquiring readable database.");
                SQLiteOpenHelper sqLiteOpenHelper = new DbHelper(params[0]);
                return sqLiteOpenHelper.getReadableDatabase();
            } else {
                Log.w(TAG, "No contexts passed to the task. Returning null.");
                return null;
            }
        }

        @Override
        protected void onPostExecute(SQLiteDatabase sqLiteDatabase) {
            setArticleDb(sqLiteDatabase);
        }
    }
}
