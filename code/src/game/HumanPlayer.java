package game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Location;
import game.Whist.Suit;

public class HumanPlayer extends Player{
	private CardListener cardListener;
	private Card selected;
	
	public HumanPlayer(Hand hand,Location handLocation,Location scoreLocation) {
		super(hand, handLocation, scoreLocation);
	}
	
	// player select card by double click
	@Override
	public Card selectedCard(Suit lead, Suit trump) {
		this.selected = null;
		
		super.getHand().setTouchEnabled(true);
		Whist.getGame().setStatus("Player "+ Whist.getGame().playerPosition(this)+" double-click on card to lead.");
		
		while (null == this.selected) Whist.getGame().WhistDelay(100);
		return this.selected;
	}
	
	// register listener
	public void registerListener() {
		Hand hand = this.getHand();
		cardListener = new CardAdapter() // Human Player plays card
				{
					public void leftDoubleClicked(Card card) {
						selected = card;
						hand.setTouchEnabled(false);
					}
				};
		hand.addCardListener(cardListener);				
	}
	
}
