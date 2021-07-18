import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Panel extends JPanel implements Runnable {
    // screen-props
    private static final int SCREEN_HEIGHT = 600;
    private static final int SCREEN_WIDTH = 1000;

    // game-thread
    Thread gameThread;

    // paddle-props
    private static final int PADDLE_WIDTH = 15;
    private static final int PADDLE_HEIGHT = 150;
    private Paddle left;
    private Paddle right;

    // ball-props
    private static final int BALL_RADIUS = 20;
    private static final int BALL_VELOCITY_X = 5;
    private static final int BALL_VELOCITY_Y = 5;
    private Ball ball;

    // constructor
    Panel() {
        // panel-config
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        initGame();
    }

    // game-init
    private void initGame() {
        // paddles
        left = new Paddle(PADDLE_WIDTH, SCREEN_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT);
        right = new Paddle(SCREEN_WIDTH - 2 * PADDLE_WIDTH, SCREEN_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT);

        // ball
        ball = new Ball(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, BALL_RADIUS, BALL_VELOCITY_X, BALL_VELOCITY_Y);

        // key-listener
        this.addKeyListener(new MyKeyAdapter());

        // init-thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    // draw
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    private void draw(Graphics g) {
        // render-paddles
        left.render(g);
        right.render(g);

        // render-ball
        ball.render(g);
    }

    // key-listener
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> {
                    left.changeDirection('U');
                }
                case KeyEvent.VK_S -> {
                    left.changeDirection('D');
                }
                case KeyEvent.VK_UP -> {
                    right.changeDirection('U');
                }
                case KeyEvent.VK_DOWN -> {
                    right.changeDirection('D');
                }
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W, KeyEvent.VK_S -> {
                    left.changeDirection('S');
                }
                case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> {
                    right.changeDirection('S');
                }
            }
        }
    }

    // game-loop
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 120.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                // ball-movement
                ball.move();

                // paddles-movement
                left.move(SCREEN_HEIGHT);
                right.move(SCREEN_HEIGHT);

                // ball-collisions
                ball.collideWithEdges(SCREEN_WIDTH, SCREEN_HEIGHT);
                ball.collideWithPaddles(left, right);

                // repaint
                repaint();

                delta--;
            }
        }
    }

}
