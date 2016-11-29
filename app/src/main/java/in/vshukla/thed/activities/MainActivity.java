package in.vshukla.thed.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import hugo.weaving.DebugLog;
import in.vshukla.thed.R;
import in.vshukla.thed.adapters.ArticleListAdapter;
import in.vshukla.thed.api.OpinionApiService;
import in.vshukla.thed.messages.ArticleListRest;
import in.vshukla.thed.messages.ArticleRest;
import in.vshukla.thed.models.Article;
import in.vshukla.thed.tasks.PersistAsyncTask;
import in.vshukla.thed.utils.AppConstants;
import io.realm.Realm;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.vshukla.thed.models.Article.COL_TIMESTAMP;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final String PREF_TIMESTAMP = "latest_timestamp";

    private RecyclerView recyclerView;
    private SharedPreferences prefs;
    private OpinionApiService apiService;
    private Realm realm;
    private Retrofit retrofit;
    private PersistAsyncTask persistTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_list);

        // Initialize RetroFit APIs to fetch a list of entries.
        retrofit = new Retrofit.Builder().baseUrl(AppConstants.SERVER_BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(OpinionApiService.class);

        // Get realm instance.
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        // Initialise the shared preferences
        prefs = getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);

        // Populate the recycler list view
        recyclerView = (RecyclerView) findViewById(R.id.list_recycle_parent);
        setUpRecyclerView();

        // Get the latest news
        getLatestNews(this);
    }

    @DebugLog
    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ArticleListAdapter(this, realm.where(Article.class).findAllSortedAsync(COL_TIMESTAMP, Sort.DESCENDING), true));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
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

        Callback<ArticleListRest> callback = new Callback<ArticleListRest>() {
            @Override
            public void onResponse(Call<ArticleListRest> call, Response<ArticleListRest> response) {
                if (response == null) {
                    Log.e(TAG, "Null response to API call");
                    return;
                }

                ArticleListRest articleListRest = response.body();
                if (articleListRest == null) {
                    Log.e(TAG, "Null response body");
                    return;
                }
                Log.i(TAG, articleListRest.toString());

                if (articleListRest.getNum() == 0 || articleListRest.getEntries().size() == 0) {
                    Toast.makeText(context, "No new articles.", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Recieved no articles");
                    return;
                }

                Toast.makeText(context, "Received " + articleListRest.getNum() + " articles.", Toast.LENGTH_SHORT).show();
                persistTask = new PersistAsyncTask(getApplicationContext());
                persistTask.execute(articleListRest.getEntries().toArray(new ArticleRest[] {}));

                saveLatestTimestamp(getLatestTimestamp(articleListRest.getEntries()));
            }

            @Override
            public void onFailure(Call<ArticleListRest> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(context, "Error getting Articles.", Toast.LENGTH_SHORT).show();
            }
        };

        Call<ArticleListRest> articleListCall = apiService.getArticleList(timestamp);
        articleListCall.enqueue(callback);
    }

    @DebugLog
    private Long getLatestTimestamp(List<ArticleRest> articleRestList) {
        Long latest = 0L;
        if (articleRestList == null || articleRestList.isEmpty()) {
            return latest;
        }
        for (ArticleRest rest : articleRestList) {
            if (rest.getTimestamp() > latest) {
                latest = rest.getTimestamp();
            }
        }
        return latest;
    }

    @DebugLog
    private void saveLatestTimestamp(Long timestamp) {
        if (timestamp == null)
            return;
        Log.d(TAG, "Saving timestamp : " + timestamp);
        SharedPreferences.Editor spEditor = prefs.edit();
        spEditor.putLong(PREF_TIMESTAMP, timestamp);
        spEditor.apply();
    }
}