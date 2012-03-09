package mkz.labyrinth3D.game.objects;

/**
 *
 * @author Hans
 */
public abstract class Tile extends Object3D
{
    public static final int TYPE_WALL = 0;
    public static final int TYPE_FLOOR = 1;
    public static final int TYPE_START = 2;
    public static final int TYPE_FINISH = 3;
    public static final int TYPE_HOLE = 4;
    
    public int x;
    public int y;
    
    public abstract void interact(Item item);
    public abstract float size();
    public abstract int type();
}
