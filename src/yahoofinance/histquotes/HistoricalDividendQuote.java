package yahoofinance.histquotes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import yahoofinance.Utils;
import yahoofinance.YahooFinance;

public class HistoricalDividendQuote extends HistoricalQuote{

	private BigDecimal payout;
	
	public HistoricalDividendQuote(String symbol, Calendar date, BigDecimal payout) {
		super(symbol, date);
		this.setPayout(payout);
	}

	public static HistoricalDividendQuote fromCSVLine(String symbol, String csvLine){
		String[] data = csvLine.split(YahooFinance.QUOTES_CSV_DELIMITER);
		return new HistoricalDividendQuote(
				symbol, 
				Utils.parseHistDate(data[0]),
				Utils.getBigDecimal(data[1]));
	}
	
	public BigDecimal getPayout() {
		return payout;
	}

	public void setPayout(BigDecimal payout) {
		this.payout = payout;
	}


	@Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(getDate().getTime());
        return getSymbol() + "@" + dateStr + ": " + getPayout();
    }

	

}
