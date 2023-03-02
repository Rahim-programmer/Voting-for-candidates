package model;

public class Candidate {
    private Integer id;
    private String name;
    private String photo;
    private Integer votes;

    public Candidate(Integer id, String name, String photo, Integer votes) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.votes = votes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
