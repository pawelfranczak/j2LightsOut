package pl.pfranczak.j2LightsOut;

import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {

	// TRUE = WHITE
	// FALSE = BLACK

	static int TAB_SIZE;
	static int EDGE_LENGHT = 60;

	boolean[][] valuesTab;
	Button[][] buttonsTab;
	TextField movedTextField;

	int moveCounter = 0;

	final Background white = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
	final Background black = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
	Random rd = new Random();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("j2LightsOut");

		askForLevel();

		initGlobalVariables(primaryStage);
		initCounter();
		initStartGame(primaryStage);

		primaryStage.setScene(new Scene(initGridPane(), TAB_SIZE * EDGE_LENGHT, TAB_SIZE * EDGE_LENGHT + EDGE_LENGHT));
		primaryStage.show();
	}

	private void askForLevel() {
		ButtonType easy = new ButtonType("Easy (3x3)");
		ButtonType medium = new ButtonType("Medium (5x5)");
		ButtonType hard = new ButtonType("Hard(8x8)");
		ButtonType very = new ButtonType("Very Hard(12x12)");

		Alert alert = new Alert(AlertType.NONE, "SELECT LEVEL: ", easy, medium, hard, very);
		alert.showAndWait();

		if (alert.getResult() == easy) {
			TAB_SIZE = 3;
		} else if (alert.getResult() == medium) {
			TAB_SIZE = 5;
		} else if (alert.getResult() == hard) {
			TAB_SIZE = 8;
		} else if (alert.getResult() == very) {
			TAB_SIZE = 12;
		}
	}

	private void initGlobalVariables(Stage primaryStage) {
		buttonsTab = new Button[TAB_SIZE][TAB_SIZE];
		valuesTab = new boolean[TAB_SIZE][TAB_SIZE];
		movedTextField = new TextField();
		movedTextField.setMinSize(EDGE_LENGHT * TAB_SIZE, EDGE_LENGHT);

		createButtons(primaryStage);
	}

	private GridPane initGridPane() {
		GridPane gridPane = new GridPane();

		for (int i = 0; i < TAB_SIZE; i++) {
			for (int j = 0; j < TAB_SIZE; j++) {
				gridPane.add(buttonsTab[i][j], i, j);
			}
		}

		gridPane.add(movedTextField, 0, TAB_SIZE + 1, TAB_SIZE, 1);

		return gridPane;
	}

	private void initValuseTab() {
		for (int i = 0; i < TAB_SIZE; i++) {
			for (int j = 0; j < TAB_SIZE; j++) {
				valuesTab[i][j] = rd.nextBoolean();
			}
		}
	}

	private void colorButtons() {
		for (int i = 0; i < TAB_SIZE; i++) {
			for (int j = 0; j < TAB_SIZE; j++) {
				setColor(valuesTab[i][j], i, j);
			}
		}
	}

	private void createButtons(Stage primaryStage) {

		BorderStroke borderStroke = new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				new BorderWidths(1, 1, 1, 0));
		Border border = new Border(borderStroke);

		for (int i = 0; i < TAB_SIZE; i++) {
			for (int j = 0; j < TAB_SIZE; j++) {

				buttonsTab[i][j] = new Button(i + " " + j);
				buttonsTab[i][j].setMaxSize(EDGE_LENGHT, EDGE_LENGHT);
				buttonsTab[i][j].setMinSize(EDGE_LENGHT, EDGE_LENGHT);
				buttonsTab[i][j].setBorder(border);

				final int x = i;
				final int y = j;

				buttonsTab[i][j].setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						handleButtonAction(event, x, y, primaryStage);
					}
				});
			}
		}
	}

	private void initStartGame(Stage primaryStage) {
		initValuseTab();
		colorButtons();
	}

	private void initCounter() {
		moveCounter = 0;
		movedTextField.setText("Moved: " + moveCounter + " times");
	}

	private void handleButtonAction(ActionEvent event, int x, int y, Stage primaryStage) {
		setColorAndValue(!valuesTab[x][y], x, y);
		changeAbove(x, y);
		changeBelow(x, y);
		changeLeft(x, y);
		changeRight(x, y);

		System.out.println("Moved " + ++moveCounter + " times");
		movedTextField.setText("Moved: " + moveCounter + " times");

		if (win()) {

			Alert popup = new Alert(AlertType.CONFIRMATION);
			popup.setTitle("YOU WIN");
			popup.setHeaderText("You WIN in " + moveCounter + " moves");
			popup.setOnCloseRequest(new EventHandler<DialogEvent>() {
				@Override
				public void handle(DialogEvent t) {
					initStartGame(primaryStage);
					initCounter();
				}
			});
			popup.show();
		}
	}

	private void setColor(boolean state, int x, int y) {
		if (state) {
			buttonsTab[x][y].setBackground(white);
		} else {
			buttonsTab[x][y].setBackground(black);
		}
	}

	private void setColorAndValue(boolean state, int x, int y) {
		valuesTab[x][y] = state;
		setColor(state, x, y);
	}

	private void changeAbove(int x, int y) {
		if (y > 0)
			setColorAndValue(!valuesTab[x][y - 1], x, y - 1);
	}

	private void changeBelow(int x, int y) {
		if (y < TAB_SIZE - 1)
			setColorAndValue(!valuesTab[x][y + 1], x, y + 1);
	}

	private void changeLeft(int x, int y) {
		if (x > 0)
			setColorAndValue(!valuesTab[x - 1][y], x - 1, y);
	}

	private void changeRight(int x, int y) {
		if (x < TAB_SIZE - 1)
			setColorAndValue(!valuesTab[x + 1][y], x + 1, y);
	}

	private boolean win() {
		boolean gameWin = true;
		for (int i = 0; i < TAB_SIZE; i++) {
			for (int j = 0; j < TAB_SIZE; j++) {
				if (valuesTab[i][j]) {
					gameWin = false;
				}
			}
		}
		return gameWin;
	}

}
