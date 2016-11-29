package in.vshukla.thed.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.vshukla.thed.R;
import in.vshukla.thed.activities.ReaderActivity;
import in.vshukla.thed.models.Article;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

import static in.vshukla.thed.utils.AppUtils.getDateDiffString;

/**
 * Adapter for the article list.
 *
 * Created by venkatesh on 28/11/16.
 */

public class ArticleListAdapter extends RealmRecyclerViewAdapter<Article, ArticleListAdapter.ArticleViewHolder> {

    private static final String TAG = "ArticleListAdapter";

    private final Context context;

    public ArticleListAdapter(Context context, OrderedRealmCollection<Article> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        this.context = context;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = null;
                ViewGroup vg = (ViewGroup) view;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    TextView child = (TextView) vg.getChildAt(i);
                    if (child.getId() == R.id.tv_list_key) {
                        key = child.getText().toString();
                        break;
                    }
                }
                if (key == null) {
                    Log.e(TAG, "No key found");
                    return;
                }
                Intent intent = new Intent(context, ReaderActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra(context.getString(R.string.extras_key), key);
                context.startActivity(intent, bundle);
            }
        });
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        OrderedRealmCollection<Article> articles = getData();
        if (articles == null || articles.isEmpty()) {
            return;
        }
        Article article = articles.get(position);
        holder.tvAuthor.setText(article.getAuthor());
        holder.tvKind.setText(article.getKind());
        holder.tvKey.setText(article.getKey());
        holder.tvTitle.setText(article.getTitle());
        holder.tvDate.setText(getDateDiffString(article.getDate()));
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvKind, tvTitle, tvAuthor, tvKey;
        ArticleViewHolder(View itemView) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_list_author);
            tvDate = (TextView) itemView.findViewById(R.id.tv_list_date);
            tvKind = (TextView) itemView.findViewById(R.id.tv_list_kind);
            tvKey = (TextView) itemView.findViewById(R.id.tv_list_key);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_list_title);
        }
    }
}
