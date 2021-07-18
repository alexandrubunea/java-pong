import java.awt.*;

public class Paddle extends Rectangle {
    // paddle-config
    private static final int PADDLE_VELOCITY = 7;

    private final int x;
    private int y;
    private final int width;
    private final int height;
    private final int centerX;
    private boolean ai;
    private int centerY;
    private int score;
    private final boolean position;
    private char dir;

    // constructor
    Paddle(int x, int y, int width, int height, boolean ai, String position) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.ai = ai;

        this.centerX = x + width / 2;
        this.centerY = y + height / 2;

        this.position = !position.equals("left");

        this.score = 0;

        dir = 'S';
    }

    // movement
    public void move(int SCREEN_LIMIT_HEIGHT, int SCREEN_LIMIT_WIDTH, Ball ball) {
        if(!this.ai) {
            if (this.dir == 'U' && this.y > 0) {
                this.y -= PADDLE_VELOCITY;
                this.centerY = this.y + this.height / 2;
            } else if (this.dir == 'D' && this.y + this.height < SCREEN_LIMIT_HEIGHT) {
                this.y += PADDLE_VELOCITY;
                this.centerY = this.y + this.height / 2;
            }
        } else {
            // this.height / 4 - because we want a small error to make everything more fun
            if(!(this.centerY - this.height / 4 <= ball.centerY() + ball.height / 2 && this.centerY + this.height / 4 >= ball.centerY() - ball.height() / 2)) {
                if((this.position && ball.centerX() + ball.width() / 2 >= SCREEN_LIMIT_WIDTH / 2 && ball.speedX() > 0) || (!this.position && ball.centerX() - ball.width() / 2 <= SCREEN_LIMIT_WIDTH / 2 && ball.speedX() < 0)) {
                    if (this.centerY >= ball.centerY() && this.y > 0) {
                        this.y -= PADDLE_VELOCITY;
                        this.centerY = this.y + this.height / 2;
                    } else if (this.centerY <= ball.centerY() && this.y + this.height < SCREEN_LIMIT_HEIGHT) {
                        this.y += PADDLE_VELOCITY;
                        this.centerY = this.y + this.height / 2;
                    }
                }
            }
        }
    }
    public void changeDirection(char dir) { this.dir = dir; }

    // render
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(this.x, this.y, this.width, this.height);
    }

    // score
    public void increaseScore() { this.score ++; }

    // take-the-control-over-ai-paddle
    public void takeControl() { this.ai = !this.ai; }

    // get-props
    public int centerX() { return this.centerX; }
    public int centerY() { return this.centerY; }
    public int height() { return this.height; }
    public int width() { return this.width; }
    public int score() { return this.score; }
    public boolean ai() { return this.ai; }
}
