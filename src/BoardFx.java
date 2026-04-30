import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;

/**
 * JavaFX view for the Quax board.
 * BoardFx is responsible only for UI:
 *  - Rendering tile grid
 *  - Displaying coordinate labels and current turn indicators
 *  - Forwarding clicks to QuaxGame and updating board.
 *
 *  The actual game state is stored in QuaxBoard.
 * */


public class BoardFx {

    private ThemeSet theme;

    private boolean testingMode;
    private static final int N = 11;           // 11x11 octagon cells
    private static final int V = 2 * N - 1;  // 21x21 visual grid (octagons + rhombi)

    // Shape sizing
    private static final double OCT_SIZE = 30; // octagon radius
    private static final double RHO_W = 21;    // rhombus half-width
    private static final double RHO_H = 21;    // rhombus half-height

    private BorderPane root;
    private BorderPane winOverlay;

    // Win overlay components
    private Label winText;
    private Button restartButton;
    private Runnable onRestart;

    // Turn indicator UI components
    private Polygon turnOctagon;
    private Polygon turnRhombus;
    private Label turnText;
    private Button pieRuleButton;
    private Button showStrategyButton;
    private Button hideStrategyButton;
    private Label strategyLabel;
    private boolean showStrategy = false;
    Label arrow;

    // Bot controller for Player2
    private final BotController bot = new BotController();
    private Turn botTurn;

    // store references so later you can render model properly
    private final StackPane[][] octagonNodes = new StackPane[N][N];
    private final StackPane[][] rhombusNodes = new StackPane[N - 1][N - 1];

    // Helper for column label.
    private static String numToLetter(int i) {
        return String.valueOf((char) ('A' + i));
    }

