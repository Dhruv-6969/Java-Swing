import javax.swing.JFrame;

public class CatchTheBall{
    public static void main(String[] args){
        JFrame frame = new JFrame("Catch The Ball");
        GamePanel gamePanel = new GamePanel(); //Creates Game Panel

        frame.add(gamePanel); //Add panel to window
        frame.pack(); //Adjust Window to fit Panel Size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}