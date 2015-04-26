package yahoofinance.histquotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import yahoofinance.Utils;
import yahoofinance.YahooFinance;

/**
 *
 * @author Stijn Strickx
 */
public class HistQuotesRequest {

	private final String symbol;

	private final Calendar from;
	private final Calendar to;

	private final Interval interval;

	public static final Calendar DEFAULT_FROM = Calendar.getInstance();
	static {
		DEFAULT_FROM.add(Calendar.YEAR, -1);
	}
	public static final Calendar DEFAULT_TO = Calendar.getInstance();
	public static final Interval DEFAULT_INTERVAL = Interval.MONTHLY;

	public HistQuotesRequest(String symbol) {
		this(symbol, DEFAULT_INTERVAL);
	}

	public HistQuotesRequest(String symbol, Interval interval) {
		this.symbol = symbol;
		this.interval = interval;

		this.from = DEFAULT_FROM;
		this.to = DEFAULT_TO;
	}

	public HistQuotesRequest(String symbol, Calendar from, Calendar to) {
		this(symbol, from, to, DEFAULT_INTERVAL);
	}

	public HistQuotesRequest(String symbol, Calendar from, Calendar to,
			Interval interval) {
		this.symbol = symbol;
		this.from = from;
		this.to = to;
		this.interval = interval;
	}

	public HistQuotesRequest(String symbol, Date from, Date to) {
		this(symbol, from, to, DEFAULT_INTERVAL);
	}

	public HistQuotesRequest(String symbol, Date from, Date to,
			Interval interval) {
		this(symbol, interval);
		this.from.setTime(from);
		this.to.setTime(to);
	}

	public List<HistoricalStockQuote> getQuotes() {

		List<HistoricalStockQuote> result = new ArrayList<HistoricalStockQuote>();

		try {
			BufferedReader br = getData(getParams());
			// Parse CSV
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				YahooFinance.logger.log(Level.INFO,
						("Parsing CSV line: " + Utils.unescape(line)));
				HistoricalStockQuote quote = HistoricalStockQuote.fromCSVLine(
						this.symbol, line);
				result.add(quote);
			}
		} catch (IOException ex) {
			YahooFinance.logger.log(Level.SEVERE, ex.toString(), ex);
		}
		return result;
	}

	public List<HistoricalDividendQuote> getDividendQuotes() {

		List<HistoricalDividendQuote> result = new ArrayList<HistoricalDividendQuote>();

		try {
			BufferedReader br = getData(getParams());
			// Parse CSV
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				YahooFinance.logger.log(Level.INFO,
						("Parsing CSV line: " + Utils.unescape(line)));
				HistoricalDividendQuote quote = HistoricalDividendQuote.fromCSVLine(
						this.symbol, line);
				result.add(quote);
			}
		} catch (IOException ex) {
			YahooFinance.logger.log(Level.SEVERE, ex.toString(), ex);
		}
		return result;
	}

	private BufferedReader getData(Map<String, String> params)
			throws MalformedURLException, IOException {
		String url = YahooFinance.HISTQUOTES_BASE_URL + "?"
				+ Utils.getURLParameters(params);
		// Get CSV from Yahoo
		YahooFinance.logger.log(Level.INFO, ("Sending request: " + url));

		URL request = new URL(url);
		URLConnection connection = request.openConnection();
		InputStreamReader is = new InputStreamReader(
				connection.getInputStream());
		BufferedReader br = new BufferedReader(is);
		br.readLine(); // skip the first line
		return br;
	}

	private Map<String, String> getParams() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("s", this.symbol);

		params.put("a", String.valueOf(this.from.get(Calendar.MONTH)));
		params.put("b", String.valueOf(this.from.get(Calendar.DAY_OF_MONTH)));
		params.put("c", String.valueOf(this.from.get(Calendar.YEAR)));

		params.put("d", String.valueOf(this.to.get(Calendar.MONTH)));
		params.put("e", String.valueOf(this.to.get(Calendar.DAY_OF_MONTH)));
		params.put("f", String.valueOf(this.to.get(Calendar.YEAR)));

		params.put("g", this.interval.getTag());

		params.put("ignore", ".csv");
		return params;
	}

}
