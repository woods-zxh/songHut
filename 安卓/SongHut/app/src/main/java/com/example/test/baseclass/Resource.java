package com.example.test.baseclass;

/**
 * created by 李军邑
 */
public class Resource {
    private String img;
    private String name;
    private String[] creators;
    private String bio;
    private boolean[] possess = new boolean[4];
    private int favorites;
    private int forks;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getCreators() {
        return creators;
    }

    public void setCreators(String[] creators) {
        this.creators = creators;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean[] getPossess() {
        return possess;
    }

    public void setPossess(boolean[] possess) {
        this.possess = possess;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }
}
