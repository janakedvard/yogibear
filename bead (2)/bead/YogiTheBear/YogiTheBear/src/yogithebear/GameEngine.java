
package yogithebear;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.sql.SQLException;
import java.util.logging.Level;

public class GameEngine extends JPanel {
    
    private final int FPS = 60;
    private final int YOGI_WIDTH = 50;
    private final int YOGI_HEIGHT = 80;
    private final int YOGI_MOVEMENT = 5;

    private boolean paused = false;
    private Image background;
    private int levelNum = 0;
    private int basketNum = 0;
    private Levels level;
    private Yogi yogi;
    private int HP = 3;
    private Timer newFrameTimer;
    private long startTime;
    private HighScores highScores;
    
    public GameEngine() {
        super();
        background = new ImageIcon("data/images/background.jpg").getImage();
        this.getInputMap().put(KeyStroke.getKeyStroke("W"), "pressed up");
        this.getInputMap().put(KeyStroke.getKeyStroke("released W"), "released w");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                yogi.setDirection("UP");
                yogi.setVely(-YOGI_MOVEMENT);
            }
        });
        this.getActionMap().put("released w", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                yogi.setVely(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("D"), "pressed right");
        this.getInputMap().put(KeyStroke.getKeyStroke("released D"), "released d");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                yogi.setDirection("RIGHT");
                yogi.setVelx(YOGI_MOVEMENT);
            }
        });
        this.getActionMap().put("released d", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                yogi.setVelx(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "pressed left");
        this.getInputMap().put(KeyStroke.getKeyStroke("released A"), "released a");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                yogi.setDirection("LEFT");
                yogi.setVelx(-YOGI_MOVEMENT);
            }
        });
        this.getActionMap().put("released a", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                yogi.setVelx(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "pressed down");
        this.getInputMap().put(KeyStroke.getKeyStroke("released S"), "released s");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                yogi.setDirection("DOWN");
                yogi.setVely(YOGI_MOVEMENT);
            }
        });
        this.getActionMap().put("released s", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                yogi.setVely(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        this.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                paused = !paused;
            }
        });
          try {
           highScores = new HighScores(10);
          } catch (SQLException ex) {
            Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
          restart();
          startTime = System.currentTimeMillis();
          newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
          newFrameTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(background, 0, 0, 1000, 800, null);
        yogi.draw(grphcs);
        level.draw(grphcs);
    }
    
    public void restart() {
        try {
            level = new Levels("data/levels/level" + levelNum + ".txt");
        } catch (IOException ex) {
            Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        Image yogiImage = new ImageIcon("data/images/yogi.png").getImage();
        yogi = new Yogi(450, 680, YOGI_WIDTH, YOGI_HEIGHT, yogiImage);
        yogi.setHealth(HP);
    }
    
    class NewFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused) {
                level.moveGuards();
                level.guardMovementCheck();
                if(yogi.getHealth() <= 0) {
                    levelNum = 0;
                    HP = 3;
                    yogi.setHealth(HP);
                    String name = JOptionPane.showInputDialog("Név:");
                    try {
                        highScores.putHighScore(name, basketNum);
                    } catch (SQLException ex) {
                        Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    basketNum = 0;
                    startTime = System.currentTimeMillis();
                    
                    restart();
                }
                if(level.spotted(yogi)) {
                    yogi.moveToStart();
                    HP--;
                    yogi.setHealth(-1);
                }
                switch (level.collidesTree(yogi)) {
                    case 0:
                        if(level.collidesBasket(yogi)) {
                            basketNum++;
                        }   yogi.move();
                        break;
                    case 1:
                        yogi.setVelx(1);
                        yogi.move();
                        yogi.setVelx(0);
                        break;
                    case 2:
                        yogi.setVelx(-1);
                        yogi.move();
                        yogi.setVelx(0);
                        break;
                    case 3:
                        yogi.setVely(1);
                        yogi.move();
                        yogi.setVely(0);
                        break;
                    case 4:
                        yogi.setVely(-1);
                        yogi.move();
                        yogi.setVely(0);
                        break;
                    default:
                        break;
                }
                if(level.isOver()) {
                    levelNum = (levelNum+1) % 10;
                    restart();
                }
            }
            repaint();
        }

    }
    public long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    public int getBasketNum() {
        return basketNum;
    }
    
    public int getRemainingHealth() {
        return yogi.getHealth();
    }
    
    public HighScores getHighScores() {
        return highScores;
    }
    
}