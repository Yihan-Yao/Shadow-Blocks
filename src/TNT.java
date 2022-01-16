import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class TNT extends Block {
	private String blockType = "tnt";
	
	public TNT(float x, float y) throws SlickException {
		super("res/tnt.png", x, y);
	}
	
	public void moveToDest(Dir dir) {
		super.moveToDest(dir);
	}
	
	public void undoMove() {
		super.undoMove();
	}
	
	public void update(Input input, int delta, Position playerPos, 
			Position roguePos) {
		super.update(input, delta, playerPos, roguePos, blockType);
	}
}
