package in.vshukla.thed.messages;

import java.util.List;

/**
 * Response to a list API call.
 *
 * Created by venkatesh on 24/11/16.
 */
public class ArticleListRest {

    private long r_timestamp;
    private long u_timestamp;
    private int num;
    private List<ArticleRest> entries;


    public long getR_timestamp() {
        return r_timestamp;
    }

    public void setR_timestamp(long r_timestamp) {
        this.r_timestamp = r_timestamp;
    }

    public long getU_timestamp() {
        return u_timestamp;
    }

    public void setU_timestamp(long u_timestamp) {
        this.u_timestamp = u_timestamp;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<ArticleRest> getEntries() {
        return entries;
    }

    public void setEntries(List<ArticleRest> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "ArticleListRest{" +
                "r_timestamp=" + r_timestamp +
                ", u_timestamp=" + u_timestamp +
                ", num=" + num +
                ", entries=" + entries +
                '}';
    }
}
