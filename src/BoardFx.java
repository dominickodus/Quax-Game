import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
/*The purpose of this new class is to have a no co-existence of UI and Back-end code in QuaxBoard.
* however, this class should be heavily linked to QuaxBoard */

// feel free to delete and do another way if more efficient
public class BoardFx {

    private ThemeSet theme;

    private static final int N = 11;           // 11x11 octagon cells
    private static final int V = 2 * N - 1;  // 21x21 visual grid (octagons + rhombi)

    // Control how big the shapes are
    private static final double OCT_SIZE = 30; // octagon radius
    private static final double RHO_W = 21;    // rhombus half-width
    private static final double RHO_H = 21;    // rhombus half-height

    private BorderPane root;
    private Polygon turnOctagon;
    private Polygon turnRhombus;
    private Label turnText;

    // store references so later you can render model properly
    private final StackPane[][] octagonNodes = new StackPane[N][N];

    // Helper for column label.
    private static String numToLetter(int i) {
        return String.valueOf((char) ('A' + i));
    }

    public BoardFx(Stage stage, QuaxBoard boardState, ThemeSet theme) {

        this.theme = theme;

        root = new BorderPane();
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

        turnOctagon = createOctagon(12);
        turnRhombus = createRhombus(10, 10);

        Label arrow = new Label("→");
        arrow.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        turnText = new Label();

        turnText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        turnOctagon.setId("turnOctagon");
        turnRhombus.setId("turnRhombus");
        turnText.setId("turnText");

        turnBox.getChildren().addAll(turnOctagon, turnRhombus, turnText);
        root.setBottom(turnBox);

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
            boardGrid.add(wrap, 1 + 2 * x, 0); //Place labels at col 1, 3 ,5 etc
        }

        // Left labels 1–11 aligned beside octagons
        for (int y = 0; y < N; y++) {
            Label numLabel = new Label(String.valueOf(y + 1));
            numLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            StackPane wrap = new StackPane(numLabel);
            wrap.setMinSize(35, 24);

            boardGrid.add(wrap, 0, 1 + 2 * y); // Place labels at col 1,3, 5
        }

        // Place octagons and rhombi in a 21x21 visual grid
        for (int vx = 0; vx < V; vx++) {
            for (int vy = 0; vy < V; vy++) {

                int col = vx + 1; // +1 because labels occupy row/col 0
                int row = vy + 1;

                // Octagon cells at even-even coordinates
                if (vx % 2 == 0 && vy % 2 == 0) {
                    int bx = vx / 2;
                    int by = vy / 2;

                    StackPane cell = new StackPane();
                    cell.setMinSize(36, 36);

                    Polygon oct = createOctagon(OCT_SIZE);
                    oct.getStyleClass().add("octagon");

                    cell.getChildren().add(oct);

                    // Sprint 1: click places a "stone"
                    final int fx = bx;
                    final int fy = by;



                    //When a cell (Octagon only for now) is Clicked
                    cell.setOnMouseClicked(e -> {

                        // later: Rhombus compatibility

                        fillOctagon(oct, boardState);
                        displayTurn(boardState); //display current turn each time
                    });


                    octagonNodes[bx][by] = cell;
                    boardGrid.add(cell, col, row);
                }

                // Rhombic connector cells at odd-odd coordinates
                else if (vx % 2 == 1 && vy % 2 == 1) {
                    StackPane cell = new StackPane();
                    cell.setMinSize(24, 24);

                    Polygon rh = createRhombus(RHO_W, RHO_H);
                    rh.setFill(Color.rgb(0, 0, 0, 0.08)); // faint
                    rh.setStroke(Color.rgb(0, 0, 0, 0.35));
                    rh.setStrokeWidth(1);

                    cell.getChildren().add(rh);
                    boardGrid.add(cell, col, row);
                }

                // spacer cells (odd-even or even-odd)
            }
        }

        root.setCenter(boardGrid);


        //initial display for Turn 1;
        displayTurn(boardState);

        Scene scene = new Scene(root, 900, 750);
        stage.setTitle("Quax Game (Player vs. Player)");
        stage.setScene(scene);
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
            return p;
        }

        //Fills octagon with according to Board State
         public void fillOctagon(Polygon oct, QuaxBoard boardState) {
            Turn lastTurn = boardState.getTurn();

             //switches turn after placed, if valid move

             //get current colour
             Colour colour;
             Paint paint = oct.getFill();

             if (paint.equals(Color.WHITE)) {
                 colour = Colour.WHITE;
             }
             else if (paint.equals(Color.BLACK)) {
                 colour = Colour.BLACK;
             }
             else {
                 colour = Colour.NULL;
             }

             boolean canPlace = QuaxGame.placeStone(boardState, colour);

             if(!canPlace){
                 return; //FOR NOW , NO MESSAGE YET
             }

             oct.setFill(lastTurn == Turn.Player1 ? Color.BLACK: Color.WHITE );
        }



    public void displayTurn(QuaxBoard boardState) {
        boolean blackToPlay = (boardState.getTurn() == Turn.Player1); // based on your mapping below
        Color c = blackToPlay ? Color.BLACK : Color.WHITE;

        turnOctagon.setFill(c);
        turnRhombus.setFill(c);
        turnText.setText((blackToPlay ? "BLACK" : "WHITE") + " to play");
    }

        public BorderPane getRoot() {
            return root;
        }
}