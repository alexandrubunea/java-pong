import java.awt.*;

public class Paddle extends Rectangle {
    // paddle-config
    private static final int PADDLE_VELOCITY = 7;

    private final int x;
    private int y;
    private final int width;
    private final int height;
    private final int centerX;
    private int centerY;
    private int score;
    private char dir;

    // constructor
    Paddle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.centerX = x + width / 2;
        this.centerY = y + height / 2;

        this.score = 0;

        dir = 'S';
    }

    // movement
    public void move(int SCREEN_LIMIT) {
        if(this.dir == 'U' && this.y > 0) {
            this.y -= PADDLE_VELOCITY;
            this.centerY = this.y + this.height / 2;
        }
        else if(this.dir == 'D' && this.y + this.height < SCREEN_LIMIT) {
            this.y += PADDLE_VELOCITY;
            this.centerY = this.y + this.height / 2;
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

    // get-props
    public int centerX() { return this.centerX; }
    public int centerY() { return this.centerY; }
    public int height() { return this.height; }
    public int width() { return this.width; }
    public int score() { return this.score; }
}
