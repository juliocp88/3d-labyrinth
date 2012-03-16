package mkz.labyrinth3D.game.objects;

import mkz.labyrinth3D.math.Vector3;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 *
 * @author Hans
 */
public class ObjectRenderer
{
    public static final int MAX_OBJECTS = 500;
    
    private Renderable[] objects;
    private int[] indexes;
    private int objectCount;

    public ObjectRenderer(int maxObjects)
    {
        objects = new Renderable[maxObjects];
        indexes = new int[maxObjects];
        objectCount = 0;
        for (int i = 0; i < maxObjects; i++)
        {
            indexes[i] = i;
        }
    }

    public int addObject(Renderable addedObject)
    {
        if (objectCount >= objects.length)
        {
            return -1;
        }
        else
        {
            objects[objectCount] = addedObject;
            objectCount++;
            return 0;
        }
    }

    private void sort(Vector3 camPosition)
    {
        int tmp;
        boolean change = true;
        while (change)
        {
            change = false;
            for (int i = 0; i < objectCount - 1; i++)
            {
                if (objects[indexes[i]].distance(camPosition) < objects[indexes[i + 1]].distance(camPosition))
                {
                    change = true;
                    tmp = indexes[i];
                    indexes[i] = indexes[i + 1];
                    indexes[i + 1] = tmp;
                }
            }
        }
    }

    public void render(GL10 gl, Vector3 camPosition)
    {
        //sort(camPosition);
        for (int i = 0; i < objectCount; i++)
        {
            objects[indexes[i]].render((GL11) gl, camPosition);
        }
    }

    public void update(long time)
    {
        for (int i = 0; i < objectCount; i++)
        {
            objects[i].update(time);
        }
    }

    public void destroy(GL11 gl)
    {
        for (int i = 0; i < objectCount; i++)
        {
            objects[i].destroy(gl);
        }
        System.out.println("GL11: " + objectCount + " objects unloaded.");
    }
}
