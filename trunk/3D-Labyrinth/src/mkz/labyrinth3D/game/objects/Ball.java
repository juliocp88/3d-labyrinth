package mkz.labyrinth3D.game.objects;

import android.graphics.Rect;
import mkz.labyrinth3D.math.Vector2;
import mkz.labyrinth3D.math.Vector3;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Player ball.
 * @author Hans
 */
public class Ball extends Object3D
{
    /**Radius in meters*/
    public static final float BALL_RADIUS = 0.3f;
    /**Radius in meters*/
    public static final float MAX_VELOCITY = 6f;
    
    /**Bounding box for ball*/
    public Rect boundingBox;
    /**Ball radius*/
    private float radius;
    /**Number of slices on ball*/
    private int slices;
    /**Number of stacks on ball*/
    private int stacks;
    /**Buffer for slices*/
    private FloatBuffer[] slicesBuffers;
    /**Buffer for normals*/
    private FloatBuffer[] normalsBuffers;
    /**Buffer for texture coordinates*/
    private FloatBuffer[] texCoordsBuffers;
    /**Buffer for indices*/
    private ShortBuffer[] indicesBuffers;
    /**Ball position*/
    private Vector3 position;
    /**Ball velocity*/
    private Vector2 velocity;
    /**Ball rotation*/
    private Vector2 rotation;

    /**
     * Creates new ball.
     * 
     * @param radius    Ball radius
     * @param slices    Number of slices
     * @param stacks    Nuber of stacks
     * @param gl        OPENGL context
     * @param texture   Texture
     */
    public Ball(float radius, int slices, int stacks, GL11 gl, Texture texture)
    {
        this.radius = radius;
        this.stacks = stacks;
        this.slices = slices;
        createSphere(gl);
        this.texture = texture;

        slicesBuffers = null;
        normalsBuffers = null;
        texCoordsBuffers = null;
        indicesBuffers = null;

        position = new Vector3(-(float) Math.PI, (float) Math.PI, 1);
        velocity = new Vector2(0, 0);
        rotation = new Vector2(0, 0);
        int sizeCentimeters = (int) (BALL_RADIUS * 100);

        boundingBox = new Rect();
        boundingBox.left = (int) (-position.x * 100 - sizeCentimeters);
        boundingBox.top = (int) (position.y * 100 - sizeCentimeters);
        boundingBox.right = (int) (-position.x * 100 + sizeCentimeters);
        boundingBox.bottom = (int) (position.y * 100 + sizeCentimeters);
    }

