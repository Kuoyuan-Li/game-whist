package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import game.Whist.Suit;

public class RandomSelectStrategy implements ICardSelectStrategy {
	
	// random select card from hand
	@Override
	public Card getSelect(Hand hand, Suit lead, Suit trump) {
		int x = Whist.random.nextInt(hand.getNumberOfCards());
	    return hand.get(x);
	}
	
}
