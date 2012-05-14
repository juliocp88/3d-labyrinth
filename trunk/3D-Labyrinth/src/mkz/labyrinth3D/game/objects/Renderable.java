package mkz.labyrinth3D.game.objects;

import javax.microedition.khronos.opengles.GL11;
import mkz.labyrinth3D.math.Vector3;

/**
 * Interface for renderable objects.
 * @author Hans
 */
public interface Renderable
{   
    /**
     * Returns if object collides with ball.
     * @param ball  Ball
     * @return collision status
     */
    public abstract boolean colidesWith(Ball ball);
    
    /**
     * Rensers the object.
     * @param gl    OPENGL context
     * @param camPosition camera position
     */
    public abstract void render(GL11 gl, Vector3 camPosition);
    
    /**
     * Counts distance from position.
     * @param from  position
     * @return      distance
     */
    public abstract float distance(Vector3 from);
    
    /**
     * Object position.
     * @return position
     */
    public abstract Vector3 position();
    
    /**
     * Updates object in time.
     * @param time time in ms
     */
    public abstract void update(long time);
    
    /**
     * Returns if object is displayed.
     * @return displayed
     */
    public abstract boolean displayed();
    
    /**
     * Sets displayed parameter.
     * @param displayed displayed
     */
    public abstract void setDisplayed(boolean displayed);
    
    /**
     * Cleans up.
     * @param gl OPENGL context
     */
    public abstract void destroy(GL11 gl);
}
