
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;

// CLIENT
public class JavaFXTemplate extends Application {
	int plusBet = 0;
	int anteBet = 0;
	int winningAmount = 0;
	ListView<String> gameMessages;
	Client clientConnection;
	ArrayList<ImageView> animateCards = new ArrayList<>(6);
	ArrayList<TranslateTransition> transitions = new ArrayList<>(6);
	String buttonStyle = "-fx-padding: 8 15 15 15;\n" +
			"    -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;\n" +
			"    -fx-background-radius: 8;\n" +
			"    -fx-background-color: \n" +
			"        linear-gradient(from 0% 93% to 0% 100%, #911010 0%, #851010 100%),\n" +
			"        #8a1e1e,\n" +
			"        #ce3737,\n" +
			"        radial-gradient(center 50% 50%, radius 100%, #b00202, #b92828);\n" +
			"    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );\n";


	EventHandler<ActionEvent> chooseAnte;
	EventHandler<ActionEvent> chooseFold;
	EventHandler<ActionEvent> choosePairPlus;
	EventHandler<ActionEvent> choosePlay;
	EventHandler<ActionEvent> playGame;
	EventHandler<ActionEvent> foldGame;
	EventHandler<ActionEvent> exitGame;
	EventHandler<ActionEvent> menuFreshStart;
	EventHandler<ActionEvent> menuNewLook;
	ArrayList<Button> anteButtons;
	ArrayList<Button> pairplusButtons;
	PokerInfo testing = new PokerInfo();
	HBox playerHand;
	HBox dealerHand;
	HBox playAndFold;
	VBox gameResults;
	VBox badDeal;
	VBox maxMessage;
	String originalBG = "-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #083401, #041c00);";
	String newBG = "-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #dbc13b, #736312);";
	String currBG = originalBG;
	String goldTitle = "-fx-font: bold 60pt \"Book Antiqua\"; -fx-fill: #856b05;";
	String greenTitle = "-fx-font: bold 60pt \"Book Antiqua\"; -fx-fill: #032605;";
	String goldSubtitle = "-fx-font: bold 40pt \"Book Antiqua\"; -fx-fill: #856b05;";
	String greenSubtitle = "-fx-font: bold 40pt \"Book Antiqua\"; -fx-fill: #032605;";
	String currTitle = goldTitle;
	String currSubtitle = goldSubtitle;
	Text allBets;
	Text allWinnings;
	Text condtionText;
	Text amountText;
	Text badDealText2;
	Text thanks = new Text("THANK YOU FOR\nPLAYING 3-CARD\nPOKER");
	Text playerTitle = new Text("PLAYER DECK");
	Text dealerTitle = new Text("DEALER DECK");
	Text wagerTitle = new Text("PLACE WAGERS");
	TextField port;
	TextField IPaddress;
	BorderPane gpRoot;
	BorderPane welcomeRoot;
	BorderPane foldRoot = new BorderPane();
	boolean newLook = false;
	boolean dealSend = false;

//	boolean redoFlag = false;
	Scene fold = new Scene(foldRoot);
	Stage primaryStage;

