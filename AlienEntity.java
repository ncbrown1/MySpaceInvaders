public class AlienEntity extends Entity {
    private double moveSpeed = 75;
    private Game game;
    
    public AlienEntity(Game game,String ref,int x,int y) {
        super(ref,x,y);     
        this.game = game;
        dx = -moveSpeed;
    }
    
    public void move(long delta) {
        if ((dx < 0) && (x < 10)) {
            game.updateBump();
        }
        if ((dx > 0) && (x > 750)) {
            game.updateBump();
        }
        super.move(delta);
    }
    
    public void doLogic() {
        dx = -dx;
        y += 10;
        
        if (y > 570) {
            game.notifyDeath();
        }
    }
    
    public void collidedWith(Entity other) {
        //collisions handled in ShotEntity class
    }
}