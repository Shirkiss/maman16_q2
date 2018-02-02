import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;


/**
 * CurrencyConvertServer.java
 * Purpose: to convert currency value to other currency
 *
 * @author Shir Cohen
 */
public class CurrencyConvertServer {
    private static HashMap<String, Double> currencies;
    private static byte[] data;
    private static DatagramPacket packet;
    private static DatagramSocket socket;

    public static void main(String[] args) {
        try {
            socket = new DatagramSocket(4444);
            currencies = getCurrencies("C:\\Users\\shir.cohen\\Desktop\\studies\\Java\\maman16_Q2\\src\\currencies.txt");
            System.out.println("Server's Ready");
            while (true) {
                data = new byte[256];
                packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                String[] packetString = (new String(packet.getData(), 0, packet.getLength())).split(";");
                System.out.println("Server>> received: " + packetString[0] + " with id:" + packetString[1]);
                if (Objects.equals(packetString[0], "convert"))
                {
                    double convertedValue = getExchangeRate(packetString[2], packetString[3]) * Double.parseDouble(packetString[4]);
                    String response = packetString[1] + ";" + convertedValue;
                    sendData(response);
                }
                else if (Objects.equals(packetString[0], "getCurrencies"))
                {
                    String response = packetString[1] + ";" + currencies.keySet().toString();
                    sendData(response);
                }
                else
                    sendData("Error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the exchange rate of 2 currencies
     *
     * @param fromCurrency to identify the request
     * @param toCurrency   to identify the request
     * @return the exchange rate
     */
    private static double getExchangeRate(String fromCurrency, String toCurrency) {
        return (currencies.get(toCurrency) / currencies.get(fromCurrency));
    }

    /**
     * Get a file path and create a list of Currency extracted from the file
     *
     * @param path A pathname string for the questions file
     * @return A list of Currency extracted from the file
     */
    private static HashMap<String, Double> getCurrencies(String path) {
        HashMap<String, Double> currenciesList = new HashMap<>();
        try (Scanner input = new Scanner(new File(path))) {
            while (input.hasNext()) // more data to read
            {
                String line = input.nextLine();
                String[] arrayLine = line.split(",");

                String currencyCode = arrayLine[0];
                double currencyExchangeRate = Double.parseDouble(arrayLine[1]);
                currenciesList.put(currencyCode, currencyExchangeRate);
            }
            input.close();
        } catch (NoSuchElementException elementException) {
            System.err.println("File improperly formed. Terminating.");
        } catch (IllegalStateException stateException) {
            System.err.println("Error reading from file. Terminating.");
        } catch (IOException e) {
            System.err.println("Error processing file. Terminating.");
            System.exit(1);
        }
        return currenciesList;
    }

    /**
     * Send message to the client
     *
     * @param message to send
     */
    private static void sendData(String message) {
        try {
            data = message.getBytes();
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Server sending to client>>" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
