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
        left = new Paddle(PADDLE_WIDTH, SCREEN_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT, true, "left");
        right = new Paddle(SCREEN_WIDTH - 2 * PADDLE_WIDTH, SCREEN_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT, true, "right");

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
    private void renderScore(Graphics g, Paddle p1, Paddle p2) {
        if(!p1.ai() || !p2.ai()) {
            g.setColor(Color.WHITE);
            String score_left = Integer.toString(p1.score());
            String score_right = Integer.toString(p2.score());

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(score_left, SCREEN_WIDTH / 2 - 50, 40);
            g.drawString(score_right, SCREEN_WIDTH / 2 + 30, 40);
        }
    }
    private void renderInfoText(Graphics g, Paddle p1, Paddle p2) {
        if(p1.ai() && p2.ai()) {
            g.setColor(Color.ORANGE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("PONG", SCREEN_WIDTH / 2 - 60, SCREEN_HEIGHT / 2 - 180);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("To start playing take control over one of the paddles!", SCREEN_WIDTH / 2 - 180, SCREEN_HEIGHT / 2 - 150);
            g.drawString("To take control over the left paddle press W or S", SCREEN_WIDTH / 2 - 180, SCREEN_HEIGHT / 2 - 130);
            g.drawString("and for the right one press UP or DOWN",SCREEN_WIDTH / 2 - 180, SCREEN_HEIGHT / 2 - 110);
            g.drawString("You can take control over the both of them if you want!", SCREEN_WIDTH / 2 - 180, SCREEN_HEIGHT / 2 - 90);
        }
    }
    private void renderCenterLine(Graphics g, Paddle p1, Paddle p2) {
        if(!p1.ai() || !p2.ai()) {
            g.setColor(Color.WHITE);
            for (int i = 0; i <= 19; i++) g.fillRect(SCREEN_WIDTH / 2 - 5, 15 * 2 * i, 5, 15);
        }
    }
    private void draw(Graphics g) {
        // render-paddles
        left.render(g);
        right.render(g);

        // render-ball
        ball.render(g);

        // render-center-line
        renderCenterLine(g, left, right);

        // render-score
        renderScore(g, left, right);

        // render-info-text
        renderInfoText(g, left, right);
    }

    // key-listener
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> {
                    if(left.ai()) left.takeControl();
                    left.changeDirection('U');
                }
                case KeyEvent.VK_S -> {
                    if(left.ai()) left.takeControl();
                    left.changeDirection('D');
                }
                case KeyEvent.VK_UP -> {
                    if(right.ai()) right.takeControl();
                    right.changeDirection('U');
                }
                case KeyEvent.VK_DOWN -> {
                    if(right.ai()) right.takeControl();
                    right.changeDirection('D');
                }
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W, KeyEvent.VK_S -> left.changeDirection('S');
                case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> right.changeDirection('S');
            }
        }
    }

    // game-loop
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 144.0;
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
                left.move(SCREEN_HEIGHT, SCREEN_WIDTH, ball);
                right.move(SCREEN_HEIGHT, SCREEN_WIDTH, ball);

                // ball-collisions
                ball.collide(SCREEN_HEIGHT, left, right);

                // repaint
                repaint();

                delta--;
            }
        }
    }

}
