package hw6;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;

public class TicTacToe extends Application {
	private Label displayText = new Label("Player X's turn");
	private Button reset = new Button();
	private boolean timeToPlay = true;
	private boolean Xturn = true;
	private Tile[][] board = new Tile[3][3];
	private List<Combo> executions = new ArrayList<>();
	private Pane mainPane = new Pane();

	private Parent createContent() {
		mainPane.setPrefSize(800, 700);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Tile tile = new Tile();
				tile.setTranslateX(j * 200 + 50);
				tile.setTranslateY(i * 200 + 50);

				mainPane.getChildren().add(tile);

				board[j][i] = tile;
			}
		}
			displayText.setFont(new Font("Arial", 30));
			displayText.setTranslateY(0);
			mainPane.getChildren().add(displayText);
			
			reset.setTranslateX(670);
			reset.setTranslateY(300);
			reset.setText("Reset");
			reset.setVisible(false);
			
			reset.setOnAction(value-> {
			displayText.setText("Player X's turn");
				timeToPlay = true;
				Xturn = true;
				board = new Tile[3][3];
				executions = new ArrayList<>();
					
				mainPane.getChildren().clear();
				createContent();
				
			});
			
		  mainPane.getChildren().add(reset);

		// horizontal
		for (int y = 0; y < 3; y++) {
			executions.add(new Combo(board[0][y], board[1][y], board[2][y]));
		}

		// vertical
		for (int x = 0; x < 3; x++) {
			executions.add(new Combo(board[x][0], board[x][1], board[x][2]));
		}

		// diagonals
		executions.add(new Combo(board[0][0], board[1][1], board[2][2]));
		executions.add(new Combo(board[2][0], board[1][1], board[0][2]));

		return mainPane;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
	}

	private void checkState() {
		int numBox = 0;
		boolean done = false;
		
		for (Combo combo : executions) {
			if (combo.xWins() || combo.oWins()) {
				timeToPlay = false;
				winAnimation(combo);
				done = true;
				break;
			}
		}
		
		for(Tile[] tile : board) {
			for(Tile j : tile) {
				if(!j.text.equals("")) {
					numBox++;
				}
			}
		}
		
		if (numBox == 9 && !done) {
			timeToPlay = false;
			done = true;
			displayText.setText("The game is a draw");
		}
		
		if (done) {
			reset.setVisible(true);
		}
	}

	private void winAnimation(Combo combo) {
		if(combo.xWins()) {
			displayText.setText("Player X has won");
		}
		else if(combo.oWins()) {
			displayText.setText("Player O has won");
		}
		
		Line line = new Line();
		line.setStartX(combo.tiles[0].getCenterX());
		line.setStartY(combo.tiles[0].getCenterY());
		line.setEndX(combo.tiles[0].getCenterX());
		line.setEndY(combo.tiles[0].getCenterY());

		mainPane.getChildren().add(line);

		Timeline timeline = new Timeline();
		timeline.getKeyFrames()
				.add(new KeyFrame(Duration.seconds(1), new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
						new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
		timeline.play();
		
	}

	private class Combo {
		private Tile[] tiles;

		public Combo(Tile... tiles) {
			this.tiles = tiles;
		}

		public boolean xWins() {
			if (tiles[0].getValue().isEmpty())
				return false;

			return tiles[0].getValue().equals(tiles[1].getValue()) && tiles[0].getValue().equals(tiles[2].getValue()) &&  tiles[0].getValue().equals("X");
		}
		
		public boolean oWins() {
			if (tiles[0].getValue().isEmpty())
				return false;

			return tiles[0].getValue().equals(tiles[1].getValue()) && tiles[0].getValue().equals(tiles[2].getValue()) &&  tiles[0].getValue().equals("O");
		}
	}

	private class Tile extends StackPane {
		private String text = "";
		private Image o = new Image("o.jpg");
		private Image x = new Image("x.jpg");
		

		public Tile() {
			Rectangle border = new Rectangle(200, 200);
			border.setFill(null);
			border.setStroke(Color.BLACK);

			setAlignment(Pos.CENTER);

			getChildren().addAll(border);
			

			setOnMouseClicked(event -> {
				if (!timeToPlay) {
					return;
				}

				if (Xturn && border.getFill() == null) {
					displayText.setText("Player O's turn");
					border.setFill(new ImagePattern(x));
					Xturn = false;
					drawX();
				}
				
				else if (border.getFill() == null) {
					displayText.setText("Player X's turn");
					border.setFill(new ImagePattern(o));
					Xturn = true;
					drawO();
				}

				checkState();
			});
		}

		public double getCenterX() {
			return getTranslateX() + 100;
		}

		public double getCenterY() {
			return getTranslateY() + 100;
		}

		public String getValue() {
			return text;
		}

		private void drawX() {
			text = ("X");
		}

		private void drawO() {
			text = ("O");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}