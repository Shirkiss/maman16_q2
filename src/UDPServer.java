import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * @author Shir Cohen
 */
public class UDPServer {

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(7777);
            System.out.println("Server's Ready");
            while (true) {
                byte[] data = new byte[256];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                String[] packetString = (new String(packet.getData(), 0, packet.getLength())).split(";");
                System.out.println("Server>> received: " + packetString[1] + " with id:" + packetString[0]);
                String[] convert = packetString[1].split("-");
                double convertedValue = getExchangeRate(convert[0], convert[1]) * Double.parseDouble(convert[2]);

                String response = packetString[0] + ";" + convertedValue;
                System.out.println("Server sending to client>>" + response);
                data = response.getBytes();
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double getExchangeRate(String fromCurrency, String toCurrency) {
        final double ILS_USD = 0.29;
        final double ILS_AUD = 0.36;
        final double ILS_INR = 18.75;
        final double ILS_GBP = 0.2;
        final double ILS_EUR = 0.23;

        switch (fromCurrency + "_" + toCurrency) {
            case "USD_AUD":
                return ILS_AUD / ILS_USD;
            case "USD_INR":
                return ILS_INR / ILS_USD;
            case "USD_GBP":
                return ILS_GBP / ILS_USD;
            case "USD_USD":
                return 1;
            case "USD_ILS":
                return 1 / ILS_USD;
            case "USD_EUR":
                return ILS_EUR / ILS_USD;

            case "AUD_AUD":
                return 1;
            case "AUD_INR":
                return ILS_INR / ILS_AUD;
            case "AUD_GBP":
                return ILS_GBP / ILS_AUD;
            case "AUD_USD":
                return ILS_USD / ILS_AUD;
            case "AUD_ILS":
                return 1 / ILS_AUD;
            case "AUD_EUR":
                return ILS_EUR / ILS_AUD;

            case "GBP_AUD":
                return ILS_AUD / ILS_GBP;
            case "GBP_INR":
                return ILS_INR / ILS_GBP;
            case "GBP_GBP":
                return 1;
            case "GBP_USD":
                return ILS_USD / ILS_GBP;
            case "GBP_ILS":
                return 1 / ILS_GBP;
            case "GBP_EUR":
                return ILS_EUR / ILS_GBP;

            case "INR_AUD":
                return ILS_AUD / ILS_INR;
            case "INR_INR":
                return 1;
            case "INR_GBP":
                return ILS_GBP / ILS_INR;
            case "INR_USD":
                return ILS_USD / ILS_INR;
            case "INR_ILS":
                return 1 / ILS_INR;
            case "INR_EUR":
                return ILS_EUR / ILS_INR;

            case "EUR_AUD":
                return ILS_AUD / ILS_EUR;
            case "EUR_INR":
                return ILS_INR / ILS_EUR;
            case "EUR_GBP":
                return ILS_GBP / ILS_EUR;
            case "EUR_USD":
                return ILS_USD / ILS_EUR;
            case "EUR_ILS":
                return 1 / ILS_EUR;
            case "EUR_EUR":
                return 1;

            case "ILS_AUD":
                return ILS_AUD;
            case "ILS_INR":
                return ILS_INR;
            case "ILS_GBP":
                return ILS_GBP;
            case "ILS_USD":
                return ILS_USD;
            case "ILS_ILS":
                return 1;
            case "ILS_EUR":
                return ILS_EUR;

            default:
                return 0;
        }
    }
}
