
import java.awt.*;

import javax.swing.JPanel;

import java.util.*;


public class GameWindow extends JPanel implements Runnable {

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    int groundLevel = 325;
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
        //FIXME: collLeft and collRight detect at the same time, nullifying the collison. fix their if statements.

        //Big change: TODO: make (and work on) a whole new class for objects/terrain
        // if (collTop()) {
        //     jumpVelocity = 1;
        // }

        if (collBottom()) {
            isJumping = false;
        }

        if (collLeft()) {
            playerX+=playerSpeed;
        } else if (collTop()) {
            jumpVelocity = -10;
        }

        if (collRight()) {
            playerX-=playerSpeed;
        } else if (collTop()) {
            jumpVelocity = 1;
        }
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
    g2d.setColor(Color.white);
    g2d.drawLine(0, 400, 800, 400);
    g2d.fillOval((playerX), (playerY), 15, 15);
    g2d.drawLine((playerX + 7), (playerY + 7), (playerX + 7), (playerY + 50));
    g2d.drawLine((playerX + 7), (playerY + 20), (playerX + 20), (playerY + 37));
    g2d.drawLine((playerX + 7), (playerY + 20), (playerX - 5), (playerY + 37));
    g2d.drawLine((playerX + 7), (playerY + 50), (playerX - 5), (playerY + 75));
    g2d.drawLine((playerX + 7), (playerY + 50), (playerX + 20), (playerY + 75));

    g2d.drawLine((playerX - 5), playerY, (playerX + 20), playerY);
    g2d.drawLine((playerX + 20), playerY, (playerX + 20), (playerY + 75));
    g2d.drawLine((playerX + 20), (playerY + 75), (playerX - 5), (playerY + 75));
    g2d.drawLine((playerX - 5), (playerY + 75), (playerX - 5), playerY);
    
    addAndDrawLine(200, 300, 600, 300, g);
    addAndDrawLine(200, 300, 200, 400, g);
}


private boolean collLeft() {
    Rectangle playerBoundingBox = new Rectangle(playerX - 5, playerY, 25, 75); // Assuming player's bounding box dimensions are 25x75

    for (int i = 0; i < objectsX.size(); i += 2) {
        int objectLeft = objectsX.get(i);
        int objectRight = objectsX.get(i + 1);
        int objectTop = objectsY.get(i);
        int objectBottom = objectsY.get(i + 1);

        Rectangle objectBoundingBox = new Rectangle(objectLeft, objectTop, objectRight - objectLeft+1, objectBottom - objectTop+1);

        if (playerBoundingBox.intersects(objectBoundingBox)) {
            if (playerBoundingBox.getMaxX() >= objectBoundingBox.getMinX()) {
                System.out.println("left");
                return true;
            }
        }
    }

    return false; // No collision detected
}

private boolean collRight() {
    Rectangle playerBoundingBox = new Rectangle(playerX - 5, playerY, 25, 75); // Assuming player's bounding box dimensions are 25x75

    for (int i = 0; i < objectsX.size(); i += 2) {
        int objectLeft = objectsX.get(i);
        int objectRight = objectsX.get(i + 1);
        int objectTop = objectsY.get(i);
        int objectBottom = objectsY.get(i + 1);

        Rectangle objectBoundingBox = new Rectangle(objectLeft, objectTop, objectRight - objectLeft+1, objectBottom - objectTop+1);

        if (playerBoundingBox.intersects(objectBoundingBox)) {
            if (playerBoundingBox.getMinX() <= objectBoundingBox.getMaxX()) {
                System.out.println("right");
                return true;
            }
        }
    }

    return false; // No collision detected
}

