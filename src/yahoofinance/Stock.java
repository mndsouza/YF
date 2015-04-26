package yahoofinance;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import yahoofinance.histquotes.HistQuotesRequest;
import yahoofinance.histquotes.HistoricalDividendQuote;
import yahoofinance.histquotes.HistoricalStockQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockQuote;
import yahoofinance.quotes.stock.StockQuotesData;
import yahoofinance.quotes.stock.StockQuotesRequest;
import yahoofinance.quotes.stock.StockStats;

/**
 *
 * @author Stijn Strickx
 */
public class Stock {

    private final String symbol;
    private String name;
    private String currency;
    private String stockExchange;
    
    private StockQuote quote;
    private StockStats stats;
    private StockDividend dividend;
    
    private List<HistoricalStockQuote> history;
    private List<HistoricalDividendQuote> dividendHistory;
    
    public Stock(String symbol) {
        this.symbol = symbol;
    }
    
    private void update() {
        StockQuotesRequest request = new StockQuotesRequest(this.symbol);
        StockQuotesData data = request.getSingleResult();
        if(data != null) {
            this.setQuote(data.getQuote());
            this.setStats(data.getStats());
            this.setDividend(data.getDividend());
            YahooFinance.logger.log(Level.INFO, "Updated Stock with symbol: {0}", this.symbol);
        } else {
            YahooFinance.logger.log(Level.SEVERE, "Failed to update Stock with symbol: {0}", this.symbol);
        }
    }
    
    /**
     * Returns the basic quotes data available for this stock.
     * If the data is not available yet, it will first be requested from Yahoo Finance.
     * 
     * @return      basic quotes data available for this stock
     * @see         #getQuote(boolean) 
     */
    public StockQuote getQuote() {
        if(this.quote != null) {
            return this.quote;
        }
        return this.getQuote(true);
    }
    
    /**
     * Returns the basic quotes data available for this stock.
     * This method will return null in the following situations:
     * <ul>
     * <li> the data hasn't been loaded yet
     *      in a previous request and refresh is set to false.
     * <li> refresh is true and the data cannot be retrieved from Yahoo Finance 
     *      for whatever reason (symbol not recognized, no network connection, ...)
     * </ul>
     * <p>
     * When the quote data gets refreshed, it will automatically also refresh
     * the statistics and dividend data of the stock from Yahoo Finance 
     * in the same request.
     * 
     * @param refresh   indicates whether the data should be requested again to Yahoo Finance
     * @return          basic quotes data available for this stock
     */
    public StockQuote getQuote(boolean refresh) {
        if(refresh) {
            this.update();
        }
        return this.quote;
    }
    
    public void setQuote(StockQuote quote) {
        this.quote = quote;
    }
    
    /**
     * Returns the statistics available for this stock.
     * If the data is not available yet, it will first be requested from Yahoo Finance.
     * 
     * @return      statistics available for this stock
     * @see         #getStats(boolean) 
     */
    public StockStats getStats() {
        if(this.stats != null) {
            return this.stats;
        }
        return this.getStats(true);
    }
    
    /**
     * Returns the statistics available for this stock.
     * This method will return null in the following situations:
     * <ul>
     * <li> the data hasn't been loaded yet
     *      in a previous request and refresh is set to false.
     * <li> refresh is true and the data cannot be retrieved from Yahoo Finance 
     *      for whatever reason (symbol not recognized, no network connection, ...)
     * </ul>
     * <p>
     * When the statistics get refreshed, it will automatically also refresh
     * the quote and dividend data of the stock from Yahoo Finance 
     * in the same request.
     * 
     * @param refresh   indicates whether the data should be requested again to Yahoo Finance
     * @return          statistics available for this stock
     */
    public StockStats getStats(boolean refresh) {
        if(refresh) {
            this.update();
        }
        return this.stats;
    }
    
    public void setStats(StockStats stats) {
        this.stats = stats;
    }
    
    /**
     * Returns the dividend data available for this stock.
     * If the data is not available yet, it will first be requested from Yahoo Finance.
     * 
     * @return      dividend data available for this stock
     * @see         #getDividend(boolean) 
     */
    public StockDividend getDividend() {
        if(this.dividend != null) {
            return this.dividend;
        }
        return this.getDividend(true);
    }
    
    /**
     * Returns the dividend data available for this stock.
     * 
     * This method will return null in the following situations:
     * <ul>
     * <li> the data hasn't been loaded yet
     *      in a previous request and refresh is set to false.
     * <li> refresh is true and the data cannot be retrieved from Yahoo Finance 
     *      for whatever reason (symbol not recognized, no network connection, ...)
     * </ul>
     * <p>
     * When the dividend data get refreshed, it will automatically also refresh
     * the quote and statistics data of the stock from Yahoo Finance 
     * in the same request.
     * 
     * @param refresh   indicates whether the data should be requested again to Yahoo Finance
     * @return          dividend data available for this stock
     */
    public StockDividend getDividend(boolean refresh) {
        if(refresh) {
            this.update();
        }
        return this.dividend;
    }
    
