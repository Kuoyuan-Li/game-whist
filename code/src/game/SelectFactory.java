package game;

public class SelectFactory {
	
	private static SelectFactory selectFactory = null;
	private ICardSelectStrategy selectStrategy = null;
	
	// use the singleton to access single instance of SelectFactory
	public static SelectFactory getSelectFactoryInstance() {
		if (selectFactory == null) {
			selectFactory = new SelectFactory();
		}
		return selectFactory;
	}
	
	// used to create different selectStrategy by given name of select strategy name 
	public ICardSelectStrategy createSelectStrategy(String selectStrategy) {
		switch(selectStrategy) {
		case "RANDOM_SELECT":
			this.selectStrategy = new RandomSelectStrategy();
			break;
		case "HIGHEST_RANK_SELECT":
			this.selectStrategy = new HighestRankSelectStrategy();
			break;		
		case "SMART_SELECT":
			this.selectStrategy = new SmartSelectStrategy();
			break;		
		}
		return this.selectStrategy;
	}
	
}
