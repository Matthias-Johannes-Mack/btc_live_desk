package price;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import calculation.Calculation;
import constants.Constants;
import constants.Constants.states;
import decider.Decider;

public class Price extends Thread {
	/**
	 * The runnable object
	 */
	private static Price price;
	/**
	 * counter for the n
	 */
	private Integer n;
	/**
	 * ArrayList for the average calc
	 */
	private ArrayList<Double> prices;
	/**
	 * Variable for the buyin
	 */
	protected Double buyin;
	/**
	 * Variable for the sell price
	 */
	protected Double sellPrice;

	public void run() {
		n = 0;
		buyin = 0.00;
		sellPrice = 0.00;
		prices = new ArrayList<>();
		while (true) {
			// increment n for calc
			n++;
			String[] tempStrArr = jsonGetRequest("https://api.pro.coinbase.com/products/BTC-EUR/ticker").split(",");

			String strPriceAsk = tempStrArr[5].toString();
			strPriceAsk = strPriceAsk.replace("\"", "");
			strPriceAsk = strPriceAsk.replace("ask:", "");

			String strPriceBid = tempStrArr[4].toString();
			strPriceBid = strPriceBid.replace("\"", "");
			strPriceBid = strPriceBid.replace("bid:", "");

			Double priceSell = Double.parseDouble(strPriceBid);

			// add the vals to the arraylist
			prices.add(Double.parseDouble(strPriceAsk));
			try {
				// calc the average
				Double average = Calculation.calcAVG(prices);
				// calc the price with the fee
				Double priceFee = Calculation.calcPriceWithFee(strPriceBid);
				// retrive the current state
				String state = Decider.chooseState(Double.parseDouble(strPriceAsk), average, priceFee, buyin);
				// if we buy, change buyin
				if (state.equals("buy")) {
					// put in the buyin price + fee
					buyin = priceFee;
					// put in the sales price + fee
					sellPrice = Calculation.calcProfit(buyin);
				}

				System.out.println("Buy: " + strPriceAsk + " Sell: " + priceSell + " | n: " + n + " | Avg.: " + average
						+ " | Price&Fee: " + priceFee + " | ->" + state + " | buyin: " + buyin + " | Profit: "
						+ sellPrice + " | Diff.: " + Math.floor(priceFee - sellPrice));

				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static String streamToString(InputStream inputStream) {
		String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
		return text;
	}

	/**
	 * Function that gets a json request
	 * 
	 * @param urlQueryString
	 * @return
	 */
	public static String jsonGetRequest(String urlQueryString) {
		String json = null;
		try {
			URL url = new URL(urlQueryString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "utf-8");
			connection.connect();
			InputStream inStream = connection.getInputStream();
			json = streamToString(inStream); // input stream to string
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return json;
	}
}
