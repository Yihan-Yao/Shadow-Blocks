import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Explosion{
	
	private float x;
	private float y;
	private Image image = null;
	private int playedTime = 0;
	private boolean playing = false;
	
	public Explosion() {
		try {
			image = new Image("res/explosion.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void play(float[] pos) {
		x = pos[0];
		y = pos[1];
		playing = true;
	}
	
	public void update(int delta){
		if (playedTime > 400) {
			playing = false;
			playedTime = 0;
		}
		if (playing) {
			playedTime += delta;
		}
	}
	
	public void render(Graphics g) {
		if (playing) {
			image.drawCentered(x + App.TILE_SIZE/2, y + App.TILE_SIZE/2);
		}
	}
}