    /**
     * Creates 3D sphere.
     * @param gl OPENGL context
     */
    private void createSphere(GL11 gl)
    {
        slicesBuffers = new FloatBuffer[slices];
        normalsBuffers = new FloatBuffer[slices];
        texCoordsBuffers = new FloatBuffer[slices];
        indicesBuffers = new ShortBuffer[slices];

        for (int i = 0; i < slices; i++)
        {
            float[] vertexCoords = new float[6 * (stacks + 1)];
            float[] normalCoords = new float[6 * (stacks + 1)];
            float[] textureCoords = new float[4 * (stacks + 1)];
            short[] indicesCoords = new short[6 * stacks];

            double alpha0 = i * (2 * Math.PI) / slices;
            double alpha1 = (i + 1) * (2 * Math.PI) / slices;

            float cosAlpha0 = (float) Math.cos(alpha0);
            float sinAlpha0 = (float) Math.sin(alpha0);
            float cosAlpha1 = (float) Math.cos(alpha1);
            float sinAlpha1 = (float) Math.sin(alpha1);

            for (int j = 0; j <= stacks; j++)
            {
                double beta = j * Math.PI / stacks - Math.PI / 2;

                float cosBeta = (float) Math.cos(beta);
                float sinBeta = (float) Math.sin(beta);

                vertexCoords[6 * j] = radius * cosBeta * cosAlpha1;
                vertexCoords[6 * j + 1] = radius * sinBeta;
                vertexCoords[6 * j + 2] = radius * cosBeta * sinAlpha1;

                vertexCoords[6 * j + 3] = radius * cosBeta * cosAlpha0;
                vertexCoords[6 * j + 3 + 1] = radius * sinBeta;
                vertexCoords[6 * j + 3 + 2] = radius * cosBeta * sinAlpha0;

                normalCoords[6 * j] = cosBeta * cosAlpha1;
                normalCoords[6 * j + 1] = sinBeta;
                normalCoords[6 * j + 2] = cosBeta * sinAlpha1;

                normalCoords[6 * j + 3] = cosBeta * cosAlpha0;
                normalCoords[6 * j + 3 + 1] = sinBeta;
                normalCoords[6 * j + 3 + 2] = cosBeta * sinAlpha0;

                textureCoords[4 * j] = ((float) (i + 1)) / slices;
                textureCoords[4 * j + 1] = ((float) j) / stacks;

                textureCoords[4 * j + 2] = ((float) i) / slices;
                textureCoords[4 * j + 2 + 1] = ((float) j) / stacks;

                if (j != stacks)
                {
                    indicesCoords[6 * j + 0] = (short) (i * 2 * (stacks + 1) + 2 * j + 0);
                    indicesCoords[6 * j + 1] = (short) (i * 2 * (stacks + 1) + 2 * j + 1);
                    indicesCoords[6 * j + 2] = (short) (i * 2 * (stacks + 1) + 2 * j + 2);

                    indicesCoords[6 * j + 3] = (short) (i * 2 * (stacks + 1) + 2 * j + 1);
                    indicesCoords[6 * j + 4] = (short) (i * 2 * (stacks + 1) + 2 * j + 3);
                    indicesCoords[6 * j + 5] = (short) (i * 2 * (stacks + 1) + 2 * j + 2);
                }
            }
            slicesBuffers[i] = FloatBuffer.wrap(vertexCoords);
            normalsBuffers[i] = FloatBuffer.wrap(normalCoords);
            texCoordsBuffers[i] = FloatBuffer.wrap(textureCoords);
            indicesBuffers[i] = ShortBuffer.wrap(indicesCoords);
        }

        //Vertexes
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(slices * slicesBuffers[0].capacity() * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        for (int i = 0; i < slices; i++)
        {
            vertexBuffer.position(slicesBuffers[i].capacity() * i);
            vertexBuffer.put(slicesBuffers[i]);
        }
        vertexBuffer.position(0);

        int[] buffer = new int[1];
        gl.glGenBuffers(1, buffer, 0);
        vbo = buffer[0];
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, slices * slicesBuffers[0].capacity() * 4, vertexBuffer, GL11.GL_STATIC_DRAW);

        //Tex coordinates
        byteBuf = ByteBuffer.allocateDirect(slices * texCoordsBuffers[0].capacity() * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        for (int i = 0; i < slices; i++)
        {
            textureBuffer.position(texCoordsBuffers[i].capacity() * i);
            textureBuffer.put(texCoordsBuffers[i]);
        }
        textureBuffer.position(0);

        buffer = new int[1];
        gl.glGenBuffers(1, buffer, 0);
        tbo = buffer[0];
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, tbo);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, slices * texCoordsBuffers[0].capacity() * 4, textureBuffer, GL11.GL_STATIC_DRAW);

