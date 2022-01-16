import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Mage extends Sprite {
	
	private float x;
	private float y;
	private Image image = null;
	private Position playerPos;
	
	public Mage(float x, float y) 
			throws SlickException {
				super("res/mage.png", x, y);
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
	
	public void update(int delta, Position playerPos) {
		this.playerPos = playerPos;
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y);
	}
	
}