	public Scene welcome() {
		gameMessages = new ListView<>();
		// play button
		Button play = new Button("\uD83C\uDCB1 Play");
		play.setStyle("-fx-font: normal 35pt \"Tahoma\"; -fx-text-fill: #856b05; -fx-border-color: transparent; -fx-background-color: transparent;");
		play.setTextAlignment(TextAlignment.LEFT);
		play.setOnMouseEntered(e->play.setStyle("-fx-font: normal 35pt \"Tahoma\"; -fx-text-fill: #efeeeb; -fx-border-color: transparent; -fx-background-color: transparent;"));
		play.setOnMouseExited(e->play.setStyle("-fx-font: normal 35pt \"Tahoma\"; -fx-text-fill: #856b05; -fx-border-color: transparent; -fx-background-color: transparent;"));
		play.setOnAction(lambda->{
			String IPNum = IPaddress.getText();
			int portNum = 0;
			boolean correctInput = true;
			testing.dealSend = false;
			try{
				portNum = Integer.parseInt(port.getText());
			}
			catch (NumberFormatException nfe) {
				port.setText("Invalid port number");
				correctInput = false;
			}
			if(1 > portNum || portNum > 65535){
				port.setText("Incorrect port number: "+ portNum);
				correctInput = false;
			}
			if(!IPNum.equals("127.0.0.1") ){
				IPaddress.setText("Incorrect IP address: " + IPNum);
				correctInput = false;
			}
			if(correctInput){
				testing.justJoined = true;
				testing.dealSend = false;

				clientConnection = new Client(
				data2 -> { Platform.runLater(() -> {
					if(((PokerInfo) data2).closeScreen){
						Platform.exit();
					}
					if(((PokerInfo) data2).newLook){
						if(((PokerInfo) data2).newLookFlip){
							currBG = originalBG;
							currTitle = goldTitle;
							currSubtitle = goldSubtitle;

							testing.newLookFlip = false;
						}
						else{
							currBG = newBG;
							currTitle = greenTitle;
							currSubtitle = greenSubtitle;
							testing.newLookFlip = true;
						}
						gpRoot.setStyle(currBG);
						welcomeRoot.setStyle(currBG);
						foldRoot.setStyle(currBG);
						thanks.setStyle(currTitle);
						playerTitle.setStyle(currSubtitle);
						dealerTitle.setStyle(currSubtitle);
						wagerTitle.setStyle(currSubtitle);
					}
					else
					{
						testing.winningsTotal = ((PokerInfo) data2).winningsTotal;
						if(((PokerInfo) data2).ppWins < 0) {
							gameMessages.getItems().add("Player " + ((PokerInfo) data2).playerNum + " loses Pair Plus");
						}
						if(((PokerInfo) data2).ppWins > 0) {
							gameMessages.getItems().add("Player " + ((PokerInfo) data2).playerNum + " wins Pair Plus");
						}

						if(((PokerInfo) data2).maxPlayers){
							PauseTransition pauses = new PauseTransition(Duration.seconds(4));
							pauses.setOnFinished(e -> Platform.exit());
							pauses.play();
							maxMessage.setVisible(true);
						}
						if(!((PokerInfo) data2).dealSend && !((PokerInfo) data2).maxOnce && !((PokerInfo) data2).playAgain && !((PokerInfo) data2).justJoined){
							PauseTransition paused = new PauseTransition(Duration.seconds(6));
							allBets.setText("Ante Bet: $"+((PokerInfo) data2).anteBet+" | Pair Plus Bet: $"+((PokerInfo) data2).PairPlusBet+" | Play Bet: $"+((PokerInfo) data2).anteBet);
							if(((PokerInfo) data2).win){
								gameMessages.getItems().add("Player " + ((PokerInfo) data2).playerNum + " beats dealer");
								paused.setOnFinished(e -> primaryStage.setScene(end(true, false, false,((PokerInfo) data2).anteWins, ((PokerInfo) data2).playWins, ((PokerInfo) data2).ppWins, ((PokerInfo) data2).amountWon)));

							}
							if(((PokerInfo) data2).lose && !((PokerInfo) data2).fold){
								gameMessages.getItems().add("Player " + ((PokerInfo) data2).playerNum + " loses to dealer");
								paused.setOnFinished(e -> primaryStage.setScene(end(false, true, false,((PokerInfo) data2).anteWins, ((PokerInfo) data2).playWins, ((PokerInfo) data2).ppWins, ((PokerInfo) data2).amountWon)));

							}
							if(((PokerInfo) data2).fold){
								gameMessages.getItems().add("Player " + ((PokerInfo) data2).playerNum + " loses to dealer");
								primaryStage.setScene(end(false, true, false,((PokerInfo) data2).anteWins, ((PokerInfo) data2).playWins, ((PokerInfo) data2).ppWins, ((PokerInfo) data2).amountWon));
							}
							if(((PokerInfo) data2).draw){
								gameMessages.getItems().add("Player " + ((PokerInfo) data2).playerNum + " ties with dealer");
								paused.setOnFinished(e -> primaryStage.setScene(end(false, false, true,((PokerInfo) data2).anteWins, ((PokerInfo) data2).playWins, ((PokerInfo) data2).ppWins, ((PokerInfo) data2).amountWon)));

							}
							if(((PokerInfo) data2).badDeal && !((PokerInfo) data2).fold){
								gameMessages.getItems().add("For Player " + ((PokerInfo) data2).playerNum + ", Dealer does not have at least Queen high; ante wager is pushed”");
								badDealText2.setText("Returning Play wager and Pushing Ante wager to next hand\n" +
											"Your earnings for your pair plus bet: " + ((PokerInfo) data2).amountWon + "\n" +
											"In Total: " + ((PokerInfo) data2).amountWon);

								PauseTransition pause = new PauseTransition(Duration.seconds(3));
								pause.setOnFinished(e -> badDeal.setVisible(true));
								pause.play();
								PauseTransition pause2 = new PauseTransition(Duration.seconds(5.5));
								pause2.setOnFinished(e -> primaryStage.setScene(gamePlay(((PokerInfo) data2).anteBet)));
								pause2.play();

							}
							else{
								paused.play();
							}
							PauseTransition pauseWin = new PauseTransition(Duration.seconds(4));
							pauseWin.setOnFinished(e -> allWinnings.setText("Total Winnings: $"+((PokerInfo) data2).winningsTotal));
							pauseWin.play();

						}
						if(((PokerInfo) data2).dealSend) {
							allBets.setText("Ante Bet: $"+((PokerInfo) data2).anteBet+" | Pair Plus Bet: $"+((PokerInfo) data2).PairPlusBet+" | Play Bet: $"+ 0);

							ImageView Card4 = new ImageView(((PokerInfo) data2).playerHand.get(0).imgLink);
							Card4.setPreserveRatio(true);
							Card4.setFitHeight(115);

							ImageView Card5 = new ImageView(((PokerInfo) data2).playerHand.get(1).imgLink);
							Card5.setPreserveRatio(true);
							Card5.setFitHeight(115);

							ImageView card6 = new ImageView(((PokerInfo) data2).playerHand.get(2).imgLink);
							card6.setPreserveRatio(true);
							card6.setFitHeight(115);

							playerHand.getChildren().removeAll();
							playerHand.getChildren().clear();
							playerHand.getChildren().addAll(Card4, Card5, card6);

							ImageView Card7 = new ImageView(((PokerInfo) data2).dealerHand.get(0).imgLink);
							Card7.setPreserveRatio(true);
							Card7.setFitHeight(115);

							ImageView Card8 = new ImageView(((PokerInfo) data2).dealerHand.get(1).imgLink);
							Card8.setPreserveRatio(true);
							Card8.setFitHeight(115);

							ImageView card9 = new ImageView(((PokerInfo) data2).dealerHand.get(2).imgLink);
							card9.setPreserveRatio(true);
							card9.setFitHeight(115);

							dealerHand.getChildren().removeAll();
							dealerHand.getChildren().clear();
							dealerHand.getChildren().addAll(Card7, Card8, card9);

//							//set pause transition
//							PauseTransition currPause2 = new PauseTransition(Duration.seconds(5.5));
//							currPause2.setOnFinished(e -> dealerHand.setVisible(true));
//							currPause2.play();

							//set pause transition
							PauseTransition currPause = new PauseTransition(Duration.seconds(5.5));
							currPause.setOnFinished(e -> playerHand.setVisible(true));
							currPause.play();

							testing.dealerHand.clear();
							testing.dealerHand.addAll(((PokerInfo) data2).dealerHand);
							testing.playerHand.clear();
							testing.playerHand.addAll(((PokerInfo) data2).playerHand);
						}
				}});
				}
		,portNum, IPNum);

			clientConnection.start();
			primaryStage.setScene(gamePlay(0));
			testing.win = false;
			testing.lose = false;
			testing.draw = false;
			testing.closeScreen = false;
			testing.dealSend = false;
			testing.badDeal = false;
			testing.maxOnce = false;
			testing.maxPlayers = false;
			testing.newLook = false;
			testing.fold = false;

			clientConnection.send(testing);
		}});


;
		// welcome message
		Text welcome = new Text(" WELCOME\n TO 3-CARD\n POKER");
		welcome.setStyle("-fx-font: bold 80pt \"Book Antiqua\"; -fx-fill: #856b05;");
		welcome.setTextAlignment(TextAlignment.LEFT);

		// user input port number
		port = new TextField();
		port.setPromptText("Enter port number here.");
		port.setPrefWidth(380);
		port.setMaxWidth(380);
		port.setStyle("-fx-font: normal 12pt \"Tahoma\"; -fx-text-fill: white; -fx-border-color: #856b05; -fx-background-color: transparent;");
		port.setTranslateX(30);

		//user input ip address
		IPaddress = new TextField();
		IPaddress.setPromptText("Enter IP address here.");
		IPaddress.setPrefWidth(380);
		IPaddress.setMaxWidth(380);
		IPaddress.setStyle("-fx-font: normal 12pt \"Tahoma\"; -fx-text-fill: white; -fx-border-color: #856b05; -fx-background-color: transparent;");
		IPaddress.setTranslateX(30);
		IPaddress.setTranslateY(10);

		// add to vbox
		VBox title = new VBox();
		title.getChildren().addAll(welcome, play, port, IPaddress);
		title.setAlignment(Pos.CENTER_LEFT);

		// dice image
		ImageView dice = new ImageView("dice.png");
		dice.setPreserveRatio(true);
		dice.setFitHeight(230);
		dice.setRotate(-30);
		dice.setTranslateX(50);
		dice.setTranslateY(45);
		// deck spread image
		ImageView deckSpread = new ImageView("DeckSpread.png");
		deckSpread.setPreserveRatio(true);
		deckSpread.setFitHeight(600);
		deckSpread.setRotate(235);
		deckSpread.setTranslateX(-141);
		deckSpread.setTranslateY(-120);
		// poker coin images
		ImageView redCoin = new ImageView("RedToken.png");
		redCoin.setPreserveRatio(true);
		redCoin.setFitHeight(150);
		redCoin.setTranslateX(850);
		redCoin.setTranslateY(350);
		ImageView blueCoin = new ImageView("BlueToken.png");
		blueCoin.setPreserveRatio(true);
		blueCoin.setFitHeight(150);
		blueCoin.setTranslateX(780);
		blueCoin.setTranslateY(465);
		ImageView redCoin2 = new ImageView("RedToken.png");
		redCoin2.setPreserveRatio(true);
		redCoin2.setFitHeight(150);
		redCoin2.setTranslateX(890);
		redCoin2.setTranslateY(470);
		ImageView greenCoin = new ImageView("GreenToken.png");
		greenCoin.setPreserveRatio(true);
		greenCoin.setFitHeight(150);
		greenCoin.setTranslateX(990);
		greenCoin.setTranslateY(420);
		ImageView greenCoin2 = new ImageView("GreenToken.png");
		greenCoin2.setPreserveRatio(true);
		greenCoin2.setFitHeight(150);
		greenCoin2.setTranslateX(680);
		greenCoin2.setTranslateY(535);
		ImageView redCoin3 = new ImageView("RedToken.png");
		redCoin3.setPreserveRatio(true);
		redCoin3.setFitHeight(150);
		redCoin3.setTranslateX(780);
		redCoin3.setTranslateY(600);
		ImageView blueCoin2 = new ImageView("BlueToken.png");
		blueCoin2.setPreserveRatio(true);
		blueCoin2.setFitHeight(150);
		blueCoin2.setTranslateX(905);
		blueCoin2.setTranslateY(595);
		ImageView blueCoin3 = new ImageView("BlueToken.png");
		blueCoin3.setPreserveRatio(true);
		blueCoin3.setFitHeight(150);
		blueCoin3.setTranslateX(1045);
		blueCoin3.setTranslateY(553);
		ImageView redCoin4 = new ImageView("RedToken.png");
		redCoin4.setPreserveRatio(true);
		redCoin4.setFitHeight(150);
		redCoin4.setTranslateX(1020);
		redCoin4.setTranslateY(690);
		ImageView greenCoin3 = new ImageView("GreenToken.png");
		greenCoin3.setPreserveRatio(true);
		greenCoin3.setFitHeight(150);
		greenCoin3.setTranslateX(1150);
		greenCoin3.setTranslateY(650);
		ImageView blueCoin4 = new ImageView("BlueToken.png");
		blueCoin4.setPreserveRatio(true);
		blueCoin4.setFitHeight(150);
		blueCoin4.setTranslateX(665);
		blueCoin4.setTranslateY(677);
		ImageView greenCoin4 = new ImageView("GreenToken.png");
		greenCoin4.setPreserveRatio(true);
		greenCoin4.setFitHeight(150);
		greenCoin4.setTranslateX(547);
		greenCoin4.setTranslateY(605);

		//Main images and inputs
		HBox subRoot = new HBox();
		subRoot.getChildren().addAll(title, dice, deckSpread);
		subRoot.setStyle("-fx-background-color: #032605;");

		//Welcome scene root
		welcomeRoot = new BorderPane();
		welcomeRoot.setCenter(subRoot);
		welcomeRoot.getChildren().addAll(redCoin, blueCoin, redCoin2, greenCoin, greenCoin2, redCoin3, blueCoin2, blueCoin3, redCoin4, greenCoin3, greenCoin4, blueCoin4);
		welcomeRoot.setStyle(currBG);

		// return welcomeScene
		return new Scene(welcomeRoot, 1300, 730);
	}

