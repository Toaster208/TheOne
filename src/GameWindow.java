
import java.awt.*;

import javax.swing.JPanel;


public class GameWindow extends JPanel implements Runnable {

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    int playerX = 100;
    int playerY = 284;
    int playerSpeed = 4;
    int playerDimension = 1;
    // Jump variables
    double jumpVelocity = 0; // Initial jump velocity
    double jumpAcceleration = 0.5; // Acceleration due to gravity
    boolean isJumping = false;

    public GameWindow() {
        this.setPreferredSize(new Dimension(768, 576));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        this.gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                // 1: Update information, such as character position
                update();

                // 2: Draw the screen with updated information
                repaint(); // Calls the paintComponent() method

                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        if (keyHandler.upPressed && !isJumping) {
            // Start jumping
            isJumping = true;
            jumpVelocity = -10; // Adjust the initial jump velocity
        }

        if (isJumping) {
            // Update jump physics
            playerY += jumpVelocity;
            jumpVelocity += jumpAcceleration;
            if (keyHandler.leftPressed) {
                playerX -= playerSpeed;
            }
            if (keyHandler.rightPressed) {
                playerX += playerSpeed;
            }

            if (playerY >= 284) {
                // Ground level, end the jump
                isJumping = false;
                playerY = 284; // Reset to ground level
            }

        } else {
            if (keyHandler.leftPressed) {
                playerX -= playerSpeed;
            }
            if (keyHandler.rightPressed) {
                playerX += playerSpeed;
            }
        }
    }

    //TODO: Make a level

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        double s = 0.5;
        g2d.setColor(Color.white);
        g2d.drawLine(0, 576*2/3, 768, 576*2/3);
        g2d.fillOval((int)(playerX+50*s), (int)(playerY+50*s), (int)(30*s), (int)(30*s));
        g2d.drawLine((int)(playerX+65*s), (int)(playerY+65*s), (int)(playerX+65*s), (int)(playerY+150*s));
        g2d.drawLine((int)(playerX+65*s), (int)(playerY+90*s), (int)(playerX+90*s), (int)(playerY+125*s));
        g2d.drawLine((int)(playerX+65*s), (int)(playerY+90*s), (int)(playerX+40*s), (int)(playerY+125*s));
        g2d.drawLine((int)(playerX+65*s), (int)(playerY+150*s), (int)(playerX+40*s), (int)(playerY+200*s));
        g2d.drawLine((int)(playerX+65*s), (int)(playerY+150*s), (int)(playerX+90*s), (int)(playerY+200*s));
    }

}