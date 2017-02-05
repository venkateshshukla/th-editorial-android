package in.vshukla.thed.models;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import in.vshukla.thed.R;
import in.vshukla.thed.activities.ReaderActivity;
import in.vshukla.thed.utils.AppConstants;

/**
 * Created by venkatesh on 6/2/17.
 */

public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvDate, tvKind, tvTitle, tvAuthor, tvKey;
    public Article article;
    public Context context;

    public ArticleViewHolder(View itemView) {
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
