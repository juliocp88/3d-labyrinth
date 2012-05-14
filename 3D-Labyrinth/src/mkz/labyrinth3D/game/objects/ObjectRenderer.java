package mkz.labyrinth3D.game.objects;

import mkz.labyrinth3D.math.Vector3;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Obeject 3D renderer. Renders all registered objects.
 * Handles object order for rendering (for alpha rendering).
 * @author Hans
 */
public class ObjectRenderer
{
    /**Default maximum number of objects*/
    public static final int MAX_OBJECTS = 500;
    
    /**Array of asociated objects*/
    private Renderable[] objects;
    /**Array of sorted indexes*/
    private int[] indexes;
    /**Current object count*/
    private int objectCount;

    /**
     * Creates new renderer.
     * @param maxObjects maximal object count
     */
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

    /**
     * Adds new object to array.
     * @param addedObject   Added object
     * @return              0 if succesfull -1 othewise
     */
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

    /**
     * Sorts objects by distance from camera.
     * @param camPosition camera position
     */
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

    /**
     * Renders all abjects.
     * @param gl            OPENGL context
     * @param camPosition   camera position
     */
    public void render(GL10 gl, Vector3 camPosition)
    {
        //sort(camPosition);
        for (int i = 0; i < objectCount; i++)
        {
            objects[indexes[i]].render((GL11) gl, camPosition);
        }
    }

    /**
     * Updates all objects in time.
     * @param time time in ms
     */
    public void update(long time)
    {
        for (int i = 0; i < objectCount; i++)
        {
            objects[i].update(time);
        }
    }

    /**
     * Clears resources for all objects.
     * @param gl OPENGL context
     */
    public void destroy(GL11 gl)
    {
        for (int i = 0; i < objectCount; i++)
        {
            objects[i].destroy(gl);
        }
        System.out.println("GL11: " + objectCount + " objects succesfully unloaded.");
    }
}
