import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.*;

public class Client extends JFrame implements ActionListener {
    private static final String SERVER_IP = "127.0.0.1"; 
    private static final int SERVER_PORT = 12345;

    private JTextArea ta;
    private JTextField tf;
    private JButton sendButton;
    private PrintWriter pw;

    public Client(String title) {
        super(title);

        // Chat Area
        ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ta.setForeground(new Color(60, 60, 60));
        ta.setBackground(new Color(250, 250, 250));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setMargin(new Insets(10, 10, 10, 10));

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(ta);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(new TitledBorder(new LineBorder(new Color(100, 100, 255), 2, true),
                " Chat ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12), new Color(100, 100, 255)));

        // Text Field
        tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf.setForeground(new Color(40, 40, 40));
        tf.setBackground(new Color(235, 255, 255));
        tf.setBorder(new CompoundBorder(new LineBorder(new Color(100, 100, 255), 2, true),
                new EmptyBorder(5, 10, 5, 10)));
        tf.addActionListener(this);

        // Send Button
        sendButton = new JButton(new ImageIcon("send.png"));
        sendButton.setPreferredSize(new Dimension(40, 40));
        sendButton.setToolTipText("Send Message");
        sendButton.addActionListener(this);

        //TextField + SendButton
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(tf, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Layout
        setLayout(new BorderLayout(10, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        setSize(600, 450);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().setBackground(new Color(245, 250, 255));

        // Window listener
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (pw != null) {
                    pw.close();
                }
                System.exit(0);
            }
        });
    }

    public void connectToServer(String name) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);

            pw.println(name);
            ta.append("[Connected as " + name + "]\n");

            new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        ta.append(message + '\n');
                        ta.setCaretPosition(ta.getDocument().getLength());
                    }
                } catch (IOException e) {
                    ta.append("[Disconnected from server]\n");
                }
            }).start();

        } catch (IOException e) {
            ta.append("[Unable to connect to server]\n");
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = tf.getText();
        if (!message.trim().isEmpty() && pw != null) {
            ta.append("You: " + message + '\n');
            pw.println(message);
            tf.setText("");
        }
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your name: ");
            String name = br.readLine();

            Client client = new Client("Client: " + name);
            client.connectToServer(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
