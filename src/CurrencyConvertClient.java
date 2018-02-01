import javax.swing.*;

/**
 * CurrencyConvertClient.java
 * Purpose: app to convert currency value to other currency
 *
 * @author Shir Cohen
 */
public class CurrencyConvertClient {

    public static void main(String[] args) {
        CurrencyConverterApp application = new CurrencyConverterApp();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(500, 400);
        application.setVisible(true);
    }
}
