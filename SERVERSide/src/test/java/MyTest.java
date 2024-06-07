import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Random;

class MyTest {

	@Test
	void testSort() {
		PokerInfo data = new PokerInfo();
		Deck deck = new Deck();
		ArrayList<Card> cards = new ArrayList<>();
		cards = deck.createDeck();
		ThreeCardLogic cards2 = new ThreeCardLogic(data, cards,0);
		ArrayList<Card> hand = new ArrayList<>();
		hand = cards2.givePLayerCards();
		cards2.sort(hand);
		for (Card c : hand) {
			System.out.println(c.value);
		}
		assertTrue(hand.get(0).value <= hand.get(1).value && hand.get(1).value <= hand.get(2).value);
	}
	@Test
	void testStraightFlush() {
		ArrayList<Card> cards = new ArrayList<>();
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = i;
			c.suit = "hearts";
			cards.add(c);
		}
		assertTrue(ThreeCardLogic.straightFlush(cards));
		cards.get(0).value = 7;
		assertFalse(ThreeCardLogic.straightFlush(cards));
		cards.get(0).value = 7;
		cards.get(1).value = 9;
		cards.get(2).value = 8;
		assertTrue(ThreeCardLogic.straightFlush(cards));
	}
	@Test
	void testThreeKind() {
		ArrayList<Card> cards = new ArrayList<>();
		String[] suits = {"clubs","diamonds","hearts","spades"};
		Random random = new Random();
		// generate random number from 0 to 3
		int index = random.nextInt(4);
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = 4;
			c.suit = suits[index];
			cards.add(c);
			index = random.nextInt(4);
		}
		assertTrue(ThreeCardLogic.threeOfAKind(cards));
		cards.get(1).value = 5;
		assertFalse(ThreeCardLogic.threeOfAKind(cards));
	}
	@Test
	void testStraight() {
		ArrayList<Card> cards = new ArrayList<>();
		String[] suits = {"clubs","diamonds","hearts","spades"};
		Random random = new Random();
		// generate random number from 0 to 3
		int index = random.nextInt(4);
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = i;
			c.suit = suits[index];
			cards.add(c);
		}
		assertTrue(ThreeCardLogic.straight(cards));
		cards.get(0).value = 12;
		assertFalse(ThreeCardLogic.straight(cards));
	}
	@Test
	void testFlush() {
		ArrayList<Card> cards = new ArrayList<>();
		int[] Values = {1,2,3,4,5,6,7,8,9,10,11,12,13};
		Random random = new Random();
		int randVal = random.nextInt(14) + 1;
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = randVal;
			c.suit = "hearts";
			cards.add(c);
			randVal = random.nextInt(14) + 1;
		}
		assertTrue(ThreeCardLogic.flush(cards));
		cards.get(2).suit = "spades";
		assertFalse(ThreeCardLogic.flush(cards));
	}
	@Test
	void testFlush2() {
		ArrayList<Card> cards = new ArrayList<>();
		int[] Values = {1,2,3,4,5,6,7,8,9,10,11,12,13};
		Random random = new Random();
		int randVal = random.nextInt(14) + 1;
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = randVal;
			c.suit = "clubs";
			cards.add(c);
			randVal = random.nextInt(14) + 1;
		}
		assertTrue(ThreeCardLogic.flush(cards));
		cards.get(2).suit = "spades";
		assertFalse(ThreeCardLogic.flush(cards));
	}
	@Test
	void testPair() {
		ArrayList<Card> cards = new ArrayList<>();
		String[] suits = {"clubs","diamonds","hearts","spades"};
		Random random = new Random();
		// generate random number from 0 to 3
		int index = random.nextInt(4);
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = i;
			c.suit = suits[index];
			cards.add(c);
		}
		assertFalse(ThreeCardLogic.pair(cards));
		cards.get(0).value = cards.get(2).value;
		assertTrue(ThreeCardLogic.pair(cards));
	}
	@Test
	void testQueenOrHigher() {
		ArrayList<Card> cards = new ArrayList<>();
		String[] suits = {"clubs","diamonds","hearts","spades"};
		Random random = new Random();
		// generate random number from 0 to 3
		int index = random.nextInt(4);
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = i;
			c.suit = suits[index];
			cards.add(c);
		}
		cards.get(0).value = 13;
		assertTrue(ThreeCardLogic.queenHighOrBetter(cards));
	}

	@Test
	void testEvalHand() {
		ArrayList<Card> cards = new ArrayList<>();
		for(int i = 6; i < 9; i++) {
			Card c = new Card();
			c.value = i;
			c.suit = "hearts";
			cards.add(c);
		}
		assertEquals(6, ThreeCardLogic.evalHand(cards), "straight flush expected");
	}
	@Test
	void testEvalHand2() {
		ArrayList<Card> cards = new ArrayList<>();
		String[] suits = {"clubs","diamonds","hearts","spades"};
		Random random = new Random();
		// generate random number from 0 to 3
		int index = random.nextInt(4);
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = 4;
			c.suit = suits[index];
			cards.add(c);
			index = random.nextInt(4);
		}
		assertEquals(5, ThreeCardLogic.evalHand(cards), "3 of a kind expected");
	}
	@Test
	void testEvalHand3() {
		ArrayList<Card> cards = new ArrayList<>();
		String[] suits = {"clubs","diamonds","hearts","spades"};
		Random random = new Random();
		// generate random number from 0 to 3
		int index = random.nextInt(4);
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = i;
			c.suit = suits[index];
			cards.add(c);
		}
		if(index > 0) {
			cards.get(0).suit = suits[index-1];
		}
		else {
			cards.get(0).suit = suits[index+1];
		}
		assertEquals(4, ThreeCardLogic.evalHand(cards), "straight expected");
	}
	@Test
	void testEvalHand4() {
		ArrayList<Card> cards = new ArrayList<>();
		int[] Values = {1,2,3,4,5,6,7,8,9,10,11,12,13};
		Random random = new Random();
		int randVal = random.nextInt(14) + 1;
		for(int i = 1; i < 3; i++) {
			Card c = new Card();
			c.value = randVal;
			c.suit = "hearts";
			cards.add(c);
			randVal = random.nextInt(14) + 1;
		}
		Card c = new Card();
		c.value = cards.get(0).value;
		c.suit = "hearts";
		cards.add(c);

		assertEquals(3, ThreeCardLogic.evalHand(cards), "flush expected");
	}
	@Test
	void testEvalHand5() {
		ArrayList<Card> cards = new ArrayList<>();
		Card c1 = new Card();
		c1.value = 4;
		c1.suit = "clubs";
		Card c2 = new Card();
		c2.value = 5;
		c2.suit = "spades";
		Card c3 = new Card();
		c3.value = 4;
		c3.suit = "hearts";
		cards.add(c1);
		cards.add(c2);
		cards.add(c3);
		assertEquals(2, ThreeCardLogic.evalHand(cards), "pair expected");
	}
	@Test
	void testEvalHand6() {
		ArrayList<Card> cards = new ArrayList<>();
		Card c1 = new Card();
		c1.value = 14;
		c1.suit = "clubs";
		Card c2 = new Card();
		c2.value = 5;
		c2.suit = "spades";
		Card c3 = new Card();
		c3.value = 4;
		c3.suit = "hearts";
		cards.add(c1);
		cards.add(c2);
		cards.add(c3);
		assertEquals(1, ThreeCardLogic.evalHand(cards), "queen or higher expected");
	}
	@Test
	void testEvalHand7() {
		ArrayList<Card> cards = new ArrayList<>();
		Card c1 = new Card();
		c1.value = 9;
		c1.suit = "clubs";
		Card c2 = new Card();
		c2.value = 7;
		c2.suit = "spades";
		Card c3 = new Card();
		c3.value = 4;
		c3.suit = "hearts";
		cards.add(c1);
		cards.add(c2);
		cards.add(c3);
		assertEquals(0, ThreeCardLogic.evalHand(cards), "no bet expected");
	}
	@Test
	void testPairPlus() {
		ArrayList<Card> cards = new ArrayList<>();
		int bet = 25;
		Random random = new Random();
		// generate random number from 2 to 14
		int value = random.nextInt(15) + 2;
		Card c1 = new Card();
		c1.suit = "hearts";
		c1.value = value;
		Card c2 = new Card();
		c2.suit = "spades";
		c2.value = value;
		Card c3 = new Card();
		c3.suit = "spades";
		if(value == 14 || value == 13) {
			c3.value = value-3;
		}
		else {
			c3.value = value+2;
		}
		cards.add(c1);
		cards.add(c2);
		cards.add(c3);
		assertEquals(25, ThreeCardLogic.evalPPWinnings(cards, bet), "pair pair plus winning");
	}
	@Test
	void testPairPlus2() {
		int bet = 23;
		ArrayList<Card> cards = new ArrayList<>();
		int[] Values = {1,2,3,4,5,6,7,8,9,10,11,12,13};
		Random random = new Random();
		int randVal = random.nextInt(14) + 1;
		for(int i = 1; i < 3; i++) {
			Card c = new Card();
			c.value = randVal;
			c.suit = "spades";
			cards.add(c);
			randVal = random.nextInt(14) + 1;
		}
		Card c = new Card();
		c.value = cards.get(0).value;
		c.suit = "spades";
		cards.add(c);
		assertEquals(69, ThreeCardLogic.evalPPWinnings(cards, bet), "flush pair plus winning");
	}
	@Test
	void testPairPlus3() {
		int bet = 10;
		ArrayList<Card> cards = new ArrayList<>();
		String[] suits = {"clubs","diamonds","hearts","spades"};
		Random random = new Random();
		// generate random number from 0 to 3
		int index = random.nextInt(4);
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = i;
			c.suit = suits[index];
			cards.add(c);
		}
		if(index > 0) {
			cards.get(0).suit = suits[index-1];
		}
		else {
			cards.get(0).suit = suits[index+1];
		}
		assertEquals(60, ThreeCardLogic.evalPPWinnings(cards, bet), "straight pair plus winning");
	}
	@Test
	void testPairPlus4() {
		int bet = 14;
		ArrayList<Card> cards = new ArrayList<>();
		String[] suits = {"clubs","diamonds","hearts","spades"};
		Random random = new Random();
		// generate random number from 0 to 3
		int index = random.nextInt(4);
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = 4;
			c.suit = suits[index];
			cards.add(c);
			index = random.nextInt(4);
		}
		assertEquals(420, ThreeCardLogic.evalPPWinnings(cards, bet), "3 of a kind pair plus winning");
	}
	@Test
	void testPairPlus5() {
		int bet = 17;
		ArrayList<Card> cards = new ArrayList<>();
		for(int i = 10; i < 13; i++) {
			Card c = new Card();
			c.value = i;
			c.suit = "spades";
			cards.add(c);
		}
		assertEquals(680, ThreeCardLogic.evalPPWinnings(cards, bet), "straight flush pair plus winning");
	}
	@Test
	void testPairPlus6() {
		int bet = 13;
		ArrayList<Card> cards = new ArrayList<>();
		Card c1 = new Card();
		c1.value = 4;
		c1.suit = "clubs";
		Card c2 = new Card();
		c2.value = 10;
		c2.suit = "spades";
		Card c3 = new Card();
		c3.value = 12;
		c3.suit = "hearts";
		cards.add(c1);
		cards.add(c2);
		cards.add(c3);
		assertEquals(-13, ThreeCardLogic.evalPPWinnings(cards, bet), "no pair plus winning");
	}
	@Test
	void testUpdateWinnings() {
		PokerInfo data = new PokerInfo();
		data.anteBet = 14;
		// straight flush hand
		ArrayList<Card> cardsSF = new ArrayList<>();
		for(int i = 10; i < 13; i++) {
			Card c = new Card();
			c.value = i;
			c.suit = "spades";
			cardsSF.add(c);
		}
		// pair hand
		ArrayList<Card> cardsP = new ArrayList<>();
		Card c1 = new Card();
		c1.value = 4;
		c1.suit = "clubs";
		Card c2 = new Card();
		c2.value = 4;
		c2.suit = "spades";
		Card c3 = new Card();
		c3.value = 12;
		c3.suit = "hearts";
		cardsP.add(c1);
		cardsP.add(c2);
		cardsP.add(c3);
		// player should win because straight flush > pair
		ThreeCardLogic.compareHands(cardsP, cardsSF);
		assertEquals(28, ThreeCardLogic.updateWinnings(data.anteBet), "total winnings not calculated correctly");
	}
	@Test
	void testUpdateWinnings2() {
		PokerInfo data = new PokerInfo();
		data.anteBet = 10;
		//data.winningsTotal = 0;
		// flush hand
		ArrayList<Card> cardsF = new ArrayList<>();
		int[] Values = {1,2,3,4,5,6,7,8,9,10,11,12,13};
		Random random = new Random();
		int randVal = random.nextInt(14) + 1;
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = randVal;
			c.suit = "spades";
			cardsF.add(c);
			randVal = random.nextInt(14) + 1;
		}

		ThreeCardLogic test = new ThreeCardLogic(data,cardsF,0);

		// 3 of a kind hand
		ArrayList<Card> cardsK = new ArrayList<>();
		String[] suits = {"clubs","diamonds","hearts","spades"};
		Random random2 = new Random();
		// generate random number from 0 to 3
		int index = random2.nextInt(4);
		for(int i = 1; i < 4; i++) {
			Card c = new Card();
			c.value = 4;
			c.suit = suits[i];
			cardsK.add(c);
			index = random2.nextInt(4);
		}
		// player should lose because flush < 3 of a kind
		ThreeCardLogic.x.winningsTotal = 0;
		ThreeCardLogic.x.anteBet = 10;
		ThreeCardLogic.x.PairPlusBet = 0;
		ThreeCardLogic.x.win = false;
		ThreeCardLogic.x.lose = false;
		ThreeCardLogic.compareHands(cardsK, cardsF);
		System.out.println(ThreeCardLogic.x.win);
		System.out.println(ThreeCardLogic.x.lose);
		assertEquals(-20, ThreeCardLogic.updateWinnings(data.anteBet), "total winnings not calculated correctly");
	}

}