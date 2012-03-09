package mkz.labyrinth3D.game;

import android.content.Context;
import mkz.labyrinth3D.R;
import mkz.labyrinth3D.game.objects.Texture;
import mkz.labyrinth3D.game.objects.Item;
import mkz.labyrinth3D.game.objects.Map;
import mkz.labyrinth3D.game.objects.Tile;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 *
 * @author Hans
 */
public class Level
{
    public Map map;
    public ArrayList<Tile> tiles;
    public ArrayList<Item> items;
    private Context context;
    private int x;
    private int y;
    
    public Level(Context context)
    {
        this.context = context;
    }
    
    public void load(String name, GL10 gl)
    {
        //TEST
        Texture tex = new Texture(gl, context, R.drawable.map_atlas);
        
        int[][] mapAray = new int[][]
        {
            new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            new int[]{2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 0, 0, 1, 1, 1, 0, 2},
            new int[]{2, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 2},
            new int[]{2, 1, 1, 1, 2, 0, 1, 1, 0, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2},
            new int[]{2, 1, 2, 2, 2, 0, 0, 1, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
            new int[]{2, 1, 2, 1, 2, 1, 1, 1, 0, 2, 1, 2, 2, 2, 2, 1, 0, 0, 1, 2},
            new int[]{2, 1, 1, 1, 2, 1, 2, 2, 2, 2, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2},
            new int[]{2, 0, 1, 2, 2, 1, 2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 1, 2},
            new int[]{2, 0, 1, 2, 1, 1, 1, 0, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2},
            new int[]{2, 2, 1, 2, 1, 0, 1, 0, 1, 2, 1, 2, 2, 2, 1, 2, 1, 1, 1, 2},
            new int[]{2, 1, 1, 2, 1, 0, 1, 1, 1, 2, 1, 0, 1, 1, 1, 2, 2, 2, 2, 2},
            new int[]{2, 1, 1, 2, 1, 0, 1, 0, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2},
            new int[]{2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 1, 2},
            new int[]{2, 1, 0, 0, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 2},
            new int[]{2, 1, 1, 0, 2, 1, 0, 2, 1, 2, 0, 1, 0, 0, 2, 2, 1, 2, 1, 2},
            new int[]{2, 0, 1, 0, 2, 1, 0, 2, 1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 2},
            new int[]{2, 0, 1, 1, 1, 1, 1, 2, 1, 2, 1, 0, 2, 2, 2, 1, 2, 2, 1, 2},
            new int[]{2, 0, 1, 0, 2, 0, 1, 2, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1, 2},
            new int[]{2, 0, 0, 0, 2, 1, 1, 1, 1, 2, 0, 1, 1, 1, 2, 0, 1, 1, 1, 2},
            new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        };
        
        map = new Map(mapAray, tex, (GL11) gl);
    }
}
