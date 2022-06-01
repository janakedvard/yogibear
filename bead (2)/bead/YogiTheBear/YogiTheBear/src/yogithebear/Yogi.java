
package yogithebear;

import java.awt.Image;

public class Yogi extends Sprite{
    
    private int velx;
    private int vely;
    private String direction;
    private int health;
    private final int START_X = 450;
    private final int START_Y = 680;

    public Yogi(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }

    public void move() {
        if ((velx < 0 && x > 0) || (velx > 0 && x + width <= 980)) {
            x += velx;
        }
        if ((vely < 0 && y > 0) || (vely > 0 && y + height <= 760)) {
            y += vely;
        }
    }

    public int getVelx() {
        return velx;
    }

    public void setVelx(int velx) {
        this.velx = velx;
    }
    
    public int getVely() {
        return velx;
    }

    public void setVely(int vely) {
        this.vely = vely;
    }
    
    public void invertVelX() {
        velx = -velx;
    }

    public void invertVelY() {
        vely = -vely;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public void setHealth(int num) {
        health += num;
    }
    
    public int getHealth() {
        return health;
    }
    
    public void moveToStart() {
        x = START_X;
        y = START_Y;
    }
}
