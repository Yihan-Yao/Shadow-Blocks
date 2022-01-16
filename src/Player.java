import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player extends Sprite {
	
	private float x;
	private float y;
	private float speed = App.TILE_SIZE;
	private Image image = null;
	private Dir facing = Dir.NONE;
	/** player's position info (tile_x, tile_y, facing) */
	private Position pos = new Position(x, y, facing);
	private ArrayList<float[]> pastPos = new ArrayList<float[]>();
	
	public Player(float x, float y) 
	throws SlickException {
		super("res/player_left.png", x, y);
		this.x = super.getX();
		this.y = super.getY();
		this.image = super.getImage();
	}
	
	public Position getPos() {
		return pos;
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
		
		// if no wall or stone ahead, move
		if (!Loader.isBlocked(x + delta_x, y + delta_y) && 
			!Loader.isStone(x + delta_x, y + delta_y)) {
			x += delta_x;
			y += delta_y;
		// else if stone ahead, also move
		} else if (Loader.isStone(x + delta_x, y + delta_y)) {
			x += delta_x;
			y += delta_y;
			// but if encounter another wall or stone(not cracked), cancel move
			if (!Loader.isCracked(x + delta_x, y + delta_y) && (
				Loader.isBlocked(x + delta_x, y + delta_y) || 
				Loader.isStone(x + delta_x, y + delta_y))) {
				x -= delta_x;
				y -= delta_y;
			}
		}
		World.moved();
	}
	
	public void undoMove() {
		float[] coords = pastPos.get(pastPos.size()-1);
		pastPos.remove(pastPos.size()-1);
		x = coords[0];
		y = coords[1];
	}
	
	public void recordMove() {
		float[] coords = new float[] {x, y};
		pastPos.add(coords);
	}
	
	public void update(Input input, int delta) {
		// move according to user input
		if (input.isKeyPressed(Input.KEY_LEFT)) {
			World.recordMove();
			facing = Dir.LEFT;
			moveToDest(Dir.LEFT);
		} else if (input.isKeyPressed(Input.KEY_RIGHT)) {
			World.recordMove();
			facing = Dir.RIGHT;
			moveToDest(Dir.RIGHT);
		} else if (input.isKeyPressed(Input.KEY_UP)) {
			World.recordMove();
			facing = Dir.UP;
			moveToDest(Dir.UP);
		} else if (input.isKeyPressed(Input.KEY_DOWN)) {
			World.recordMove();
			facing = Dir.DOWN;
			moveToDest(Dir.DOWN);
		}
		
		// update pos after each move for blocks to be pushed on same frame
		pos.facing = facing;
		int[] offset = new int[2];
		offset = Loader.getStarpointCoords();
		pos.tile_x = (int)((x - offset[0])/App.TILE_SIZE);
		pos.tile_y = (int)((y - offset[1])/App.TILE_SIZE);
		
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y);
	}
	
}
