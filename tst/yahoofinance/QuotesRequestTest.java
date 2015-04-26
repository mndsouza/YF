package yahoofinance;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import yahoofinance.histquotes.HistoricalDividendQuote;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class QuotesRequestTest {

	@Test
	public void getHistoricalData() {
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -1); // from 1 year ago

		Stock google = YahooFinance.get("GE");
		List<HistoricalDividendQuote> googleHistQuotes = google.getDividendHistory(from, to);
	}
}
