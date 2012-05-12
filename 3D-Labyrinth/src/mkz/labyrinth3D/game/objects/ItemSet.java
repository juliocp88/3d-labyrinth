/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mkz.labyrinth3D.game.objects;

import java.util.LinkedList;
import java.util.List;
import javax.microedition.khronos.opengles.GL11;
import mkz.labyrinth3D.math.Vector3;

/**
 *
 * @author Hans
 */
public class ItemSet implements Renderable
{
    private List<Item> items;
    private boolean displayed;
    
    public ItemSet(GL11 gl, Texture texture)
    {
        Item.createGemVBO(gl, texture);
        items = new LinkedList<Item>();
        displayed = true;
    }
    
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

    public float distance(Vector3 from)
    {
        return Math.abs(from.z);
    }

    public Vector3 position()
    {
        return new Vector3();
    }

    public void update(long time)
    {
        for (Item item : items)
        {
            item.update(time);
        }
    }

    public boolean displayed()
    {
        return displayed;
    }

    public void setDisplayed(boolean displayed)
    {
        this.displayed = displayed;
    }

    public void destroy(GL11 gl)
    {
        for (Item item : items)
        {
            item.destroy(gl);
        }
    }

    public List<Item> getItems()
    {
        return items;
    }
    
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
