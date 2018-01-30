import javax.swing.*;

/**
 * CurrencyConverClient.java
 * Purpose: app to convert currency value to other currency
 *
 * @author Shir Cohen
 */
public class CurrencyConverClient {

    public static void main(String[] args) {
        CurrencyConverterApp application = new CurrencyConverterApp();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(500, 400);
        application.setVisible(true);
    }
}