    public void setDividend(StockDividend dividend) {
        this.dividend = dividend;
    }
    
    /**
     * This method will return historical quotes from this stock.
     * If the historical quotes are not available yet, they will 
     * be requested first from Yahoo Finance.
     * <p>
     * If the historical quotes are not available yet, the
     * following characteristics will be used for the request:
     * <ul>
     * <li> from: 1 year ago (default)
     * <li> to: today (default)
     * <li> interval: MONTHLY (default)
     * </ul>
     * <p>
     * There are several more methods available that allow you
     * to define some characteristics of the historical data.
     * Calling one of those methods will result in a new request
     * being sent to Yahoo Finance.
     * 
     * @return      a list of historical quotes from this stock
     * @see         #getHistory(yahoofinance.histquotes.Interval) 
     * @see         #getHistory(java.util.Calendar) 
     * @see         #getHistory(java.util.Calendar, java.util.Calendar) 
     * @see         #getHistory(java.util.Calendar, yahoofinance.histquotes.Interval) 
     * @see         #getHistory(java.util.Calendar, java.util.Calendar, yahoofinance.histquotes.Interval) 
     */
    public List<HistoricalStockQuote> getHistory() {
        if(this.history != null) {
            return this.history;
        }
        return this.getHistory(HistQuotesRequest.DEFAULT_FROM);
    }
    
    /**
     * Requests the historical quotes for this stock with the following characteristics.
     * <ul>
     * <li> from: 1 year ago (default)
     * <li> to: today (default)
     * <li> interval: specified value
     * </ul>
     * 
     * @param interval      the interval of the historical data
     * @return              a list of historical quotes from this stock
     * @see                 #getHistory() 
     */
    public List<HistoricalStockQuote> getHistory(Interval interval) {
        return this.getHistory(HistQuotesRequest.DEFAULT_FROM, interval);
    }
    
    /**
     * Requests the historical quotes for this stock with the following characteristics.
     * <ul>
     * <li> from: specified value
     * <li> to: today (default)
     * <li> interval: MONTHLY (default)
     * </ul>
     * 
     * @param from          start date of the historical data
     * @return              a list of historical quotes from this stock
     * @see                 #getHistory() 
     */
    public List<HistoricalStockQuote> getHistory(Calendar from) {
        return this.getHistory(from, HistQuotesRequest.DEFAULT_TO);
    }
    
    /**
     * Requests the historical quotes for this stock with the following characteristics.
     * <ul>
     * <li> from: specified value
     * <li> to: today (default)
     * <li> interval: specified value
     * </ul>
     * 
     * @param from          start date of the historical data
     * @param interval      the interval of the historical data
     * @return              a list of historical quotes from this stock
     * @see                 #getHistory() 
     */
    public List<HistoricalStockQuote> getHistory(Calendar from, Interval interval) {
        return this.getHistory(from, HistQuotesRequest.DEFAULT_TO, interval);
    }
    
    /**
     * Requests the historical quotes for this stock with the following characteristics.
     * <ul>
     * <li> from: specified value
     * <li> to: specified value
     * <li> interval: MONTHLY (default)
     * </ul>
     * 
     * @param from          start date of the historical data
     * @param to            end date of the historical data
     * @return              a list of historical quotes from this stock
     * @see                 #getHistory() 
     */
    public List<HistoricalStockQuote> getHistory(Calendar from, Calendar to) {
        return this.getHistory(from, to, Interval.MONTHLY);
    }
    
    /**
     * Requests the historical quotes for this stock with the following characteristics.
     * <ul>
     * <li> from: specified value
     * <li> to: specified value
     * <li> interval: specified value
     * </ul>
     * 
     * @param from          start date of the historical data
     * @param to            end date of the historical data
     * @param interval      the interval of the historical data
     * @return              a list of historical quotes from this stock
     * @see                 #getHistory() 
     */
    public List<HistoricalStockQuote> getHistory(Calendar from, Calendar to, Interval interval) {
        HistQuotesRequest hist = new HistQuotesRequest(this.symbol, from, to, interval);
        this.setHistory(hist.getQuotes());
        return this.history;
    }
    
    public List<HistoricalDividendQuote> getDividendHistory(Calendar from, Calendar to) {
        HistQuotesRequest hist = new HistQuotesRequest(this.symbol, from, to, Interval.DIVIDEND);
        this.setDividendHistory(hist.getDividendQuotes());
        return this.dividendHistory;
    }
    
    private void setHistory(List<HistoricalStockQuote> history) {
        this.history = history;
    }
    
    private void setDividendHistory(List<HistoricalDividendQuote> history) {
        this.dividendHistory = history;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getStockExchange() {
        return stockExchange;
    }
    
    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }
    
    @Override
    public String toString() {
        return this.symbol + ": " + this.quote.getPrice();
    }
    
    public void print() {
        System.out.println(this.symbol);
        System.out.println("--------------------------------");
        for (Field f : this.getClass().getDeclaredFields()) {
            try {
                System.out.println(f.getName() + ": " + f.get(this));
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Stock.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Stock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("--------------------------------");
    }
    
}
