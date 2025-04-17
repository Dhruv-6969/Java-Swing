import javax.swing.JFrame;

public class CatchTheBall {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Catch the Ball");
        GamePanel gamePanel = new GamePanel();
        
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}