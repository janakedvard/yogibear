
package yogithebear;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.Rectangle;

public class Levels {
    private final int UNIT = 50;
    
    ArrayList<Basket> baskets;
    ArrayList<Tree> trees;
    ArrayList<Guard> guards;
    
    public Levels(String levelPath) throws IOException {
        loadLevel(levelPath);
    }
    
    public void loadLevel(String levelPath) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(levelPath));
        baskets = new ArrayList<>();
        trees = new ArrayList<>();
        guards = new ArrayList<>();
        int y = 0;
        String line;
        while ((line = br.readLine()) != null) {
            int x = 0;
            for (char blockType : line.toCharArray()) {
                if (Character.isAlphabetic(blockType)) {
                    if(blockType == 'b') {
                        Image image = new ImageIcon("data/images/tree.png").getImage();
                        trees.add(new Tree(x * UNIT, y * UNIT, UNIT, UNIT, image));
                    }
                    if(blockType == 'p') {
                        Image image = new ImageIcon("data/images/picnic_basket.png").getImage();
                        baskets.add(new Basket(x * UNIT, y * UNIT, UNIT, UNIT, image));
                    }
                    if(blockType == 'x') {
                        Image image = new ImageIcon("data/images/guard.png").getImage();
                        guards.add(new Guard(x * UNIT, y * UNIT, UNIT, UNIT, "RL", image));
                    }
                    if(blockType == 'y') {
                        Image image = new ImageIcon("data/images/guard.png").getImage();
                        guards.add(new Guard(x * UNIT, y * UNIT, UNIT, UNIT, "UD", image));
                    }
                }
                x++;
            }
            y++;
        }
    }
    
    public int collidesTree(Yogi yogi) {
        Rectangle yogiRectangle = new Rectangle(yogi.x, yogi.y, yogi.width, yogi.height);
        for (Tree tree : trees) {
            Rectangle treeRectangle = new Rectangle(tree.x, tree.y, tree.width, tree.height);
            if(yogiRectangle.intersects(treeRectangle)) {
                Rectangle overlap = yogiRectangle.intersection(treeRectangle);
                if(overlap.height >= overlap.width) {
                    if(yogi.getDirection() == "LEFT") {
                        return 1;
                    }
                    if(yogi.getDirection() == "RIGHT") {
                        return 2;
                    }
                } if(overlap.width >= overlap.height) {
                    if(yogi.getDirection() == "UP") {
                        return 3;
                    }
                    if(yogi.getDirection() == "DOWN") {
                        return 4;
                    }
                } 
            }
        }
        return 0;
    }
    
    public boolean collidesBasket(Yogi yogi) {
        Basket collidedWith = null;
        for (Basket basket : baskets) {
            if (yogi.collides(basket)) {
                collidedWith = basket;
                break;
            }
        }
        if (collidedWith != null) {
            baskets.remove(collidedWith);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean spotted(Yogi yogi) {
        Rectangle yogiRectangle = new Rectangle(yogi.x, yogi.y, yogi.width, yogi.height);
        for(Guard guard : guards) {
            Rectangle guardRectangle = new Rectangle(guard.x, guard.y, guard.width + UNIT, guard.height + UNIT);
            if(yogiRectangle.intersects(guardRectangle)) {
                return true;
            }
        }
        return false;
    }
    
    public void guardMovementCheck() {
        for(Tree tree : trees) {
            for(Guard guard : guards) {
                if(guard.collides(tree)) {
                    guard.invertVel();
                }
            }
        }
    }
    
    public void moveGuards() {
        for(Guard guard : guards) {
            guard.moveGuard();
        }
    }
    
    public boolean isOver() {
        return baskets.isEmpty();
    }
    
    public void draw(Graphics g) {
        for (Tree tree : trees) {
            tree.draw(g);
        }
        for(Basket basket : baskets) {
            basket.draw(g);
        }
        for (Guard guard : guards) {
            guard.draw(g);
        }
    }
    
}
