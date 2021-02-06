package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class ShuffleToPlayer {
	
	private static ShuffleToPlayer shuffleToPlayer = null;
	
	// use the singleton to access single instance of ShuffleToPlayer
	public static ShuffleToPlayer getInstanceOfShuffle() {
		if (shuffleToPlayer == null) {
			shuffleToPlayer = new ShuffleToPlayer();
		}
		return shuffleToPlayer;
	}
	
	// shuffle from given deck to each player
	public Hand[] shuffle(Deck deck, int nbPlayers, int nbStartCards) {
		
		Hand initialCards = deck.toHand(false);
		Hand[] hands = new Hand[nbPlayers];
		
		// random select card from initialCards and assign to each player
		for (int i = 0; i < nbPlayers; i++) {
			for (int j = 0; j < nbStartCards; j++) {
				if(hands[i] == null) {
					hands[i] = new Hand(deck);
				}
				
				Card randomCard = initialCards.get(Whist.random.nextInt(initialCards.getNumberOfCards()));
				hands[i].insert(randomCard, false);
				initialCards.remove(randomCard, false);
			}
		}
		return hands;
	}
}
