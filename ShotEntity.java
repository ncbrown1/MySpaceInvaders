public class ShotEntity extends Entity {
    private double moveSpeed = -300;
    private Game game;
    private boolean used = false;
    private ShipEntity shooter;

    public ShotEntity(ShipEntity ship, Game game,String sprite,int x,int y) {
        super(sprite,x,y);      
        this.game = game;       
        dy = moveSpeed;
        shooter = ship;
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

        if (other instanceof AlienEntity || other instanceof MotherShipEntity || 
                    other instanceof BossEntity || other instanceof AlienShotEntity) {
            if(other.getRef().equals("alien.gif")) {
                other.setSprite("alienHurt.gif");
            } else if(other.getRef().equals("motherShip.gif") && other.getHealth() > 0) {
                other.takeHit();
            } else if(other.getRef().equals("bossyboss.gif") && other.getHealth() > 0) {
                other.takeHit();
            } else if(other.getRef().equals("alienShot.gif") || other.getRef().equals("bossShot.gif")) {
                game.removeEntity(other);
            }else if(other.getRef().equals("alienHurt.gif") || other.getHealth() <= 0 ||
               other.getRef().equals("alien - Copy.gif")){
                game.removeEntity(other);
                game.notifyAlienKilled();
            }
            game.removeEntity(this);
            used = true;
        }
    }
}