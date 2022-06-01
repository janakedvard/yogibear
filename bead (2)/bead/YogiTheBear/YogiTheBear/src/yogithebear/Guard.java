
package yogithebear;

import java.awt.Image;


public class Guard extends Sprite {
    private final String way;
    private int vel;
    
    public Guard(int x, int y, int width, int height, String way ,Image image) {
        super(x, y, width, height, image);
        vel = 1;
        this.way = way;
    }
    public void moveGuard() {
        if(way == "RL") {
            x += vel;
        }
        if(way == "UD") {
            y += vel;
        }
    }
    
    public String getWay() {
        return way;
    }
    
    public void invertVel() {
        vel = -vel;
    }

}
