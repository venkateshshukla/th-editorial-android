package in.vshukla.thed.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import hugo.weaving.DebugLog;
import in.vshukla.thed.R;
import in.vshukla.thed.api.OpinionApiService;
import in.vshukla.thed.messages.ArticleRest;
import in.vshukla.thed.models.Article;
import in.vshukla.thed.utils.AppConstants;
import in.vshukla.thed.utils.AppUtils;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.vshukla.thed.models.Article.COL_KEY;

public class ReaderActivity extends Activity {

    private static final String TAG = "ReaderActivity";

    private TextView tvTitle, tvDate, tvAuthor, tvKind, tvBody;
    private OpinionApiService apiService;
    private Realm realm;

    private Article article;
    private boolean articleInitialized;

    @Override
    @DebugLog
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        ActionBar supportActionBar = getActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        initializeViews();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.e(TAG, "No Bundle received. Exiting.");
            Toast.makeText(this, "Error. Going back.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String key = extras.getString(getString(R.string.extras_key));
        if (key == null || key.isEmpty()) {
            Log.e(TAG, "Received empty/null key. Exiting.");
            Toast.makeText(this, "Error. Going back.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG, "Received key : " + key);

        realm = Realm.getDefaultInstance();

        articleInitialized = false;
        fetchArticleFromDb(key);

        if (!articleInitialized) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.SERVER_BASEURL).addConverterFactory(GsonConverterFactory.create()).build();
            apiService = retrofit.create(OpinionApiService.class);
            getArticleBody(key);
        }

    }

    // If the article is present in the DB, fetch from it.
    @DebugLog
    private void fetchArticleFromDb(final String key) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(TAG, "Trying to fetch article from DB");
                Article article = realm.where(Article.class).equalTo(COL_KEY, key).findFirst();
                if (article == null) {
                    Log.i(TAG, "No article found.");
                    return;
                }
                Log.i(TAG, "Found persisted article.");
                setArticleBody(article);
            }
        });
    }

    // For the given key, the snippet is preserved in the database.
    @DebugLog
    private void persistToDb(final String key, final String snippet) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(TAG, "Saving the snippet for the key : " + key);
                Article articleToUpdate = realm.where(Article.class).equalTo(COL_KEY, key).findFirst();
                articleToUpdate.setBody(snippet);
                realm.copyToRealmOrUpdate(articleToUpdate);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "Successfully saved snippet for key : " + key);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "Failed saving the snippet in DB", error);
            }
        });
    }

    @DebugLog
    private void setArticleBody(ArticleRest articleRest) {
        String date = articleRest.getPrint_date();
        if (articleRest.getPrint_date() == null && article != null) {
            date = article.getDate();
        }
        setArticleBody(articleRest.getAuthor(), articleRest.getSnippet(), AppUtils.getDateDiffString(date), articleRest.getKind(), articleRest.getTitle());
    }

    @DebugLog
    private void setArticleBody(Article article) {
        this.article = article;
        if (article.getBody() != null && !article.getBody().trim().isEmpty()) {
            this.articleInitialized = true;
        }
        setArticleBody(article.getAuthor(), article.getBody(),  AppUtils.getDateDiffString(article.getDate()), article.getKind(), article.getTitle());
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
    private void getArticleBody(final String key) {
        Callback<ArticleRest> callback = new Callback<ArticleRest>() {
            @Override
            public void onResponse(Call<ArticleRest> call, Response<ArticleRest> response) {
                if (response == null) {
                    Log.e(TAG, "Null response to API call");
                    Toast.makeText(getApplicationContext(), "Null response", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                ArticleRest articleRest = response.body();
                if (articleRest == null) {
                    Log.e(TAG, "Response body is null. Returning.");
                    Toast.makeText(getApplicationContext(), "Null response", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                setArticleBody(articleRest);
                persistToDb(key, articleRest.getSnippet());
            }

            @Override
            public void onFailure(Call<ArticleRest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error calling API", Toast.LENGTH_SHORT).show();
                Log.e(TAG, t.getMessage());
                finish();
            }
        };
        Call<ArticleRest> articleCall = apiService.getArticle(key);
        articleCall.enqueue(callback);
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
