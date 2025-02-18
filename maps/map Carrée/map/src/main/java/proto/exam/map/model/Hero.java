package proto.exam.map.model;

import java.awt.*;
import java.nio.file.Path;
import java.util.List;

public class Hero {
    private int row;
    private int col;
    private String color;
    private int speed;
    private String classe;
    private String race;

    public Hero() { //methode vide pour la récupération JSON
    }

    public Hero(String classe, String race, int speed) {
        this.classe = classe;
        this.race = race;
        this.speed = speed;
    }

    // Méthode pour dessiner le héros sur la carte (selon sa classe)
    public void draw(Graphics g, int squareSize) {
        int x = col * squareSize;
        int y = row * squareSize;

        g.setColor(Color.decode(color)); // Applique la couleur

        switch (classe) {
            case "guerrier" -> g.fillRect(x, y, squareSize, squareSize);
            case "magicien" -> g.fillOval(x, y, squareSize, squareSize);
            case "archer" -> {
                Polygon p = new Polygon();
                p.addPoint(x, y);
                p.addPoint(x + squareSize, y);
                p.addPoint(x + (squareSize / 2), y + squareSize);
                g.fillPolygon(p);
            }
            case "soigneur" -> {
                Polygon diamond = new Polygon();
                int halfSize = squareSize / 2;

                // Ajouter les points pour créer un losange
                diamond.addPoint(x + halfSize, y); // Haut
                diamond.addPoint(x + squareSize, y + halfSize); // Droite
                diamond.addPoint(x + halfSize, y + squareSize); // Bas
                diamond.addPoint(x, y + halfSize); // Gauche

                // Dessiner le losange
                g.fillPolygon(diamond);
            }
        }
    }

    public void setPath(List<Path> path) {
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

    public int getRow() {
    return row;}

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
