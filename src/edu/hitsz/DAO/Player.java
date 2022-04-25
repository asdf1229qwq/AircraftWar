package edu.hitsz.DAO;

public class Player {
    private String name;
    private int score;
    private String date;

    public Player(String name, int score, String date) {

        this.name = name;
        this.score = score;
        this.date = date;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return this.score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return this.date;
    }
    public void setScore(String date) {
        this.date = date;
    }
}
