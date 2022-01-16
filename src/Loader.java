import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.newdawn.slick.SlickException;

/**
 * Contains static methods to be used for
 * checking if the given tile is blocked
 * and loading the lvl from file 
 * Changed based on sample solution written by
 * "All-seeing and All-knowing Eleanor" 
 */
public class Loader {	
	
	private static int targetsToBeCovered;
	/** stores type of every loaded sprite */
	private static String[][] types;
	/** dimensions of map, x and y */
	private static int[] dimensions = new int[2];
	/** coordinates of top-left pixel to draw, x and y */
	private static int[] startpointCoords = new int[2];
	/** current level index */
	private static int curLvl = 0;
	/** list of levels to be loaded */
	private static String[] lvlList = new String[]{"res/levels/0.lvl",
										   "res/levels/1.lvl",
										   "res/levels/2.lvl",
										   "res/levels/3.lvl",
										   "res/levels/4.lvl",
										   "res/levels/5.lvl"};
	/** cracked wall index */
	public static int crackedIndex = -1; // allow me this time plz
	/** TNT index */
	public static int tntIndex = -1; // i know privacy leak is bad
	/** cracked wall coordinates */
	public static float[] crackedPos = new float[2]; // but just for this time
	// i will write getters if i were working on this with others
	// but this time, just for our ease of reading :D
	
	/** create new sprite object of according type*/
	private static Sprite createSprite(String name, float x, float y) 
	throws SlickException {
		switch (name) {
			case "wall":
				return new Wall(x, y);
			case "floor":
				return new Floor(x, y);
			case "stone":
				return new Stone(x, y);
			case "target":
				targetsToBeCovered++;
				return new Target(x, y);
			case "tnt":
				return new TNT(x, y);
			case "ice":
				return new Ice(x, y);
			case "cracked":
				return new CrackedWall(x, y);
			case "switch":
				return new Switch(x, y);
			case "door":
				return new Door(x, y);
		}
		return null;
	}
	
	public static void cheat() throws SlickException {
		curLvl++;
		World.reLoad();
	}
	
	/** returns the type of the sprite on given position */
	public static String typeOf(int x, int y) {
		return types[x][y];
	}
	
	/** returns the coordinates of the top left pixel */
	public static int[] getStarpointCoords () {
		return startpointCoords;
	}
	
	/** set string representation of given location to given type*/
	public static void setType(int x, int y, String type) {
		if (types[x][y].equals("target") && type.equals("stone")) {
			targetsToBeCovered--;
		} else if (types[x][y].equals("target") && type.equals("ice")) {
			targetsToBeCovered--;
		} else if (types[x][y].equals("stone") && type.equals("target")) {
			targetsToBeCovered++;
		} else if (types[x][y].equals("ice") && type.equals("target")) {
			targetsToBeCovered++;
		} else if (types[x][y].equals("cracked") && type.equals("tnt")) {
			World.deleteCrackedTNT(); // remove cracked wall
		}
		types[x][y] = type;
	}
	
	/** set string representation of given location to given type */
	public static void checkTargets() throws SlickException {
		if (curLvl != 5 && targetsToBeCovered == 0) {
			curLvl++;
			World.reLoad();
		}
	}
	
	/** Converts a world coordinate to a tile coordinate,
	  * and returns if that location is a blocked tile */
	public static boolean isBlocked(float x, float y) {
		
		//translate coordinates
		x -= startpointCoords[0];
		x /= App.TILE_SIZE;
		y -= startpointCoords[1];
		y /= App.TILE_SIZE;
		x = Math.round(x);
		y = Math.round(y);
		if (x >= 0 && x < dimensions[0] && y >= 0 && y < dimensions[1]) {
			return (types[(int)x][(int)y].equals("wall") || 
					types[(int)x][(int)y].equals("cracked") ||
					types[(int)x][(int)y].equals("door"));
		}
		return true;
	}
	
	/** returns if that location is a stone tile */
	public static boolean isStone(float x, float y) {
		
		//translate coordinates
		x -= startpointCoords[0];
		x /= App.TILE_SIZE;
		y -= startpointCoords[1];
		y /= App.TILE_SIZE;
		x = Math.round(x);
		y = Math.round(y);
		return (types[(int)x][(int)y].equals("stone") || 
				types[(int)x][(int)y].equals("ice") || 
				types[(int)x][(int)y].equals("tnt"));
	}
	
	/** returns if that location is a cracked wall */
	public static boolean isCracked(float x, float y) {
		//translate coordinates
		x -= startpointCoords[0];
		x /= App.TILE_SIZE;
		y -= startpointCoords[1];
		y /= App.TILE_SIZE;
		x = Math.round(x);
		y = Math.round(y);
		return (types[(int)x][(int)y].equals("cracked"));
	}
	
