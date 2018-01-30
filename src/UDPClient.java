import javax.swing.*;

/**
 * @author Shir Cohen
 */
public class UDPClient {

    public static void main(String[] args) {
        Client clientApplication = new Client();
        clientApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientApplication.setSize(500, 400);
        clientApplication.setVisible(true);
    }
}
