package mkz.labyrinth3D.game;

import android.content.Context;
import mkz.labyrinth3D.R;
import mkz.labyrinth3D.game.objects.Texture;
import mkz.labyrinth3D.game.objects.Map;
import javax.microedition.khronos.opengles.GL11;
import mkz.labyrinth3D.game.objects.ItemSet;
import mkz.labyrinth3D.math.Vector3;

/**
 *
 * @author Hans
 */
public class Level
{
    private Map map;
    private ItemSet itemSet;
    private Context context;
    private Vector3 ballPosition;
    
    public Level(Context context)
    {
        this.context = context;
    }
    
    public void load(int ID, GL11 gl)
    {
        Texture mapAtlasTex = new Texture(gl, context, R.drawable.map_atlas);
        Texture gemTex = new Texture(gl, context, R.drawable.ruby);
        
        int[][] mapArray = LevelMaps.getLevel(ID);
        
        map = new Map(mapArray, mapAtlasTex, (GL11) gl);
        itemSet = new ItemSet(gl, gemTex);
        itemSet.createItems(mapArray);
        for (int i = 0; i < mapArray.length; i++)
        {
            for (int j = 0; j < mapArray[0].length; j++)
            {
                if (mapArray[i][j] == 4)
                {
                    ballPosition = new Vector3(-j - 0.5f, i + 0.5f, 1);
                }
            }
        }
    }

    public Map getMap()
    {
        return map;
    }

    public ItemSet getItemSet()
    {
        return itemSet;
    }

    public Vector3 getBallPosition()
    {
        if (ballPosition == null)
        {
            return new Vector3(-(float) Math.PI, (float) Math.PI, 1);
        }
        return ballPosition;
    }
}
