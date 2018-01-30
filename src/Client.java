import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Created by shir.cohen on 1/28/2018.
 */
public class Client extends JFrame {
    private JTextField valueField;
    private JButton submitButton;
    private JComboBox currencyComboFrom;
    private JComboBox currencyComboTo;
    private String[] currencies = {"USD", "EUR", "INR", "ILS", "AUD", "GBP"};
    private DatagramPacket p;
    private DatagramSocket socket;

    Client() {
        super("Client");
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

        submitButton = new JButton("Convert");
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

    private boolean isValidValue(String message) {
        try {
            Double.parseDouble(message);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter only float numbers!"
                    , "Error",
                    JOptionPane.PLAIN_MESSAGE, null);
            return false;
        }
    }

    private String sendMessage(String message) {
        try {
            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            byte[] data;
            String packetId = dateFormat.format(date);

            data = (packetId + ";" + message).getBytes();
            System.out.println("Client>> Sending packet containing: " + data);
            p = new DatagramPacket(data, data.length, address, 7777);
            socket.send(p);
            return packetId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getResponse(String packetId) {
        byte[] message = new byte[256];
        p = new DatagramPacket(message, message.length);
        while(true){
            try {
                socket.setSoTimeout(5000);
                socket.receive(p);
                String data = new String(p.getData(), 0, p.getLength());
                String messageId = data.split(";")[0];
                if (Objects.equals(messageId, packetId))
                    return data.split(";")[1];
                else {
                    System.out.println("Server>> can't identify packet id");
                }
            } catch (SocketTimeoutException s) {
                System.out.println("Client>> Timeout: more than 5 sec passed");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        return null;
    }
}
