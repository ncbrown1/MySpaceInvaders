public class ShipEntity extends Entity {
	private Game game;
	private int lives = 5;
	
	public ShipEntity(Game game,String ref,int x,int y) {
		super(ref,x,y);		
		this.game = game;
	}
	
	public void takeHit() {
	    lives--;
	}
	
	public int getLives() {
	    return lives;
	}
	
	public void setLives(int num) {
	    lives = num;
	}
	
	public void move(long delta) {
		if ((dx < 0) && (x < 10)) {
			return;
		}
		if ((dx > 0) && (x > 750)) {
			return;
		}		
		super.move(delta);
	}
	
	public void collidedWith(Entity other) {
		// if its an alien, notify the game that the player is dead
		if (other instanceof AlienEntity) {
			game.notifyDeath();
		}
	}
}