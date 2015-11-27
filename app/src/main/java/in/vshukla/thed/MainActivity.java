package in.vshukla.thed;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hugo.weaving.DebugLog;

import in.vshukla.thed.db.DbContract.DbEntry;
import in.vshukla.thed.db.DbHelper;
import in.vshukla.thed.ui.ArticleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String[] PROJECTION = new String[]{
            DbEntry._ID,
            DbEntry.COL_AUTHOR,
            DbEntry.COL_KEY,
            DbEntry.COL_KIND,
            DbEntry.COL_PDATE,
            DbEntry.COL_TITLE
    };
    private static final String SORT_ORDER = DbEntry.COL_TIMESTAMP + " DESC";

    private SQLiteDatabase articleDb;
    private ListView listView;
    private ArticleCursorAdapter articleCursorAdapter;
    private Cursor articleCursor;
    private SharedPreferences prefs;

    static final String PREF_TIMESTAMP = "latest_timestamp";

    @DebugLog
    private void setArticleDb(SQLiteDatabase db) {
        if (db == null) {
            Log.w(TAG, "Setting articleDb to null.");
            return;
        }
        articleDb = db;
        articleCursor = articleDb.query(DbEntry.TABLE_NAME, PROJECTION, null, null, null, null, SORT_ORDER);
        articleCursorAdapter = new ArticleCursorAdapter(this, articleCursor);
        if (articleCursorAdapter == null) {
            Log.e(TAG, "Received a null cursorAdapter.");
            return;
        } else {
            listView.setAdapter(articleCursorAdapter);
            Log.d(TAG, "Set cursorAdapter to listView.");
        }
    }

    @DebugLog
    private void setArticleDb() {
        setArticleDb(articleDb);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_parent);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private static final String TAG = "LV.OnItemClickListener";
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Clicked view at position : " + position);
                ViewGroup vg = (ViewGroup) view;
                String key = null;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    TextView child = (TextView) vg.getChildAt(i);
                    if (child.getId() == R.id.tv_list_key) {
                        key = child.getText().toString();
                    }
                }
                if (key == null) {
                    Log.e(TAG, "Could not find key tag. Abnormal adapter??");
                } else {
                    Log.d(TAG, "Key : " + key);
                    Intent i = new Intent(MainActivity.this, ReaderActivity.class);
                    i.putExtra(getString(R.string.extras_key), key);
                    startActivity(i);
                }
            }
        });
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

    @DebugLog
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

    @DebugLog
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

        @DebugLog
        public FillDatabaseTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @DebugLog
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
                    Log.d(TAG, "Inserted successfully article of Timestamp : " + ts);

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

        @DebugLog
        @Override
        protected void onPostExecute(Long timestamp) {
            super.onPostExecute(timestamp);
            saveAndRefresh(timestamp);
        }
    }

    private class GetDatabaseTask extends AsyncTask<Context, Void, SQLiteDatabase> {
        private static final String TAG = "GetDatabaseTask";

        @DebugLog
        @Override
        protected SQLiteDatabase doInBackground(Context... params) {
            if (params.length != 0) {
                SQLiteOpenHelper sqLiteOpenHelper = new DbHelper(params[0]);
                return sqLiteOpenHelper.getReadableDatabase();
            } else {
                Log.w(TAG, "No contexts passed to the task. Returning null.");
                return null;
            }
        }

        @DebugLog
        @Override
        protected void onPostExecute(SQLiteDatabase sqLiteDatabase) {
            setArticleDb(sqLiteDatabase);
        }
    }
}
