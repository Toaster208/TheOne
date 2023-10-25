
import java.awt.*;

import javax.swing.JPanel;

import java.util.*;


public class GameWindow extends JPanel implements Runnable {

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    int groundLevel = 307;
    int playerX = 100;
    int playerY = groundLevel;
    int playerSpeed = 4;
    // int playerDimension = 1;
    // Jump variables
    double jumpVelocity = 0; // Initial jump velocity
    double jumpAcceleration = 0.5; // Acceleration due to gravity
    boolean isJumping = false;
    ArrayList<Integer> objectsX = new ArrayList<Integer>();
    ArrayList<Integer> objectsY = new ArrayList<Integer>();

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
        //TODO: check for specific collision (top, bottom, right, left)
        // if (colliding()) {
        //     if (keyHandler.leftPressed) {
        //         playerX+=playerSpeed;
        //     } else if (keyHandler.rightPressed) {
        //         playerX-=playerSpeed;
        //     }
        //     if (keyHandler.upPressed) {
        //         jumpVelocity=1;
        //     }
        //     if(!keyHandler.upPressed){
        //         isJumping = false;
        //     }
        // }
        // if (!colliding() && ()) {

        // }
        if (keyHandler.upPressed && !isJumping) {
            // Start jumping
            isJumping = true;
            jumpVelocity = -10; // Adjust the initial jump velocity
        }

        if (isJumping) {
            // Update jump physics
            if (!keyHandler.upPressed) {
                jumpVelocity = Math.abs(jumpVelocity);
            }
            playerY += jumpVelocity;
            jumpVelocity += jumpAcceleration;
            if (keyHandler.leftPressed) {
                playerX -= playerSpeed;
            }
            if (keyHandler.rightPressed) {
                playerX += playerSpeed;
            }

            if (playerY >= groundLevel) {
                // Ground level, end the jump
                isJumping = false;
                playerY = groundLevel; // Reset to ground level
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
        double s = 0.5; //this variable doesn't work*
        g2d.setColor(Color.white);
        g2d.drawLine(0, 576*2/3, 768, 576*2/3);
        //Todo: shift playerX and playerY to corner of player
        g2d.fillOval((int)(playerX), (int)(playerY), (int)(30*s), (int)(30*s));
        g2d.drawLine((int)(playerX+15*s), (int)(playerY+15*s), (int)(playerX+15*s), (int)(playerY+100*s));
        g2d.drawLine((int)(playerX+15*s), (int)(playerY+40*s), (int)(playerX+40*s), (int)(playerY+75*s));
        g2d.drawLine((int)(playerX+15*s), (int)(playerY+40*s), (int)(playerX-10*s), (int)(playerY+75*s));
        g2d.drawLine((int)(playerX+15*s), (int)(playerY+100*s), (int)(playerX-10*s), (int)(playerY+150*s));
        g2d.drawLine((int)(playerX+15*s), (int)(playerY+100*s), (int)(playerX+40*s), (int)(playerY+150*s));

        //boundary box
        g2d.drawLine((int)(playerX-10*s), playerY, (int)(playerX+40*s), playerY);
        g2d.drawLine((int)(playerX+40*s), playerY, (int)(playerX+40*s), (int)(playerY+150*s));
        g2d.drawLine((int)(playerX+40*s), (int)(playerY+150*s), (int)(playerX-10*s), (int)(playerY+150*s));
        g2d.drawLine((int)(playerX-10*s), (int)(playerY+150*s), (int)(playerX-10*s), playerY);
        
        //platform in the middle of the screen:
        g2d.drawLine(800/4, 600/2, 800*3/4, 600/2);
        objectsX.add(800/4);
        objectsX.add(800*3/4);
        objectsY.add(600/2);
        objectsY.add(600/2);
        //objectsX.get(i) determines the start X of the platform
        //objectsX.get(i+1) determines the end X of the platform
        //objectsY.get(i) determines the start Y of the platform
        //objectsY.get(i+1) determines the end Y of the platform
    }
    public boolean colliding() {
        //Using boundary box to check for collision
        int playerLeft = (int)(playerX - 10 * 0.5);
        int playerRight = (int)(playerX + 40 * 0.5);
        int playerTop = playerY;
        int playerBottom = (int)(playerY + 150 * 0.5);

        // Check for collision with each object
        for (int i = 0; i < objectsX.size(); i += 2) {
            int objectLeft = objectsX.get(i);
            int objectRight = objectsX.get(i + 1);
            int objectTop = objectsY.get(i);
            int objectBottom = objectsY.get(i + 1);

            // Check if the boundaries overlap
            if (playerRight >= objectLeft && playerLeft <= objectRight &&
                playerBottom >= objectTop && playerTop <= objectBottom) {
                    if (playerBottom >= objectBottom) {
                        playerY = objectBottom - 76;
                    }
                return true; // Collision detected
            }
        }

        return false; // No collision detected
    }
}