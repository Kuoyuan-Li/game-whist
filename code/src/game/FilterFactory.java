package game;


public class FilterFactory {
	
	private static FilterFactory filterFactory = null;
	private ICardFilterStrategy filterStrategy = null;
	
	// use the singleton to access single instance of FilterFactory
	public static FilterFactory getFilterFactoryInstance() {
		if (filterFactory == null) {
			filterFactory = new FilterFactory();
		}
		return filterFactory;
	}
	
	// used to create different filterStrategy by given filter strategy name
	public ICardFilterStrategy createFilterStrategy(String filterStrategy) {
		switch(filterStrategy) {
		case "NO_FILTER":
			this.filterStrategy = new NoFilteringStrategy();
			break;
		case "NAIVE_LEGAL_FILTER":
			this.filterStrategy = new NaiveLegalFilteringStrategy();
			break;
		case "TRUMP_SAVING_FILTER":
			this.filterStrategy = new TrumpSavingFilteringStrategy();
			break;
		}	
		return this.filterStrategy;
	}
	
}