    public BoardFx(Stage stage, QuaxBoard boardState, ThemeSet theme, Runnable onRestart, Turn forcedBotTurn, boolean testingMode) {

        this.theme = theme;
        this.onRestart = onRestart;
        this.testingMode = testingMode;

        if (forcedBotTurn != null) {
            botTurn = forcedBotTurn;
        } else {
            botTurn = Math.random() < 0.5 ? Turn.Player1 : Turn.Player2;
        }

        if (!testingMode) {
            showStartDialog(boardState);
        }

        root = new BorderPane();
        root.setId("root");
        root.setBackground(theme.getBackground());

        winOverlay = new BorderPane();
        winOverlay.setBackground(new Background(
                new BackgroundFill(Color.rgb(0, 0, 0, 0.7), null, null)
        ));
        winOverlay.setMouseTransparent(true);
        winOverlay.setVisible(false);

        winText = new Label();
        winText.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        winText.setTextFill(Color.WHITESMOKE);
        winOverlay.setCenter(winText);

        restartButton = new Button();
        restartButton.setText("Restart");
        restartButton.setOnAction(e -> {
            System.out.println("restarting");
            if (onRestart != null) onRestart.run();
        });
        restartButton.setDisable(true);

        HBox bottomBox = new HBox(restartButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        winOverlay.setBottom(bottomBox);

        StackPane rootPane = new StackPane(root, winOverlay);

        // TITLE
        Label title = new Label("QUAX (Human V Bot)");

        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        title.setTextFill(theme.getTextColour());

        VBox topSection = new VBox(title);
        topSection.setSpacing(10);
        topSection.setStyle("-fx-alignment: center; -fx-padding: 5 20 5 20;");
        root.setTop(topSection);

        // TURN INDICATOR
        HBox turnBox = new HBox(10);
        turnBox.setAlignment(Pos.CENTER);
        turnBox.setStyle("-fx-padding: 10;");
        turnBox.setMinHeight(90);
        turnBox.setPrefHeight(90);

        turnOctagon = createOctagon(12);
        turnRhombus = createRhombus(10, 10);

        arrow = new Label("→");
        arrow.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        turnText = new Label();
        turnText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        turnText.setMinWidth(140);

        pieRuleButton = new Button("Activate Pie Rule");
        pieRuleButton.setStyle(
                "-fx-background-color: #c6d8ee;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #6b8fb3;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 4 10 4 10;"
        );
        pieRuleButton.setId("pieButton");
        pieRuleButton.setVisible(false);
        pieRuleButton.setManaged(false);
        pieRuleButton.setOnAction(e ->{
                activatePie(boardState);
        });

        showStrategyButton = new Button("Show Strategy");
        showStrategyButton.setId("showStrategyButton");

        hideStrategyButton = new Button("Hide Strategy");
        hideStrategyButton.setId("hideStrategyButton");
        hideStrategyButton.setVisible(false);
        hideStrategyButton.setManaged(false);

        strategyLabel = new Label("No bot move yet.");
        strategyLabel.setId("strategyLabel");
        strategyLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        strategyLabel.setWrapText(true);
        strategyLabel.setMaxWidth(350);
        strategyLabel.setVisible(false);
        strategyLabel.setManaged(false);

        showStrategyButton.setOnAction(e -> {
            showStrategy = true;
            strategyLabel.setVisible(true);
            strategyLabel.setManaged(true);
            showStrategyButton.setVisible(false);
            showStrategyButton.setManaged(false);
            hideStrategyButton.setVisible(true);
            hideStrategyButton.setManaged(true);
            updateStrategyDisplay();
            setScoreVisibility(true);
        });

        hideStrategyButton.setOnAction(e -> {
            showStrategy = false;
            strategyLabel.setVisible(false);
            strategyLabel.setManaged(false);
            hideStrategyButton.setVisible(false);
            hideStrategyButton.setManaged(false);
            showStrategyButton.setVisible(true);
            showStrategyButton.setManaged(true);
            setScoreVisibility(false);
        });

        turnOctagon.setId("turnOctagon");
        turnRhombus.setId("turnRhombus");
        turnText.setId("turnText");

        turnBox.getChildren().addAll(
                turnOctagon,
                turnRhombus,
                arrow,
                turnText,
                pieRuleButton,
                showStrategyButton,
                hideStrategyButton,
                strategyLabel
        );
        root.setBottom(turnBox);
        BorderPane.setAlignment(turnBox, Pos.CENTER);

        // BOARD GRID (VIS+1 because row 0 and col 0 are for labels)
        GridPane boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(0);
        boardGrid.setVgap(0);
        boardGrid.setGridLinesVisible(false); // turn on if debugging

        // Top labels A–K aligned over octagons
        for (int x = 0; x < N; x++) {
            Label letterLabel = new Label(numToLetter(x));
            letterLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            StackPane wrap = new StackPane(letterLabel);
            wrap.setMinSize(24, 35);
            wrap.setStyle("-fx-border-width: 0 0 8 0; -fx-border-style: solid;");
            GridPane.setMargin(wrap, new Insets(0, 0, 16, 0));
            boardGrid.add(wrap, 1 + 2 * x, 0); //Place labels at col 1, 3 ,5 etc
        }

        // Top border for cells without a letter label
        for (int x = 0; x < N - 1; x++) {
            StackPane wrap = new StackPane();
            wrap.setMinSize(24, 35);
            wrap.setStyle("-fx-border-width: 0 0 8 0; -fx-border-style: solid;");
            GridPane.setMargin(wrap, new Insets(0, 0, 16, 0));
            boardGrid.add(wrap, 2 + 2 * x, 0); //Place labels at col 2, 4 ,6 etc
        }

        // Left labels 1–11 aligned beside octagons
        for (int y = 0; y < N; y++) {
            Label numLabel = new Label(String.valueOf(y + 1));
            numLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            StackPane wrap = new StackPane(numLabel);
            wrap.setMinSize(45, 24);
            wrap.setStyle("-fx-border-width: 0 8 0 0; -fx-border-style: solid; -fx-border-color: white;");
            GridPane.setMargin(wrap, new Insets(0, 16, 0, 0));

            boardGrid.add(wrap, 0, 1 + 2 * y); // Place labels at col 1,3, 5
        }

        // Left border for cells without a number label
        for (int y = 0; y < N - 1; y++) {
            StackPane wrap = new StackPane();
            wrap.setMinSize(45, 24);
            wrap.setStyle("-fx-border-width: 0 8 0 0; -fx-border-style: solid; -fx-border-color: white;");
            GridPane.setMargin(wrap, new Insets(0, 16, 0, 0));
            boardGrid.add(wrap, 0, 2 + 2 * y); //Place labels at col 2, 4 ,6 etc
        }

        // Place octagons and rhombi in a 21x21 visual grid
        for (int vx = 0; vx < V; vx++) {
            for (int vy = 0; vy < V; vy++) {

                int col = vx + 1; // +1 because labels occupy row/col 0
                int row = vy + 1;

                // Octagon cells at even-even coordinates
                if (vx % 2 == 0 && vy % 2 == 0) {

                    // Convert visual grid coordinates to board coordinates
                    int bx = vx / 2;
                    int by = vy / 2;

                    StackPane cell = new StackPane();
                    cell.setMinSize(36, 36);

                    Polygon oct = createOctagon(OCT_SIZE);
                    oct.getStyleClass().add("octagon");
                    Label scoreLabel = new Label();
                    scoreLabel.setText("0");
                    scoreLabel.setVisible(false);
                    StackPane pane = new StackPane(oct, scoreLabel);
                    pane.setPickOnBounds(false);
                    scoreLabel.setMouseTransparent(true);
                    cell.getChildren().add(pane);


                    // On click: forward octagonal placement request to QuaxGame, then update UI
                    cell.setOnMouseClicked(e -> {
                        if (boardState.doesWinnerExist()) return;
                        if (boardState.getTurn() == botTurn) return;

                        boolean ok = QuaxGame.placeStone(boardState, bx, by);
                        if (!ok) return;
                        Colour owner = boardState.getStone(bx, by);
                        oct.setFill(owner == Colour.BLACK ? Color.BLACK : Color.WHITE);

                        displayTurn(boardState);

                        if (!boardState.doesWinnerExist() && boardState.getTurn() == botTurn) {

                            // Bot may choose pie rule immediately after the first move
                            if (boardState.getTurnsPassed() == 1) {
                                boolean usePieRule = Math.random() < .5;

                                if (usePieRule) {
                                    activatePie(boardState);
                                    redrawBoard(boardState);
                                    displayTurn(boardState);
                                    updateStrategyDisplay();
                                    updateScores();
                                    return;
                                }
                            }

                            redrawBoard(boardState);
                            botMoveWithDelay(boardState);
                        }
                    });


                    octagonNodes[bx][by] = cell;
                    boardGrid.add(cell, col, row);
                }

                // Rhombic tiles appear between four octagons at odd - odd coordinates
                else if (vx % 2 == 1 && vy % 2 == 1) {
                    StackPane cell = new StackPane();
                    cell.setMinSize(24, 24);

                    Polygon rh = createRhombus(RHO_W, RHO_H);
                    rh.getStyleClass().add("rhombus");
                    rh.setFill(Color.rgb(0, 0, 0, 0.08));
                    rh.setStroke(Color.rgb(0, 0, 0, 0.35));
                    rh.setStrokeWidth(1);
                    rh.setStrokeType(StrokeType.INSIDE);

                    Label scoreLabel = new Label();
                    scoreLabel.setText("");
                    scoreLabel.setVisible(false);
                    scoreLabel.setStyle("-fx-font-size: 9px; -fx-font-weight: bold;");

                    StackPane pane = new StackPane(rh, scoreLabel);
                    pane.setPickOnBounds(false);
                    scoreLabel.setMouseTransparent(true);

                    cell.getChildren().add(pane);

                    // Convert visual coordinates to rhombus grid coordinates
                    final int rx = vx / 2;
                    final int ry = vy / 2;

                    rhombusNodes[rx][ry] = cell;

                    // On click: forward rhombus placement request to QuaxGame, update UI
                    cell.setOnMouseClicked(e -> {
                        if (boardState.doesWinnerExist()) return;
                        if (boardState.getTurn() == botTurn) return;

                        boolean ok = QuaxGame.placeRhombus(boardState, rx, ry);
                        if (!ok) return;

                        Colour owner = boardState.getRhombus(rx, ry);
                        rh.setFill(owner == Colour.BLACK ? Color.BLACK : Color.WHITE);

                        displayTurn(boardState);

                        if (!boardState.doesWinnerExist() && boardState.getTurn() == botTurn) {

                            // Bot may choose pie rule immediately after the first move
                            if (boardState.getTurnsPassed() == 1) {
                                boolean usePieRule = Math.random() < .5; // use 1.0 while testing

                                if (usePieRule) {
                                    activatePie(boardState);
                                    redrawBoard(boardState);
                                    displayTurn(boardState);
                                    updateStrategyDisplay();
                                    updateScores();
                                    return;
                                }
                            }

                            redrawBoard(boardState);
                            botMoveWithDelay(boardState);
                        }

                    });

                    boardGrid.add(cell, col, row);
                }
                // spacer cells (odd-even or even-odd)
            }
        }

        root.setCenter(boardGrid);

        // Initial display for Turn 1;
        displayTurn(boardState);

        Scene scene = new Scene(rootPane, 900, 750);
        stage.setTitle("Quax Game (Player vs. Player)");
        stage.setScene(scene);

        /* Prevent window becoming smaller than the board layout */
        stage.setMinWidth(1000);
        stage.setMinHeight(850);

        stage.show();

        // If it is bot's turn at startup, let bot act
        if (boardState.getTurn() == botTurn) {
            redrawBoard(boardState);
            botMoveWithDelay(boardState);
        }
    }


        private static Polygon createOctagon(double r) {
            Polygon p = new Polygon();
            for (int i = 0; i < 8; i++) {
                double angle = Math.toRadians(22.5 + i * 45.0);
                double x = r * Math.cos(angle);
                double y = r * Math.sin(angle);
                p.getPoints().addAll(x, y);
            }
            p.setFill(Color.rgb(0, 0, 0, 0.03));
            p.setStroke(Color.rgb(0, 0, 0, 0.55));
            p.setStrokeWidth(1.5);
            p.setStrokeType(StrokeType.INSIDE);
            return p;
        }

        private static Polygon createRhombus(double halfW, double halfH) {
            Polygon p = new Polygon(
                    0.0, -halfH,
                    halfW, 0.0,
                    0.0, halfH,
                    -halfW, 0.0
            );
            return p;        }


    // Update turn indicator based on current players turn
        public void displayTurn(QuaxBoard boardState) {
        boolean blackToPlay = (boardState.getTurn() == Turn.Player1);
        Color c = blackToPlay ? Color.BLACK : Color.WHITE;

        if(!boardState.doesWinnerExist()) {
            turnOctagon.setFill(c);
            turnRhombus.setFill(c);
            turnText.setText((blackToPlay ? "BLACK" : "WHITE") + " to play");
            pieRuleButton.setVisible(boardState.getTurnsPassed() == 1);
            pieRuleButton.setManaged(boardState.getTurnsPassed() == 1);
        }
        else{
            turnOctagon.setVisible(false);
            turnRhombus.setVisible(false);
            arrow.setVisible(false);
            turnOctagon.setManaged(false);
            turnRhombus.setManaged(false);
            arrow.setManaged(false);
            turnText.setText((blackToPlay ? "WHITE" : "BLACK") + " wins");
            enableWinOverlay(blackToPlay ? "WHITE" : "BLACK");
        }
    }

    private void enableWinOverlay(String winner) {
        GaussianBlur blur = new GaussianBlur(10);
        root.setEffect(blur);
        winText.setText(winner + " wins!");
        restartButton.setDisable(false);
        winOverlay.setVisible(true);
        winOverlay.setMouseTransparent(false);
        System.out.println(restartButton.isDisable());
    }

    private void redrawBoard(QuaxBoard boardState) {
        // Redraw stones
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {

                StackPane cell = octagonNodes[x][y];
                StackPane pane = (StackPane) cell.getChildren().get(0);
                Polygon oct = (Polygon) pane.getChildren().get(0);

                Colour owner = boardState.getStone(x, y);

                if (owner == Colour.BLACK) {
                    oct.setFill(Color.BLACK);
                } else if (owner == Colour.WHITE) {
                    oct.setFill(Color.WHITE);
                } else {
                    oct.setFill(Color.rgb(0, 0, 0, 0.03));
                }
            }
        }

        // Redraw rhombi
        for (int x = 0; x < N - 1; x++) {
            for (int y = 0; y < N - 1; y++) {

                StackPane cell = rhombusNodes[x][y];
                if (cell == null) continue;

                StackPane pane = (StackPane) cell.getChildren().get(0);
                Polygon rh = (Polygon) pane.getChildren().get(0);

                Colour owner = boardState.getRhombus(x, y);

                if (owner == Colour.BLACK) {
                    rh.setFill(Color.BLACK);
                } else if (owner == Colour.WHITE) {
                    rh.setFill(Color.WHITE);
                } else {
                    rh.setFill(Color.rgb(0, 0, 0, 0.08));
                }
            }
        }
    }

