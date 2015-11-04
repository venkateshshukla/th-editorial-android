package in.vshukla.thed;

public class Article {

    private String key;
    private String author;
    private String title;
    private String body;
    private String kind;
    private String date;

    public Article(String key, String author, String title, String body, String kind, String date) {
        this.key = key;
        this.author = author;
        this.title = title;
        this.body = body;
        this.kind = kind;
        this.date = date;
    }

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


