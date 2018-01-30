import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * CurrencyConverterApp.java
 * Purpose: show the frame of the currency converter
 *
 * @author Shir Cohen
 */
class CurrencyConverterApp extends JFrame {
    private JTextField valueField;
    private JComboBox currencyComboFrom;
    private JComboBox currencyComboTo;
    private String[] currencies = {"USD", "EUR", "INR", "ILS", "AUD", "GBP"};
    private DatagramPacket datagramPacket;
    private DatagramSocket socket;

    CurrencyConverterApp() {
        super("currencyConverterApp");
        JPanel currencyPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JPanel buttonPanel = new JPanel();

        JLabel valueLabel = new JLabel("Value:");
        JLabel currencyFromLabel = new JLabel("From:");
        JLabel currencyToLabel = new JLabel("To:");
        valueField = new JTextField("Type the value to convert");
        currencyComboFrom = new JComboBox(currencies);
        currencyComboTo = new JComboBox(currencies);
        JLabel resultLabel = new JLabel("Result:");
        JTextField resultField = new JTextField("0");
        resultField.setEditable(false);

        currencyPanel.add(valueLabel);
        currencyPanel.add(valueField);
        currencyPanel.add(currencyFromLabel);
        currencyPanel.add(currencyComboFrom);
        currencyPanel.add(currencyToLabel);
        currencyPanel.add(currencyComboTo);
        currencyPanel.add(resultLabel);
        currencyPanel.add(resultField);

        JButton submitButton = new JButton("Convert");
        submitButton.addActionListener(e -> {
            String message = currencies[currencyComboFrom.getSelectedIndex()] + "-" + currencies[currencyComboTo.getSelectedIndex()] + "-"
                    + valueField.getText();
            if (isValidValue(valueField.getText())) {
                String packetId = sendMessage(message);
                String respond = getResponse(packetId);
                if (respond != null)
                    resultField.setText(respond);
                else
                    JOptionPane.showMessageDialog(null, "Something went wrong!\nPlease try again!"
                            , "Error",
                            JOptionPane.ERROR_MESSAGE, null);
            }
        });
        buttonPanel.add(submitButton);
        add(currencyPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Check if the text inside of the field is a float
     *
     * @param text to check
     * @return true if the value is valid and false otherwise
     */
    private boolean isValidValue(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter only float numbers!"
                    , "Error",
                    JOptionPane.PLAIN_MESSAGE, null);
            return false;
        }
    }

    /**
     * Send message to the server
     *
     * @param message to send
     */
    private String sendMessage(String message) {
        try {
            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            byte[] data;
            String packetId = dateFormat.format(date);

            data = (packetId + ";" + message).getBytes();
            System.out.println("currencyConverterApp>> Sending packet containing: " + data);
            datagramPacket = new DatagramPacket(data, data.length, address, 7777);
            socket.send(datagramPacket);
            return packetId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get response from the server on a specific request id
     *
     * @param packetId to identify the request
     */
    private String getResponse(String packetId) {
        byte[] message = new byte[256];
        datagramPacket = new DatagramPacket(message, message.length);
        while (true) {
            try {
                socket.setSoTimeout(5000);
                socket.receive(datagramPacket);
                String data = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                String messageId = data.split(";")[0];
                if (Objects.equals(messageId, packetId))
                    return data.split(";")[1];
                else {
                    System.out.println("currencyConverterApp>> can't identify packet id");
                }
            } catch (SocketTimeoutException s) {
                System.out.println("currencyConverterApp>> Timeout: more than 5 sec passed");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        return null;
    }
}