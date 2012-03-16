/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mkz.labyrinth3D.game.objects;

import javax.microedition.khronos.opengles.GL11;
import mkz.labyrinth3D.math.Vector3;

/**
 *
 * @author Hans
 */
public interface Renderable
{   
    public abstract boolean colidesWith(Ball ball);
    public abstract void render(GL11 gl, Vector3 camPosition);
    public abstract float distance(Vector3 from);
    public abstract Vector3 position();
    public abstract void update(long time);
    public abstract boolean displayed();
    public abstract void setDisplayed(boolean displayed);
    public abstract void destroy(GL11 gl);
}
