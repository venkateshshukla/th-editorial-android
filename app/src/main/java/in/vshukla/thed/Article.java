package in.vshukla.thed;



public class Article {
        String key;
        String author;
        String title;
        public Article(String key,String author,String title)
        {
            this.key = key;
            this.author = author;
            this.title = title;
        }
        public String getKey()
        {
            return this.key;
        }


        public String getAuthor()
        {
            return this.author;
        }
        public String getTitle()
        {
            return this.title;
        }
    }


