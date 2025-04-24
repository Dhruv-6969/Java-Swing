import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private JFrame parentFrame;

    public LoginPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        statusLabel = new JLabel("");

        gbc.gridx = 0; gbc.gridy = 0;
        add(userLabel, gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(passLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(loginButton, gbc);

        gbc.gridy = 3;
        add(statusLabel, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Simple hardcoded check (you can extend this to file/database check)
        if (username.equals("player") && password.equals("1234")) {
            statusLabel.setText("Login successful!");
            startGame();
        } else {
            statusLabel.setText("Invalid credentials. Try again.");
        }
    }

    private void startGame() {
        parentFrame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel();
        parentFrame.add(gamePanel);
        parentFrame.revalidate();
        gamePanel.requestFocusInWindow();
    }
}
