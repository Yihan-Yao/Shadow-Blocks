public class Position {
	public int tile_x;
	public int tile_y;
	public Dir facing = Dir.NONE;
	
	public Position(float x, float y, Dir facing) {
		this.facing = facing;
		x -= Loader.getStarpointCoords()[0];
		x /= App.TILE_SIZE;
		y -= Loader.getStarpointCoords()[1];
		y /= App.TILE_SIZE;
		tile_x = Math.round(x);
		tile_y = Math.round(y);
	}
}
