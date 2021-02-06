package game;

import ch.aplu.jcardgame.Hand;
import game.Whist.Suit;

public class NaiveLegalFilteringStrategy implements ICardFilterStrategy {
	
	// using naiveLegalFilter strategy
	@Override
	public Hand filter(Hand hand, Suit lead,Suit trump){
		// if lead(first play), return original hand
		if (lead == null) {
			return hand;
		}
		
		// if follow
		// filter all cards with suit of lead and trump
		// if there are no card with suit of lead and trump, return original hand
		else {
			Hand filteredHand = hand.extractCardsWithSuit(lead);
			Hand trumpCards = hand.extractCardsWithSuit(trump);
			filteredHand.insert(trumpCards, false);
			if (!filteredHand.isEmpty()) {
				return filteredHand; // return copy of cards in the lead and trump suits
			}else {
				return hand;
			}
		}
	}

}
