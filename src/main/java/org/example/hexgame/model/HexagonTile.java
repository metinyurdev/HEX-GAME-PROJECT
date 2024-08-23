package org.example.hexgame.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class HexagonTile extends Polygon {
    private Color playerColor = Color.TRANSPARENT;
    private double centerX;
    private double centerY;
    private double radius;
    private boolean isFirstOrLast;

    public HexagonTile(double centerX, double centerY, double size, boolean isFirstOrLast) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = size;
        this.isFirstOrLast = isFirstOrLast;
        double[] points = calculateHexagonPoints(centerX, centerY, size);
        for (int i = 0; i < points.length; i += 2) {
            this.getPoints().addAll(points[i], points[i + 1]);
        }
        if (isFirstOrLast) {
            this.setStrokeWidth(3.0); // Bold border for first and last column tiles
        } else {
            this.setStrokeWidth(1.0); // Regular border width for other tiles
        }
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.BLACK);
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color currentPlayerColor) {
        playerColor = currentPlayerColor;
        this.setFill(playerColor);
    }

    private double[] calculateHexagonPoints(double centerX, double centerY, double size) {
        double[] points = new double[12];
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3 * i;
            points[2 * i] = centerX + size * Math.cos(angle);
            points[2 * i + 1] = centerY + size * Math.sin(angle);
        }
        return points;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getRadius() {
        return radius;
    }

    public boolean isFirstOrLast() {
        return isFirstOrLast;
    }
}
