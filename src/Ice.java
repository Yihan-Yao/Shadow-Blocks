import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Ice extends Block {
	
	private String blockType = "ice";
	private float x;
	private float y;
	private Image image = null;
	private boolean pushed = false;
	private float msFromLastMove;
	private String typeCovered = "floor"; // default to be on a floor tile
	private Dir dir = Dir.NONE;
	private float speed = App.TILE_SIZE;
	
	public Ice(float x, float y) 
	throws SlickException {
		super("res/ice.png", x, y);
		this.x = super.getX();
		this.y = super.getY();
		this.image = super.getImage();
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
		super.undoMove();
	}
	
	public void update(Input input, int delta, Position playerPos, 
			Position roguePos) {
		
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
		
		if (!pushed && tile_x == player_x && tile_y == player_y) {
			dir = playerPos.facing;
			pushed = true;
		}
		
		msFromLastMove += delta;

		// find out coords of next tile
		float nextX = x;
		float nextY = y;
		switch (dir) {
		case NONE:
			break;
		case LEFT:
			nextX = x - App.TILE_SIZE;
			nextY = y;
			break;
		case RIGHT:
			nextX = x + App.TILE_SIZE;
			nextY = y;
			break;
		case UP:
			nextX = x;
			nextY = y - App.TILE_SIZE;
			break;
		case DOWN:
			nextX = x;
			nextY = y + App.TILE_SIZE;
			break;
		}
		
		// stop when reach another block
		if (Loader.isBlocked(nextX, nextY) || Loader.isStone(nextX, nextY)) {
			pushed = false;
		}
		
		if (pushed && msFromLastMove > 250) {
			Loader.setType(tile_x, tile_y, typeCovered);
			moveToDest(dir);
			blockX = x;
			blockY = y;
			blockX -= Loader.getStarpointCoords()[0];
			blockX /= App.TILE_SIZE;
			blockY -= Loader.getStarpointCoords()[1];
			blockY /= App.TILE_SIZE;
			tile_x = Math.round(blockX);
			tile_y = Math.round(blockY);
			typeCovered = Loader.typeOf(tile_x, tile_y);
			Loader.setType(tile_x, tile_y, blockType);
			msFromLastMove = 0;
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y);
	}
}
