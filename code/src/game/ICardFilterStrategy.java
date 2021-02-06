package game;
import ch.aplu.jcardgame.*;
import game.Whist.Suit;

// filter strategy interface
public interface ICardFilterStrategy {
	public Hand filter(Hand hand, Suit lead, Suit trump);
}
