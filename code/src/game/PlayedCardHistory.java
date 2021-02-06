package game;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import game.Whist.Suit;

public class PlayedCardHistory {
	private static PlayedCardHistory playedCardHistory = null;
	
	private ArrayList<Card> allPlayedCard = new ArrayList<Card>();;
	private ArrayList<Card> eachRound = new ArrayList<Card>();;
	
	// use the singleton to access single instance of playedCardHistory
	public static PlayedCardHistory getPlayedCardHistoryInstance() {
		if(playedCardHistory == null) {
			playedCardHistory = new PlayedCardHistory();
		}			
		return playedCardHistory;
	}
	
	// add played card into eachRound 
	public void cardPlayed(Card card) {
		this.eachRound.add(card);
	}
	
	// transfer played cards in eachRound to allPlayerCard and then empty the eachRound
	public void newRound() {
		// move cards in eachRound into allPlayedCard
		for(Card card: eachRound) {
			if(card!=null) {
				this.allPlayedCard.add(card);
			}
		}
		this.eachRound.clear();
	}	
	
	// reset ArrayLists of allPlayedCard and eachRound
	public void resetAllPlayed() {			
		this.allPlayedCard.clear();
		this.eachRound.clear();
	}
	
	//tells a player in which position they are playing
	public int getPlayerPosition() {
		return eachRound.size();
	}
	
	// check whether the given card can win in this round (compare played cards in eachRound)
	public boolean canWin(Card card, Suit lead, Suit trump) {
		for (Card playedCard: eachRound) {
			if (!cardABeatCardB(card, playedCard, lead, trump)) {
				return false;
			}
		}
		return true;
	}
	
	// make a decision of card A has higher rank to card B
	private boolean cardABeatCardB(Card cardA, Card cardB, Suit lead, Suit trump) {
		
		// both are lead suit or both are trumps
		// return true if card A has higher rank
		if((cardA.getSuit() == lead && cardB.getSuit() == lead) || (cardA.getSuit() == trump && cardB.getSuit() == trump)) {
			if(cardA.getRankId() < cardB.getRankId()) {
				return true;
			}
		}
		
		// Card A is lead or trumps, card B is not lead and not trumps
		if(cardA.getSuit() == lead && (cardB.getSuit() != lead && cardB.getSuit() != trump)) {
			return true;
		}
		
		// Card A is trumps, card B is not
		if(cardA.getSuit() == trump && cardB.getSuit() != trump) {
			return true;
		}
		
		// otherwise, card B has higher rank
		return false;
	}

}
