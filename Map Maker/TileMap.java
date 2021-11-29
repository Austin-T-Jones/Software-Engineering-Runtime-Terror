
/**
 * Tile Map class : Creates a Tile Map using a double array
 * Runtime Terror
 */
public class TileMap
{
    private Tile[][] tiles;
    
    //Create a TileMap with specified dimensions(width and height)
    public TileMap(int width, int height)
    {
        tiles = new Tile[width][height];
    }
    
    //Gets width of TileMap (number of tiles across)
    public int getWidth()
    {
        return tiles.length;
    }
    
    //Gets height of TileMap (number of tiles down)
    public int getHeight()
    {
        return tiles[0].length;
    }
    
    //Gets tile at specific locaion, but returns null if no tile is at the location or out of bounds 
    public Tile getTile(int x,int y)
    {
        if(x < 0 || x >=getWidth() || y < 0 || y>= getHeight())
        {
            return null;
        }
        else
        {
            return tiles[x][y];
        }
    }
    
    //sets tile at specified location
    public void setTile(int x, int y, Tile tile)
    {
        tiles[x][y] = tile;
    }
}
