
package yogithebear;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;


public class YogiTheBearGUI {
        private JFrame frame;
        private GameEngine gameArea;
        private JPanel sidePanel;
        private Timer timer;
        
        
        public YogiTheBearGUI() {
        frame = new JFrame("Maci Laci");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Menü");
        menuBar.add(gameMenu);
        JMenuItem newGameItem = new JMenuItem("Új játék");
        gameMenu.add(newGameItem);
        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                frame.getContentPane().remove(gameArea);
                frame.getContentPane().remove(sidePanel);
                gameArea = new GameEngine();
                newSideLabel();
                frame.getContentPane().add(gameArea, BorderLayout.CENTER);
                frame.getContentPane().add(sidePanel, BorderLayout.SOUTH);
                frame.pack();
            }
        });
        JMenuItem menuHighScores = new JMenuItem(new AbstractAction("Dicsőség tábla") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new HighScoreWindow(gameArea.getHighScores().getHighScores(), frame);
                } catch (SQLException ex) {
                    Logger.getLogger(YogiTheBearGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        gameMenu.add(menuHighScores);
        
        gameArea = new GameEngine();
        newSideLabel();
        
        frame.getContentPane().add(gameArea, BorderLayout.CENTER);
        frame.getContentPane().add(sidePanel, BorderLayout.SOUTH);
        
        frame.setPreferredSize(new Dimension(1000, 900));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
        
    private void newSideLabel() {
        JLabel timeLabel = new JLabel(" ");
        JLabel scoreLabel = new JLabel(" ");
        JLabel healthLabel = new JLabel(" ");
        timeLabel.setHorizontalAlignment(JLabel.LEFT);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        healthLabel.setHorizontalAlignment(JLabel.LEFT);
        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText( ("Eltelt idő: " +  (int) gameArea.elapsedTime() / 1000) + " mp");
                scoreLabel.setText("Begyűjtött kosarak száma: " +  gameArea.getBasketNum());
                healthLabel.setText("Maradék életek száma: " + gameArea.getRemainingHealth() +  "   ");
                
            }
        });
        timer.start();
        sidePanel = new JPanel( new BorderLayout());
        sidePanel.add(timeLabel, BorderLayout.WEST);
        sidePanel.add(scoreLabel, BorderLayout.CENTER);
        sidePanel.add(healthLabel, BorderLayout.EAST);
    }
    
}