	public Scene end(boolean win, boolean lose, boolean draw, int anteWins, int playWins, int ppWins, int amountWon) {
		// card icon
		ImageView jack = new ImageView("jack_of_hearts.png");
		jack.setPreserveRatio(true);
		jack.setFitHeight(90);
		jack.setRotate(25);
		jack.setTranslateX(10);
		jack.setTranslateY(20);
		ImageView jack2 = new ImageView("jack_of_hearts.png");
		jack2.setPreserveRatio(true);
		jack2.setFitHeight(90);
		jack2.setRotate(-25);
		jack2.setTranslateX(-30);
		jack2.setTranslateY(-65);
		VBox cardIcon = new VBox(jack, jack2);
		cardIcon.setAlignment(Pos.TOP_CENTER);

		//Results thank you message
		thanks.setStyle(currTitle);
		thanks.setTextAlignment(TextAlignment.CENTER);
		thanks.setTranslateY(-40);
		VBox title = new VBox();
		title.getChildren().addAll(cardIcon, thanks);
		title.setAlignment(Pos.TOP_CENTER);

		//Results message title
		Text results = new Text("GAME RESULTS!");
		results.setStyle("-fx-font: normal 38pt \"Tahoma\"; -fx-fill: white; -fx-stroke: red; -fx-stroke-width: 1;");
		results.setTextAlignment(TextAlignment.CENTER);

		//moneyBag symbol
		Text moneyBag = new Text("\uD83D\uDCB0");
		moneyBag.setStyle("-fx-font: normal 18pt \"Tahoma\"; -fx-fill: red");

		//Results info message
		Text winCondtion = new Text();
		Text winCondtionText = new Text();
		winCondtion.setStyle("-fx-font: normal 22pt \"Tahoma\"; -fx-fill: white;");
		winCondtionText.setStyle("-fx-font: normal 16pt \"Tahoma\"; -fx-fill: white;");

		if(win){
			winCondtion.setText("You Win!");
			winCondtionText.setText("Your winnnings: " + (anteWins + playWins) + "\n" +
					"Your earnings for your pair plus bet: " + ppWins + "\n" +
					"In Total: " + amountWon);
			winCondtionText.setTextAlignment(TextAlignment.CENTER);
		}
		else if(lose) {
			winCondtion.setText("You Lose!");
			winCondtionText.setText("Your losses: " + (anteWins + playWins) + "\n" +
					"Your earnings for your pair plus bet: " + ppWins + "\n" +
					"In Total: " + amountWon);
			winCondtionText.setTextAlignment(TextAlignment.CENTER);
		}
		else if(draw){
			winCondtion.setText("Its a Draw!");
			winCondtionText.setText("Returning play and ante wager\n" +
					"Your earnings for your pair plus bet: " + amountWon+ "\n" +
					"In Total: " + amountWon);
			winCondtionText.setTextAlignment(TextAlignment.CENTER);
		}

		// HBox for win condition
		HBox winConditionHeader = new HBox();
		winConditionHeader.getChildren().addAll(moneyBag, winCondtion);
		winConditionHeader.setAlignment(Pos.CENTER);
		winConditionHeader.setTranslateY(10);

		// VBox for all end scene messages
		gameResults = new VBox();
		gameResults.getChildren().addAll(results, winConditionHeader, winCondtionText);
		gameResults.setAlignment(Pos.TOP_CENTER);
		gameResults.setStyle("-fx-border-color: red;\n" +
				"-fx-border-insets: 5;" +
				"-fx-padding: 25,0,25,0;" +
				"-fx-min-width: 200;" +
				"-fx-max-width: 600;" +
				"-fx-min-height: 250;" +
				"-fx-max-height: 350;" +
				"-fx-border-width: 4;" +
				"-fx-border-style: dashed;" +
				"-fx-border-radius: 10;");
		gameResults.setTranslateX(60);
		gameResults.setTranslateY(-120);
		gameResults.setSpacing(15);

		// play again button
		Text play = new Text("Play Again   ");
		play.setStyle("-fx-font: normal 25pt \"Tahoma\"; -fx-fill: white;");
		Button playB = new Button();
		playB.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z'");
		playB.setOnAction(e -> {
			testing.playAgain = true;
			testing.anteBet = 0;
			testing.PairPlusBet = 0;
			testing.dealSend = false;
			testing.ppWins = 0;
			testing.fold = false;
			primaryStage.setScene(gamePlay(0));
			clientConnection.send(testing);
		});
		HBox playAgain = new HBox();
		playAgain.getChildren().addAll(play, playB);

		// exit game button
		Text exit = new Text("Exit Game   ");
		exit.setStyle("-fx-font: normal 25pt \"Tahoma\"; -fx-fill: white;");
		Button exitB = new Button();
		exitB.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z'");
		exitB.setOnAction(exitGame);
		HBox exitGame = new HBox();
		exitGame.getChildren().addAll(exit, exitB);
		VBox nextMove = new VBox();
		nextMove.getChildren().addAll(playAgain, new Text("\n"), exitGame);
		nextMove.setAlignment(Pos.CENTER);
		nextMove.setTranslateX(-150);
		nextMove.setTranslateY(80);

		//root for fold scene
		foldRoot.setTop(title);
		foldRoot.setBottom(gameResults);
		foldRoot.setRight(nextMove);
		foldRoot.setStyle(currBG);

		//update fold scene root
		fold.setRoot(foldRoot);
		return fold;
	}

