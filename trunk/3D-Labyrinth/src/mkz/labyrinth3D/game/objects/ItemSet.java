package mkz.labyrinth3D.game.objects;

import java.util.LinkedList;
import java.util.List;
import javax.microedition.khronos.opengles.GL11;
import mkz.labyrinth3D.math.Vector3;

/**
 * Set of collectable items (gems).
 * Has only one 3D model for all gems for much better performance.
 * 
 * @author Hans
 */
public class ItemSet implements Renderable
{
    /**List of gems*/
    private List<Item> items;
    /**Gems are displayed or not*/
    private boolean displayed;
    
    /**
     * Creates new empty item set.
     * @param gl        OPENGL context
     * @param texture   Texture
     */
    public ItemSet(GL11 gl, Texture texture)
    {
        Item.createGemVBO(gl, texture);
        items = new LinkedList<Item>();
        displayed = true;
    }
    
    /**
     * Creates items from level map.
     * @param mapArray Level map
     */
    public void createItems(int[][] mapArray)
    {
        for (int i = 0; i < mapArray.length; i++)
        {
            for (int j = 0; j < mapArray[0].length; j++)
            {
                if (mapArray[i][j] == 3)
                {
                    Vector3 position = new Vector3(j + 0.5f, i + 0.5f, 0.36f);
                    items.add(new Item(position));
                }
            }
        }
    }

    /**
     * Detects colision with ball.
     * @param ball  Ball    
     * @return      colision status
     */
    public boolean colidesWith(Ball ball)
    {
        boolean colision = false;
        for (Item item : items)
        {
            if (item.colidesWith(ball))
            {
                colision = true;
                break;
            }
        }
        return colision;
    }

    /**
     * Finds item colliding with ball.
     * @param ball  Ball
     * @return      Colliding item or null if none
     */
    public Item findColision(Ball ball)
    {
        for (Item item : items)
        {
            if (item.colidesWith(ball))
            {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Renders the item set.
     * @param gl            OPENGL context
     * @param camPosition   Camera position
     */
    public void render(GL11 gl, Vector3 camPosition)
    {
        if (Item.getGem() == null)
        {
            return;
        }
        Item.bindBuffers(gl);
        for (Item item : items)
        {
            if (item.displayed())
            {
                item.render(gl, camPosition);
            }
        }
        Item.cleanBuffers(gl);
    }

    /**
     * Returns 0. Has no use.
     * @param from  position from
     * @return      0
     */
    public float distance(Vector3 from)
    {
        return Math.abs(from.z);
    }

    /**
     * Returns zero Vector. Has no use.
     * @return zero vector
     */
    public Vector3 position()
    {
        return new Vector3();
    }

    /**
     * Updates angle and height of items.
     * @param time time in ms
     */
    public void update(long time)
    {
        for (Item item : items)
        {
            item.update(time);
        }
    }

    /**
     * Return if gems are displayed.
     * @return displayed
     */
    public boolean displayed()
    {
        return displayed;
    }

    /**
     * Sets if gems are displayed.
     * @param displayed displayed
     */
    public void setDisplayed(boolean displayed)
    {
        this.displayed = displayed;
    }

    /**
     * Cleans up resources.
     * @param gl OPENGL context
     */
    public void destroy(GL11 gl)
    {
        for (Item item : items)
        {
            item.destroy(gl);
        }
    }

    /**
     * Returns list of items/gems.
     * @return gems
     */
    public List<Item> getItems()
    {
        return items;
    }
    
    /**
     * Returns remaining amount of gems.
     * @return gem count
     */
    public int getRemaining()
    {
        int count = 0;
        for (Item item : items)
        {
            if (item.displayed())
            {
                count++;
            }
        }
        return count;
    }
}
