import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public abstract class Block extends Sprite{
	
	private float x;
	private float y;
	private Image image = null;
	private String typeCovered = "floor"; // default to be on a floor tile
	private ArrayList<float[]> pastPos = new ArrayList<float[]>();
	private float speed = App.TILE_SIZE;
	
	public Block(String image_src, float x, float y) 
	throws SlickException {
		super(image_src, x, y);
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
		if (!Loader.isBlocked(x + delta_x, y + delta_y) || 
			Loader.isCracked(x + delta_x, y + delta_y)) {
			x += delta_x;
			y += delta_y;
		}
	}
	
	/** record every blocks pos before each move attempted*/
	public void undoMove(String blockType) {
		float[] pos = pastPos.get(pastPos.size()-1);
		pastPos.remove(pastPos.size()-1);
		
		// translate pixel coordinates to tile coordinates
		float blockX = x;
		float blockY = y;
		blockX -= Loader.getStarpointCoords()[0];
		blockX /= App.TILE_SIZE;
		blockY -= Loader.getStarpointCoords()[1];
		blockY /= App.TILE_SIZE;
		int tile_x = Math.round(blockX);
		int tile_y = Math.round(blockY);
		Loader.setType(tile_x, tile_y, getTypeCovered());
		
		// undo move
		x = pos[0];
		y = pos[1];
		
		// set types correctly
		blockX = x;
		blockY = y;
		blockX -= Loader.getStarpointCoords()[0];
		blockX /= App.TILE_SIZE;
		blockY -= Loader.getStarpointCoords()[1];
		blockY /= App.TILE_SIZE;
		tile_x = Math.round(blockX);
		tile_y = Math.round(blockY);
		setTypeCovered(Loader.typeOf(tile_x, tile_y));
		Loader.setType(tile_x, tile_y, blockType);
	}
	
	public void recordMove() {
		float[] pos = new float[] {x, y};
		pastPos.add(pos);
	}
	
	public void update(Input input, int delta, Position playerPos, 
			Position roguePos, String blockType) {
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
		int rogue_x = 0;
		int rogue_y = 0;
		if (roguePos != null) {
			rogue_x = roguePos.tile_x;
			rogue_y = roguePos.tile_y;
		}
		
		// records the covered tile type and rewrite the exposed tile type
		if ((tile_x == player_x && tile_y == player_y)) {
			Loader.setType(tile_x, tile_y, getTypeCovered());
			moveToDest(playerPos.facing);
			blockX = x;
			blockY = y;
			blockX -= Loader.getStarpointCoords()[0];
			blockX /= App.TILE_SIZE;
			blockY -= Loader.getStarpointCoords()[1];
			blockY /= App.TILE_SIZE;
			tile_x = Math.round(blockX);
			tile_y = Math.round(blockY);
			setTypeCovered(Loader.typeOf(tile_x, tile_y));
			Loader.setType(tile_x, tile_y, blockType);
		} else if ((tile_x == rogue_x && tile_y == rogue_y)) {
			Loader.setType(tile_x, tile_y, getTypeCovered());
			moveToDest(roguePos.facing);
			blockX = x;
			blockY = y;
			blockX -= Loader.getStarpointCoords()[0];
			blockX /= App.TILE_SIZE;
			blockY -= Loader.getStarpointCoords()[1];
			blockY /= App.TILE_SIZE;
			tile_x = Math.round(blockX);
			tile_y = Math.round(blockY);
			setTypeCovered(Loader.typeOf(tile_x, tile_y));
			Loader.setType(tile_x, tile_y, blockType);
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y);
	}

	public String getTypeCovered() {
		return typeCovered;
	}

	public void setTypeCovered(String typeCovered) {
		this.typeCovered = typeCovered;
	}
	
}