	public VBox maxPlayers() {
		// card icon
		ImageView jack = new ImageView("jack_of_hearts.png");
		jack.setPreserveRatio(true);
		jack.setFitHeight(90);
		jack.setRotate(25);
		jack.setTranslateX(10);
		jack.setTranslateY(20);
		ImageView jack2 = new ImageView("jack_of_hearts.png");
		jack2.setPreserveRatio(true);
		jack2.setFitHeight(90);
		jack2.setRotate(-25);
		jack2.setTranslateX(-30);
		jack2.setTranslateY(-65);
		VBox cardIcon = new VBox(jack, jack2);
		cardIcon.setAlignment(Pos.TOP_CENTER);

		// Max players message
		Text noPlayers = new Text("Max number of\nplayers reached\non the server.");
		noPlayers.setStyle("-fx-font: bold 60pt \"Book Antiqua\"; -fx-fill: #856b05;");
		noPlayers.setTextAlignment(TextAlignment.CENTER);
		noPlayers.setTranslateY(-40);

		VBox title = new VBox();
		title.getChildren().addAll(cardIcon, noPlayers);
		title.setAlignment(Pos.TOP_CENTER);
		title.setTranslateY(100);
		title.setStyle("-fx-background-color: #032605;");
		title.setMinHeight(800);

		return title;
	}

