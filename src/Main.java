import javax.swing.*;

public class Main extends JFrame {

    // public double s = .25;
    // public static double x;
    // public static double y;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("The One");

        GameWindow gameWindow = new GameWindow();
        frame.add(gameWindow);

        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameWindow.startGameThread();
    }

    // public void drawLines(Graphics g) {
    //     Graphics2D g2d = (Graphics2D) g;
    //     g2d.fillOval((int) (225*s+x), (int) (225*s+y), (int) (50*s), (int) (50*s));
    //     g2d.setStroke(new BasicStroke((int) (5*s)));
    //     g2d.drawLine((int) (250*s+x), (int) (250*s+y), (int) (250*s+x), (int) (350*s+y));
    //     g2d.drawLine((int) (250*s+x), (int) (285*s+y), (int) (225*s+x), (int) (300*s+y));
    //     g2d.drawLine((int) (250*s+x), (int) (285*s+y), (int) (275*s+x), (int) (300*s+y));
    //     g2d.drawLine((int) (250*s+x), (int) (350*s+y), (int) (225*s+x), (int) (400*s+y));
    //     g2d.drawLine((int) (250*s+x), (int) (350*s+y), (int) (275*s+x), (int) (400*s+y));
    // }

    
}
