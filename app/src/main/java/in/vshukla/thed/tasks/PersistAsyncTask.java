package in.vshukla.thed.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import hugo.weaving.DebugLog;
import in.vshukla.thed.messages.ArticleRest;
import in.vshukla.thed.models.Article;
import io.realm.Realm;

/**
 * Task to store the Articles in the DB.
 *
 * Created by venkatesh on 28/11/16.
 */

public class PersistAsyncTask extends AsyncTask<ArticleRest, Void, Void> {

    private static final String TAG = "PersistAsyncTask";

    private Realm realm;
    private Context context;

    public PersistAsyncTask(Context context) {
        this.context = context;
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    @DebugLog
    protected Void doInBackground(final ArticleRest... articleRests) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (ArticleRest rest : articleRests) {
                    Article article = new Article();
                    article.setAuthor(rest.getAuthor());
                    article.setBody(null);
                    article.setDate(rest.getPrint_date());
                    article.setKey(rest.getKey());
                    article.setKind(rest.getKind());
                    article.setTimestamp(rest.getTimestamp());
                    article.setTitle(rest.getTitle());
                    realm.copyToRealmOrUpdate(article);
                }
            }
        });
        String result = String.format("Inserted %d articles in the DB.", articleRests.length);
        Log.i(TAG, result);
        return null;
    }
}
