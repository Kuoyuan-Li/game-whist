package game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import game.Whist.Suit;

// abstract player class
public abstract class Player {
	private Hand hand;
	private Location handLocation;
	private Location scoreLocation;
	private Actor scoreActor;
	private int score;
	
	public Player(Hand hand, Location handLocation, Location scoreLocation) {
		this.setHandLocation(handLocation);
		this.setScoreLocation(scoreLocation);
		this.setHand(hand);
		setScoreActor(null);
	}
	
	public abstract Card selectedCard(Suit lead, Suit trump);
	
	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public Location getHandLocation() {
		return handLocation;
	}

	public void setHandLocation(Location handLocation) {
		this.handLocation = handLocation;
	}

	public Location getScoreLocation() {
		return scoreLocation;
	}

	public void setScoreLocation(Location scoreLocation) {
		this.scoreLocation = scoreLocation;
	}

	public Actor getScoreActor() {
		return scoreActor;
	}

	public void setScoreActor(Actor scoreActor) {
		this.scoreActor = scoreActor;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	

	public void winning() {
		this.score = score + 1;
	}


}
