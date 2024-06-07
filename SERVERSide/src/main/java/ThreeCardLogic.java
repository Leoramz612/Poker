import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class ThreeCardLogic{

    static PokerInfo x = new PokerInfo();
    int currMaxPlayers;
    static ArrayList<Card> fullDeck = new ArrayList<>();
    static ArrayList<Card> saveDeck = new ArrayList<>();

    ThreeCardLogic(PokerInfo data, ArrayList<Card> Deck, int maxPlayers){
        Collections.shuffle(Deck);
        fullDeck = Deck;
        currMaxPlayers = maxPlayers;
    }

    public void saveData(PokerInfo data){
        x.left = data.left;
        x.justJoined = data.justJoined;
        x.fold = data.fold;
        x.totalPlayers = currMaxPlayers;
        x.playerNum = data.playerNum;
        x.anteBet = data.anteBet;
        x.PairPlusBet = data.PairPlusBet;
        x.maxPlayers = data.maxPlayers;
        x.ppWins = data.ppWins;
        x.dealSend = data.dealSend;
        x.win = data.win;
        x.lose = data.lose;
        x.draw = data.draw;
        x.badDeal = data.badDeal;
        x.playAgain = data.playAgain;
        x.winningsTotal = data.winningsTotal;
        x.newLook = data.newLook;
        x.newLookFlip = data.newLookFlip;


        if(data.newLook || data.justJoined || data.left || data.playAgain){
            return;
        }
        else if(data.dealSend){
            x.playerHand = givePLayerCards();
            x.dealerHand = giveDealerCards();
        }
        else{
            x.playerHand = data.playerHand;
            x.dealerHand = data.dealerHand;
            if(evalHand(x.dealerHand) == 0){

                x.anteWins = 0;
                x.playWins = 0;
                x.amountWon = 0;
                x.winningsTotal += x.anteWins + x.playWins;
                x.badDeal = true;
            }
            else{
                x.badDeal = false;
                compareHands(x.dealerHand,x.playerHand);
                updateWinnings(x.anteBet);

            }

            x.amountWon += evalPPWinnings(x.playerHand,x.PairPlusBet);
        }


    }

    public PokerInfo getData(){
        return x;
    }

    public ArrayList<Card> givePLayerCards(){
        ArrayList<Card> playerHand = new ArrayList<>();
        for(int i = 0; i<3;i++){
            playerHand.add(fullDeck.get(i));
            fullDeck.remove(i);
        }
        return playerHand;
    }

    public ArrayList<Card> giveDealerCards(){
        ArrayList<Card> dealerHand = new ArrayList<>();
        for(int i = 0; i<3;i++){
            dealerHand.add(fullDeck.get(i));
            fullDeck.remove(i);
        }
        return dealerHand;
    }

    // sort
    public static void sort(ArrayList<Card> hand) {
        hand.sort(Comparator.comparing(card -> Integer.valueOf(card.value)));
    }

    // cards in a sequence, all of the same suit
    public static boolean straightFlush(ArrayList<Card> hand) {
        sort(hand);
        if(Objects.equals(hand.get(0).suit, hand.get(1).suit) && Objects.equals(hand.get(1).suit, hand.get(2).suit)) {
            return (hand.get(0).value == hand.get(1).value - 1) && (hand.get(0).value == hand.get(2).value - 2);
        }
        return false;
    }
    // 3 cards of equal value (can be diff suit)
    public static boolean threeOfAKind(ArrayList<Card> hand) {
        sort(hand);
        return (hand.get(0).value == hand.get(1).value) && (hand.get(0).value == hand.get(2).value);
    }
    // consecutive values but not all in the same suit
    public static boolean straight(ArrayList<Card> hand) {
        sort(hand);
        return (hand.get(0).value == hand.get(1).value - 1) && (hand.get(0).value == hand.get(2).value - 2);
    }
    // all cards are the same suit
    public static boolean flush(ArrayList<Card> hand) {
        sort(hand);
        return Objects.equals(hand.get(0).suit, hand.get(1).suit) && Objects.equals(hand.get(1).suit, hand.get(2).suit);
    }
    // 2 cards have the same value
    public static boolean pair(ArrayList<Card> hand) {
        sort(hand);
        return hand.get(0).value == hand.get(1).value || hand.get(0).value == hand.get(2).value || hand.get(1).value == hand.get(2).value;
    }

    //queen high card or better
    public static boolean queenHighOrBetter(ArrayList<Card> hand) {
        sort(hand);
        return hand.get(2).value == 14 || hand.get(2).value == 12 || hand.get(2).value == 13;
    }

    //evaluates what type of hand the 3 cards are
    public static int evalHand(ArrayList<Card> hand){
        if(straightFlush(hand)){
            return 6;
        }
        else if(threeOfAKind(hand)){
            return 5;
        }
        else if(straight(hand)){
            return 4;
        }
        else if(flush(hand)){
            return 3;
        }
        else if(pair(hand)){
            return 2;
        }
        else if(queenHighOrBetter(hand)){
            return 1;
        }
        else{return 0;}
    }

    // If player does not have at least pair of 2's, they lose the bet
    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        int pairPlus;

        // pay out 40 to 1
        if(straightFlush(hand)) {
            pairPlus = 40 * bet;
        }
        // pay out 30 to 1
        else if(threeOfAKind(hand)) {
            pairPlus = 30 * bet;
        }
        // payout 6 to 1
        else if(straight(hand)) {
            pairPlus = 6 * bet;
        }
        // payout 3 to 1
        else if(flush(hand)) {
            pairPlus = 3 * bet;
        }
        // payout 1 to 1
        else if(pair(hand)) {
            pairPlus = bet;
        }
        else {
            pairPlus = -bet;
        }

        x.ppWins = pairPlus;
        x.winningsTotal += pairPlus;
        return pairPlus;
    }

    //updates winning depending on if it's a winning, losing, or tied hand
    public static int updateWinnings(int anteBet) {
        int profit = 0;

        if(x.win){
            profit = anteBet;
            x.anteWins = profit;
            x.playWins = profit;
            x.amountWon = profit * 2;
            x.winningsTotal += profit * 2;
        }
        else if(x.lose){
            profit = -anteBet;
            x.anteWins = profit;
            x.playWins = profit;
            x.amountWon = profit * 2;
            x.winningsTotal += profit * 2;
        }
        else if(x.draw){
            x.anteWins = profit;
            x.playWins = profit;
            x.amountWon = profit * 2;
            x.winningsTotal += profit * 2;
        }


        return x.winningsTotal;
    }

    //compares the hands of the player and dealer
    public static void compareHands(ArrayList<Card> dealer, ArrayList<Card> player){
        if(evalHand(dealer) > evalHand(player)){
            x.lose = true;
        }
        else if(evalHand(dealer) < evalHand(player)){
            x.win = true;
        }
        else if(evalHand(dealer) == evalHand(player)){
            // find who has the higher card
            sort(dealer);
            sort(player);
            int indexDealer = 0;
            int indexPlayer = 0;

            if(straightFlush(player)) {
                if(dealer.get(2).value < player.get(2).value){
                    x.win = true;
                }
                else if(dealer.get(2).value > player.get(2).value){
                    x.lose = true;
                }
                else if(dealer.get(2).value == player.get(2).value){
                    x.draw = true;
                }
            }
            else if(threeOfAKind(player)) {
                if(dealer.get(2).value < player.get(2).value){
                    x.win = true;
                }
                else if(dealer.get(2).value > player.get(2).value){
                    x.lose = true;
                }
            }
            else if(straight(player)) {
                if(dealer.get(2).value < player.get(2).value){
                    x.win = true;
                }
                else if(dealer.get(2).value > player.get(2).value){
                    x.lose = true;
                }
                else if(dealer.get(2).value == player.get(2).value){
                    x.draw = true;
                }
            }
            else if(flush(player)) {
                if(dealer.get(2).value < player.get(2).value){
                    x.win = true;
                }
                else if (dealer.get(2).value > player.get(2).value) {
                    x.lose = true;
                }
                else if(dealer.get(2).value ==  player.get(2).value){
                    if(dealer.get(1).value < player.get(1).value){
                        x.win = true;
                    }
                    else if (dealer.get(1).value > player.get(1).value) {
                        x.lose = true;
                    }
                    else if(dealer.get(1).value ==  player.get(1).value){
                        if(dealer.get(0).value < player.get(0).value){
                            x.win = true;
                        }
                        else if (dealer.get(0).value > player.get(0).value) {
                            x.lose = true;
                        }
                        else if(dealer.get(0).value ==  player.get(0).value){
                            x.draw = true;
                        }
                    }
                }

            }
            else if(pair(player)) {
                if(dealer.get(0).value == dealer.get(2).value) {
                    indexDealer = 0;
                }
                else {
                    indexDealer = 1;
                }
                if(player.get(0).value == player.get(2).value) {
                    indexPlayer = 0;
                }
                else {
                    indexPlayer = 1;
                }
                if(dealer.get(indexDealer).value > dealer.get(indexPlayer).value) {
                    x.lose = true;
                }
                else if(dealer.get(indexDealer).value < dealer.get(indexPlayer).value) {
                    x.win = true;
                }
                else if(dealer.get(indexDealer).value == dealer.get(indexPlayer).value) {
                    x.draw = true;
                }

            }
            else if(queenHighOrBetter(player)){
                    if(dealer.get(2).value < player.get(2).value){
                        x.win = true;
                    }
                    else if (dealer.get(2).value > player.get(2).value) {
                        x.lose = true;
                    }
                    else if(dealer.get(2).value ==  player.get(2).value){
                        if(dealer.get(1).value < player.get(1).value){
                            x.win = true;
                        }
                        else if (dealer.get(1).value > player.get(1).value) {
                            x.lose = true;
                        }
                        else if(dealer.get(1).value ==  player.get(1).value){
                            if(dealer.get(0).value < player.get(0).value){
                                x.win = true;
                            }
                            else if (dealer.get(0).value > player.get(0).value) {
                                x.lose = true;
                            }
                            else if(dealer.get(0).value ==  player.get(0).value){
                                x.draw = true;
                            }
                        }
                    }
            }
            else {
                x.lose = true;
            }
        }
    }
}

