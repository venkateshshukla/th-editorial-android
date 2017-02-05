package in.vshukla.thed.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hugo.weaving.DebugLog;
import in.vshukla.thed.R;
import in.vshukla.thed.models.Article;
import in.vshukla.thed.utils.AppConstants;

import static in.vshukla.thed.utils.AppUtils.getDateDiffString;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private SharedPreferences prefs;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Article, ArticleViewHolder> firebaseAdapter;
    private ChildEventListener childEventListener;

    static class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDate, tvKind, tvTitle, tvAuthor, tvKey;
        Article article;
        Context context;

        ArticleViewHolder(View itemView) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_list_author);
            tvDate = (TextView) itemView.findViewById(R.id.tv_list_date);
            tvKind = (TextView) itemView.findViewById(R.id.tv_list_kind);
            tvKey = (TextView) itemView.findViewById(R.id.tv_list_key);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_list_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent readerActivityIntent = new Intent(context, ReaderActivity.class);
            Bundle extras = new Bundle();
            extras.putSerializable(AppConstants.EXTRAS_ARTICLE, article);
            readerActivityIntent.putExtras(extras);
            context.startActivity(readerActivityIntent);
        }
    }

    @Override
    @DebugLog
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_list);

        // Initialise the shared preferences
        prefs = getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);

        // Initialize firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabaseReference = firebaseDatabase.getReference();
        firebaseDatabaseReference.keepSynced(true);

        // Populate the recycler list view
        recyclerView = (RecyclerView) findViewById(R.id.list_recycle_parent);
        setUpRecyclerView();

    }

    @DebugLog
    private void setUpRecyclerView() {
        firebaseAdapter = new FirebaseRecyclerAdapter<Article, ArticleViewHolder>(
                Article.class,
                R.layout.article_list_item,
                ArticleViewHolder.class,
                firebaseDatabaseReference.child(AppConstants.DB_FEED_NEWSFEED)
        ) {
            @DebugLog
            @Override
            protected void populateViewHolder(ArticleViewHolder holder, Article article, int position) {
                holder.tvAuthor.setText(article.getAuthor());
                holder.tvKind.setText(article.getCategory());
                holder.tvKey.setText(article.getId());
                holder.tvTitle.setText(article.getTitle());
                holder.tvDate.setText(getDateDiffString(article.getPublishedDate()));
                holder.article = article;
                holder.context = getApplicationContext();
            }
        };

        firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            @DebugLog
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int articleCount = firebaseAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // Scroll to newly added message
                if (lastVisiblePosition == -1 || (positionStart >= (articleCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setAdapter(firebaseAdapter);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAdapter.cleanup();
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
                break;
            default:
                Log.w(TAG, "Unknown menu item pressed.");
        }
        return super.onOptionsItemSelected(item);
    }
}