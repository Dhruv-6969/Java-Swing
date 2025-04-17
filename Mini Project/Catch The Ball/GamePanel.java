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

    private static final long serialVersionUID = 1L;
    
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
    private int paddleSpeed = 12; // Increased paddle speed from 8 to 12
    private int consecutiveHits = 0;
    
    // Colors for gradient background
    private final Color bgColor1 = new Color(25, 25, 112); // Midnight Blue
    private final Color bgColor2 = new Color(0, 0, 0);     // Black
    
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
        ballSize = 20;
        
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
        ballY = 300; // Start in middle of screen
        
        // Reset ball speed to initial values
        ballSpeedX = random.nextInt(5) - 2;
        ballSpeedY = 3; // Start with a reasonable speed
        
        // Reset consecutive hits
        consecutiveHits = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw gradient background
        GradientPaint gp = new GradientPaint(
            0, 0, bgColor1, 
            0, getHeight(), bgColor2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (!gameOver) {
            // Draw Paddle with gradient
            GradientPaint paddleGradient = new GradientPaint(
                paddleX, paddleY, new Color(30, 144, 255), // Dodger Blue
                paddleX, paddleY + paddleHeight, new Color(0, 0, 139)); // Dark Blue
            g2d.setPaint(paddleGradient);
            g2d.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);
            
            // Draw Ball with radial glow
            Color ballColor = new Color(255, 69, 0); // Red-Orange
            g2d.setColor(ballColor);
            g2d.fillOval(ballX, ballY, ballSize, ballSize);
            
            // Add subtle glow around ball
            g2d.setColor(new Color(255, 69, 0, 50)); // Semi-transparent
            g2d.fillOval(ballX - 5, ballY - 5, ballSize + 10, ballSize + 10);
            
            // Draw Score and Current Speed with more stylish text
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Score: " + score, 20, 30);
            g2d.drawString("Hits: " + consecutiveHits, 20, 60);
            g2d.drawString("Ball Speed: " + Math.abs(ballSpeedY), 20, 90);
        } else {
            // Game Over screen with better styling
            g2d.setColor(new Color(255, 0, 0, 150)); // Semi-transparent red
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
        
        // Bounce off sides
        if (ballX <= 0 || ballX >= getWidth() - ballSize) {
            ballSpeedX = -ballSpeedX;
        }
        
        // Bounce off top
        if (ballY <= 0) {
            ballY = 0;  // Ensure ball doesn't go off-screen
            ballSpeedY = -ballSpeedY;  // Reverse vertical direction
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
            
            // Increase ball speed each time it hits the paddle
            consecutiveHits++;
            
            // Increase the speed by a fixed amount each hit
            if (ballSpeedY < 0) {
                ballSpeedY--; // Make it faster going up (more negative)
            } else {
                ballSpeedY++; // Make it faster going down (more positive)
            }
            
            // Make sure to flip the direction if coming down
            if (ballSpeedY > 0) {
                ballSpeedY = -ballSpeedY; // Make sure ball goes up after hitting paddle
            }
            
            // Change horizontal direction based on where ball hits paddle
            int paddleCenter = paddleX + paddleWidth / 2;
            int ballCenter = ballX + ballSize / 2;
            int hitPosition = ballCenter - paddleCenter;
            
            // Calculate new X speed based on where ball hits paddle
            ballSpeedX = hitPosition / 10;
            
            // Increase score based on how many consecutive hits
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
        // Not used but required by KeyListener interface
    }
}