import java.io.Serializable;
import java.util.ArrayList;

public class PokerInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    int playerNum;
    int totalPlayers;
    int portNum;
    int anteBet;
    int PairPlusBet;
    int anteWins;
    int playWins;
    int ppWins;
    int winningsTotal;
    int amountWon;
    Boolean playingAnotherHand;
    Boolean fold = false;
    Boolean play = false;
    Boolean justJoined = false;
    Boolean win = false;
    Boolean lose = false;
    Boolean draw = false;
    Boolean badDeal = false;
    Boolean gotPairPlus;
    Boolean dealSend = false;
    Boolean playAgain = false;
    Boolean maxPlayers = false;
    Boolean maxOnce = false;
    Boolean newLook = false;
    Boolean newLookFlip = false;
    Boolean left = false;
    Boolean closeScreen = false;
    ArrayList<Card> playerHand = new ArrayList<>(3);
    ArrayList<Card> dealerHand = new ArrayList<>(3);
    PokerInfo(){

    }
}
