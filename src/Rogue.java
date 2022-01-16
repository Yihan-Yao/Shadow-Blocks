import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Rogue extends Sprite {
	
	private float x;
	private float y;
	private float speed = App.TILE_SIZE;
	private Image image = null;
	private Dir facing = Dir.LEFT;
	/** rogue's position info (tile_x, tile_y, facing) */
	private Position pos = new Position(x, y, facing);
	private Position playerPos;
	private boolean moved = false; // indicates whether if a move was made
	
	public Rogue(float x, float y) 
	throws SlickException {
		super("res/rogue.png", x, y);
		this.x = super.getX();
		this.y = super.getY();
		this.image = super.getImage();
	}
	
	public Position getPos() {
		return pos;
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
	
	@Override
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
		if (!Loader.isBlocked(x + delta_x, y + delta_y) && 
			!Loader.isStone(x + delta_x, y + delta_y)) {
			x += delta_x;
			y += delta_y;
		} else if (Loader.isStone(x + delta_x, y + delta_y)) {
			x += delta_x;
			y += delta_y;
			if (Loader.isBlocked(x + delta_x, y + delta_y) || 
				Loader.isStone(x + delta_x, y + delta_y)) {
				x -= delta_x;
				y -= delta_y;
			}
		}
	}
	
	public void playerMoved() {
		moved = true;
	}
	
	@SuppressWarnings("incomplete-switch")
	public void update(Input input, int delta, Position playerPos) {
		this.playerPos = playerPos;
		// moves whenever player tries to move
		if (moved) {
			// turn around when reaches wall
			switch (facing) {
			case LEFT:
				if (Loader.isBlocked(x - speed, y)) {
					facing = Dir.RIGHT;
					x += speed;
					if (Loader.isStone(x + speed, y)) {
						x -= speed;
						break;
					}
					break;
				} else if (!Loader.isStone(x - speed, y)) {
					x -= speed;
					break;
				} else {
					x -= speed;
					if (Loader.isBlocked(x - speed, y)) {
						facing = Dir.RIGHT;
						x += (speed * 2);
						break;
					}
					break;
				}
			case RIGHT:
				if (Loader.isBlocked(x + speed, y)) {
					facing = Dir.LEFT;
					x -= speed;
					if (Loader.isStone(x - speed, y)) {
						x += speed;
						break;
					}
					break;
				} else if (!Loader.isStone(x + speed, y)) {
					x += speed;
					break;
				} else {
					x += speed;
					if (Loader.isBlocked(x + speed, y)) {
						x -= (speed * 2);
						break;
					}
					break;
				}
			}
			moved = false;
		}
		// update pos after each move for blocks to be pushed on same frame
		pos.facing = facing;
		pos.tile_x = (int)(x - Loader.getStarpointCoords()[0]) / App.TILE_SIZE;
		pos.tile_y = (int)(y - Loader.getStarpointCoords()[1]) / App.TILE_SIZE;
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y);
	}

}
