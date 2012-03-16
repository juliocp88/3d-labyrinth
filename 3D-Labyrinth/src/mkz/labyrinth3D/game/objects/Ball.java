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
 *
 * @author Hans
 */
public class Ball extends Object3D
{
    /**Radius in meters*/
    public static final float BALL_RADIUS = 0.3f;
    /**Radius in meters*/
    public static final float MAX_VELOCITY = 6f;
    /**Weight in kilograms*/
    public static final float BALL_MASS = 350.0f;
    
    public Rect boundingBox;
    
    private float radius;
    private int slices;
    private int stacks;
    private FloatBuffer[] slicesBuffers;
    private FloatBuffer[] normalsBuffers;
    private FloatBuffer[] texCoordsBuffers;
    private ShortBuffer[] indicesBuffers;
    
    private Vector3 position;
    private Vector2 velocity;   
    private Vector2 rotation;
    
    float test;

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
        
        position = new Vector3(-(float)Math.PI, (float)Math.PI, 0);
        velocity = new Vector2(0, 0);
        rotation = new Vector2(0, 0);
        int sizeCentimeters = (int) (BALL_RADIUS * 100);
        
        boundingBox = new Rect();
        boundingBox.left = (int) (-position.x * 100 - sizeCentimeters);
        boundingBox.top = (int) (position.y * 100 - sizeCentimeters);
        boundingBox.right = (int) (-position.x * 100 + sizeCentimeters);
        boundingBox.bottom = (int) (position.y * 100 + sizeCentimeters);
    }

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
    
    public void move(Vector3 acceleration, long time)
    {
        float duration = ((float) time) / 1000.0f;
        float velocityLimit = MAX_VELOCITY * duration;
        
        Vector2 deaceleration = velocity.mul(8.5f);
        velocity = velocity.add((acceleration.xy().sub(deaceleration)).mul(duration));
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
    
    public void move(Vector3 newPosition, int axxis)
    {       
        //rotation.x += (newPosition.x - position.x) / BALL_RADIUS * 57.3f;
        //rotation.y += (newPosition.y - position.y) / BALL_RADIUS * 57.3f;
        
        if (axxis == 0)
        {
            velocity.x = 0;
        }
        else if(axxis == 1)
        {
            velocity.y = 0;
        }
        
        position.x = newPosition.x;
        position.y = newPosition.y;
        
        int sizeCentimeters = (int) (BALL_RADIUS * 100);
        boundingBox.left = (int) (-position.x * 100 - sizeCentimeters);
        boundingBox.top = (int) (position.y * 100 - sizeCentimeters);
        boundingBox.right = (int) (-position.x * 100 + sizeCentimeters);
        boundingBox.bottom = (int) (position.y * 100 + sizeCentimeters);
    }
    
    public void bounce(Vector2 normal)
    {
        //this.Velocity = velocity - (2 * (velocity * normal)) * normal;
        velocity = velocity.sub(normal.mul(velocity.dot(normal) * 2));
        
        velocity.x = -velocity.x;
        velocity.y = -velocity.y;    
        
        rotation.x -= velocity.x / BALL_RADIUS * 57.3f;
        rotation.y -= velocity.y / BALL_RADIUS * 57.3f;
        
        position.x = position.x - (velocity.y);
        position.y = position.y + (velocity.x);
        
        int sizeCentimeters = (int) (BALL_RADIUS * 100);
        boundingBox.left = (int) (-position.x * 100 - sizeCentimeters);
        boundingBox.top = (int) (position.y * 100 - sizeCentimeters);
        boundingBox.right = (int) (-position.x * 100 + sizeCentimeters);
        boundingBox.bottom = (int) (position.y * 100 + sizeCentimeters);
    }

    @Override
    public boolean colidesWith(Ball ball)
    {
        return false;
    }

    @Override
    public void render(GL11 gl, Vector3 camPosition)
    {       
        gl.glPushMatrix();
        
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, camPosition.z + BALL_RADIUS);
        gl.glScalef(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS); 
        
        rotation.x -= velocity.y / BALL_RADIUS * 57.3f;
        rotation.y -= velocity.x / BALL_RADIUS * 57.3f;

        //gl.glRotatef(rotation.size(), rotation.y / rotation.size(), rotation.x / rotation.size(), 0);
        
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.texPointer);
        gl.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        gl.glEnable(GL10.GL_CULL_FACE);

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

    @Override
    public float distance(Vector3 from)
    {
        return Vector3.vectorDistance(from, position);
    }

    @Override
    public Vector3 position()
    {
        return position;
    }

    @Override
    public void update(long time)
    {
        float duration = ((float) time) / 1000.0f;
        
    }

    @Override
    public boolean displayed()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDisplayed(boolean displayed)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void destroy(GL11 gl)
    {
        texture.destroy(gl);
        int[] buffers = new int[]{vbo, tbo, nbo, ibo};
        gl.glDeleteBuffers(4, buffers, 0);
    }
}
