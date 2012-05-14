package mkz.labyrinth3D.game.objects;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * 3D object adds some attributes to renderable interface.
 * @author Hans
 */
public abstract class Object3D implements Renderable
{
    /**Verex array buffer*/
	protected FloatBuffer vertexBuffer;
    /**Texture coordinates array buffer*/
	protected FloatBuffer textureBuffer;
    /**Indices array buffer*/
	protected ShortBuffer indexBuffer;
    /**Normal array buffer*/
	protected FloatBuffer normalBuffer;
    /**Object texture*/
    protected Texture texture;
    /**vertices array*/
    protected float vertices[];
    /**normal array*/
    protected float normals[];
    /**Texture coordinates array*/
    protected float texCoord[];
    /**Indices array*/
    protected short indices[];
    /**Vertex buffer object*/
    protected int vbo;
    /**Normal buffer object*/
    protected int nbo;
    /**Texture buffer object*/
    protected int tbo;
    /**Index buffer object*/
    protected int ibo;
}
