package dhbw.pojo.result.search;

public class SearchResultList {
    private String id;
    private String title;
    private String description;
    private String playLink;
    private String imgUrl;

    public SearchResultList(String id, String titel, String description, String playLink, String imgUrl) {
        this.id = id;
        this.title = titel;
        this.description = description;
        this.playLink = playLink;
        this.imgUrl = imgUrl;
    }

    public SearchResultList(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPlayLink() {
        return playLink;
    }

    public void setPlayLink(String playLink) {
        this.playLink = playLink;
    }

    public String getImgUrl(){
        return imgUrl;
    }

    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }
}
