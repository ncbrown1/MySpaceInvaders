import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
	protected double x; //location
	protected double y;
	protected Sprite sprite;
	protected double dx; //speeds
	protected double dy;
	protected String ref;
	protected int health;
	
	private Rectangle ent1 = new Rectangle();
	private Rectangle ent2 = new Rectangle();
	public Entity(String ref,int x,int y) {
	    this.ref = ref;
		this.sprite = SpriteStore.get().getSprite(ref);
		this.x = x;
		this.y = y;
		health = Integer.MAX_VALUE;
	}
	
	public void takeHit() {
	    health--;
	}
	
	public int getHealth() {
	    return health;
	}
	
	public String getRef() {
	    return ref;
	}
	
	public void setSprite(String ref) {
	    this.ref = ref;
	    this.sprite = SpriteStore.get().getSprite(ref);
	}

	public Sprite getSprite() {
		return sprite;
	}
	
	public void move(long delta) {
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	}
	
	public void setHorizontalMovement(double dx) {
		this.dx = dx;
	}

	public void setVerticalMovement(double dy) {
		this.dy = dy;
	}
	
	public double getHorizontalMovement() {
		return dx;
	}

	public double getVerticalMovement() {
		return dy;
	}
	
	public void draw(Graphics g) {
		sprite.draw(g,(int) x,(int) y);
	}
	
	public void doLogic() {
	}
	
	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}
	
	public boolean collidesWith(Entity other) {
		ent1.setBounds((int) x,(int) y,sprite.getWidth(),sprite.getHeight());
		ent2.setBounds((int) other.x,(int) other.y,other.sprite.getWidth(),other.sprite.getHeight());

		return ent1.intersects(ent2);
	}
	
	public abstract void collidedWith(Entity other);
}