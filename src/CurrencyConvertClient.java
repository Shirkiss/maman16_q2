import javax.swing.*;

/**
 * CurrencyConvertClient.java
 * Purpose: app to convert currency value to other currency
 *
 * @author Shir Cohen
 */
public class CurrencyConvertClient {

    public static void main(String[] args) {
        CurrencyConverterApp application = new CurrencyConverterApp(args[0], 4444);
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(500, 400);
        application.setVisible(true);
    }
}
