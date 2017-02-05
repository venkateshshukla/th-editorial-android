package in.vshukla.thed.models;

import java.io.Serializable;

/**
 * An article entity
 *
 * Created by venkatesh on 28/11/16.
 */

public class Article implements Serializable {

    private String id;

    private String author;

    private String category;

    private Long publishedDate;

    private String snippet;

    private String title;

    private String description;

    private Boolean articleUpdated;

    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Long publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getArticleUpdated() {
        return articleUpdated;
    }

    public void setArticleUpdated(Boolean articleUpdated) {
        this.articleUpdated = articleUpdated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
