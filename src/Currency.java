/**
 * Created by shir.cohen on 1/31/2018.
 */
public class Currency {
    private String currencyCode;
    private double exchangeRate;

    Currency(String currencyCode, double exchangeRate){
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
    }

    String getCurrencyCode(){
        return currencyCode;
    }

    double getExchangeRate(){
        return exchangeRate;
    }
}
