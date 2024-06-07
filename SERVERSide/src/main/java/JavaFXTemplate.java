
import java.util.HashMap;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JavaFXTemplate extends Application{
	TextField s1,s2,s3,s4, c1;
	Button serverChoice,clientChoice,b1;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;

	int portNum;

	ListView<String> listItems;

	Stage primaryStage;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Game Server");
		primaryStage.setScene(end());
		primaryStage.setHeight(800);
		primaryStage.setWidth(1300);
		primaryStage.show();
	}

	public Scene end() {
		// card icon
		ImageView jack = new ImageView("jack_of_hearts2.png");
		jack.setPreserveRatio(true);
		jack.setFitHeight(90);
		jack.setRotate(25);
		jack.setTranslateX(10);
		jack.setTranslateY(20);
		ImageView jack2 = new ImageView("jack_of_hearts2.png");
		jack2.setPreserveRatio(true);
		jack2.setFitHeight(90);
		jack2.setRotate(-25);
		jack2.setTranslateX(-30);
		jack2.setTranslateY(-65);
		VBox cardIcon = new VBox(jack, jack2);
		cardIcon.setAlignment(Pos.TOP_CENTER);

		// all clients dropped message
		Text noPlayers = new Text("No players are\nconnected to the\nserver.");
		noPlayers.setStyle("-fx-font: bold 60pt \"Book Antiqua\"; -fx-fill: #856b05;");
		noPlayers.setTextAlignment(TextAlignment.CENTER);
		noPlayers.setTranslateY(-40);
		VBox title = new VBox();
		title.getChildren().addAll(cardIcon, noPlayers);
		title.setAlignment(Pos.TOP_CENTER);
		title.setTranslateY(30);

		//tell user to put in port numbers
		Text portMessage = new Text("CHOOSE A VALID PORT NUMBER FROM 1 to 65535");
		portMessage.setStyle("-fx-font: normal 25pt \"Tahoma\"; -fx-fill: white;");
		portMessage.setTranslateX(230);
		portMessage.setTranslateY(5);
		portMessage.setTextAlignment(TextAlignment.CENTER);
		TextField inputPort = new TextField();
		inputPort.setStyle("-fx-font: normal 12pt \"Tahoma\"; -fx-fill: white;");
		inputPort.setPrefWidth(100);
		inputPort.setMaxWidth(100);
		inputPort.setAlignment(Pos.CENTER);
		inputPort.setTranslateX(560);
		inputPort.setTranslateY(20);
		VBox portPrompt = new VBox();
		portPrompt.getChildren().addAll(portMessage, inputPort);
		portPrompt.setTranslateY(10);
		portPrompt.setTranslateX(40);

		// start server button
		Text start = new Text("  Server On");
		start.setStyle("-fx-font: normal 25pt \"Tahoma\"; -fx-fill: white;");
		Button serverOn = new Button();
		HBox onS = new HBox();
		onS.getChildren().addAll(serverOn, start);
		serverOn.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z'");
		serverOn.setOnAction(e->{
			boolean portCorrect = true;
			try{
				portNum = Integer.parseInt(inputPort.getText());
			}
			catch (NumberFormatException nfe) {
				inputPort.setText("Invalid port number: "+ inputPort);
				portCorrect = false;

			}
			if(1 > portNum || portNum > 65535){
				inputPort.setText("Invalid port number: "+ inputPort);
				portCorrect = false;
			}
			if(portCorrect) {
				primaryStage.setScene(gameInfo());

				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						if(((PokerInfo) data).left){
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " left. Total players " + ((PokerInfo) data).totalPlayers);
						}
						if(((PokerInfo) data).justJoined){
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " joined. Total players " + ((PokerInfo) data).totalPlayers);
						}
						if(((PokerInfo) data).win){
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " won their game");
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " earned a total of $"+ ((PokerInfo) data).amountWon + " for their game");
						}
						if(((PokerInfo) data).lose){
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " lost their game");
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " earned a total of $"+ ((PokerInfo) data).amountWon + " for their game");

						}
						if(((PokerInfo) data).draw){
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " tied their game");
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " earned a total of $"+ ((PokerInfo) data).amountWon + " for their game");

						}
						if(((PokerInfo) data).badDeal){
							listItems.getItems().add("For Player "+ ((PokerInfo) data).playerNum + " dealer did not have queen high or better");

						}
						if(((PokerInfo) data).dealSend){
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " made an ante bet of "+ ((PokerInfo) data).anteBet);
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " made an pair plus bet of "+ ((PokerInfo) data).PairPlusBet);

						}
						if(((PokerInfo) data).play){
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " made a play bet of "+ ((PokerInfo) data).anteBet);

						}
						if(((PokerInfo) data).fold){
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " decided to fold this hand");

						}
						if(((PokerInfo) data).playAgain){
							listItems.getItems().add("Player "+ ((PokerInfo) data).playerNum + " decided to play another hand ");

						}
					});
				},portNum);
				serverConnection.onServer();
			}
		});

		// off server button
		Text off = new Text("  Server Off");
		off.setStyle("-fx-font: normal 25pt \"Tahoma\"; -fx-fill: white;");
		Button serverOff = new Button();
		HBox offS = new HBox();
		offS.getChildren().addAll(serverOff, off);
		serverOff.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z'");
		serverOff.setOnAction(e-> {
			serverConnection.closeServer();
		});

		// add both to a new hbox
		HBox onOff = new HBox();
		onOff.getChildren().addAll(onS, new Text("            "), offS);
		onOff.setAlignment(Pos.BOTTOM_CENTER);
		onOff.setTranslateY(-60);

		BorderPane root = new BorderPane();
		root.setTop(title);
		root.setCenter(portPrompt);
		root.setBottom(onOff);
		root.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #083401, #041c00);");
		// return endScene
		return new Scene(root, 1300, 730);
	}

	public Scene gameInfo(){

		listItems = new ListView<String>();
		listItems.setStyle("-fx-control-inner-background: #2e6202;");

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #083401, #041c00);");
		pane.setCenter(listItems);

		Text title = new Text("Current Game Info");
		title.setStyle("-fx-font: normal 25pt \"Tahoma\"; -fx-fill: #856b05;");
		title.setTextAlignment(TextAlignment.CENTER);

		// off server button
		Text off = new Text("  Server Off");
		off.setStyle("-fx-font: normal 10pt \"Tahoma\"; -fx-fill: white;");
		Button serverOff = new Button();
		HBox offS = new HBox();
		offS.getChildren().addAll(serverOff, off);
		serverOff.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z'");
		serverOff.setOnAction(e-> {
			serverConnection.closeServer();
		});
		offS.setAlignment(Pos.CENTER);

		Text start = new Text("  Server On");
		start.setStyle("-fx-font: normal 10pt \"Tahoma\"; -fx-fill: white;");
		Button serverOn = new Button();
		HBox onS = new HBox();
		onS.getChildren().addAll(serverOn, start);
		serverOn.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z'");
		serverOn.setOnAction(e->{
			serverConnection.onServer();
		});
		onS.setAlignment(Pos.CENTER);

		HBox onOrOff = new HBox(onS, offS);
		onOrOff.setAlignment(Pos.CENTER);
		onOrOff.setSpacing(40);

		pane.setBottom(onOrOff);
		pane.setTop(title);
		return new Scene(pane, 500, 400);
	}


}
