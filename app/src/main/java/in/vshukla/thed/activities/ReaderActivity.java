package in.vshukla.thed.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import hugo.weaving.DebugLog;
import in.vshukla.thed.utils.AppConfigs;
import in.vshukla.thed.utils.AppConstants;
import in.vshukla.thed.R;
import in.vshukla.thed.api.OpinionApiService;
import in.vshukla.thed.messages.ArticleRest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReaderActivity extends AppCompatActivity {

    private static final String TAG = "ReaderActivity";

    private String key;
    private TextView tvTitle, tvDate, tvAuthor, tvKind, tvBody;
    private OpinionApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
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

        String baseUrl = AppConfigs.getProperty(AppConstants.SERVER_BASEURL, this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(OpinionApiService.class);

        initializeViews();
        getArticleBody(key);
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
    private void getArticleBody(String key) {
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
                setArticleBody(articleRest.getAuthor(), articleRest.getBody(), articleRest.getPrint_date(), articleRest.getKind(), articleRest.getTitle());
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