    private void highlightBotMove() {
        // Reset all octagon outlines first
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                StackPane cell = octagonNodes[x][y];
                if (cell == null) continue;

                StackPane pane = (StackPane) cell.getChildren().get(0);
                Polygon oct = (Polygon) pane.getChildren().get(0);
                oct.setStroke(Color.rgb(0, 0, 0, 0.55));
                oct.setStrokeWidth(1.5);
            }
        }

        // Reset all rhombus outlines
        for (int x = 0; x < N - 1; x++) {
            for (int y = 0; y < N - 1; y++) {
                StackPane cell = rhombusNodes[x][y];
                if (cell == null) continue;

                StackPane pane = (StackPane) cell.getChildren().get(0);
                Polygon rhombus = (Polygon) pane.getChildren().get(0);
                rhombus.setStroke(Color.rgb(0, 0, 0, 0.35));
                rhombus.setStrokeWidth(1);
            }
        }

        Move lastMove = bot.getLastMove();
        if (lastMove == null) return;


        if (lastMove.isRhombus()) {
            StackPane cell = rhombusNodes[lastMove.getX()][lastMove.getY()];
            if (cell == null) return;

            StackPane pane = (StackPane) cell.getChildren().get(0);
            Polygon rhombus = (Polygon) pane.getChildren().get(0);
            rhombus.setStroke(Color.LIMEGREEN);
            rhombus.setStrokeWidth(3);
        } else {
            StackPane cell = octagonNodes[lastMove.getX()][lastMove.getY()];
            if (cell == null) return;

            StackPane pane =  (StackPane) cell.getChildren().get(0);
            Polygon oct = (Polygon) pane.getChildren().get(0);
            oct.setStroke(Color.LIMEGREEN);
            oct.setStrokeWidth(3);
        }
    }

    private void botMoveWithDelay(QuaxBoard boardState) {
        if (testingMode) return;
        if (boardState.getTurn() != botTurn || boardState.doesWinnerExist()) return;

        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> {
            bot.makeMove(boardState);
            redrawBoard(boardState);
            highlightBotMove();
            displayTurn(boardState);
            updateStrategyDisplay();
            updateScores();

            // If bot still has turn (after pie rule etc.), keep going
            botMoveWithDelay(boardState);
        });
        pause.play();
    }

    private void setScoreVisibility(boolean visible) {
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                StackPane cell = octagonNodes[x][y];
                if (cell == null) continue;

                StackPane pane = (StackPane) cell.getChildren().get(0);
                Label label = (Label) pane.getChildren().get(1);
                label.setVisible(visible);
            }
        }
        for (int x = 0; x < N - 1; x++) {
            for (int y = 0; y < N - 1; y++) {
                StackPane cell = rhombusNodes[x][y];
                if (cell == null) continue;

                StackPane pane = (StackPane) cell.getChildren().get(0);
                Label label = (Label) pane.getChildren().get(1);
                label.setVisible(visible);
            }
        }
    }

    private void updateScores() {
        // Reset all octagon text first
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                StackPane cell = octagonNodes[x][y];
                if (cell == null) continue;

                StackPane pane = (StackPane) cell.getChildren().get(0);
                Label label = (Label) pane.getChildren().get(1);
                label.setText("");
                label.setTextFill(Color.rgb(0, 0, 0));
            }
        }

        // Reset all rhombus text
        for (int x = 0; x < N - 1; x++) {
            for (int y = 0; y < N - 1; y++) {
                StackPane cell = rhombusNodes[x][y];
                if (cell == null) continue;

                StackPane pane = (StackPane) cell.getChildren().get(0);
                Label label = (Label) pane.getChildren().get(1);
                label.setText("");
                label.setTextFill(Color.rgb(0, 0, 0));
            }
        }

        // Update score text
        HashMap<Move, Integer> scores = bot.getLastScores();
        for (Move move : scores.keySet()) {
            int score = scores.get(move);

            if (move.isRhombus()) {
                StackPane cell = rhombusNodes[move.getX()][move.getY()];
                if (cell == null) continue;

                StackPane pane = (StackPane) cell.getChildren().get(0);
                Label label = (Label) pane.getChildren().get(1);

                // Only show strong rhombus scores
                if (score >= 200) {
                    label.setText(String.valueOf(score));
                } else {
                    label.setText("");
                }

            } else {
                StackPane cell = octagonNodes[move.getX()][move.getY()];
                if (cell == null) continue;

                StackPane pane = (StackPane) cell.getChildren().get(0);
                Label label = (Label) pane.getChildren().get(1);
                label.setText(String.valueOf(score));
            }
        }

        // Make the chosen move's score green
        Move move = bot.getLastMove();
        if (move == null) return;

        if (move.isRhombus()) {
            StackPane cell = rhombusNodes[move.getX()][move.getY()];
            if (cell == null) return;

            StackPane pane = (StackPane) cell.getChildren().get(0);
            Label label = (Label) pane.getChildren().get(1);
            label.setTextFill(Color.LIMEGREEN);
        } else {
            StackPane cell = octagonNodes[move.getX()][move.getY()];
            if (cell == null) return;

            StackPane pane = (StackPane) cell.getChildren().get(0);
            Label label = (Label) pane.getChildren().get(1);
            label.setTextFill(Color.LIMEGREEN);
        }
    }

    private void updateStrategyDisplay() {
        if (!showStrategy) return;

        strategyLabel.setText(
                bot.getLastStrategyUsed() + "\n" + bot.getLastExplanation()
        );
    }

    private void showStartDialog(QuaxBoard boardState) {

        String botColour = (botTurn == Turn.Player1) ? "BLACK" : "WHITE";
        String playerColour = (botTurn == Turn.Player1) ? "WHITE" : "BLACK";

        String firstPlayer = (boardState.getTurn() == botTurn) ? "BOT goes" : "YOU go";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Start");
        alert.setHeaderText("Game Setup");

        alert.setContentText(
                "You are: " + playerColour + "\n" +
                        "Bot is: " + botColour + "\n\n" +
                        firstPlayer + " first"
        );

        alert.showAndWait();
    }

        public BorderPane getRoot() {
            return root;
        }

    public void activatePie(QuaxBoard boardState){

        QuaxGame.applyPieRule(boardState);

        // Show simple message
        if (!testingMode) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pie Rule Activated");
            alert.setHeaderText("Pie Rule Used");
            alert.setContentText("Sides have been swapped.");
            alert.showAndWait();
        }



        if (botTurn == Turn.Player1) {
            botTurn = Turn.Player2;
        } else {
            botTurn = Turn.Player1;
        }

        pieRuleButton.setVisible(false);
        pieRuleButton.setManaged(false);

        redrawBoard(boardState);
        displayTurn(boardState);
        updateStrategyDisplay();
        updateScores();
        botMoveWithDelay(boardState);
    }
 }