package game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("serial")
public class Whist extends CardGame {
	
  public enum Suit
  {
    SPADES, HEARTS, DIAMONDS, CLUBS
  }

  public enum Rank
  {
    // Reverse order of rank importance (see rankGreater() below)
	// Order of cards is tied to card images
	ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
  }
  
  final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};

  public static Random random = new Random();
  
  // return random Enum value
  public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
      int x = random.nextInt(clazz.getEnumConstants().length);
      return clazz.getEnumConstants()[x];
  }	// return random Card from Hand
 
  

  public boolean rankGreater(Card card1, Card card2) {
	  return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
  }
	 
  private final String version = "1.0";
  private final int handWidth = 400;
  private final int trickWidth = 40;
  public static int seed = 0;
  public static int nbPlayers;
  public static int nbStartCards;
  public static int winningScore;
  public static String[] playerType = new String[4]; //store all play types
  public static String[] playerFilter = new String[4]; // store each player filter strategy (if npc)
  public static String[] playerSelect = new String[4]; // store each player select strategy (if npc)

  private static Whist game;
  private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
  private final Location[] handLocations = {
			  new Location(350, 625),
			  new Location(75, 350),
			  new Location(350, 75),
			  new Location(625, 350)
	  }; //fixed hand and score locations
  private final Location[] scoreLocations = {
			  new Location(575, 675),
			  new Location(25, 575),
			  new Location(575, 25),
			  new Location(650, 575)
	  };
  private final Location trickLocation = new Location(350, 350);
  private final Location textLocation = new Location(350, 450);
  private final int thinkingTime; // could be in properties
  private Player[] players = new Player[4]; // used to all players
  private Location hideLocation = new Location(-500, - 500);
  private Location trumpsActorLocation = new Location(50, 50);
  private boolean enforceRules;

  public void setStatus(String string) { setStatusText(string); }

Font bigFont = new Font("Serif", Font.BOLD, 36);

private void initScore() {
	 for (int i = 0; i < nbPlayers; i++) {
		 players[i].setScore(0);
		 players[i].setScoreActor(new TextActor("0", Color.WHITE, bgColor, bigFont));
		 addActor(players[i].getScoreActor(), players[i].getScoreLocation());
	 }
  }

private void updateScore(Player player) {
	removeActor(player.getScoreActor());
	player.setScoreActor(new TextActor(String.valueOf(player.getScore()), Color.WHITE, bgColor, bigFont));
	addActor(player.getScoreActor(), player.getScoreLocation());
}

private Card selected;

private void initRound() {
		 Hand[] hands = ShuffleToPlayer.getInstanceOfShuffle().shuffle(deck, nbPlayers, nbStartCards); // Last element of hands is leftover cards; these are ignored
		 for (int i = 0; i < nbPlayers; i++) {
			   hands[i].sort(Hand.SortType.SUITPRIORITY, true);
			   players[i].setHand(hands[i]);
		 }
		 // reset playedCardHistory
		 PlayedCardHistory.getPlayedCardHistoryInstance().resetAllPlayed();
		 
		 // register listener for human player
		for (Player p: players) {
			if (p instanceof HumanPlayer) { // if the player is human, register cardlistener
				((HumanPlayer) p).registerListener();
			}
		}
		 
		// graphics
	    RowLayout[] layouts = new RowLayout[nbPlayers];
	    for (int i = 0; i < nbPlayers; i++) {
	      layouts[i] = new RowLayout(players[i].getHandLocation(),handWidth);
	      layouts[i].setRotationAngle(90 * i);
	      // layouts[i].setStepDelay(10);
	      players[i].getHand().setView(this, layouts[i]);
	      players[i].getHand().setTargetArea(new TargetArea(trickLocation));
	      players[i].getHand().draw();
	    }
	    
	    for (int i = 1; i < nbPlayers; i++)  // This code can be used to visually hide the cards in a hand (make them face down)
	      players[i].getHand().setVerso(true);
	    // End graphics
 }

private String printHand(ArrayList<Card> cards) {
	String out = "";
	for(int i = 0; i < cards.size(); i++) {
		out += cards.get(i).toString();
		if(i < cards.size()-1) out += ",";
	}
	return(out);
}