        //Normals
        byteBuf = ByteBuffer.allocateDirect(slices * normalsBuffers[0].capacity() * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        normalBuffer = byteBuf.asFloatBuffer();
        for (int i = 0; i < slices; i++)
        {
            normalBuffer.position(normalsBuffers[i].capacity() * i);
            normalBuffer.put(normalsBuffers[i]);
        }
        normalBuffer.position(0);

        buffer = new int[1];
        gl.glGenBuffers(1, buffer, 0);
        nbo = buffer[0];
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, nbo);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, slices * normalsBuffers[0].capacity() * 4, normalBuffer, GL11.GL_STATIC_DRAW);

        //Indices
        byteBuf = ByteBuffer.allocateDirect(slices * indicesBuffers[0].capacity() * 2);
        byteBuf.order(ByteOrder.nativeOrder());
        indexBuffer = byteBuf.asShortBuffer();
        for (int i = 0; i < slices; i++)
        {
            indexBuffer.position(indicesBuffers[i].capacity() * i);
            indexBuffer.put(indicesBuffers[i]);
        }
        indexBuffer.position(0);

        buffer = new int[1];
        gl.glGenBuffers(1, buffer, 0);
        ibo = buffer[0];
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, ibo);
        gl.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, slices * indicesBuffers[0].capacity() * 2, indexBuffer, GL11.GL_STATIC_DRAW);

        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Moves the ball (roll).
     * @param acceleration  Acceleration vector
     * @param time          Acceleration duration in ms
     */
    public void move(Vector3 acceleration, long time)
    {
        float duration = ((float) time) / 1000.0f;
        float velocityLimit = MAX_VELOCITY * duration;
        float velocityDecrease = MAX_VELOCITY * duration * acceleration.xy().size();

        Vector2 deaceleration = velocity.mul(velocity.size());
        velocity = velocity.add((acceleration.xy().sub(deaceleration)).mul(duration));
        if (velocity.size() > velocityDecrease)
        {
            velocity = velocity.sub(velocity.mul(0.1f * (velocity.size() - velocityDecrease) / velocity.size()));
        }
        if (velocity.size() > velocityLimit)
        {
            velocity.setSize(velocityLimit);
        }

        position.x = position.x - (velocity.y);
        position.y = position.y + (velocity.x);

        int sizeCentimeters = (int) (BALL_RADIUS * 100);
        boundingBox.left = (int) (-position.x * 100 - sizeCentimeters);
        boundingBox.top = (int) (position.y * 100 - sizeCentimeters);
        boundingBox.right = (int) (-position.x * 100 + sizeCentimeters);
        boundingBox.bottom = (int) (position.y * 100 + sizeCentimeters);
    }

    /**
     * Moves the ball on new position (teleport).
     * @param newPosition   new position
     * @param axxis         bounce axxis if the ball bounced of a wall
     */
    public void move(Vector3 newPosition, int axxis)
    {
        //rotation.x += (newPosition.x - position.x) / BALL_RADIUS * 57.3f;
        //rotation.y += (newPosition.y - position.y) / BALL_RADIUS * 57.3f;

        if (axxis == 0)
        {
            velocity.x = 0;
        }
        else if (axxis == 1)
        {
            velocity.y = 0;
        }

        position.x = newPosition.x;
        position.y = newPosition.y;
        position.z = newPosition.z;

        int sizeCentimeters = (int) (BALL_RADIUS * 100);
        boundingBox.left = (int) (-position.x * 100 - sizeCentimeters);
        boundingBox.top = (int) (position.y * 100 - sizeCentimeters);
        boundingBox.right = (int) (-position.x * 100 + sizeCentimeters);
        boundingBox.bottom = (int) (position.y * 100 + sizeCentimeters);
    }

    /**
     * Ball does not colide with ball.
     * @param ball  Ball
     * @return      False
     */
    @Override
    public boolean colidesWith(Ball ball)
    {
        return false;
    }

    /**
     * Renders the ball.
     * @param gl            OPENGL context.
     * @param camPosition   camera position
     */
    @Override
    public void render(GL11 gl, Vector3 camPosition)
    {
        gl.glPushMatrix();

        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, camPosition.z + BALL_RADIUS);
        gl.glScalef(position.z, position.z, position.z);

        rotation.x -= velocity.y / BALL_RADIUS * 57.3f;
        rotation.y -= velocity.x / BALL_RADIUS * 57.3f;

        //gl.glRotatef(rotation.y, (float) Math.cos(rotation.x / 57.3f), 0, (float) Math.sin(rotation.x / 57.3f));
        //gl.glRotatef(rotation.x, 0, 1, 0);

        //Vector3 direction = new Vector3(velocity.x, velocity.y, 0).cross(new Vector3(0, 0, 1));
        //direction = direction.unit();
        //gl.glRotatef(rotation.size(), direction.x, direction.y, direction.z);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.texPointer);
        gl.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glEnable(GL10.GL_COLOR_MATERIAL);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glFrontFace(GL10.GL_CW);

        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, tbo);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, nbo);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, 0);       

        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, ibo);
        gl.glDrawElements(GL10.GL_TRIANGLES, indexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, 0);

        gl.glDisable(GL10.GL_CULL_FACE);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

        gl.glPopMatrix();
    }

    /**
     * Distance from position.
     * @param from  position
     * @return      distance
     */
    @Override
    public float distance(Vector3 from)
    {
        return Vector3.vectorDistance(from, position);
    }

    /**
     * Ball position.
     * @return position
     */
    @Override
    public Vector3 position()
    {
        return position;
    }

    /**
     * Updates ball.
     * @param time time in ms
     */
    @Override
    public void update(long time)
    {
        //Does nothing
    }

    /**
     * Ball is always displayed.
     * @return true
     */
    @Override
    public boolean displayed()
    {
        return true;
    }

    /**
     * Ball is always displayed.
     * @param displayed does nothing
     */
    @Override
    public void setDisplayed(boolean displayed)
    {
        //Does nothing
    }

    /**
     * Cleans up resources.
     * @param gl OPENGL context
     */
    @Override
    public void destroy(GL11 gl)
    {
        texture.destroy(gl);
        int[] buffers = new int[]
        {
            vbo, tbo, nbo, ibo
        };
        gl.glDeleteBuffers(4, buffers, 0);
    }
}
