import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.geometry.Insets;
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

    private static final int N = 11;           // 11x11 octagon cells
    private static final int V = 2 * N - 1;  // 21x21 visual grid (octagons + rhombi)

    // Shape sizing
    private static final double OCT_SIZE = 30; // octagon radius
    private static final double RHO_W = 21;    // rhombus half-width
    private static final double RHO_H = 21;    // rhombus half-height

    private BorderPane root;

    // Turn indicator UI components
    private Polygon turnOctagon;
    private Polygon turnRhombus;
    private Label turnText;
    private Button pieRuleButton;
    Label arrow;

    // store references so later you can render model properly
    private final StackPane[][] octagonNodes = new StackPane[N][N];

    // Helper for column label.
    private static String numToLetter(int i) {
        return String.valueOf((char) ('A' + i));
    }

    public BoardFx(Stage stage, QuaxBoard boardState, ThemeSet theme) {

        this.theme = theme;

        root = new BorderPane();
        root.setId("root");
        root.setBackground(theme.getBackground());

        // TITLE
        Label title = new Label("QUAX (Human V Human)");

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
        turnBox.setMinHeight(50);
        turnBox.setPrefHeight(50);

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


        turnOctagon.setId("turnOctagon");
        turnRhombus.setId("turnRhombus");
        turnText.setId("turnText");

        turnBox.getChildren().addAll(turnOctagon, turnRhombus, arrow, turnText, pieRuleButton);
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

                    cell.getChildren().add(oct);


                    // On click: forward octagonal placement request to QuaxGame, then update UI
                    cell.setOnMouseClicked(e -> {
                        boolean ok = QuaxGame.placeStone(boardState, bx, by);
                        if (!ok) return;

                        Colour owner = boardState.getStone(bx, by);
                        oct.setFill(owner == Colour.BLACK ? Color.BLACK : Color.WHITE);

                        displayTurn(boardState);
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

                    cell.getChildren().add(rh);

                    // Convert visual coordinates to rhombus grid coordinates
                    final int rx = vx / 2;
                    final int ry = vy / 2;

                    // On click: forward rhombus placement request to QuaxGame, update UI
                    cell.setOnMouseClicked(e -> {
                        boolean ok = QuaxGame.placeRhombus(boardState, rx, ry);
                        if (!ok) return;

                        Colour owner = boardState.getRhombus(rx, ry);
                        rh.setFill(owner == Colour.BLACK ? Color.BLACK : Color.WHITE);

                        displayTurn(boardState);
                    });

                    boardGrid.add(cell, col, row);
                }
                // spacer cells (odd-even or even-odd)
            }
        }

        root.setCenter(boardGrid);

        // Initial display for Turn 1;
        displayTurn(boardState);

        Scene scene = new Scene(root, 900, 750);
        stage.setTitle("Quax Game (Player vs. Player)");
        stage.setScene(scene);

        /* Prevent window becoming smaller than the board layout */
        stage.setMinWidth(950);
        stage.setMinHeight(800);

        stage.show();
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
        boolean blackToPlay = (boardState.getTurn() == Turn.Player1); // based on your mapping below
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
        }
    }

        public BorderPane getRoot() {
            return root;
        }

        public void activatePie(QuaxBoard boardState){
                QuaxGame.applyPieRule(boardState);
                pieRuleButton.setVisible(false);
                pieRuleButton.setManaged(false);
        }
 }