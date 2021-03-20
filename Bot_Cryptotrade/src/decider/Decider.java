package decider;

import constants.Constants.states;

/**
 * Class for the decisions in this program
 * 
 * @author Matthias
 *
 */
public class Decider {
	/**
	 * Method that chooeses the state to use
	 * 
	 * @param price
	 * @param average
	 * @param priceFee
	 * @param buyin
	 * @return
	 */
	public static String chooseState(Double price, Double average, Double priceSell, Double buyin) {
		if (price < average && buyin == 0.00) {
			// if the price is in a good pos buy crypto
			return states.buy.toString();
		} else if (price > priceSell && buyin != 0.00) {
			return states.sell.toString();
		}
		return states.hold.toString();
	}
}
