package in.vshukla.thed;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
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
    private SharedPreferences prefs;

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

    private void setArticleDb() {
        setArticleDb(articleDb);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_parent);
        new GetDatabaseTask().execute(this);
        prefs = getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
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
                getLatestNews(this);
                break;
            default:
                Log.w(TAG, "Unknown menu item pressed.");
        }

        return super.onOptionsItemSelected(item);
    }

    private void getLatestNews(final Context context) {
        long timestamp = prefs.getLong(PREF_TIMESTAMP, 0L);
        Log.d(TAG, "Latest timestamp available : " + timestamp);

        Response.ErrorListener apiErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                Toast.makeText(context, "Error getting Articles.", Toast.LENGTH_SHORT).show();
            }
        };

        Response.Listener<JSONObject> apiResponseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response == null) {
                    Log.e(TAG, "Null response to API call");
                    return;
                }
                try {
                    int len = response.getInt("num");
                    Toast.makeText(context, "Received " + response.getInt("num") + " articles.", Toast.LENGTH_SHORT).show();
                    long r_ts = response.getLong("r_timestamp");
                    long u_ts = response.getLong("u_timestamp");
                    JSONArray entries = response.getJSONArray("entries");

                    Log.d(TAG, "Received num : " + String.valueOf(len));
                    Log.d(TAG, "Received r_ts : " + String.valueOf(r_ts));
                    Log.d(TAG, "Received u_ts : " + String.valueOf(u_ts));
                    Log.d(TAG, "Received entries : " + entries.toString());
                    new FillDatabaseTask(context).execute(entries);

                } catch (JSONException e) {
                    Log.e(TAG, "Received response seems abnormal.");
                    e.printStackTrace();
                }
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

    private void saveAndRefresh(Long timestamp) {
        if (timestamp == null)
            return;
        Log.d(TAG, "Saving timestamp : " + timestamp);
        SharedPreferences.Editor spEditor = prefs.edit();
        spEditor.putLong(PREF_TIMESTAMP, timestamp);
        spEditor.commit();
        setArticleDb();
    }

    private class FillDatabaseTask extends AsyncTask<JSONArray, Void, Long> {

        private static final String TAG = "FillDatabaseTask";
        private Context context;

        public FillDatabaseTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Long doInBackground(JSONArray... params) {
            if (params == null || params.length == 0) {
                return null;
            }

            JSONArray entries = params[0];
            if (entries.length() == 0) {
                return null;
            }

            SQLiteOpenHelper sqLiteOpenHelper = new DbHelper(context);
            SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

            int count = 0;
            long timestamp = 0L, ts = 0L, ets = 2000000000L;
            JSONObject entry;

            for (int i = 0; i < entries.length(); i++) {
                try {
                    entry = entries.getJSONObject(i);
                    Log.d(TAG, "Inserting into database article : " + entry.getString("title"));
                    ts = entry.getLong(DbEntry.COL_TIMESTAMP);

                    ContentValues values = new ContentValues();
                    values.put(DbEntry.COL_KEY, entry.getString(DbEntry.COL_KEY));
                    values.put(DbEntry.COL_AUTHOR, entry.getString(DbEntry.COL_AUTHOR));
                    values.put(DbEntry.COL_KIND, entry.getString(DbEntry.COL_KIND));
                    values.put(DbEntry.COL_PDATE, entry.getString(DbEntry.COL_PDATE));
                    values.put(DbEntry.COL_TITLE, entry.getString(DbEntry.COL_TITLE));
                    values.put(DbEntry.COL_TIMESTAMP, entry.getString(DbEntry.COL_TIMESTAMP));
                    sqLiteDatabase.insertOrThrow(DbEntry.TABLE_NAME, null, values);
                    Log.d(TAG, "Inserted successfully");
                    Log.d(TAG, "Timestamp of article : " + ts);

                    if (ts > timestamp) {
                        timestamp = ts;
                    }
                    count++;
                } catch (JSONException e) {
                    if (ts < ets) {
                        ets = ts;
                    }
                    Log.e(TAG, "Abnormal article entry.");
                } catch (SQLException e) {
                    if (ts > timestamp) {
                        timestamp = ts;
                    }
                    Log.e(TAG, "Error inserting value");
                }
            }
            Log.d(TAG, "Max Timestamp : " + timestamp);
            Log.d(TAG, "Error timestamp : " + ets);
            Log.d(TAG, "Num of columns inserted = " + String.valueOf(count));
            return (ets < timestamp) ? ets : timestamp;
        }

        @Override
        protected void onPostExecute(Long timestamp) {
            super.onPostExecute(timestamp);
            Log.d(TAG, "Latest timestamp : " + String.valueOf(timestamp));
            saveAndRefresh(timestamp);
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
