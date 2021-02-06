package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import game.Whist.Rank;
import game.Whist.Suit;

public class HighestRankSelectStrategy implements ICardSelectStrategy {
	
	// select the highest rank card after using filter strategy
	@Override
	public Card getSelect(Hand hand, Suit lead, Suit trump) {
		if(hand.isEmpty()) {
			return null;
		}
		int maxIndex = hand.getMaxPosition(Hand.SortType.RANKPRIORITY);
		Card maxCard = hand.get(maxIndex);
		Rank rank = (Rank) maxCard.getRank();
		Hand maxCards = hand.extractCardsWithRank(rank);
		// if lead(first play), just select the card with highest rank
		if (lead == null) {
			return maxCard;
		}
		
		// priority on selecting when has multiple suit of card with highest rank
		// 1. lead suit
		// 2. trump suit
		// 3. other suit
		// check whether has highest rank card with lead suit
		if(maxCards.getCard(lead, rank)!=null) {
			return maxCards.getCard(lead, rank);
		}
		
		// check whether has highest rank card with trump suit
		else if (maxCards.getCard(trump, rank)!=null) {
			return maxCards.getCard(trump, rank);
		}
		
		// otherwise, choose the highest rank card
		else {
			return maxCards.getFirst();
		}
		
	}
	
	
}
