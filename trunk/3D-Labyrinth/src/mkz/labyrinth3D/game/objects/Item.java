package mkz.labyrinth3D.game.objects;

import android.graphics.Rect;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import mkz.labyrinth3D.math.Vector3;

/**
 * Collectable item.
 * @author Hans
 */
public class Item
{
    /**Item size in cm*/
    private static int SIZE = 60;
    /**3D model*/
    private static Ball gem = null;
    /**Position*/
    private Vector3 position;
    /**Collected property*/
    private boolean collected;
    /**Bounding box for colision*/
    private Rect boundingBox;
    /**Rotation angle*/
    private float rotation;
    /**Height*/
    private float height;

    /**
     * Creates new item on defined position.
     * @param position position
     */
    public Item(Vector3 position)
    {
        this.position = position;
        collected = false;
        boundingBox = new Rect();
        boundingBox.left = (int) (position.x * 100 - SIZE / 2);
        boundingBox.top = (int) (position.y * 100 - SIZE / 2);
        boundingBox.right = (int) (position.x * 100 + SIZE / 2);
        boundingBox.bottom = (int) (position.y * 100 + SIZE / 2);
        rotation = (float) (Math.random() * 360);
    }

    /**
     * Creates gem 3D model (same for all items)
     * @param gl        OPENGL context
     * @param texture   texture
     */
    public static void createGemVBO(GL11 gl, Texture texture)
    {
        gem = new Ball(0.3f, 5, 2, gl, texture);
    }

    /**
     * Detects colision with ball.
     * @param ball  Player ball
     * @return      colision status
     */
    public boolean colidesWith(Ball ball)
    {
        return Rect.intersects(boundingBox, ball.boundingBox);
    }

    /**
     * Binds buffers for items. 
     * @param gl OPENGL context
     */
    public static void bindBuffers(GL11 gl)
    {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gem.texture.texPointer);
        gl.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        gl.glDisable(GL10.GL_CULL_FACE);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glFrontFace(GL10.GL_CCW);

        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, gem.vbo);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, gem.tbo);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, gem.nbo);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, 0);

        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, gem.ibo);
    }

    /**
     * Cleans buffers.
     * @param gl OPENGL context
     */
    public static void cleanBuffers(GL11 gl)
    {
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }

    /**
     * Renders one item.
     * @param gl            OPENGL context
     * @param camPosition   camera position
     */
    public void render(GL11 gl, Vector3 camPosition)
    {
        gl.glPushMatrix();

        gl.glLoadIdentity();
        gl.glTranslatef(camPosition.x + position.x, camPosition.y - position.y, camPosition.z + position.z + height);
        gl.glRotatef(80, 1, 0, 0);
        gl.glRotatef(rotation, 0, 0.98f, 0.17f);
        gl.glRotatef(rotation * -2.0f, 0, 1, 0);

        gl.glDrawElements(GL10.GL_TRIANGLES, gem.indexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, 0);

        gl.glPopMatrix();
    }

    /**
     * Returns distance from position.
     * @param from  position
     * @return      distance
     */
    public float distance(Vector3 from)
    {
        return Vector3.vectorDistance(from, position);
    }

    /**
     * Item position.
     * @return position
     */
    public Vector3 position()
    {
        return position;
    }

    /**
     * Updates item rotation and height in time.
     * @param time time in ms
     */
    public void update(long time)
    {
        float timeF = time / 1000f;
        rotation += 45 * timeF;
        rotation = rotation % 360;
        height = (float) Math.sin(rotation / 10f) * 0.06f;
    }

    /**
     * Returns if item should be displayed.
     * @return displayed
     */
    public boolean displayed()
    {
        return !collected;
    }

    /**
     * Sets if the item should be displayed.
     * @param displayed displayed
     */
    public void setDisplayed(boolean displayed)
    {
        collected = !displayed;
    }

    /**
     * Returns 3D model.
     * @return 3D model of item
     */
    public static Ball getGem()
    {
        return gem;
    }

    /**
     * Cleans up resources.
     * @param gl OPENGL context
     */
    public void destroy(GL11 gl)
    {
        if (gem != null)
        {
            gem.destroy(gl);
            gem = null;
        }
    }
}