private Optional<Integer> playRound() {  // Returns winner, if any
	// Select and display trump suit
		final Suit trumps = randomEnum(Suit.class);
		final Actor trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
	    addActor(trumpsActor, trumpsActorLocation);
	// End trump suit
	Hand trick;
	int winner;
	Card winningCard;
	Suit lead;
	int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
	for (int i = 0; i < nbStartCards; i++) {
		PlayedCardHistory.getPlayedCardHistoryInstance().resetAllPlayed();
		trick = new Hand(deck);
    	selected = null;
    	lead = null;
    	selected = players[nextPlayer].selectedCard(lead, trumps);
        // Lead with selected card
	        trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
			trick.draw();
			selected.setVerso(false);
			// No restrictions on the card being lead
			lead = (Suit) selected.getSuit();
			selected.transfer(trick, true); // transfer to trick (includes graphic effect)
			winner = nextPlayer;
			winningCard = selected;
			PlayedCardHistory.getPlayedCardHistoryInstance().cardPlayed(selected);
			System.out.println("New trick: Lead Player = "+nextPlayer+", Lead suit = "+selected.getSuit()+", Trump suit = "+trumps);
			System.out.println("Player "+nextPlayer+" play: "+selected.toString()+" from ["+printHand(players[nextPlayer].getHand().getCardList())+"]");
		// End Lead
		for (int j = 1; j < nbPlayers; j++) {
			if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
			selected = null;
			selected = players[nextPlayer].selectedCard(lead, trumps);
	        // Follow with selected card
		        trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
				trick.draw();
				selected.setVerso(false);  // In case it is upside down
				// Check: Following card must follow suit if possible
					if (selected.getSuit() != lead && players[nextPlayer].getHand().getNumberOfCardsWithSuit(lead) > 0) {
						 // Rule violation
						 String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
						 //System.out.println(violation);
						 if (enforceRules) 
							 try {
								 throw(new BrokeRuleException(violation));
								} catch (BrokeRuleException e) {
									e.printStackTrace();
									System.out.println("A cheating player spoiled the game!");
									System.exit(0);
								}  
					 }
				// End Check
				 selected.transfer(trick, true); // transfer to trick (includes graphic effect)
				 PlayedCardHistory.getPlayedCardHistoryInstance().cardPlayed(selected);
				 System.out.println("Winning card: "+winningCard.toString());
				 System.out.println("Player "+nextPlayer+" play: "+selected.toString()+" from ["+printHand(players[nextPlayer].getHand().getCardList())+"]");
				 if ( // beat current winner with higher card
					 (selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
					  // trumped when non-trump was winning
					 (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
					 winner = nextPlayer;
					 winningCard = selected;
				 }
			// End Follow
		}
		delay(600);
		trick.setView(this, new RowLayout(hideLocation, 0));
		trick.draw();		
		nextPlayer = winner;
		System.out.println("Winner: "+winner);
		setStatusText("Player " + nextPlayer + " wins trick.");
		players[nextPlayer].winning();
		updateScore(players[nextPlayer]);
		if (winningScore == players[nextPlayer].getScore()) return Optional.of(nextPlayer);
	}
	removeActor(trumpsActor);
	return Optional.empty();
}

  public Whist()
  {
    super(700, 700, 30);
    Whist.game = this;
    setTitle("Whist (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    setStatusText("Initializing...");
    
    Properties gameProperties = new Properties();
    
    // Default properties
    gameProperties.setProperty("winningScore", "24");
    gameProperties.setProperty("nbStartCards", "13");
    gameProperties.setProperty("enforceRules", "false");
    gameProperties.setProperty("nbPlayers", "4");
    gameProperties.setProperty("thinking_time", "2000");
    gameProperties.setProperty("Seed", "30006");
    
	// Read properties from property file
	FileReader inStream = null;
	try {
		inStream = new FileReader("whist.properties");
		gameProperties.load(inStream);
	} catch (IOException e) {
		e.printStackTrace();
	}

	winningScore = Integer.parseInt(gameProperties.getProperty("winningScore"));
	nbStartCards = Integer.parseInt(gameProperties.getProperty("nbStartCards"));
	enforceRules = Boolean.parseBoolean(gameProperties.getProperty("enforceRules"));
	nbPlayers = Integer.parseInt(gameProperties.getProperty("nbPlayers"));
	thinkingTime = Integer.parseInt(gameProperties.getProperty("thinking_time"));
	
	playerType[0] = gameProperties.getProperty("player_0");
	playerType[1] = gameProperties.getProperty("player_1");
	playerType[2] = gameProperties.getProperty("player_2");
	playerType[3] = gameProperties.getProperty("player_3");
	
	playerFilter[0] = gameProperties.getProperty("player_0_Filter");
	playerFilter[1] = gameProperties.getProperty("player_1_Filter");
	playerFilter[2] = gameProperties.getProperty("player_2_Filter");
	playerFilter[3] = gameProperties.getProperty("player_3_Filter");
	
	playerSelect[0] = gameProperties.getProperty("player_0_Select");
	playerSelect[1] = gameProperties.getProperty("player_1_Select");
	playerSelect[2] = gameProperties.getProperty("player_2_Select");
	playerSelect[3] = gameProperties.getProperty("player_3_Select");
	
	if(gameProperties.getProperty("Seed") != null) {
		seed = Integer.parseInt(gameProperties.getProperty("Seed"));
		random.setSeed(seed);
	}
	
	// create players from read properties
	for (int i = 0; i < nbPlayers; i++) {   
		   players[i] = PlayerFactory.getPlayerFactoryInstance().createPlayer(null, handLocations[i], scoreLocations[i], playerType[i], playerFilter[i], playerSelect[i],thinkingTime);//create player
	 }
    initScore();
    Optional<Integer> winner;
    do { 
      initRound();
      winner = playRound();
    } while (!winner.isPresent());
    addActor(new Actor("sprites/gameover.gif"), textLocation);
    setStatusText("Game over. Winner is player: " + winner.get());
    refresh();
  }

  public static void main(String[] args)
  {
    new Whist();
  }
  
  // use the singleton to access single instance of Whist
  public static Whist getGame() {
	  return game;
  }
  
  public void WhistDelay(int delay) {
		delay(delay);
  }
  
  // return the position of player
  public int playerPosition(Player player) {
	  for (int i =0;i<nbPlayers;i++) {
		  if (players[i]==player) {
			  return i;
		  }
	  }
	  System.out.println("there is a bug in player position");
	  return 10;
  }
}
