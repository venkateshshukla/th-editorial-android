package in.vshukla.thed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import hugo.weaving.DebugLog;

public class ReaderActivity extends AppCompatActivity {

    private static final String TAG = "ReaderActivity";

    private String key;
    private TextView tvTitle, tvDate, tvAuthor, tvKind, tvBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.e(TAG, "No Bundle received. Exiting.");
            Toast.makeText(this, "Error. Going back.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        key = extras.getString(getString(R.string.extras_key));
        if (key == null || key.isEmpty()) {
            Log.e(TAG, "Received empty/null key. Exiting.");
            Toast.makeText(this, "Error. Going back.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG, "Received key : " + key);
        initializeViews();
        getArticleBody();
    }

    @DebugLog
    private void setArticleBody(String author, String body, String date, String kind, String title) {
        tvAuthor.setText(author);
        tvBody.setText(body);
        tvDate.setText(date);
        tvKind.setText(kind);
        tvTitle.setText(title);
    }

    @DebugLog
    private void getArticleBody() {
        final String TAG = "Rdr.ApiListeners";
        Response.ErrorListener apiErrorListener = new Response.ErrorListener() {
            @DebugLog
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                Toast.makeText(ReaderActivity.this, "Error getting Articles.", Toast.LENGTH_SHORT).show();
            }
        };

        Response.Listener<JSONObject> apiResponseListener = new Response.Listener<JSONObject>() {
            @DebugLog
            @Override
            public void onResponse(JSONObject response) {
                if (response == null) {
                    Log.e(TAG, "Null response to API call");
                    return;
                }
                try {
                    String author, body, date, kind, title;
                    author = response.getString("author");
                    body = response.getString("snippet");
                    date = response.getString("date");
                    kind = response.getString("kind");
                    title = response.getString("title");
                    setArticleBody(author, body, date, kind, title);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Error? Abnormal response.");
                    e.printStackTrace();
                }
            }
        };

        Api api = new Api(this);
        try {
            api.getArticleText(key, apiResponseListener, apiErrorListener);
        } catch (JSONException e) {
            Log.e(TAG, "Error fetching ArticleList.");
            e.printStackTrace();
        }
    }

    @DebugLog
    private void initializeViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvAuthor = (TextView) findViewById(R.id.tv_author);
        tvKind = (TextView) findViewById(R.id.tv_kind);
        tvBody = (TextView) findViewById(R.id.tv_article);
    }
}
