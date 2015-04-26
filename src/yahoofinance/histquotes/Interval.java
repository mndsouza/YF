
package yahoofinance.histquotes;

/**
 *
 * @author Stijn Strickx
 */
public enum Interval {
    
    DAILY("d"),
    WEEKLY("w"),
    MONTHLY("m"),
    DIVIDEND("v");
    
    private final String tag;
    
    Interval(String tag) {
        this.tag = tag;
    }
    
    public String getTag() {
        return this.tag;
    }
    
}