	public Scene gamePlay(int ante) {
		setAnimateCards();

		//Menu options
		Menu options = new Menu("Options");

		//MenuItems
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(exitGame);
		MenuItem freshStart = new MenuItem("Fresh Start");
		freshStart.setOnAction(menuFreshStart);
		MenuItem newLook = new MenuItem("New Look");
		newLook.setOnAction(menuNewLook);
		options.getItems().addAll(exit, freshStart, newLook);

		//Add menu to menubar
		MenuBar optionsBar = new MenuBar(options);

		//Bet information
		allBets = new Text("Ante Bet: "+ante+" | Pair Plus Bet: "+plusBet+" | Play Bet: "+anteBet);
		allBets.setStyle("-fx-font: bold 15pt \"Tahoma\"; -fx-fill: white;");

		//total winnings information
		allWinnings = new Text("Total Winnings: $" + testing.winningsTotal);
		allWinnings.setStyle("-fx-font: bold 15pt \"Tahoma\"; -fx-fill: white; -fx-border-style: dashed;-fx-border-color: red;-fx-border-width: 1;-fx-border-radius: 2;-fx-border-insets: 10;-fx-label-padding: 0;");

		//Displays bets and total winnings
		HBox currAmountDisplay = new HBox(allBets, allWinnings);
		currAmountDisplay.setSpacing(500);
		currAmountDisplay.setPadding(new Insets(0,50,0,0));
		currAmountDisplay.setAlignment(Pos.CENTER);

		//Add menu and dollar info to top
		VBox topRegion = new VBox(optionsBar, currAmountDisplay);

		//Titles for players and dealers
		playerTitle.setStyle(currSubtitle);
		dealerTitle.setStyle(currSubtitle);

		ImageView card = new ImageView("2_of_clubs.png");
		card.setPreserveRatio(true);
		card.setFitHeight(115);
		ImageView card2 = new ImageView("queen_of_clubs.png");
		card2.setPreserveRatio(true);
		card2.setFitHeight(115);
		ImageView card3 = new ImageView("4_of_diamonds.png");
		card3.setPreserveRatio(true);
		card3.setFitHeight(115);

		ImageView backCardStack = new ImageView("StackedBackside.png");
		backCardStack.setPreserveRatio(true);
		backCardStack.setFitHeight(237);

		dealerHand = new HBox(card, card2, card3);
		dealerHand.setSpacing(10);
		dealerHand.setAlignment(Pos.CENTER);
		dealerHand.setVisible(false);

		ImageView playerCard1 = new ImageView("3_of_hearts.png");
		playerCard1.setPreserveRatio(true);
		playerCard1.setFitHeight(115);

		ImageView playerCard2 = new ImageView("10_of_spades.png");
		playerCard2.setPreserveRatio(true);
		playerCard2.setFitHeight(115);

		ImageView playerCard3 = new ImageView("8_of_clubs.png");
		playerCard3.setPreserveRatio(true);
		playerCard3.setFitHeight(115);

		playerHand = new HBox(playerCard1,playerCard2,playerCard3);
		playerHand.setSpacing(10);
		playerHand.setAlignment(Pos.CENTER);
		playerHand.setVisible(false);

		//Displays the hands and hand titles
		VBox leftPlay = new VBox();
		leftPlay.getChildren().addAll(dealerTitle, dealerHand, playerTitle, playerHand);
		leftPlay.setAlignment(Pos.CENTER);
		leftPlay.setPadding(new Insets(0, 0, 50, 50));
		leftPlay.setSpacing(25);

		//Title for placing bets
		wagerTitle.setStyle(currSubtitle);

		//Ante bet title
		Text anteText = new Text("Ante : ");
		anteText.setStyle("-fx-font: bold 15pt \"Tahoma\"; -fx-fill: #856b05;");

		//Ante button selection
		anteButtons = new ArrayList<>();
		GridPane anteSelection = new GridPane();
		int currNum = 5;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				ImageView tokenButon = new ImageView("RedToken.png");
				tokenButon.setPreserveRatio(true);
				tokenButon.setFitHeight(30);
				//create button and set style
				Button currButton = new Button(String.valueOf(currNum));
				currButton.setStyle(" -fx-font: bold 8pt \"Tahoma\";-fx-background-color: transparent;");
				currButton.setTextFill(Paint.valueOf("#FFFFFFFF"));
				currButton.setGraphic(tokenButon);
				currButton.setContentDisplay(ContentDisplay.CENTER);
				////add select/deselect event
				currButton.setOnAction(chooseAnte);
				////add to grid pane
				anteSelection.add(currButton, j, i);
				////store button in an array list
				anteButtons.add(currButton);
				////increase current number created
				if(currNum == ante){
					currButton.setDisable(true);
				}
				currNum++;
			}
		}

