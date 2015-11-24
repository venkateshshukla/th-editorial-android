package in.vshukla.thed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import in.vshukla.thed.db.DbHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SQLiteDatabase articleDb;

    private void setArticleDb(SQLiteDatabase db) {
        if (db == null) {
            Log.w(TAG, "Setting articleDb to null.");
        } else {
            Log.d(TAG, "articleDb initialised.");
        }
        this.articleDb = db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetDatabaseTask().execute(this);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetDatabaseTask extends AsyncTask <Context, Void, SQLiteDatabase> {
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
