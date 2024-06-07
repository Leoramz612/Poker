import java.util.ArrayList;

public class Deck {
    String[] Suits = {"clubs","diamonds","hearts","spades"};
    int[] Values = {2,3,4,5,6,7,8,9,10,11,12,13,14};
    String[] nameValues = {"2","3","4","5","6","7","8","9","10","jack","queen","king","ace"};
    public ArrayList<Card> createDeck(){
        ArrayList<Card> deck = new ArrayList<>();

        for (String suit : Suits) {
            for (int j = 0; j < Values.length; j++) {
                Card currCard = new Card();
                currCard.suit = suit;
                currCard.value = Values[j];
                currCard.nameValue = nameValues[j];
                currCard.imgLink = nameValues[j] + "_of_" + suit+".png";
                deck.add(currCard);
            }
        }
        return deck;
    }
}
