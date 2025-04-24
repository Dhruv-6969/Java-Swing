import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    
    private int paddleX;
    private final int paddleY; 
    private int paddleWidth;
    private final int paddleHeight;
    private int ballX, ballY;
    private final int ballSize;
    private int ballSpeedX, ballSpeedY;
    private int score = 0;
    private int lives = 1;
    private boolean gameOver = false;
    private final Timer timer;
    private final Random random;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private int paddleSpeed = 12;
    private int consecutiveHits = 0;
    
    private final Color bgColor1 = new Color(25, 25, 112);
    private final Color bgColor2 = new Color(0, 0, 0);
    
    public GamePanel() {
        random = new Random();
        
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        paddleWidth = 100;
        paddleHeight = 15;
        paddleX = 350;
        paddleY = 550;

        ballSize = 20;
        
        resetBall();

        timer = new Timer(16, this);
        timer.start();
    }
    
    public void stopGame() {
        timer.stop();
    }
    
    private void resetBall() {
        int width = getWidth();
        if (width == 0) width = 800;
        
        ballX = random.nextInt(width - ballSize);
        ballY = 300;
        
        ballSpeedX = random.nextInt(5) - 2;
        ballSpeedY = 5;
        
        consecutiveHits = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        GradientPaint gp = new GradientPaint(
            0, 0, bgColor1, 
            0, getHeight(), bgColor2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (!gameOver) {
            GradientPaint paddleGradient = new GradientPaint(
                paddleX, paddleY, new Color(30, 144, 255),
                paddleX, paddleY + paddleHeight, new Color(0, 0, 139));
            g2d.setPaint(paddleGradient);
            g2d.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);
            
            Color ballColor = new Color(255, 69, 0);
            g2d.setColor(ballColor);
            g2d.fillOval(ballX, ballY, ballSize, ballSize);
            
            g2d.setColor(new Color(255, 69, 0, 50));
            g2d.fillOval(ballX - 5, ballY - 5, ballSize + 10, ballSize + 10);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Score: " + score, 20, 30);
            g2d.drawString("Hits: " + consecutiveHits, 20, 60);
            g2d.drawString("Ball Speed: " + Math.abs(ballSpeedY), 20, 90);
        } else {
            g2d.setColor(new Color(255, 0, 0, 150));
            g2d.fillRect(200, 200, 400, 250);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 50));
            g2d.drawString("GAME OVER", 250, 250);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("Final Score: " + score, 300, 300);
            g2d.drawString("Consecutive Hits: " + consecutiveHits, 250, 340);
            g2d.drawString("Press SPACE to restart", 250, 380);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            updatePaddle();
            updateBall();
            checkCollision();
        }
        repaint();
    }
    
    private void updatePaddle() {
        if (leftPressed && paddleX > 0) {
            paddleX -= paddleSpeed;
        }
        if (rightPressed && paddleX < getWidth() - paddleWidth) {
            paddleX += paddleSpeed;
        }
        
        if (paddleX < 0) {
            paddleX = 0;
        }
        if (paddleX > getWidth() - paddleWidth) {
            paddleX = getWidth() - paddleWidth;
        }
    }
    
    private void updateBall() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;
        
        if (ballX <= 0 || ballX >= getWidth() - ballSize) {
            ballSpeedX = -ballSpeedX;
        }
        
        if (ballY <= 0) {
            ballY = 0;
            ballSpeedY = -ballSpeedY;
        }
        
        if (ballY > getHeight()) {
            lives--;
            if (lives <= 0) {
                gameOver = true;
            } else {
                resetBall();
            }
        }
    }
    
    private void checkCollision() {
        if (ballY + ballSize >= paddleY && 
            ballY <= paddleY + paddleHeight && 
            ballX + ballSize >= paddleX && 
            ballX <= paddleX + paddleWidth) {
            
            ballY = paddleY - ballSize;
            consecutiveHits++;
            
            if (ballSpeedY < 0) {
                ballSpeedY--;
            } else {
                ballSpeedY++;
            }
            
            if (ballSpeedY > 0) {
                ballSpeedY = -ballSpeedY;
            }
            
            int paddleCenter = paddleX + paddleWidth / 2;
            int ballCenter = ballX + ballSize / 2;
            int hitPosition = ballCenter - paddleCenter;
            
            ballSpeedX = hitPosition / 10;
            score += 10 + consecutiveHits;
        }
    }
    
    private void resetGame() {
        score = 0;
        lives = 1;
        gameOver = false;
        resetBall();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (gameOver) {
            if (key == KeyEvent.VK_SPACE) {
                resetGame();
            }
            return;
        }
        
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_P) {
            timer.stop();
        }
        if (key == KeyEvent.VK_R) {
            timer.restart();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
