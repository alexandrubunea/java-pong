import java.awt.*;
import java.util.Random;

public class Ball extends Rectangle {
    // ball-props
    private int x;
    private int y;
    private int centerX;
    private int centerY;
    private int speedX;
    private int speedY;
    private final int initX;
    private final int initY;
    private final int velocityX;
    private final int velocityY;
    private final int radius;

    // constructor
    Ball(int x, int y, int radius, int velocityX, int velocityY) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocityX = velocityX;
        this.velocityY = velocityY;

        this.centerX = this.x + radius / 2;
        this.centerY = this.y + radius / 2;

        this.initX = x;
        this.initY = y;

        double angle = Math.toRadians(Math.random() * 60);
        Random random = new Random();
        int chance = random.nextInt(100);
        if(chance <= 50) this.speedX = (int) (velocityX * Math.cos(angle));
        else this.speedX = (int) -(velocityX * Math.cos(angle));
        chance = random.nextInt(100);
        if(chance <= 50) this.speedY = (int) (velocityY * Math.sin(angle));
        else this.speedY = (int) -(velocityY * Math.sin(angle));
    }

    // movement
    public void move() {
        this.x += this.speedX;
        this.y += this.speedY;

        this.centerX = this.x + this.radius / 2;
        this.centerY = this.y + this.radius / 2;
    }

    // reset-position
    public void reset() {
        this.x = this.initX;
        this.y = this.initY;

        this.centerX = this.x + this.radius / 2;
        this.centerY = this.y + this.radius / 2;

        double angle = Math.toRadians(Math.random() * 60);
        Random random = new Random();
        int chance = random.nextInt(100);
        if(chance <= 50) this.speedX = (int) (velocityX * Math.cos(angle));
        else this.speedX = (int) -(velocityX * Math.cos(angle));
        chance = random.nextInt(100);
        if(chance <= 50) this.speedY = (int) (velocityY * Math.sin(angle));
        else this.speedY = (int) -(velocityY * Math.sin(angle));
    }

    // render
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(this.x, this.y, this.radius, this.radius);
    }

    // collisions-with-the-edges
    public void collide(int SCREEN_LIMIT_HEIGHT, Paddle left, Paddle right) {
        // Y Axis
        if(this.centerY - this.radius / 2 <= 0 && this.speedY < 0) this.speedY = -this.speedY;
        if(this.centerY + this.radius / 2 >= SCREEN_LIMIT_HEIGHT && this.speedY > 0) this.speedY = -this.speedY;
        // X Axis
        if(this.centerX - this.radius / 2 <= left.centerX() - left.width() / 2 && this.speedX < 0){
            reset();
            right.increaseScore();
        }
        if(this.centerX + this.radius / 2 >= right.centerX() + right.width() / 2 && this.speedX > 0){
            reset();
            left.increaseScore();
        }

        // with the paddles
        // left paddle
        if(this.centerX - this.radius / 2 <= left.centerX() + left.width() / 2 &&
                this.centerY + this.radius / 2 > left.centerY() - left.height() / 2 &&
                this.centerY - this.radius / 2 < left.centerY() + left.height() / 2 &&
                this.speedX < 0) {
            double offsetY = left.centerY() - this.centerY;
            double ratioY = offsetY / ((double) left.height() / 2);
            double bounceAngle = Math.toRadians(75 * ratioY);
            this.speedX = (int) (this.velocityX * Math.cos(bounceAngle));
            this.speedY = (int) -(this.velocityY * Math.sin(bounceAngle));
            if(this.speedX == 0) this.speedX = this.velocityX;

        }
        // right paddle
        if(this.centerX + this.radius / 2 >= right.centerX() - right.width() / 2 &&
                this.centerY + this.radius / 2 > right.centerY() - right.height() / 2 &&
                this.centerY - this.radius / 2 < right.centerY() + right.height() / 2 &&
                this.speedX > 0) {
            double offsetY = right.centerY() - this.centerY;
            double ratioY = offsetY / ((double) right.height() / 2);
            double bounceAngle = Math.toRadians(75 * ratioY);
            this.speedX = (int) -(this.velocityX * Math.cos(bounceAngle));
            this.speedY = (int) -(this.velocityY * Math.sin(bounceAngle));
            if(this.speedX == 0) this.speedX = -this.velocityX;
        }
    }

    // get-props
    public int centerY() { return this.centerY; }
    public int centerX() { return this.centerX; }
    public int height() { return this.height; }
    public int width() { return this.width; }
    public int speedX() { return this.speedX; }
}
