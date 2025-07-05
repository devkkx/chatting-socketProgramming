import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterPage extends JFrame implements ActionListener {
    JTextField tfUsername;
    JPasswordField pfPassword;
    JButton registerBtn, loginBtn;

    public RegisterPage() {
        setTitle("User Registration");
        setSize(350, 200);
        setLayout(new GridLayout(4, 1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tfUsername = new JTextField();
        pfPassword = new JPasswordField();
        registerBtn = new JButton("Register");
        loginBtn = new JButton("Go to Login");

        add(new JLabel("Username:"));
        add(tfUsername);
        add(new JLabel("Password:"));
        add(pfPassword);
        add(registerBtn);
        add(loginBtn);

        registerBtn.addActionListener(this);
        loginBtn.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerBtn) {
            String user = tfUsername.getText();
            String pass = new String(pfPassword.getPassword());

            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/java", "root", "Kshirod#2003")) {

                String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, user);
                ps.setString(2, pass);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration successful!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "User already exists!");
            }

        } else if (e.getSource() == loginBtn) {
            dispose();
            new LoginPage(); // launch login
        }
    }

    public static void main(String[] args) {
        new RegisterPage();
    }
}
