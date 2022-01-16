import java.util.ArrayList;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Represents the game world
 */
public class World {
	
	private static ArrayList<Sprite> sprites;
	private static Player player = null;
	private static Rogue rogue = null;
	private static Skeleton skeleton = null;
	private static Mage mage = null;
	private static Explosion explosion = new Explosion();
	Position playerPos = null;
	Position roguePos = null;
	/** moves count */
	private static int moves = 0;
	
	public World() 
	throws SlickException{
		sprites = Loader.loadSprites();
		player = Loader.loadPlayer();
		rogue = Loader.loadRogue();
		skeleton = Loader.loadSkeleton();
		mage = Loader.loadMage();
	}
	
	// getter for moves
	public static int getMoves() {
		return moves;
	}
	
	// reload lvl when needed
	public static void reLoad() throws SlickException {
		moves = 0;
		sprites.clear();
		sprites = Loader.loadSprites();
		player = Loader.loadPlayer();
		rogue = Loader.loadRogue();
		skeleton = Loader.loadSkeleton();
		mage = Loader.loadMage();
	}

	// for move detection inside player class
	public static void moved() {
		if (rogue != null) {
			rogue.playerMoved();
		}
		moves++;
	}
	
	// let player call all sprites' record method when player make moves
	public static void recordMove() {
		player.recordMove();
		for (Sprite sprite : sprites) {
			if (sprite != null) {
				sprite.recordMove();
			}
		}
	}
	
	// when TNT encounters cracked wall, things happens
	// called in Loader.setType method
	public static void deleteCrackedTNT() {
		explosion.play(Loader.crackedPos);
		sprites.set(Loader.crackedIndex, null);
		sprites.set(Loader.tntIndex, null);
	}
	
	// update the world for a frame
	public void update(Input input, int delta) 
	throws SlickException {
		
		// game without cheat?
		if (input.isKeyPressed(Input.KEY_GRAVE)) {
			Loader.cheat();
		}
		
		// check if all targets are covered by stones
		Loader.checkTargets();
		
		// reload lvl when R is pressed
		if (input.isKeyPressed(Input.KEY_R)) {
			reLoad();
		}
		
		// undo every block's last move
		if (input.isKeyPressed(Input.KEY_Z)) {
			player.undoMove();
			for (Sprite sprite : sprites) {
				if (sprite != null) {
					sprite.undoMove();
				}
			}
			moves--;
		}
		
		//update player first in order to update others correctly
		if (player != null) {
			player.update(input, delta);
			playerPos = player.getPos();
		}
		
		if (rogue != null) {
			rogue.update(input, delta, playerPos);
			if (rogue.checkContact()) {
				reLoad();
				return;
			}
			roguePos = rogue.getPos();
		}
		
		if (skeleton != null) {
			skeleton.update(delta, playerPos);
			if (skeleton.checkContact()) {
				reLoad();
				return;
			}
		}
		
		if (mage != null) {
			mage.update(delta, playerPos);
			if (mage.checkContact()) {
				reLoad();
				return;
			}
		}
		
		for (Sprite sprite : sprites) {
			if (player != null && sprite != null) {
				sprite.update(input, delta, playerPos, roguePos);
			}
		}
		
		explosion.update(delta);
	}
	
	public void render(Graphics g)
	throws SlickException{
		for (Sprite sprite : sprites) {
			if (sprite != null) {
				sprite.render(g);
			}
		}
		if (rogue != null) {
			rogue.render(g);
		}
		if (skeleton != null) {
			skeleton.render(g);
		}
		if (mage != null) {
			mage.render(g);
		}
		// render player at last to keep player on top
		if (player != null) {
			player.render(g);
		}
		
		explosion.render(g);
		
		// display move count
		g.drawString("Moves: " + Integer.toString(moves), 0, 0);
	}
}
