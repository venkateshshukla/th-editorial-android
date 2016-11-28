package in.vshukla.thed.api;

import in.vshukla.thed.messages.ArticleRest;
import in.vshukla.thed.messages.ArticleListRest;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Created by venkatesh on 24/11/16.
 */

public interface OpinionApiService {

    @GET("list")
    Call<ArticleListRest> getArticleList(@Query("timestamp") long timestamp);

    @GET("news")
    Call<ArticleRest> getArticle(@Query("id") String id);

}
