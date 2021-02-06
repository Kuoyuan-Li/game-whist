package game;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import game.Whist.Suit;

public class TrumpSavingFilteringStrategy implements ICardFilterStrategy {
	
	// using trumpSaving filter strategy
	@Override
	public Hand filter(Hand hand, Suit lead,Suit trump) {
		// if lead (first play), return the original hand
		if (lead == null) {return hand;}
		
		// otherwise, if has cards with lead suit
		Hand filteredHand = hand.extractCardsWithSuit(lead);
		if (!filteredHand.isEmpty() ) {
			return filteredHand;// return copy of cards in the lead suit
		}
		// if does not has cards with lead suit, return cards with trump suit
		else {
			filteredHand = hand.extractCardsWithSuit(trump);
			if (!filteredHand.isEmpty()) {
				return filteredHand;// return copy of cards in the trump suit
			}else {
				return hand;
			}
		}
	}

}
