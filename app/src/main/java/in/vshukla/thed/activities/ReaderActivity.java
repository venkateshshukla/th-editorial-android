package in.vshukla.thed.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import hugo.weaving.DebugLog;
import in.vshukla.thed.R;
import in.vshukla.thed.models.Article;
import in.vshukla.thed.utils.AppConstants;
import in.vshukla.thed.utils.AppUtils;

public class ReaderActivity extends Activity {

    private static final String TAG = "ReaderActivity";

    private TextView tvTitle, tvDate, tvAuthor, tvKind, tvBody;

    @Override
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

        Article article = (Article) extras.get(AppConstants.EXTRAS_ARTICLE);
        if (article == null) {
            Log.e(TAG, "Received null article. Exiting.");
            Toast.makeText(this, "Error. Going back.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG, "Received article {}" + article);

        setArticleBody(article);
    }

    @DebugLog
    private void setArticleBody(Article article) {
        setArticleBody(article.getAuthor(), article.getSnippet(),  AppUtils.getDateDiffString(article.getPublishedDate()), article.getCategory(), article.getTitle());
    }

    @DebugLog
    private void setArticleBody(String author, String body, String date, String kind, String title) {
        tvAuthor.setText(author);
        tvBody.setText(Html.fromHtml(body));
        tvDate.setText(date);
        tvKind.setText(kind);
        tvTitle.setText(title);
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
