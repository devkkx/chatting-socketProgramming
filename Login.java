import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Login extends JFrame implements ActionListener {
    JTextField tfUsername;
    JPasswordField pfPassword;
    JButton loginBtn, registerBtn;

    public Login() {
        setTitle("Login");
        setSize(350, 200);
        setLayout(new GridLayout(4, 1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tfUsername = new JTextField();
        pfPassword = new JPasswordField();
        loginBtn = new JButton("Login");
        registerBtn = new JButton("Go to Register");

        add(new JLabel("Username:"));
        add(tfUsername);
        add(new JLabel("Password:"));
        add(pfPassword);
        add(loginBtn);
        add(registerBtn);

        loginBtn.addActionListener(this);
        registerBtn.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String user = tfUsername.getText();
            String pass = new String(pfPassword.getPassword());

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
                    JOptionPane.showMessageDialog(this, "Invalid credentials!");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == registerBtn) {
            dispose();
            new Register();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
