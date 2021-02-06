package game;

import ch.aplu.jcardgame.Hand;
import game.Whist.Suit;

// no filtering strategy
public class NoFilteringStrategy implements ICardFilterStrategy {
	
	// just return the original hand
	@Override
	public Hand filter(Hand hand, Suit lead,Suit trump){
		return hand;
	}

}
