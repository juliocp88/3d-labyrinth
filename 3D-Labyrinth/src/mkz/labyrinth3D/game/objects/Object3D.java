package mkz.labyrinth3D.game.objects;

import mkz.labyrinth3D.math.Vector3;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL11;

/**
 *
 * @author Hans
 */
public abstract class Object3D
{
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer textureBuffer;
	protected ShortBuffer indexBuffer;
	protected FloatBuffer normalBuffer;
    protected Texture texture;
    protected float vertices[];
    protected float normals[];
    protected float texCoord[];
    protected short indices[];
    
    public abstract boolean colidesWith(Ball ball);
    public abstract void render(GL11 gl, Vector3 camPosition);
    public abstract float distance(Vector3 from);
    public abstract Vector3 position();
    public abstract void update(long time);
    public abstract boolean displayed();
    public abstract void setDisplayed(boolean displayed);
    public abstract void destroy(GL11 gl);
}
