package game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

public class PlayerFactory {
	
	public static PlayerFactory playerFactory = null;
	
	private Player player = null;
	
	// use the singleton to access single instance of PlayerFactory
	public static PlayerFactory getPlayerFactoryInstance() {
		if (playerFactory == null) {
			playerFactory = new PlayerFactory();
		}
		return playerFactory;
	}
	
	// create different type of players by given properties
	public Player createPlayer(Hand hand, Location handLocation, Location scoreLocation, String playerType, String filterStrategy, String selectStrategy,int thinkingTime) {
		if (playerType.equals("PLAYER")) {
			player = new HumanPlayer(hand,handLocation,scoreLocation);
		}
		else if (playerType.contentEquals("NPC")) {
			player = new NPC(hand, handLocation,scoreLocation,filterStrategy, selectStrategy,thinkingTime);
		}
		return player;
	}
	
	
}

