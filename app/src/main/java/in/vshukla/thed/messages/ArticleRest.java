package in.vshukla.thed.messages;

public class ArticleRest {

    private String key;
    private String author;
    private String title;
    private String snippet;
    private String kind;
    private String print_date;
    private Long timestamp;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPrint_date() {
        return print_date;
    }

    public void setPrint_date(String print_date) {
        this.print_date = print_date;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ArticleRest{" +
                "key='" + key + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", snippet='" + snippet + '\'' +
                ", kind='" + kind + '\'' +
                ", print_date='" + print_date + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}


