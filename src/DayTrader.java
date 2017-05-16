
public class DayTrader {
	static String[] portfolio = {"NVDA"};
	static int startingShares = 50;
	static double startingValue = 0;
	static double value;
	static int loopTimes = 50;
	static double targetProfit = 1.00;
	static double alertAmount = 1.00;
	static boolean notYetReset = true;
	static double todaysDelta=0;
	static boolean breakFlag = false;
	static double purchaseBuffer =100;
	public static void main(String[] args) {
		int iterator=0;
		System.out.println("Welcome to DayTrader");
		System.out.print("Currently Selling/Buying:");
		for(String s : portfolio){
			System.out.print(" "+s);
		}
		System.out.println("");
		
		double[] todaysStart = grabStartingPrices();
		System.out.println("Done grabbing initial price.");
		System.out.println("");
		System.out.println("Starting main loop...");
		System.out.println("loop will run " + loopTimes + " Times");
		for(double stP : todaysStart){
			startingValue = startingValue - stP*startingShares;
		}
		System.out.println("You're going to need to play with: "+ startingShares*sum(todaysStart));
		double startPlayValueStored = startingShares*sum(todaysStart);
		value = startingValue;
		double sellAllValue;
		double[] currentPrices = new double[todaysStart.length];
		double[] lastItPrices = new double[todaysStart.length];
		while(iterator != loopTimes){
			if(notYetReset){
				for(int i = 0; i < lastItPrices.length; i++){
					lastItPrices[i] = todaysStart[i];
				}
				notYetReset = false;
			}
			System.out.println("Fetching Quotes for iteration "+iterator);
			for(int i = 0; i < currentPrices.length; i++){
				currentPrices[i] = StockQuote.priceOf(portfolio[i]);
			}
			System.out.println("Finished Fetching Quotes for iteration "+iterator);
			/**
			 * @note: This algorithm works almost exclusively with a portfolio of one stock.
			 * It could work with multiple, but that would necessitate a re-write.
			 */
			if(sum(currentPrices)*startingShares > sum(todaysStart)*startingShares + targetProfit){
				System.out.println("Sold Stocks at a static revenue of "+sum(currentPrices));
				System.out.println("This was a profit of "+(sum(currentPrices)*startingShares + startingValue));
				todaysDelta = todaysDelta + (sum(currentPrices)*startingShares + startingValue);
			}else if(sum(currentPrices) < sum(todaysStart) - alertAmount){
				System.out.println("ALERT: WATCHED STOCK HAS DROPPED MORE THAN "+alertAmount+" DOLLARS PER SHARE SINCE RUN START");
				System.out.println("Starting Autosell...");
				System.out.println("Sold Stocks at a static revenue of "+sum(currentPrices));
				System.out.println("This was a loss of "+(sum(currentPrices)*startingShares + startingValue));
				todaysDelta = todaysDelta + (sum(currentPrices)*startingShares + startingValue);
				breakFlag = true;
			}
			for(int i = 0; i < currentPrices.length; i++){
				System.out.println("Starting price of                 "+portfolio[i]+" :: "+ todaysStart[i]);
				System.out.println("Last price of                     "+portfolio[i]+" :: "+ lastItPrices[i]);
				System.out.println("Retrieved Price this iteration of "+portfolio[i]+" :: "+currentPrices[i]);
				System.out.println("                              DeltaPrice: "+ -(lastItPrices[i]-currentPrices[i]));
				System.out.println("                         DailyDeltaPrice: "+ -(todaysStart[i]-currentPrices[i]));
				System.out.println("                   Delta Portfolio Value: "+ startingShares*-(todaysStart[i]-currentPrices[i]));
				System.out.println("");
				System.out.println("                   Stocks Owned In this: "+ startingShares);
				System.out.println("");
				System.out.println("");
				System.out.println("                   Today's Total Delta  : "+ todaysDelta);
				lastItPrices[i]=currentPrices[i];
				if(todaysDelta >= currentPrices[i] + purchaseBuffer){
					System.out.println("Purchased an additional share of "+portfolio[i]+ " with a buffer of "+purchaseBuffer);
					todaysDelta -= currentPrices[i];
					startingShares++; //Built for single stock
				}
			}
			
			if(breakFlag){
				System.out.println("Ceasing trading at a total value change of : "+todaysDelta);
				break;
			}
			iterator++;
		}
		double total = 0;
		for(int i = 0; i < portfolio.length; i++){
			total = total+currentPrices[i];
			System.out.println("Sold ALL Stocks of " +portfolio[i]);
		}
		System.out.println("Your new portfolio value: "+ total);
		System.out.println("A change of "+ (total-startPlayValueStored));
		
		
		
	}
	
	private static double[] grabStartingPrices(){
		double[] ret = new double[portfolio.length];
		System.out.println("Getting current prices...");
		for(int i = 0; i < portfolio.length; i++){
			ret[i] = StockQuote.priceOf(portfolio[i]);
		}
		return ret;
	}
	private static double sum(double[] in){
		double total = 0;
		for(double d : in){
			total = total+d;
		}
		return total;
	}
	
	
}
