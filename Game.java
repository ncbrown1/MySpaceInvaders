import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {
    private BufferStrategy strategy;
    private boolean gameRunning = true;
    private ArrayList entities = new ArrayList();
    private ArrayList removeList = new ArrayList();
    private Entity ship;
    private SpriteStore store = new SpriteStore();
    private double moveSpeed = 300;
    private long lastFire = 0;
    private long firingInterval = 350;
    private long aFiringInterval = 2000;
    private int alienCount, fps;
    private int levelNum = 1;

    private String message = "W-Up, S-Down, A-Left, D-Right";
    private String message2 = "Click to Shoot. Good Luck!";
    private boolean waitingForKeyPress = true;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean firePressed = false;
    private boolean bump = false;

    public Game() {
        JFrame container = new JFrame("Space Invaders Final Project --- Nick Brown");

        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);

        setBounds(0,0,800,600);
        panel.add(this);

        setIgnoreRepaint(true);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);
        container.setLocationRelativeTo(null);

        container.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        InputHandler input = new InputHandler();
        addKeyListener(input);
        addMouseListener(input);       

        requestFocus();

        createBufferStrategy(2);
        strategy = getBufferStrategy();

        initEntities();
    }

    private void startGame() {
        entities.clear();
        initEntities();

        leftPressed = false;
        rightPressed = false;
        firePressed = false;
    }
    
    private void startAgain(int lives) {
        entities.clear();
        initEntities(lives);

        leftPressed = false;
        rightPressed = false;
        firePressed = false;        
    }
    
    private void initEntities(int lives) {
        ship = new ShipEntity(this,"res/ship.gif",370,550);
        entities.add(ship);
        ((ShipEntity)ship).setLives(lives);

        Level level = new Level(this);
        if(levelNum == 1) 
            level.level1();
        else if(levelNum == 2)
            level.level2();
        else if(levelNum == 3)
            level.level3();
        else if(levelNum == 4)
            level.level4();
        else if(levelNum == 5)
            level.level5();
        else if(levelNum == 6)
            level.level6();
        alienCount = level.getAlienCount();
    }

    private void initEntities() {
        ship = new ShipEntity(this,"res/ship.gif",370,550);
        entities.add(ship);

        Level level = new Level(this);
        if(levelNum == 1) 
            level.level1();
        else if(levelNum == 2)
            level.level2();
        else if(levelNum == 3)
            level.level3();
        else if(levelNum == 4)
            level.level4();
        else if(levelNum == 5)
            level.level5();
        else if(levelNum == 6)
            level.level6();
        alienCount = level.getAlienCount();
    }

    public void addToEntities(Entity ent) {
        entities.add(ent);
    }

    public void updateBump() {
        bump = true;
    }

    public void removeEntity(Entity entity) {
        removeList.add(entity);
    }

    public void notifyDeath() {
        if(((ShipEntity)ship).getLives() >= 0) {
            message = "Oh no! They got you! You have " + ((ShipEntity)ship).getLives() + " lives left";
            message2 = "Want to try again?";
            waitingForKeyPress = true;
        } else if(((ShipEntity)ship).getLives() == -1) {
            message = "Oh no! They got you! You're all out of lives!";
            message2 = "Want to try all over again?";
            levelNum = 1;
            waitingForKeyPress = true;
        }
    }

    public void notifyWin() {
        if(levelNum == 1) {
            message = "Well done! You have eliminated the first wave of enemies!";
            message2 = "Oh no! Here comes more! These ones seem a bit more sturdy.";
            levelNum++;
        } else if(levelNum == 2) {
            message = "Congratulations! You cleared the second wave of aliens!";
            message2 = "Unfortunately, here comes even more. And a battleship!!";
            levelNum++;
        } else if(levelNum == 3) {
            message = "Yes! You have eliminated the third wave!";
            message2 = "Sounds like there may be a few more to kill. And TWO battleships!!";
            levelNum++;
        } else if(levelNum == 4) {
            message = "Nice! You're getting pretty good at this!";
            message2 = "But here comes three more battleships!!";
            levelNum++;
        } else if(levelNum == 5) {
            message = "Fantastic! You've even beaten the fifth wave!";
            message2 = "Uh oh... Here comes the big boss. Good Luck!";
            levelNum++;
        } else if(levelNum == 6) {
            message = "That's amazing! You have single-handedly taken down an entire army of aliens!";
            message2 = "Want to play again?";
            levelNum = 1;
        }
        waitingForKeyPress = true;
    }

    public void notifyAlienKilled() {
        alienCount--;

        if (alienCount == 0) {
            notifyWin();
        }

        for (int i=0;i<entities.size();i++) {
            Entity entity = (Entity) entities.get(i);

            if (entity instanceof AlienEntity || entity instanceof MotherShipEntity) {
                entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.01);
            }
        }
    }

    public void tryToFire() {
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        lastFire = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity((ShipEntity)ship, this,"res/shot.gif",ship.getX()+10,ship.getY()-30);
        entities.add(shot);
    }

    public void msTryToFire(MotherShipEntity alien) {        
        if (System.currentTimeMillis() - alien.getLastShot() < aFiringInterval) {
            return;
        }

        alien.setLastShot(System.currentTimeMillis());
        AlienShotEntity shot1 = new AlienShotEntity(alien, this, "res/alienShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) / 4 - 10, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot2 = new AlienShotEntity(alien, this, "res/alienShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth())  / 2 - 10, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot3 = new AlienShotEntity(alien, this, "res/alienShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) * 3 / 4 - 10, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot4 = new AlienShotEntity(alien, this, "res/alienShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) - 10, alien.getY() + alien.getSprite().getHeight() + 30);
        entities.add(shot1);
        entities.add(shot2);
        entities.add(shot3);
        entities.add(shot4);
    }

    public void bTryToFire(BossEntity alien) {
        if (System.currentTimeMillis() - alien.getLastShot() < aFiringInterval) {
            return;
        }

        alien.setLastShot(System.currentTimeMillis());
        AlienShotEntity shot1 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) / 12 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot2 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) / 6 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot3 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) / 4 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot4 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) / 3 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot5 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) * 5 / 12 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot6 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) / 2 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot7 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) * 7 / 12 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot8 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) * 2 / 3 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot9 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) * 3 / 4 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot10 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) * 5 / 6 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot11 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) * 11 / 12 + 5, alien.getY() + alien.getSprite().getHeight() + 30);
        AlienShotEntity shot12 = new AlienShotEntity(alien, this, "res/bossShot.gif", (alien.getX() + 
                    alien.getSprite().getWidth()) - 5, alien.getY() + alien.getSprite().getHeight() + 30);
        entities.add(shot1);
        entities.add(shot2);
        entities.add(shot3);
        entities.add(shot4);
        entities.add(shot5);
        entities.add(shot6);
        entities.add(shot7);
        entities.add(shot8);
        entities.add(shot9);
        entities.add(shot10);
        entities.add(shot11);
        entities.add(shot12);
    }

    public void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();        
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;

        while (gameRunning) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprocessedSeconds += passedTime / 1000000000.0;
            requestFocus();

            while(unprocessedSeconds > secondsPerTick) {
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if(tickCount % 60 == 0){
                    fps = frames;
                    previousTime += 1000;
                    frames = 0;
                }
                if(ticked) {
                    frames++;
                    long delta = System.currentTimeMillis() - lastLoopTime;
                    lastLoopTime = System.currentTimeMillis();

                    Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                    g.setColor(Color.BLACK);
                    g.fillRect(0,0,800,600);

                    g.setColor(Color.GRAY);
                    g.setFont(new Font("Verdana",0,15));
                    g.drawString("Current Level: " + levelNum, 10, 20);
                    g.drawString("Aliens left: " + alienCount, 10, 45);
                    g.drawString("Lives left: ", 635, 45);
                    g.drawString("FPS: " + fps, 725, 20);
                    Sprite sprite = store.getSprite("res/shipClip.gif");
                    Sprite dead = store.getSprite("res/deadShipClip.gif");
                    if(((ShipEntity)ship).getLives() == 5) {
                        sprite.draw(g, 715, 35);
                        sprite.draw(g, 730, 35);
                        sprite.draw(g, 745, 35);
                        sprite.draw(g, 760, 35);
                        sprite.draw(g, 775, 35);
                    } else if(((ShipEntity)ship).getLives() == 4) {                        
                        sprite.draw(g, 715, 35);
                        sprite.draw(g, 730, 35);
                        sprite.draw(g, 745, 35);
                        sprite.draw(g, 760, 35);
                        dead.draw(g, 775, 35);
                    } else if(((ShipEntity)ship).getLives() == 3) {
                        sprite.draw(g, 715, 35);
                        sprite.draw(g, 730, 35);
                        sprite.draw(g, 745, 35);
                        dead.draw(g, 760, 35);
                        dead.draw(g, 775, 35);                        
                    } else if(((ShipEntity)ship).getLives() == 2) {
                        sprite.draw(g, 715, 35);
                        sprite.draw(g, 730, 35);
                        dead.draw(g, 745, 35);
                        dead.draw(g, 760, 35);
                        dead.draw(g, 775, 35);                        
                    } else if(((ShipEntity)ship).getLives() == 1) {
                        sprite.draw(g, 715, 35);
                        dead.draw(g, 730, 35);
                        dead.draw(g, 745, 35);
                        dead.draw(g, 760, 35);
                        dead.draw(g, 775, 35);                        
                    } else if(((ShipEntity)ship).getLives() == 0) {
                        dead.draw(g, 715, 35);
                        dead.draw(g, 730, 35);
                        dead.draw(g, 745, 35);
                        dead.draw(g, 760, 35);
                        dead.draw(g, 775, 35);                        
                    }

                    if (!waitingForKeyPress) {
                        for (int i=0;i<entities.size();i++) {
                            Entity entity = (Entity) entities.get(i);

                            entity.move(delta);
                        }
                    }

                    for (int i=0;i<entities.size();i++) {
                        Entity entity = (Entity) entities.get(i);

                        entity.draw(g);
                    }

                    for (int p=0;p<entities.size();p++) {
                        for (int s=p+1;s<entities.size();s++) {
                            Entity ent1 = (Entity) entities.get(p);
                            Entity ent2 = (Entity) entities.get(s);

                            if (ent1.collidesWith(ent2)) {
                                ent1.collidedWith(ent2);
                                ent2.collidedWith(ent1);
                            }
                        }
                    }

                    entities.removeAll(removeList);
                    removeList.clear();

                    if (bump) {
                        for (int i=0;i<entities.size();i++) {
                            Entity entity = (Entity) entities.get(i);
                            entity.doLogic();
                        }

                        bump = false;
                    }

                    if (waitingForKeyPress) {
                        g.setColor(Color.white);
                        g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);                        
                        g.drawString(message2,(800-g.getFontMetrics().stringWidth(message2))/2,275);
                        g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
                    }

                    g.dispose();
                    strategy.show();

                    ship.setHorizontalMovement(0);

                    if (leftPressed && !rightPressed){
                        ship.setHorizontalMovement(-moveSpeed);
                    } else if (rightPressed && !leftPressed) {
                        ship.setHorizontalMovement(moveSpeed);
                    }

                    boolean second = false;

                    for(int i = 0; i < entities.size(); i++) {
                        Entity ent = (Entity)entities.get(i);
                        if(ent instanceof MotherShipEntity) {
                            msTryToFire((MotherShipEntity)ent);
                            second = true;
                        } else if(ent instanceof BossEntity) {
                            bTryToFire((BossEntity)ent);
                        } else
                            second = false;
                    }

                    for(int i = 0; i < entities.size(); i++) {
                        Entity ent = (Entity)entities.get(i);
                        if((ent instanceof MotherShipEntity && second)) {
                            msTryToFire((MotherShipEntity)ent);
                            second = false;
                        }
                    }

                    if (firePressed) {
                        tryToFire();
                    }

                    try { 
                        Thread.sleep(10); 
                    } catch (Exception e) {}
                }
            }
        }
    }

    private class InputHandler implements KeyListener, MouseListener {
        private int pressCount = 1;

        public void mouseEntered(MouseEvent e) {}    
        public void mouseExited(MouseEvent e) {}   
        public void mouseClicked(MouseEvent e) {}  

        public void mousePressed(MouseEvent e) {
            if(waitingForKeyPress)
                return;
            if(e.getButton() == 1)
                firePressed = true;
        }    

        public void mouseReleased(MouseEvent e) {
            if(waitingForKeyPress)
                return;
            if(e.getButton() == 1)
                firePressed = false;
        }

        public void keyPressed(KeyEvent e) {
            if (waitingForKeyPress)
                return;   

            if (e.getKeyCode() == KeyEvent.VK_A) 
                leftPressed = true;
            if (e.getKeyCode() == KeyEvent.VK_D) 
                rightPressed = true;
        } 

        public void keyReleased(KeyEvent e) {
            if (waitingForKeyPress) 
                return;

            if (e.getKeyCode() == KeyEvent.VK_A) 
                leftPressed = false;
            if (e.getKeyCode() == KeyEvent.VK_D) 
                rightPressed = false;
        }

        public void keyTyped(KeyEvent e) {
            if (waitingForKeyPress) {
                if(((ShipEntity)ship).getLives() >= 0) {
                    waitingForKeyPress = false;
                    startAgain(((ShipEntity)ship).getLives());
                    pressCount = 0;
                } else if (pressCount == 1 && ((ShipEntity)ship).getLives() == -1) {
                    waitingForKeyPress = false;
                    startGame();
                    pressCount = 0;
                } else {
                    pressCount++;
                }
            }

            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        Game g =new Game();
        g.gameLoop();
    }
}
