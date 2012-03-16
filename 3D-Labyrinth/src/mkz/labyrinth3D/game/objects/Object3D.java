package mkz.labyrinth3D.game.objects;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author Hans
 */
public abstract class Object3D implements Renderable
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
    protected int vbo;
    protected int nbo;
    protected int tbo;
    protected int ibo;
}
