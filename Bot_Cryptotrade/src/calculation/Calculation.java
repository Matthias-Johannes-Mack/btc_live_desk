package calculation;

import constants.Constants;

public class Calculation {
	/**
	 * Calculates the profit from your buyin
	 * @param buyin
	 * @return
	 */
	public static double calcProfit(double buyin) {
		return (Math.floor((1 + Constants.profit) * buyin));
	}

}
