import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Skeleton extends Sprite {
	
	private float x;
	private float y;
	private float speed = App.TILE_SIZE;
	private Image image = null;
	private Dir facing = Dir.UP;
	private float msFromLastMove;
	private Position playerPos;
	
	public Skeleton(float x, float y) 
	throws SlickException {
		super("res/skull.png", x, y);
		this.x = x;
		this.y = y;
		this.image = super.getImage();
	}
	
	public boolean checkContact() {
		// translate pixel coordinates to tile coordinates
		float blockX = x;
		float blockY = y;
		blockX -= Loader.getStarpointCoords()[0];
		blockX /= App.TILE_SIZE;
		blockY -= Loader.getStarpointCoords()[1];
		blockY /= App.TILE_SIZE;
		int tile_x = Math.round(blockX);
		int tile_y = Math.round(blockY);
		int player_x = playerPos.tile_x;
		int player_y = playerPos.tile_y;
		if (tile_x == player_x && tile_y == player_y) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("incomplete-switch")
	public void update(int delta, Position playerPos) {
		this.playerPos = playerPos;
		
		// moves once per second
		msFromLastMove += delta;
		if (msFromLastMove > 1000) {
			switch (facing) {
			// until reaches a wall
			case UP:
				if (Loader.isBlocked(x, y - speed) || 
					Loader.isStone(x, y - speed)) {
					facing = Dir.DOWN;
					y += speed;
					break;
				}
				y -= speed;
				break;
			case DOWN:
				if (Loader.isBlocked(x, y + speed) || 
					Loader.isStone(x, y + speed)) {
					facing = Dir.UP;
					y -= speed;
					break;
				}
				y += speed;
				break;
			}
			msFromLastMove = 0; // zero when move is made
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y);
	}
	
}
