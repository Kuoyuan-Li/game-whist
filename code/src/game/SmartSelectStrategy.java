package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import game.Whist.Suit;

public class SmartSelectStrategy implements ICardSelectStrategy {
	
	@Override
	public Card getSelect(Hand hand, Suit lead, Suit trump) {
		// get the position of play in each round
		int position = PlayedCardHistory.getPlayedCardHistoryInstance().getPlayerPosition();
		
		// if lead (first play), choose the lowest rank of card with non-trump suit
		if (position == 0) {
			return selectLowestCard(hand, trump);
		}
		// if follow
		else {
			Card selected = null;
			
			// if can win by using lead card, choose the lowest rank of card which can win in this round
			if (hand.getNumberOfCardsWithSuit(lead) > 0) {
				selected = givenSuitCanWin(hand, lead, lead, trump);
				if (selected != null) {
					return selected;
				}
			}
			// if cannot win by using lead card, choose the lowest rank of card with trump suit which can win in this round
			if (hand.getNumberOfCardsWithSuit(trump) > 0) {
				selected = givenSuitCanWin(hand, trump, lead, trump);
				if (selected != null) {
					return selected;
				}
			}
			// if cannot win, choose the lowest rank of Card with non-trump suit
			return selectLowestCard(hand, trump);
		}
	}
	
	// choose the lowest rank of card with non-trump suit
	private Card selectLowestCard(Hand hand, Suit trump) {
		hand.reverseSort(Hand.SortType.RANKPRIORITY, false);
		Card lowestCard = null;
		
		// find the lowest rank of card with non-trump suit
		for (Suit suit: Suit.values()) {
			if (!suit.equals(trump) && hand.getNumberOfCardsWithSuit(suit) > 0) {
				if (lowestCard == null) {
					lowestCard = hand.extractCardsWithSuit(suit).sort(Hand.SortType.RANKPRIORITY, false);
				}
				else {
					if (hand.extractCardsWithSuit(suit).sort(Hand.SortType.RANKPRIORITY, false).getRankId() > lowestCard.getRankId()) {
						lowestCard = hand.extractCardsWithSuit(suit).sort(Hand.SortType.RANKPRIORITY, false);
					}
				}
			}
		}
		
		// if only have cards with trump suit, return the lowest card with trump suit
		if (lowestCard == null) {
			lowestCard = hand.extractCardsWithSuit(trump).sort(Hand.SortType.RANKPRIORITY, false);
		}
		hand.sort(Hand.SortType.SUITPRIORITY, false);
		return lowestCard;
	}
	
	// select the card with lowest rank which can win in this round 
	private Card givenSuitCanWin(Hand hand, Suit suit, Suit lead, Suit trump) {
		Hand selectHand = hand.extractCardsWithSuit(suit);
		selectHand.reverseSort(Hand.SortType.RANKPRIORITY, false);
		for (Card card: selectHand.getCardList()) {
			if (PlayedCardHistory.getPlayedCardHistoryInstance().canWin(card, lead, trump)) {
				return card;
			}
		}
		return null;
	}
	
}
