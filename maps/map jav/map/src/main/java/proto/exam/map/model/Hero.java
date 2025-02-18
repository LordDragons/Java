package proto.exam.map.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hero {
    private String id;
    private int row;
    private int col;
    private String color;
    private int speed;

    @JsonProperty("race")
    private String race;

    @JsonProperty("type")
    private String type;


    public Hero() {
    }


    // Constructeur avec tous les paramètres nécessaires
    public Hero(String id, int row, int col, String color, int speed, String race, String type) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.color = color;
        this.speed = speed;
        this.race = race;
        this.type = type;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // Méthode pour déplacer le héros
    public void move(int targetRow, int targetCol) {
        this.row = targetRow;
        this.col = targetCol;
    }
}
