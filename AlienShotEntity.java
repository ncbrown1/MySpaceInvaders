public class AlienShotEntity extends Entity {
    private double moveSpeed = 200;
    private Game game;
    private boolean used = false;
    private Entity shooter;

    public AlienShotEntity(Entity alien, Game game,String sprite,int x,int y) {
        super(sprite,x,y);      
        this.game = game;       
        dy = moveSpeed;
        shooter = alien;
    }

    public void move(long delta) {
        super.move(delta);
        if (y < -100) {
            game.removeEntity(this);
        }
    }

    public void collidedWith(Entity other) {
        if (used) {
            return; 
        }

        if (other instanceof ShipEntity) {
            if(other.getRef().equals("ship.gif") && ((ShipEntity)other).getLives() == 0)
                game.removeEntity(other);
            ((ShipEntity)other).takeHit();
            game.notifyDeath();
            game.removeEntity(this);
            used = true;
        }
    }
}