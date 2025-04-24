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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
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
    private int highScore = 0;
    private int lives = 1;
    private boolean gameOver = false;
    private final Timer timer;
    private final Random random;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private int paddleSpeed = 12;
    private int consecutiveHits = 0;
    private final int MAX_BALL_SPEED = 40;
    private final String HIGH_SCORE_FILE = "highscore.txt";
    
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
        
        loadHighScore();
        resetBall();

        timer = new Timer(16, this);
        timer.start();
    }
    
    public void stopGame() {
        timer.stop();
    }
    
    private void loadHighScore() {
        try {
            File file = new File(HIGH_SCORE_FILE);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextInt()) {
                    highScore = scanner.nextInt();
                }
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, will be created when a high score is saved
        }
    }
    
    private void saveHighScore() {
        try {
            FileWriter writer = new FileWriter(HIGH_SCORE_FILE);
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving high score: " + e.getMessage());
        }
    }
    
    private void resetBall() {
        int width = getWidth();
        if (width == 0) width = 800;
        
        ballX = random.nextInt(width - ballSize);
        ballY = 300;
        
        // Ensure the ball has some horizontal movement
        do {
            ballSpeedX = random.nextInt(7) - 3;
        } while (ballSpeedX == 0);
        
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
            g2d.fillArc(paddleX, paddleY - paddleHeight / 2, paddleWidth, paddleHeight * 4, 0, 180);

            
            Color ballColor = new Color(255, 69, 0);
            g2d.setColor(ballColor);
            g2d.fillOval(ballX, ballY, ballSize, ballSize);
            
            g2d.setColor(new Color(255, 69, 0, 50));
            g2d.fillOval(ballX - 5, ballY - 5, ballSize + 10, ballSize + 10);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Score: " + score, 20, 30);
            g2d.drawString("High Score: " + highScore, 20, 60);
            g2d.drawString("Hits: " + consecutiveHits, 20, 90);
            g2d.drawString("Ball Speed: " + Math.abs(ballSpeedY), 20, 120);
        } else {
            g2d.setColor(new Color(255, 0, 0, 150));
            g2d.fillRect(200, 200, 400, 250);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 50));
            g2d.drawString("GAME OVER", 250, 250);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("Final Score: " + score, 300, 300);
            g2d.drawString("High Score: " + highScore, 300, 340);
            g2d.drawString("Consecutive Hits: " + consecutiveHits, 250, 380);
            g2d.drawString("Press SPACE to restart", 250, 420);
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
                if (score > highScore) {
                    highScore = score;
                    saveHighScore();
                }
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
            
            // Increase ball speed but cap it at MAX_BALL_SPEED
            if (Math.abs(ballSpeedY) < MAX_BALL_SPEED) {
                if (ballSpeedY < 0) {
                    ballSpeedY--;
                } else {
                    ballSpeedY++;
                }
            }
            
            if (ballSpeedY > 0) {
                ballSpeedY = -ballSpeedY;
            }
            
            int paddleCenter = paddleX + paddleWidth / 2;
            int ballCenter = ballX + ballSize / 2;
            int hitPosition = ballCenter - paddleCenter;
            
            // Calculate the horizontal speed based on where the ball hit the paddle
            ballSpeedX = hitPosition / 10;
            
            // Add random variation to prevent perfect vertical bouncing
            int randomVariation = random.nextInt(3) - 1; // -1, 0, or 1
            ballSpeedX += randomVariation;
            
            // Ensure the ball always has some horizontal movement
            if (ballSpeedX == 0) {
                // If the ball would bounce straight up, give it a slight horizontal movement
                ballSpeedX = (random.nextBoolean() ? 1 : -1) * (1 + random.nextInt(2));
            }
            
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