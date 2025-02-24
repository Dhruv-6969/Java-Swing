import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener{

    private int paddleX, paddleY, paddleWidth, paddleHeight;
    private int ballX, ballY, ballSize;
    private Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600)); //Sets Panel Size
        setBackground(Color.BLACK); //Sets Background Colour

        // Paddle Properties
        paddleWidth = 100;
        paddleHeight = 15;
        paddleX = 350; //Center of the Screen
        paddleY = 550; //Near bottom of the Screen

        // Ball Properties
        ballSize = 20;
        ballX = 400;
        ballY = 100;

        //calls action performed every 16ms
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g); //Clears the screen

        //Draw Paddle
        g.setColor(Color.BLUE);
        g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);

        //Draw Ball
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, ballSize, ballSize);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        repaint(); //Refresh Screen 60FPS
    }
}
