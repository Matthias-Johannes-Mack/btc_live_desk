package calculation;

import java.util.ArrayList;

import constants.Constants;

public class Calculation {
	/**
	 * Calculates the profit from your buyin
	 * 
	 * @param buyin - value of the buyin
	 * @return
	 */
	public static double calcProfit(double buyin) {
		return (Math.floor((1 + Constants.profit) * buyin));
	}

	/**
	 * Calculates the average of an arraylist
	 * 
	 * @param prices-arraylist with all the prices
	 * @return
	 */
	public static double calcAVG(ArrayList<Double> prices) {
		return Math.round((prices.stream().mapToDouble(val -> val).average().orElse(0.0) * 100.0) / 100.0);
	}

	/**
	 * Calculates the price with the fee of the provider
	 * 
	 * @param strPriceBid - the bid price
	 * @return price with the fee as a double
	 */
	public static double calcPriceWithFee(String strPriceBid) {
		return Math.round((((1 + Constants.fee) * Double.parseDouble(strPriceBid)) * 100.0) / 100.0);
	}

}
