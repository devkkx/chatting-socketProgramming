import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Login extends JFrame implements ActionListener {
    JTextField tfu;
    JPasswordField pfp;
    JButton lb, rb;

    public Login() {
        setTitle("Login");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bgColor = new Color(245, 250, 255);
        Color fieldColor = new Color(230, 240, 255);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(bgColor);
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Welcome");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(255, 105, 180)); 

        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        tfu = new JTextField();
        tfu.setBackground(fieldColor);
        tfu.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tfu.setBorder(BorderFactory.createTitledBorder("Username"));

        pfp = new JPasswordField();
        pfp.setBackground(fieldColor);
        pfp.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pfp.setBorder(BorderFactory.createTitledBorder("Password"));

        lb = new JButton("Login");
        rb = new JButton("New User");

        JPanel btn = new JPanel(new FlowLayout());
        btn.setOpaque(false);
        btn.add(lb);
        btn.add(rb);

        p.add(title);
        p.add(Box.createVerticalStrut(15));
        p.add(tfu);
        p.add(Box.createVerticalStrut(10));
        p.add(pfp);
        p.add(Box.createVerticalStrut(15));
        p.add(btn);

        add(p);
        setVisible(true);

        lb.addActionListener(this);
        rb.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lb) {
            String user = tfu.getText().trim();
            String pass = new String(pfp.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/java", "root", "Kshirod#2003")) {

                String sql = "SELECT * FROM Users WHERE username=? AND password=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, user);
                ps.setString(2, pass);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    dispose();
                    Client client = new Client("Client: " + user);
                    client.connectToServer(user);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password!");
                }

            } catch (SQLException se) {
                se.printStackTrace();
            }

        } else if (e.getSource() == rb) {
            dispose();
            new Register();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