private boolean collTop() {
    Rectangle playerBoundingBox = new Rectangle(playerX - 5, playerY, 25, 75); // Assuming player's bounding box dimensions are 25x75

    for (int i = 0; i < objectsX.size(); i += 2) {
        int objectLeft = objectsX.get(i);
        int objectRight = objectsX.get(i + 1);
        int objectTop = objectsY.get(i);
        int objectBottom = objectsY.get(i + 1);

        Rectangle objectBoundingBox = new Rectangle(objectLeft, objectTop, objectRight - objectLeft+1, objectBottom - objectTop+1);

        if (playerBoundingBox.intersects(objectBoundingBox)) {
            if (playerBoundingBox.getMaxY() >= objectBoundingBox.getMinY()) {
                playerY+=1;
                System.out.println("top");
                return true;
            }
        }
    }

    return false; // No collision detected
}

private boolean collBottom() {
    Rectangle playerBoundingBox = new Rectangle(playerX - 5, playerY, 25, 75); // Assuming player's bounding box dimensions are 25x75

    for (int i = 0; i < objectsX.size(); i += 2) {
        int objectLeft = objectsX.get(i);
        int objectRight = objectsX.get(i + 1);
        int objectTop = objectsY.get(i);
        int objectBottom = objectsY.get(i + 1);

        Rectangle objectBoundingBox = new Rectangle(objectLeft, objectTop, objectRight - objectLeft+1, objectBottom - objectTop+1);

        if (playerBoundingBox.intersects(objectBoundingBox)) {
            if (playerBoundingBox.getMinY() <= objectBoundingBox.getMaxY()) {
                playerY-=1;
                System.out.println("bottom");
                return true; // Collision detected on the bottom side
            }
        }
    }

    return false; // No collision detected
}

    // private boolean collLeft() {
    //     int playerLeft = playerX-5;
    //     for (int i = 0; i < objectsX.size(); i += 2) {
    //         int objectRight = objectsX.get(i + 1);
    //         if (playerLeft <= objectRight) {
    //             return true; // Collision detected
    //         }
    //     }
    //     return false; // No collision detected
    // }


    
    // private boolean collRight() {
    //     int playerRight = playerX+20;
    //     for (int i = 0; i < objectsX.size(); i += 2) {
    //         int objectLeft = objectsX.get(i);
    //         if (playerRight >= objectLeft) {
    //             return true; // Collision detected
    //         }
    //     }
    //     return false; // No collision detected
    // }

    // private boolean collTop() {
    //     int playerTop = playerY;
    //     for (int i = 0; i < objectsY.size(); i += 2) {
    //         int objectBottom = objectsY.get(i + 1);
    //         if (playerTop <= objectBottom) {
    //             return true; // Collision detected
    //         }
    //     }
    //     return false; // No collision detected
    // }

    // private boolean collBottom() {
    //     int playerBottom = playerY+75;
    //     for (int i = 0; i < objectsY.size(); i += 2) {
    //         int objectTop = objectsY.get(i);
    //         if (playerBottom >= objectTop) {
    //             return true; // Collision detected
    //         }
    //     }
    //     return false; // No collision detected
    // }

    private void addAndDrawLine(int x1, int y1, int x2, int y2, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawLine(x1, y1, x2, y2);
        objectsX.add(x1);
        objectsX.add(x2);
        objectsY.add(y1);
        objectsY.add(y2);
    }

    private Point getIntersectionPoint(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int denominator = ((x2 - x1) * (y4 - y3)) - ((y2 - y1) * (x4 - x3));
    
        if (denominator == 0) {
            return null; // Lines are parallel, no intersection point
        }
    
        int numerator1 = ((y1 - y3) * (x4 - x3)) - ((x1 - x3) * (y4 - y3));
        int numerator2 = ((y1 - y3) * (x2 - x1)) - ((x1 - x3) * (y2 - y1));
    
        if (numerator1 == 0 || numerator2 == 0) {
            return null; // Lines are collinear, no intersection point
        }
    
        float r = (float) numerator1 / denominator;
        float s = (float) numerator2 / denominator;
    
        if (r >= 0 && r <= 1 && s >= 0 && s <= 1) {
            int x = x1 + (int) (r * (x2 - x1));
            int y = y1 + (int) (r * (y2 - y1));
            return new Point(x, y); // Intersection point
        }
    
        return null; // Lines do not intersect within the given segments
    }
}