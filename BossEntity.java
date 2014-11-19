public class BossEntity extends Entity {
    private double moveSpeed = 75;
    private Game game;
    private long lastShot = 200;
    
    public BossEntity(Game game,String ref,int x,int y) {
        super(ref,x,y);     
        this.game = game;
        dx = -moveSpeed;
        health = 100;
    }
    
    public long getLastShot() {
        return lastShot;
    }
    
    public void setLastShot(long time) {
        lastShot = time;
    }
    
    public void move(long delta) {
        if ((dx < 0) && (x < 10)) {
            game.updateBump();
        }
        if ((dx > 0) && (x > 280)) {
            game.updateBump();
        }
        super.move(delta);
    }
    
    public void doLogic() {
        dx = -dx;
        y += 10;
        
        if (y > 455) {
            game.notifyDeath();
        }
    }
    
    public void collidedWith(Entity other) {
    }
}