//package proto.exam.map.model;
//
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
//public class GameMap {
//    private int rows;
//    private int cols;
//    private int squareSize;
//    private double scale;
//    private List<Hero> heroes;
//    private List<Obstacle> obstacles;
//
//    public GameMap(int rows, int cols, int squareSize, double scale) {
//        this.rows = rows;
//        this.cols = cols;
//        this.squareSize = squareSize;
//        this.scale = scale;
//        this.heroes = new ArrayList<>();
//        this.obstacles = new ArrayList<>();
//    }
//
//    public void draw(Graphics g) {
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                drawSquare(g, col * squareSize, row * squareSize, squareSize);
//            }
//        }
//        drawObstacles(g);
//        drawHeroes(g);
//    }
//
//    public void drawSquare(Graphics g, int x, int y, int size) {
//        g.setColor(Color.LIGHT_GRAY);
//        g.fillRect(x, y, size, size);
//        g.setColor(Color.BLACK);
//        g.drawRect(x, y, size, size);
//    }
//
//    public void drawObstacles(Graphics g) {
//        for (Obstacle obstacle : obstacles) {
//            int x = obstacle.getCol() * squareSize;
//            int y = obstacle.getRow() * squareSize;
//            g.setColor(getObstacleColor(obstacle.getType()));
//            drawSquare(g, x, y, squareSize);
//        }
//    }
//
//    public void drawHeroes(Graphics g) {
//        for (Hero hero : heroes) {
//            hero.draw(g, squareSize);
//        }
//    }
//
//    private Color getObstacleColor(String type) {
//        switch (type) {
//            case "water": return Color.BLUE;
//            case "rock": return Color.GRAY;
//            case "forest": return Color.GREEN;
//            case "sand": return Color.YELLOW;
//            default: return Color.BLACK;
//        }
//    }
//
//    public void addHero(Hero hero) {
//        heroes.add(hero);
//    }
//
//    public void addObstacle(Obstacle obstacle) {
//        obstacles.add(obstacle);
//    }
//
//    // Getters et setters...
//}
//
