import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Defines a single sprite
 */
public abstract class Sprite {	
	
	private float x;
	private float y;
	private Image image = null;
	private float speed = App.TILE_SIZE;
	
	public Sprite(String image_src, float x, float y)
	throws SlickException
	{
		this.x = x;
		this.y = y;
		try {
			image = new Image(image_src);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void moveToDest(Dir dir) {
		
		// Translate the direction to an x and y displacement
		float delta_x = 0,
			  delta_y = 0;
		switch (dir) {
			case NONE:
				break;
			case LEFT:
				delta_x = -speed;
				break;
			case RIGHT:
				delta_x = speed;
				break;
			case UP:
				delta_y = -speed;
				break;
			case DOWN:
				delta_y = speed;
				break;
		}
		if (!Loader.isBlocked(x + delta_x, y + delta_y)) {
			x += delta_x;
			y += delta_y;
		}
	}
	
	public void undoMove() {
	}
	
	public void recordMove() {
	}
	
	// update the sprite for a frame
	public void update(Input input, int delta, Position playerPos, 
			Position roguePos) {
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y);
	}
}
