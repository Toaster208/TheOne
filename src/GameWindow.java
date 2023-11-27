
import java.awt.*;

import javax.swing.JPanel;

import java.util.*;


public class GameWindow extends JPanel implements Runnable {

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    int groundLevel = 325;
    int playerX = 100;
    int playerY = groundLevel;
    int playerSpeed = 4; //Tweak speed later
    // int playerDimension = 1;
    // Jump variables
    double jumpVelocity = 0; // Initial jump velocity
    double jumpAcceleration = .75; // Acceleration due to gravity
    boolean isJumping = false;
    ArrayList<Rectangle> objects = new ArrayList<Rectangle>();

    public GameWindow() {
        this.setPreferredSize(new Dimension(800, 600));
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
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }
    }

    public void update() {
        // check closest ground to land on: TODO:(make this its own function later)

        // arrange from highest to lowest first
        int tempY = groundLevel - 75; //CLOSE TO FIGURING IT OUT
        for (Rectangle object : objects) { //disregards player falling off an object
            if (playerX >= object.getX() && playerX <= object.getX()+object.getWidth()) {
                if (object.getY() < tempY) {
                    tempY = (int) object.getY();
                    groundLevel = (int) object.getY() - 75;
                    System.out.println(groundLevel);
                }
            }
        }
        // ----------------------------------------

        if (keyHandler.upPressed && !isJumping) {
            // Start jumping
            isJumping = true;
            jumpVelocity = -15; // Adjust the initial jump velocity
        }

        if (isJumping) {
            // Update jump physics
            if (!keyHandler.upPressed) {
                jumpVelocity = Math.abs(jumpVelocity);
            }
            playerY += jumpVelocity;
            jumpVelocity += jumpAcceleration;

            if (playerY >= groundLevel) {
                // Ground level, end the jump
                isJumping = false;
                playerY = groundLevel; // Reset to ground level
            }

        } else if (!collide() && !isJumping) {
            for (Rectangle object : objects) {
                if (playerY + 10 > object.y) {
                    isJumping = true;
                    jumpVelocity = 0;
                }
            }
        }
        

        if (keyHandler.leftPressed) {
            playerX -= playerSpeed;
            while (collide()) {
                playerX += 1;
            }
        }
        if (keyHandler.rightPressed) {
            playerX += playerSpeed;
            while (collide()) {
                playerX -= 1;
            }
        }
    }

    //TODO: Make a level

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.drawLine(0, 400, 800, 400);
        //construction of player character
        g2d.fillOval((playerX), (playerY), 15, 15);
        g2d.drawLine((playerX + 7), (playerY + 7), (playerX + 7), (playerY + 50));
        g2d.drawLine((playerX + 7), (playerY + 20), (playerX + 20), (playerY + 37));
        g2d.drawLine((playerX + 7), (playerY + 20), (playerX - 5), (playerY + 37));
        g2d.drawLine((playerX + 7), (playerY + 50), (playerX - 5), (playerY + 75));
        g2d.drawLine((playerX + 7), (playerY + 50), (playerX + 20), (playerY + 75));
        

        // bounding box (for visual)
        // g2d.drawLine((playerX - 5), playerY, (playerX + 20), playerY);
        // g2d.drawLine((playerX + 20), playerY, (playerX + 20), (playerY + 75));
        // g2d.drawLine((playerX + 20), (playerY + 75), (playerX - 5), (playerY + 75));
        // g2d.drawLine((playerX - 5), (playerY + 75), (playerX - 5), playerY);
        //TODO: player looks like it is far away from wall when colliding, slightly make bigger to compensate.
        
        // addAndDrawLine(200, 300, 600, 300, g);
        // addAndDrawLine(200, 300, 200, 400, g);
        g2d.drawRect(200, 300, 400, 100);
        objects.add(new Rectangle(200, 300, 400, 100));
        g2d.drawRect(380, 280, 40, 40);
        objects.add(0, new Rectangle(380, 280, 40, 40));
    }

    private boolean collide() {
        Rectangle playerBox = new Rectangle(playerX, playerY, 25, 75);
        for (Rectangle object : objects) {
            if (object.intersects(playerBox)) {
                return true;
            }
        }
        return false;
    }
}