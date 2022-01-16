import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Stone extends Block {

	private String blockType = "stone";
	
	public Stone(float x, float y) 
	throws SlickException 
	{
		super("res/stone.png", x, y);
	}
	
	public void undoMove() {
		super.undoMove(blockType);// pass type to super class
	}
	
	public void update(Input input, int delta, Position playerPos, 
			Position roguePos) {
		super.update(input, delta, playerPos, roguePos, blockType);
	}

}
