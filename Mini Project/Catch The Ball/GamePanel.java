import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    
    private int paddleX;
    private final int paddleY; 
    private int paddleWidth;
    private final int paddleHeight;
    private int ballX, ballY;
    private final int ballSize; // Made ballSize final since it doesn't change
    private int ballSpeedX, ballSpeedY;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private final Timer timer;
    private final Random random;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private int paddleSpeed = 8;
    private int level = 1;
    private int ballsCaught = 0;
    private int ballsNeededForLevelUp = 5;
    
    public GamePanel() {
        random = new Random();
        
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Paddle Properties
        paddleWidth = 100;
        paddleHeight = 15;
        paddleX = 350;
        paddleY = 550;

        // Ball Properties
        ballSize = 20; // Initialize final field
        
        // Initialize ball position
        resetBall();

        // Create and start timer
        timer = new Timer(16, this);
        timer.start();
    }
    
    public void stopGame() {
        timer.stop();
    }
    
    private void resetBall() {
        // Ensure width is initialized before using it
        int width = getWidth();
        if (width == 0) width = 800; // Default if not yet created
        
        ballX = random.nextInt(width - ballSize);
        ballY = 0;
        
        // Ball Speed
        ballSpeedX = random.nextInt(5) - 2;
        ballSpeedY = 3 + level;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameOver) {
            // Draw Paddle
            g.setColor(Color.BLUE);
            g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);

            // Draw Ball
            g.setColor(Color.RED);
            g.fillOval(ballX, ballY, ballSize, ballSize);
            
            // Draw Score and Lives
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 20, 30);
            g.drawString("Lives: " + lives, 20, 60);
            g.drawString("Level: " + level, 20, 90);
            g.drawString("Next Level: " + ballsCaught + "/" + ballsNeededForLevelUp, 20, 120);
        } else {
            // Game Over screen
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 250, 250);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Final Score: " + score, 300, 300);
            g.drawString("Press SPACE to restart", 250, 350);
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
        // Move paddle based on key presses
        if (leftPressed && paddleX > 0) {
            paddleX -= paddleSpeed;
        }
        if (rightPressed && paddleX < getWidth() - paddleWidth) {
            paddleX += paddleSpeed;
        }
        
        // Keep paddle within bounds
        if (paddleX < 0) {
            paddleX = 0;
        }
        if (paddleX > getWidth() - paddleWidth) {
            paddleX = getWidth() - paddleWidth;
        }
    }
    
    private void updateBall() {
        // Move ball
        ballX += ballSpeedX;
        ballY += ballSpeedY;
        
        // Bounce off walls
        if (ballX <= 0 || ballX >= getWidth() - ballSize) {
            ballSpeedX = -ballSpeedX;
        }
        
        // If ball goes below screen
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
        // Check if ball hits paddle
        if (ballY + ballSize >= paddleY && 
            ballY <= paddleY + paddleHeight && 
            ballX + ballSize >= paddleX && 
            ballX <= paddleX + paddleWidth) {
            
            // Ball hits paddle
            ballY = paddleY - ballSize;
            ballSpeedY = -ballSpeedY;
            
            // Change horizontal direction based on where ball hits paddle
            int paddleCenter = paddleX + paddleWidth / 2;
            int ballCenter = ballX + ballSize / 2;
            int hitPosition = ballCenter - paddleCenter;
            
            // Calculate new X speed based on where ball hits paddle
            ballSpeedX = hitPosition / 10;
            
            score += 10 * level;
            ballsCaught++;
            
            // Check for level up
            if (ballsCaught >= ballsNeededForLevelUp) {
                levelUp();
            }
        }
    }
    
    private void levelUp() {
        level++;
        ballsCaught = 0;
        ballsNeededForLevelUp += 2;
        
        // Make paddle narrower as levels increase (but not too narrow)
        if (paddleWidth > 40) {
            paddleWidth -= 5;
        }
        
        // Increase paddle speed slightly
        paddleSpeed++;
    }
    
    private void resetGame() {
        score = 0;
        lives = 3;
        level = 1;
        ballsCaught = 0;
        ballsNeededForLevelUp = 5;
        paddleWidth = 100;
        paddleSpeed = 8;
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
        // Not used but required by KeyListener interface
    }
}