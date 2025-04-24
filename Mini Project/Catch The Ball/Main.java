import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Catch the Ball - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        LoginPanel loginPanel = new LoginPanel(frame);
        frame.add(loginPanel);

        frame.pack();
        frame.setSize(800, 630);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
