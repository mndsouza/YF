package yahoofinance.histquotes;

import java.util.Calendar;

public abstract class HistoricalQuote {

	private String symbol;
	private Calendar date;

	public HistoricalQuote(String symbol, Calendar date) {
		this.symbol = symbol;
		this.date = date;
	}

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

}
