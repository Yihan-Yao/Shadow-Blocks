
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Floor extends Block {

	private float x;
	private float y;
	private Image image = null;
	
	public Floor(float x, float y) 
	throws SlickException 
	{
		super("res/floor.png", x, y);
		this.x = x;
		this.y = y;
		this.image = super.getImage();
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(image, x, y);
	}

}
