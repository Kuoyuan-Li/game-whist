package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.Location;
import game.Whist.Rank;
import game.Whist.Suit;

// NPC class
public class NPC extends Player{
	
	private ICardFilterStrategy filterStrategy;
	private ICardSelectStrategy selectStrategy;
	private int thinkingTime;
	
	public NPC(Hand hand, Location handLocation, Location scoreLocation,String filterStrategy, String selectStrategy, int thinkingTime) {
		super(hand, handLocation,scoreLocation);
		this.thinkingTime = thinkingTime;
		this.filterStrategy = FilterFactory.getFilterFactoryInstance().createFilterStrategy(filterStrategy);
		this.selectStrategy = SelectFactory.getSelectFactoryInstance().createSelectStrategy(selectStrategy);
	}
	
	
	// NPC selects card by using its own filter and select strategy
	@Override
	public Card selectedCard(Suit lead, Suit trump) {	
		Whist.getGame().setStatus("Player " + Whist.getGame().playerPosition(this) + " thinking...");
		Whist.getGame().WhistDelay(thinkingTime);
		Card selected;
		selected = selectStrategy.getSelect((filterStrategy.filter(this.getHand(), lead, trump)), lead, trump);
		Card cardToPlay = this.getHand().getCard(((Suit)selected.getSuit()), ((Rank)selected.getRank()));
		return cardToPlay;
	
	}
	
	
}