	/** loads player from given lvl file */
	public static Player loadPlayer() throws SlickException {
		String filename = lvlList[curLvl];
		try (BufferedReader br = 
				new BufferedReader(new FileReader(filename))) {
			String text;
			while ((text = br.readLine()) != null) {
				String[] columns = text.split(",");
				if (columns[0].equals("player")) {
					float x, y;
					x = startpointCoords[0] + 
		        			Integer.parseInt(columns[1]) * App.TILE_SIZE;
		        	y = startpointCoords[1] + 
		        			Integer.parseInt(columns[2]) * App.TILE_SIZE;
					return new Player(x, y);
	        	}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
	    }
		return null;
	}
	
	/** loads rogue from given lvl file */
	public static Rogue loadRogue() throws SlickException {
		String filename = lvlList[curLvl];
		try (BufferedReader br = 
				new BufferedReader(new FileReader(filename))) {
			String text;
			while ((text = br.readLine()) != null) {
				String[] columns = text.split(",");
				if (columns[0].equals("rogue")) {
					float x, y;
					x = startpointCoords[0] + 
		        			Integer.parseInt(columns[1]) * App.TILE_SIZE;
		        	y = startpointCoords[1] + 
		        			Integer.parseInt(columns[2]) * App.TILE_SIZE;
					return new Rogue(x, y);
	        	}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
	    }
		return null;
	}
	
	/** loads skeleton from given lvl file */
	public static Skeleton loadSkeleton() throws SlickException {
		String filename = lvlList[curLvl];
		try (BufferedReader br = 
				new BufferedReader(new FileReader(filename))) {
			String text;
			while ((text = br.readLine()) != null) {
				String[] columns = text.split(",");
				if (columns[0].equals("skeleton")) {
					float x, y;
					x = startpointCoords[0] + 
		        			Integer.parseInt(columns[1]) * App.TILE_SIZE;
		        	y = startpointCoords[1] + 
		        			Integer.parseInt(columns[2]) * App.TILE_SIZE;
					return new Skeleton(x, y);
	        	}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
	    }
		return null;
	}
	
	/** loads mage from given lvl file */
	public static Mage loadMage() throws SlickException {
		String filename = lvlList[curLvl];
		try (BufferedReader br = 
				new BufferedReader(new FileReader(filename))) {
			String text;
			while ((text = br.readLine()) != null) {
				String[] columns = text.split(",");
				if (columns[0].equals("mage")) {
					float x, y;
					x = startpointCoords[0] + 
		        			Integer.parseInt(columns[1]) * App.TILE_SIZE;
		        	y = startpointCoords[1] + 
		        			Integer.parseInt(columns[2]) * App.TILE_SIZE;
					return new Mage(x, y);
	        	}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
	    }
		return null;
	}
	
	/**
	 * Loads the sprites from a given file.
	 * @param filename
	 * @return sprites
	 * @throws SlickException 
	 */
	public static ArrayList<Sprite> loadSprites() throws SlickException {
		
		targetsToBeCovered = 0;// otherwise wont be reset when reloading lvl
		
		String filename = lvlList[curLvl];
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		// Read level file
		try (BufferedReader br = 
			new BufferedReader(new FileReader(filename))) {
			
			// Read first line of level file to get dimensions of map
			String text = br.readLine();
			String[] first_line = text.split(",");
			// get dimensions
			dimensions[0] = Integer.parseInt(first_line[0]);
			dimensions[1] = Integer.parseInt(first_line[1]);
			
			// set start point position
			startpointCoords[0] = App.MIDPOINT_COORDS[0] - 
								   dimensions[0]*App.TILE_SIZE/2;
			startpointCoords[1] = App.MIDPOINT_COORDS[1] - 
								   dimensions[1]*App.TILE_SIZE/2;
			
			types = new String[dimensions[0]][dimensions[1]];

			// read the rest to load sprite
	        while ((text = br.readLine()) != null) {
	        	
	        	String[] columns = text.split(",");
	        	
	        	float x, y;
	        	x = Float.parseFloat(columns[1]);
				y = Float.parseFloat(columns[2]);
				types[(int)x][(int)y] = columns[0];
	        	
	        	x = startpointCoords[0] + 
	        			Integer.parseInt(columns[1]) * App.TILE_SIZE;
	        	y = startpointCoords[1] + 
	        			Integer.parseInt(columns[2]) * App.TILE_SIZE;
	        	
	        	// record index of cracked wall in arraylist "sprites"
	        	// and coordinates of cracked wall in pixels
	        	if (columns[0].equals("cracked")) {
	        		crackedIndex = sprites.size();
	        		crackedPos[0] = x;
	        		crackedPos[1] = y;
	        	}
	        	
	        	// record index of tnt in arraylist "sprites"
	        	if (columns[0].equals("tnt")) {
	        		tntIndex = sprites.size();
	        	}
	        	
	        	sprites.add(createSprite(columns[0], x, y));
	        }
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
	    }
		return sprites;
	}
	
	
}
