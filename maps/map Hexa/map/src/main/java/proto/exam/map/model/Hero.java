package proto.exam.map.model;

public class Hero {

    private String classe;
    private String race;
    private int speed;
    private String color;

    // Default constructor (no-argument)
    public Hero() {
    }

    // Constructor with parameters
    public Hero(String classe, String race) {
        this.classe = classe;
        this.race = race;
    }

    // Getter and Setter methods
    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
