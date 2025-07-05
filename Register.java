import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Register extends JFrame implements ActionListener {
    JTextField tfu;
    JPasswordField pfp;
    JButton rb, lb;

    public Register() {
        setTitle("User Registration");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bg = new Color(245, 250, 255);
        Color field = new Color(230, 240, 255);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(bg);
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Register");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        tfu = new JTextField();
        tfu.setBackground(field);
        tfu.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tfu.setBorder(BorderFactory.createTitledBorder("Username"));

        pfp = new JPasswordField();
        pfp.setBackground(field);
        pfp.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pfp.setBorder(BorderFactory.createTitledBorder("Password"));

        rb = new JButton("Register");
        lb = new JButton("Go to Login");

        JPanel btn = new JPanel(new FlowLayout());
        btn.setOpaque(false);
        btn.add(rb);
        btn.add(lb);

        p.add(title);
        p.add(Box.createVerticalStrut(15));
        p.add(tfu);
        p.add(Box.createVerticalStrut(10));
        p.add(pfp);
        p.add(Box.createVerticalStrut(15));
        p.add(btn);

        add(p);
        setVisible(true);

        rb.addActionListener(this);
        lb.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rb) {
            String user = tfu.getText().trim();
            String pass = new String(pfp.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all the fields!");
                return;
            }

            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/java", "root", "Kshirod#2003")) {

                String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, user);
                ps.setString(2, pass);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
                new Login();

            } catch (SQLException se) {
                JOptionPane.showMessageDialog(this, "User already exists !!!");
                se.printStackTrace();
            }

        } else if (e.getSource() == lb) {
            dispose();
            new Login();
        }
    }

    public static void main(String[] args) {
        new Register();
    }
}
