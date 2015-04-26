package yahoofinance.histquotes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import yahoofinance.Utils;
import yahoofinance.YahooFinance;

/**
 *
 * @author Stijn Strickx
 */
public class HistoricalStockQuote extends HistoricalQuote {

	private BigDecimal open;
	private BigDecimal low;
	private BigDecimal high;
	private BigDecimal close;

	private BigDecimal adjClose;

	private long volume;

	public HistoricalStockQuote(String symbol, Calendar date, BigDecimal open,
			BigDecimal low, BigDecimal high, BigDecimal close,
			BigDecimal adjClose, long volume) {
		super(symbol, date);
		this.open = open;
		this.low = low;
		this.high = high;
		this.close = close;
		this.adjClose = adjClose;
		this.volume = volume;
	}

	public static HistoricalStockQuote fromCSVLine(String symbol, String csvLine) {
		String[] data = csvLine.split(YahooFinance.QUOTES_CSV_DELIMITER);
		return new HistoricalStockQuote(
				symbol, 
				Utils.parseHistDate(data[0]),
				Utils.getBigDecimal(data[1]),
				Utils.getBigDecimal(data[3]),
				Utils.getBigDecimal(data[2]),
				Utils.getBigDecimal(data[4]),
				Utils.getBigDecimal(data[6]),
				Utils.getLong(data[5]));
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	/**
	 * 
	 * @return the intra-day low
	 */
	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	/**
	 * 
	 * @return the intra-day high
	 */
	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	/**
	 * The adjusted closing price on a specific date reflects all of the
	 * dividends and splits since that day. The adjusted closing price from a
	 * date in history can be used to calculate a close estimate of the total
	 * return, including dividends, that an investor earned if shares were
	 * purchased on that date.
	 * 
	 * @return the adjusted close price
	 */
	public BigDecimal getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(BigDecimal adjClose) {
		this.adjClose = adjClose;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = dateFormat.format(getDate().getTime());
		return getSymbol() + "@" + dateStr + ": " + getLow() + "-" + getHigh()
				+ ", " + getOpen() + "->" + getClose() + " (" + getAdjClose()
				+ ")";
	}
}
