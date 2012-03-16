package mkz.labyrinth3D.game;

import android.content.Context;
import mkz.labyrinth3D.R;
import mkz.labyrinth3D.game.objects.Texture;
import mkz.labyrinth3D.game.objects.Map;
import javax.microedition.khronos.opengles.GL11;
import mkz.labyrinth3D.game.objects.ItemSet;

/**
 *
 * @author Hans
 */
public class Level
{
    private Map map;
    private ItemSet itemSet;
    private Context context;
    private int x;
    private int y;
    
    public Level(Context context)
    {
        this.context = context;
    }
    
    public void load(String name, GL11 gl)
    {
        //TEST
        Texture mapAtlasTex = new Texture(gl, context, R.drawable.map_atlas);
        Texture gemTex = new Texture(gl, context, R.drawable.ruby);
        
        int[][] mapAray = new int[][]
        {
            new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            new int[]{2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 0, 0, 1, 1, 1, 0, 2},
            new int[]{2, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 3, 0, 2},
            new int[]{2, 1, 1, 1, 2, 0, 1, 1, 0, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2},
            new int[]{2, 1, 2, 2, 2, 0, 0, 1, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 3, 2},
            new int[]{2, 1, 2, 3, 2, 1, 1, 1, 0, 2, 1, 2, 2, 2, 2, 1, 0, 0, 1, 2},
            new int[]{2, 1, 1, 1, 2, 1, 2, 2, 2, 2, 1, 2, 1, 3, 2, 3, 1, 1, 1, 2},
            new int[]{2, 0, 1, 2, 2, 1, 2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 1, 2},
            new int[]{2, 0, 1, 2, 1, 1, 1, 0, 3, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2},
            new int[]{2, 2, 1, 2, 1, 0, 1, 0, 1, 2, 1, 2, 2, 2, 1, 2, 1, 1, 1, 2},
            new int[]{2, 1, 1, 2, 1, 0, 1, 1, 1, 2, 1, 0, 1, 1, 1, 2, 2, 2, 2, 2},
            new int[]{2, 1, 1, 2, 3, 0, 1, 0, 3, 2, 3, 1, 1, 2, 1, 1, 1, 1, 3, 2},
            new int[]{2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 1, 2},
            new int[]{2, 1, 0, 0, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 2},
            new int[]{2, 1, 1, 0, 2, 1, 0, 2, 1, 2, 0, 1, 0, 0, 2, 2, 1, 2, 1, 2},
            new int[]{2, 0, 1, 0, 2, 1, 0, 2, 1, 2, 1, 3, 1, 1, 2, 1, 1, 2, 1, 2},
            new int[]{2, 0, 1, 1, 1, 1, 1, 2, 1, 2, 1, 0, 2, 2, 2, 1, 2, 2, 1, 2},
            new int[]{2, 0, 3, 0, 2, 0, 1, 2, 1, 2, 3, 1, 2, 1, 1, 1, 1, 2, 1, 2},
            new int[]{2, 0, 0, 0, 2, 3, 1, 1, 1, 2, 0, 1, 1, 1, 2, 0, 1, 1, 3, 2},
            new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        };
        
        map = new Map(mapAray, mapAtlasTex, (GL11) gl);
        itemSet = new ItemSet(gl, gemTex);
        itemSet.createItems(mapAray);
    }

    public Map getMap()
    {
        return map;
    }

    public ItemSet getItemSet()
    {
        return itemSet;
    }
}