//		if(redoFlag){
//			redoFlag = false;
//			for (Button anteButton : anteButtons) {
//				if (Objects.equals(anteButton.getText(), String.valueOf(anteBet))) {
//					anteButton.setVisible(false);
//				}
//			}
//		}

		//Make the 25th button and add to the rest of the ante bet selection
		ImageView tokenLastButon = new ImageView("RedToken.png");
		tokenLastButon.setPreserveRatio(true);
		tokenLastButon.setFitHeight(30);
		Button lastButton = new Button(String.valueOf(currNum));
		lastButton.setStyle(" -fx-font: bold 8pt \"Tahoma\";-fx-background-color: transparent;");
		lastButton.setTextFill(Paint.valueOf("#FFFFFFFF"));
		lastButton.setGraphic(tokenLastButon);
		lastButton.setContentDisplay(ContentDisplay.CENTER);
		lastButton.setOnAction(chooseAnte);
		anteButtons.add(lastButton);
		anteSelection.add(lastButton, 2, 5);
		anteSelection.setAlignment(Pos.CENTER);

		//Diplays title and numbers for ante
		HBox anteArea = new HBox(anteText, anteSelection);
		anteArea.setAlignment(Pos.TOP_CENTER);

		//pair plus bet title
		Text plusText = new Text("Pair\nPlus : ");
		plusText.setStyle("-fx-font: bold 15pt \"Tahoma\"; -fx-fill: #856b05;");

		//pair plus number selection
		pairplusButtons = new ArrayList<>();
		GridPane plusSelection = new GridPane();
		currNum = 5;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				ImageView tokenButon = new ImageView("RedToken.png");
				tokenButon.setPreserveRatio(true);
				tokenButon.setFitHeight(30);
				//create button and set style
				Button currButton = new Button(String.valueOf(currNum));
				currButton.setStyle(" -fx-font: bold 8pt \"Tahoma\"; -fx-background-color: rgba(0,128,0,0);");
				currButton.setTextFill(Paint.valueOf("#FFFFFFFF"));
				currButton.setGraphic(tokenButon);
				currButton.setContentDisplay(ContentDisplay.CENTER);
				currButton.setOnAction(choosePairPlus);
				////add to grid pane
				plusSelection.add(currButton, j, i);
				pairplusButtons.add(currButton);
				////increase current number created
				currNum++;
			}
		}

		//Add the 25th number selection to the rest of the pair plus bets
		tokenLastButon = new ImageView("RedToken.png");
		tokenLastButon.setPreserveRatio(true);
		tokenLastButon.setFitHeight(30);
		Button lastPlusButton = new Button(String.valueOf(currNum));
		lastPlusButton.setStyle(" -fx-font: bold 8pt \"Tahoma\";-fx-background-color: transparent;");
		lastPlusButton.setTextFill(Paint.valueOf("#FFFFFFFF"));
		lastPlusButton.setGraphic(tokenLastButon);
		lastPlusButton.setContentDisplay(ContentDisplay.CENTER);
		lastPlusButton.setOnAction(choosePairPlus);
		plusSelection.add(lastPlusButton, 2, 5);
		plusSelection.setAlignment(Pos.CENTER);
		pairplusButtons.add(lastPlusButton);

		//Displays pair plus title and number selection
		HBox plusArea = new HBox(plusText, plusSelection);
		plusArea.setAlignment(Pos.TOP_CENTER);

		//Add the option of having no pair plus
		Button noPairPlus = new Button("No Pair Plus Wager");
		noPairPlus.setStyle(buttonStyle + "-fx-font: bold 6pt \"Tahoma\";");
		noPairPlus.setTextFill(Paint.valueOf("#FFFFFFFF"));
		noPairPlus.setTranslateX(-155);
		noPairPlus.setTranslateY(20);
		noPairPlus.setOnAction(choosePairPlus);
		pairplusButtons.add(noPairPlus);

		//Right side of the screen displays the selection for ante and pair plus bets
		VBox rightPlay = new VBox();
		rightPlay.getChildren().addAll(wagerTitle,anteArea,plusArea);
		rightPlay.setAlignment(Pos.CENTER);
		rightPlay.setSpacing(20);
		rightPlay.setPadding(new Insets(20,50,0,0));

		//Main displays both the left and right info of the scene
		HBox main = new HBox();
		main.getChildren().addAll(leftPlay,rightPlay);
		main.setSpacing(400);

		//Play button
		Text playT = new Text("  PLAY");
		playT.setStyle("-fx-font: normal 25pt \"Tahoma\"; -fx-fill: white;");
		Button PlayButton = new Button();
		PlayButton.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z'");
		PlayButton.setOnAction(choosePlay);
		HBox clickPlay = new HBox();
		clickPlay.getChildren().addAll(PlayButton, playT);

		//Fold button
		Text foldT = new Text("  FOLD");
		foldT.setStyle("-fx-font: normal 25pt \"Tahoma\"; -fx-fill: white;");
		Button FoldButton = new Button();
		FoldButton.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z'");
		FoldButton.setOnAction(foldGame);
		HBox clickFold = new HBox();
		clickFold.getChildren().addAll(FoldButton, foldT);

		//Displays both the play and fold option
		playAndFold = new HBox(clickPlay,clickFold);
		playAndFold.setAlignment(Pos.CENTER);
		playAndFold.setSpacing(150);
		playAndFold.setTranslateY(-100);
		playAndFold.setVisible(false);

		Text badDealText = new Text("Dealer does not have at least\n a Queen high or better");
		badDealText.setTextAlignment(TextAlignment.CENTER);
		badDealText.setStyle("-fx-font: bold 12pt \"Tahoma\"; -fx-fill: white;");
		badDealText2 = new Text("Returning Play wager and Pushing Ante wager to next hand");
		badDealText2.setStyle("-fx-font: normal 8pt \"Tahoma\"; -fx-fill: white;");

		badDeal = new VBox(badDealText,badDealText2);
		badDeal.getChildren().addAll();
		badDeal.setAlignment(Pos.TOP_CENTER);
		badDeal.setStyle("-fx-border-color: red;\n" +
				"-fx-border-insets: 5;" +
				"-fx-padding: 25,0,25,0;" +
				"-fx-max-width: 40;" +
				"-fx-max-height: 80;" +
				"-fx-border-width: 4;" +
				"-fx-border-style: dashed;" +
				"-fx-border-radius: 10;");
		badDeal.setTranslateY(-550);
		badDeal.setVisible(false);

		//Displays info of the game on a listview
		gameMessages.setStyle("-fx-control-inner-background: #083401;");
		gameMessages.setMaxSize(500,150);
		gameMessages.setTranslateY(0);

		//sets up images of cards
		backCardStack.setTranslateX(-100);
		backCardStack.setTranslateY(-293);
		for (ImageView animateCard : animateCards) {
			animateCard.setTranslateX(60);
			animateCard.setTranslateY(-303);
//			animateCard.setTranslateX(92);
//			animateCard.setTranslateY(-143);
		}

		//displays a message when max players reached
		maxMessage = maxPlayers();
		maxMessage.setTranslateY(-400);
		maxMessage.setVisible(false);

		//Displays the cards and listview and no pair plus option
		StackPane bottomRegion = new StackPane(playAndFold, gameMessages,backCardStack,noPairPlus,badDeal,maxMessage);
		bottomRegion.setAlignment(playAndFold,Pos.TOP_CENTER);
		bottomRegion.setAlignment(gameMessages,Pos.TOP_CENTER);
		bottomRegion.setAlignment(maxMessage,Pos.TOP_CENTER);
		bottomRegion.setAlignment(backCardStack,Pos.CENTER_LEFT);
		bottomRegion.setAlignment(noPairPlus,Pos.TOP_RIGHT);
		for (ImageView animateCard : animateCards) {
			bottomRegion.getChildren().add(animateCard);
			bottomRegion.setAlignment(animateCard,Pos.CENTER_LEFT);
		}
		bottomRegion.setPadding(new Insets(0,0,200,0));

		//creates root for gameplay scene
		gpRoot = new BorderPane();
		gpRoot.setStyle(currBG);
		gpRoot.setCenter(main);
		gpRoot.setBottom(bottomRegion);
		gpRoot.setTop(topRegion);

		//returns scene for gameplay
		return new Scene(gpRoot, 1300, 700);

	}

	//Animates the cards to the right spot on the board
	public void moveAnimateCards(){
		TranslateTransition translate = new TranslateTransition();
		translate.setNode(animateCards.get(0));
		translate.setDuration(Duration.millis(900));
		translate.setByX(27);
		translate.setByY(-439);
		TranslateTransition translate2 = new TranslateTransition();
		translate2.setNode(animateCards.get(1));
		translate2.setDuration(Duration.millis(900));
		translate2.setByX(117);
		translate2.setByY(-439);
		TranslateTransition translate3 = new TranslateTransition();
		translate3.setNode(animateCards.get(2));
		translate3.setDuration(Duration.millis(900));
		translate3.setByX(206);
		translate3.setByY(-439);
		TranslateTransition translate4 = new TranslateTransition();
		translate4.setNode(animateCards.get(3));
		translate4.setDuration(Duration.millis(900));
		translate4.setByX(27);
		translate4.setByY(-210);
		TranslateTransition translate5 = new TranslateTransition();
		translate5.setNode(animateCards.get(4));
		translate5.setDuration(Duration.millis(900));
		translate5.setByX(117);
		translate5.setByY(-210);
		TranslateTransition translate6 = new TranslateTransition();
		translate6.setNode(animateCards.get(5));
		translate6.setDuration(Duration.millis(900));
		translate6.setByX(206);
		translate6.setByY(-210);

		SequentialTransition sq = new SequentialTransition(translate,translate2,translate3,translate4,translate5,translate6);

		for (int i = 3; i < 6; i++) {
			RotateTransition flip = new RotateTransition();
			flip.setNode(animateCards.get(i));
			flip.setDuration(Duration.millis(1000));
			flip.setByAngle(180);
			flip.setAxis(Rotate.Y_AXIS);
			sq.getChildren().add(flip);
			FadeTransition fade = new FadeTransition();
			fade.setNode(animateCards.get(i));
			fade.setDuration(Duration.millis(100));
			fade.setFromValue(1);
			fade.setToValue(0);
			sq.getChildren().add(fade);
		}
		sq.play();
	}

	//Animates the cards to flip
	public void showAnimateCards(){
		SequentialTransition sq = new SequentialTransition();

		for (int i = 0; i < 3; i++) {
			RotateTransition flip = new RotateTransition();
			flip.setNode(animateCards.get(i));
			flip.setDuration(Duration.millis(1000));
			flip.setByAngle(180);
			flip.setAxis(Rotate.Y_AXIS);
			sq.getChildren().add(flip);
			FadeTransition fade = new FadeTransition();
			fade.setNode(animateCards.get(i));
			fade.setDuration(Duration.millis(100));
			fade.setFromValue(1);
			fade.setToValue(0);
			sq.getChildren().add(fade);
		}
		sq.play();
	}

	//Sets the cards to the initial position
	public void setAnimateCards(){
		animateCards.clear();

		for(int i = 0; i < 6; i++){
			ImageView backSide = new ImageView("BacksideCard.png");
			backSide.setPreserveRatio(true);
			backSide.setFitHeight(116);
			animateCards.add(backSide);
		}

	}

	//main
	public static void main(String[] args) {
		launch(args);
	}

	//makes event handlers used throughout the code
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("3-Card Poker");
		primaryStage.setWidth(1300);
		primaryStage.setHeight(800);

		testing.maxPlayers = false;

		// Play button is clicked on welcome scene
		playGame = actionEvent -> {
			primaryStage.setScene(gamePlay(0));
		};

		foldGame = actionEvent -> {
			testing.winningsTotal += -testing.anteBet + -testing.PairPlusBet;
			primaryStage.setScene(end(false,true,false,-testing.anteBet, 0, -testing.PairPlusBet, -testing.anteBet + -testing.PairPlusBet));
			testing.dealSend = false;
			testing.playAgain = false;
			testing.newLook = false;
			testing.play = false;
			testing.fold = true;
			testing.justJoined = false;

			clientConnection.send(testing);
		};

		exitGame = actionEvent -> {
			Platform.exit();};

		menuFreshStart = actionEvent -> {
			testing.winningsTotal = 0;
			gameMessages = new ListView<>();
			primaryStage.setScene(gamePlay(0));
		};

		menuNewLook = actionEvent -> {
			testing.dealSend = false;
			testing.playAgain = false;
			testing.newLook = true;
			testing.justJoined = false;
			testing.fold = false;

			clientConnection.send(testing);
		};

		chooseAnte = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Button b = (Button) event.getSource();

				b.setDisable(true);

				testing.anteBet = Integer.parseInt(b.getText());

				for (Button anteButton : anteButtons) {
					if (b != anteButton) {
						anteButton.setDisable(false);
					}
				}

				for (Button pairplusButton : pairplusButtons) {
					if (pairplusButton.isDisabled()) {
						moveAnimateCards();
						PauseTransition currPause = new PauseTransition(Duration.seconds(8.5));
						currPause.setOnFinished(e->playAndFold.setVisible(true));
						currPause.play();
						testing.dealSend = true;
						testing.playAgain = false;
						testing.newLook = false;
						testing.justJoined = false;
						testing.play = false;
						testing.fold = false;
						clientConnection.send(testing);
					}
				}
			}
		};

		choosePairPlus = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Button b = (Button) event.getSource();
				b.setDisable(true);

				if(Objects.equals(b.getText(), "No Pair Plus Wager")){
					testing.PairPlusBet = 0;
				}
				else{
					testing.PairPlusBet = Integer.parseInt(b.getText());
				}
				for (Button pairplusButton : pairplusButtons) {
					if (b != pairplusButton) {
						pairplusButton.setDisable(false);
					}
				}

				for (Button anteButton : anteButtons) {
					if (anteButton.isDisabled()) {
						moveAnimateCards();
						PauseTransition currPause = new PauseTransition(Duration.seconds(8.5));
						currPause.setOnFinished(e->playAndFold.setVisible(true));
						currPause.play();
						testing.dealSend = true;
						testing.playAgain = false;
						testing.newLook = false;
						testing.justJoined = false;
						testing.play = false;
						testing.fold = false;
						pairplusButtons.get(pairplusButtons.size() - 1).setMouseTransparent(true);
						clientConnection.send(testing);
					}
				}

			}
		};

		//Play option chosen over fold option
		choosePlay = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				dealerHand.setVisible(true);
				testing.dealSend = false;
				testing.playAgain = false;
				testing.newLook = false;
				testing.play = true;
				testing.fold = false;
				testing.justJoined = false;

				clientConnection.send(testing);
				showAnimateCards();
				playAndFold.setVisible(false);
			}
		};


		primaryStage.setScene(welcome());
		primaryStage.show();

	}


}
