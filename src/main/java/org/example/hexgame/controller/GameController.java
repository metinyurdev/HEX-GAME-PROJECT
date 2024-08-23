package org.example.hexgame.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.hexgame.model.HexagonTile;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class GameController {
    @FXML
    private Label playerTurnLabel;
    @FXML
    private Button startButton;
    @FXML
    private Pane hexGridPane;

    @FXML
    private RadioButton radio5x5;

    @FXML
    private RadioButton radio11x11;

    @FXML
    private RadioButton radio17x17;

    private Color currentPlayerColor = Color.RED;

    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();
        radio5x5.setToggleGroup(toggleGroup);
        radio11x11.setToggleGroup(toggleGroup);
        radio17x17.setToggleGroup(toggleGroup);
        radio5x5.setSelected(true);
        startButton.setStyle("-fx-background-color: #0000ff");
    }

    public void onStartButtonClicked() {
        currentPlayerColor = Color.RED;
        playerTurnLabel.setText("Player's Turn: Red");
        playerTurnLabel.setTextFill(currentPlayerColor);

        int gridSize = 0;
        double tileSize = 25.0; // Default tile size
        if (radio5x5.isSelected()) {
            gridSize = 5;
        } else if (radio11x11.isSelected()) {
            gridSize = 11;
        } else if (radio17x17.isSelected()) {
            gridSize = 17;
            tileSize = 15.0;
        }

        double tileHeight = tileSize * Math.sqrt(3); // Yükseklik hesaplaması
        double tileWidth = tileSize * 2; // Genişlik hesaplaması

        hexGridPane.getChildren().clear();
        double xOffset = (hexGridPane.getWidth() - (gridSize * tileWidth * 0.75 + tileWidth * 0.25)) / 2;
        double yOffset = (hexGridPane.getHeight() - (gridSize * tileHeight)) / 2;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double x = j * (tileWidth * 0.75) + xOffset;
                double y = i * (tileHeight) + yOffset;
                if (j % 2 == 1) {
                    y += tileHeight / 2;
                }
                boolean isFirstOrLast = (j == 0 || j == gridSize - 1);
                HexagonTile hexagonTile = new HexagonTile(x, y, tileSize, isFirstOrLast);
                hexagonTile.setOnMouseClicked(event -> handleTileClick(hexagonTile));
                hexGridPane.getChildren().add(hexagonTile);
            }
        }
    }

    private void handleTileClick(HexagonTile tile) {
        if (tile.getPlayerColor() == Color.TRANSPARENT) {
            tile.setPlayerColor(currentPlayerColor);
            updateNeighborColors(tile, currentPlayerColor);
            int pathLength = checkForWin(currentPlayerColor);
            if (pathLength > 0) {
                String winner = currentPlayerColor == Color.RED ? "Red Player" : "Blue Player";
                showWinnerAlert(winner, pathLength);
            } else {
                currentPlayerColor = (currentPlayerColor == Color.RED) ? Color.BLUE : Color.RED;
                updatePlayerTurnLabel();
            }
        }
    }

    private void updatePlayerTurnLabel() {
        playerTurnLabel.setText("Player's Turn: " + (currentPlayerColor == Color.RED ? "Red" : "Blue"));
        playerTurnLabel.setTextFill(currentPlayerColor);
    }

    private void updateNeighborColors(HexagonTile tile, Color color) {
        for (Node node : hexGridPane.getChildren()) {
            if (node instanceof HexagonTile) {
                HexagonTile neighbor = (HexagonTile) node;
                if (!tile.equals(neighbor) && areNeighbors(tile, neighbor)) {
                    neighbor.setStroke(color);
                }
            }
        }
    }

    private boolean areNeighbors(HexagonTile tile1, HexagonTile tile2) {
        double xDiff = Math.abs(tile1.getCenterX() - tile2.getCenterX());
        double yDiff = Math.abs(tile1.getCenterY() - tile2.getCenterY());
        return xDiff < tile1.getRadius() * 1.75 && yDiff < tile1.getRadius() * 1.75;
    }

    private int checkForWin(Color playerColor) {
        Set<HexagonTile> visited = new HashSet<>();
        Stack<HexagonTile> stack = new Stack<>();
        int pathLength = 0;

        // Soldaki (ilk) sütundaki tüm karoları başlangıç noktası olarak ekle
        for (Node node : hexGridPane.getChildren()) {
            if (node instanceof HexagonTile) {
                HexagonTile tile = (HexagonTile) node;
                if (tile.isFirstOrLast() && tile.getPlayerColor().equals(playerColor) && tile.getCenterX() < hexGridPane.getWidth() / 2) {
                    stack.push(tile);
                }
            }
        }

        // DFS ile yol arama
        while (!stack.isEmpty()) {
            HexagonTile currentTile = stack.pop();
            if (visited.contains(currentTile)) {
                continue;
            }
            visited.add(currentTile);
            pathLength++;

            // Sağdaki (son) sütuna ulaşılmışsa kazanan mevcut oyuncudur
            if (currentTile.isFirstOrLast() && currentTile.getPlayerColor().equals(playerColor) && currentTile.getCenterX() > hexGridPane.getWidth() / 2) {
                return pathLength;
            }

            for (Node node : hexGridPane.getChildren()) {
                if (node instanceof HexagonTile) {
                    HexagonTile neighbor = (HexagonTile) node;
                    if (!visited.contains(neighbor) && areNeighbors(currentTile, neighbor) && neighbor.getPlayerColor().equals(playerColor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }

        return 0;
    }

    private void showWinnerAlert(String winner, int pathLength) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Winner: " + winner + "\nPath Length: " + pathLength);
        alert.showAndWait();
    }
}
