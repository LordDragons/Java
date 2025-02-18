//package proto.exam.map.model;
//
//import java.nio.file.Path;
//import java.util.*;
//
//public class AStar {
//    private final int rows;
//    private final int cols;
//    private final List<Obstacle> obstacles;
//    private List<Hero> heroes;
//
//    public AStar(int rows, int cols, List<Obstacle> obstacles, List<Hero> heroes) {
//        this.rows = rows;
//        this.cols = cols;
//        this.obstacles = obstacles;
//        this.heroes = heroes;
//    }
//
//    public List<Path> calculatePath(Hero hero, int targetRow, int targetCol) {
//        // Open list and closed list for A* algorithm
//        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
//        Set<Node> closedList = new HashSet<>();
//
//        // Initialize the start and target nodes
//        Node startNode = new Node(hero.getRow(), hero.getCol(), null);
//        Node targetNode = new Node(targetRow, targetCol, null);
//
//        openList.add(startNode);
//
//        while (!openList.isEmpty()) {
//            Node current = openList.poll();
//
//            if (current.equals(targetNode)) {
//                // Reconstruct path
//                List<Path> path = new ArrayList<>();
//                while (current != null) {
//                    path.add(new Path(current.row, current.col));
//                    current = current.parent;
//                }
//                Collections.reverse(path);
//                return path;  // Path found
//            }
//
//            closedList.add(current);
//
//            // Evaluate neighbors (4 directions: up, down, left, right)
//            for (int[] dir : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
//                int newRow = current.row + dir[0];
//                int newCol = current.col + dir[1];
//
//                if (isValid(newRow, newCol) && !isObstacle(newRow, newCol)) {
//                    Node neighbor = new Node(newRow, newCol, current);
//
//                    if (closedList.contains(neighbor)) {
//                        continue;
//                    }
//
//                    int gCost = current.g + 1; // Assuming uniform cost (each move is 1)
//                    int hCost = manhattanDistance(newRow, newCol, targetRow, targetCol);
//                    neighbor.g = gCost;
//                    neighbor.h = hCost;
//                    neighbor.f = gCost + hCost;
//
//                    if (!openList.contains(neighbor)) {
//                        openList.add(neighbor);
//                    }
//                }
//            }
//        }
//
//        return new ArrayList<>();  // No path found
//    }
//
//    private boolean isValid(int row, int col) {
//        return row >= 0 && row < rows && col >= 0 && col < cols;
//    }
//
//    private boolean isObstacle(int row, int col) {
//        for (Obstacle obstacle : obstacles) {
//            if (obstacle.getRow() == row && obstacle.getCol() == col) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private int manhattanDistance(int row1, int col1, int row2, int col2) {
//        return Math.abs(row1 - row2) + Math.abs(col1 - col2);
//    }
//
//    // Helper classes for Node and Path
//    private static class Node {
//        int row, col;
//        int g, h, f;
//        Node parent;
//
//        Node(int row, int col, Node parent) {
//            this.row = row;
//            this.col = col;
//            this.parent = parent;
//            this.g = Integer.MAX_VALUE;
//            this.h = 0;
//            this.f = Integer.MAX_VALUE;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (this == obj) return true;
//            if (obj == null || getClass() != obj.getClass()) return false;
//            Node node = (Node) obj;
//            return row == node.row && col == node.col;
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(row, col);
//        }
//    }
//
//    public static class Path {
//        int row, col;
//
//        Path(int row, int col) {
//            this.row = row;
//            this.col = col;
//        }
//    }
//}
//
