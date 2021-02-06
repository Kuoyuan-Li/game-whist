package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import game.Whist.Suit;

// select strategy interface
public interface ICardSelectStrategy {
	public Card getSelect(Hand hand, Suit lead, Suit trump);
}
