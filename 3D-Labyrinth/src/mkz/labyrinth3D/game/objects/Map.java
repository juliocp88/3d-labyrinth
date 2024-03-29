package mkz.labyrinth3D.game.objects;

import android.graphics.Rect;
import mkz.labyrinth3D.math.Vector2;
import mkz.labyrinth3D.math.Vector3;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * 3D map of playbable area.
 * @author Hans
 */
public class Map extends Object3D
{
    /**Vertices count*/
    private int verticesCount;
    /**Triangle count*/
    private int triangleCount;
    /**Wall colisoun bounding boxes array*/
    private Rect[] wallBoundingBoxes;
    /**Hole colision positions array*/
    private Vector2[] holePositions;
    /**Level map*/
    private int[][] map;

    /**
     * Creates new map.
     * 
     * @param mapArray  Level map
     * @param texture   Texture atlas
     * @param gl        OPENGL context
     */
    public Map(int[][] mapArray, Texture texture, GL11 gl)
    {
        this.map = new int[mapArray.length][mapArray[0].length];
        this.texture = texture;
        verticesCount = 0;
        triangleCount = 0;
        int wallCount = 0;
        int holeCount = 0;

        //Creates local copy without special tiles
        for (int i = 0; i < mapArray.length; i++)
        {
            for (int j = 0; j < mapArray[0].length; j++)
            {
                if (mapArray[i][j] > 2)
                {
                    map[i][j] = 1;
                }
                else
                {
                    map[i][j] = mapArray[i][j];
                }
            }
        }

        //Counts triangles and vertex count
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[0].length; j++)
            {
                if (map[i][j] == 0 || map[i][j] == 1)
                {
                    verticesCount += 4;
                    triangleCount += 2;
                    if (map[i][j] == 0)
                    {
                        holeCount++;
                    }
                }
                else if (map[i][j] == 2)
                {
                    verticesCount += 4;
                    triangleCount += 2;
                    wallCount++;

                    if (i > 0 && map[i - 1][j] < 2)
                    {
                        verticesCount += 4;
                        triangleCount += 2;
                    }
                    if (j > 0 && map[i][j - 1] < 2)
                    {
                        verticesCount += 4;
                        triangleCount += 2;
                    }
                    if (i < map.length - 1 && map[i + 1][j] < 2)
                    {
                        verticesCount += 4;
                        triangleCount += 2;
                    }
                    if (j < map[0].length - 1 && map[i][j + 1] < 2)
                    {
                        verticesCount += 4;
                        triangleCount += 2;
                    }
                }
            }
        }

        //Alocates arrays
        vertices = new float[3 * verticesCount];
        normals = new float[3 * verticesCount];
        texCoord = new float[2 * verticesCount];
        indices = new short[3 * triangleCount];
        wallBoundingBoxes = new Rect[wallCount];
        holePositions = new Vector2[holeCount];

        int vertextIdx = 0;
        int textIdx = 0;
        int normIdx = 0;
        int indiceIdx = 0;
        wallCount = 0;
        holeCount = 0;

        Vector3 position = new Vector3();

        //Creates geometry
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[0].length; j++)
            {
                if (map[i][j] == 0) //HOLES
                {
                    //Positions
                    holePositions[holeCount++] = new Vector2((position.x + j) + 0.5f, (int) (position.y + i) + 0.5f);

                    //Vertices
                    vertices[vertextIdx++] = position.x + j;
                    vertices[vertextIdx++] = position.y - i;
                    vertices[vertextIdx++] = position.z;

                    vertices[vertextIdx++] = position.x + j + 1.0f;
                    vertices[vertextIdx++] = position.y - i;
                    vertices[vertextIdx++] = position.z;

                    vertices[vertextIdx++] = position.x + j + 1.0f;
                    vertices[vertextIdx++] = position.y - i - 1.0f;
                    vertices[vertextIdx++] = position.z;

                    vertices[vertextIdx++] = position.x + j;
                    vertices[vertextIdx++] = position.y - i - 1.0f;
                    vertices[vertextIdx++] = position.z;

                    //Normals
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    //Texture coordinates
                    Vector2[] atlasCoords = Texture.getAtlasTexCoordinates(1, 1, 2);
                    texCoord[textIdx++] = atlasCoords[0].x;
                    texCoord[textIdx++] = atlasCoords[0].y;

                    texCoord[textIdx++] = atlasCoords[1].x;
                    texCoord[textIdx++] = atlasCoords[1].y;

                    texCoord[textIdx++] = atlasCoords[2].x;
                    texCoord[textIdx++] = atlasCoords[2].y;

                    texCoord[textIdx++] = atlasCoords[3].x;
                    texCoord[textIdx++] = atlasCoords[3].y;

                    //Triangles
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 4);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);

                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 2);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);
                }
                else if (map[i][j] == 1) //FLOOR
                {
                    //Vertices
                    vertices[vertextIdx++] = position.x + j;
                    vertices[vertextIdx++] = position.y - i;
                    vertices[vertextIdx++] = position.z;

                    vertices[vertextIdx++] = position.x + j + 1.0f;
                    vertices[vertextIdx++] = position.y - i;
                    vertices[vertextIdx++] = position.z;

                    vertices[vertextIdx++] = position.x + j + 1.0f;
                    vertices[vertextIdx++] = position.y - i - 1.0f;
                    vertices[vertextIdx++] = position.z;

                    vertices[vertextIdx++] = position.x + j;
                    vertices[vertextIdx++] = position.y - i - 1.0f;
                    vertices[vertextIdx++] = position.z;

                    //Normals
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    //Texture coordinates
                    Vector2[] atlasCoords = Texture.getAtlasTexCoordinates(0, 0, 2);
                    texCoord[textIdx++] = atlasCoords[0].x;
                    texCoord[textIdx++] = atlasCoords[0].y;

                    texCoord[textIdx++] = atlasCoords[1].x;
                    texCoord[textIdx++] = atlasCoords[1].y;

                    texCoord[textIdx++] = atlasCoords[2].x;
                    texCoord[textIdx++] = atlasCoords[2].y;

                    texCoord[textIdx++] = atlasCoords[3].x;
                    texCoord[textIdx++] = atlasCoords[3].y;

                    //Triangles
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 4);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);

                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 2);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);
                }
                else if (map[i][j] == 2) //WALL
                {
                    //Bounding boxes
                    wallBoundingBoxes[wallCount++] = new Rect((int) (position.x + j) * 100, (int) (position.y + i) * 100, (int) (position.x + j) * 100 + 100, (int) (position.y + i) * 100 + 100);

                    //Vertices
                    vertices[vertextIdx++] = position.x + j;
                    vertices[vertextIdx++] = position.y - i;
                    vertices[vertextIdx++] = position.z + 1.0f;

                    vertices[vertextIdx++] = position.x + j + 1.0f;
                    vertices[vertextIdx++] = position.y - i;
                    vertices[vertextIdx++] = position.z + 1.0f;

                    vertices[vertextIdx++] = position.x + j + 1.0f;
                    vertices[vertextIdx++] = position.y - i - 1.0f;
                    vertices[vertextIdx++] = position.z + 1.0f;

                    vertices[vertextIdx++] = position.x + j;
                    vertices[vertextIdx++] = position.y - i - 1.0f;
                    vertices[vertextIdx++] = position.z + 1.0f;

                    //Normals
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    normals[normIdx++] = 0f;
                    normals[normIdx++] = 0f;
                    normals[normIdx++] = -1f;

                    //Texture coordinates
                    Vector2[] atlasCoords = Texture.getAtlasTexCoordinates(0, 1, 2);
                    texCoord[textIdx++] = atlasCoords[0].x;
                    texCoord[textIdx++] = atlasCoords[0].y;

                    texCoord[textIdx++] = atlasCoords[1].x;
                    texCoord[textIdx++] = atlasCoords[1].y;

                    texCoord[textIdx++] = atlasCoords[2].x;
                    texCoord[textIdx++] = atlasCoords[2].y;

                    texCoord[textIdx++] = atlasCoords[3].x;
                    texCoord[textIdx++] = atlasCoords[3].y;

                    //Triangles
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 4);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);

                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 2);
                    indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);

                    if (i > 0 && map[i - 1][j] < 2)
                    {
                        //Vertices
                        vertices[vertextIdx++] = position.x + j + 1.0f;
                        vertices[vertextIdx++] = position.y - i;
                        vertices[vertextIdx++] = position.z + 1.0f;

                        vertices[vertextIdx++] = position.x + j;
                        vertices[vertextIdx++] = position.y - i;
                        vertices[vertextIdx++] = position.z + 1.0f;

                        vertices[vertextIdx++] = position.x + j;
                        vertices[vertextIdx++] = position.y - i;
                        vertices[vertextIdx++] = position.z;

                        vertices[vertextIdx++] = position.x + j + 1.0f;
                        vertices[vertextIdx++] = position.y - i;
                        vertices[vertextIdx++] = position.z;

                        //Normals
                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 1f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 1f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 1f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 1f;
                        normals[normIdx++] = 0f;

                        //Texture coordinates
                        Vector2[] atlasWallCoords = Texture.getAtlasTexCoordinates(1, 0, 2);
                        texCoord[textIdx++] = atlasWallCoords[0].x;
                        texCoord[textIdx++] = atlasWallCoords[0].y;

                        texCoord[textIdx++] = atlasWallCoords[1].x;
                        texCoord[textIdx++] = atlasWallCoords[1].y;

                        texCoord[textIdx++] = atlasWallCoords[2].x;
                        texCoord[textIdx++] = atlasWallCoords[2].y;

                        texCoord[textIdx++] = atlasWallCoords[3].x;
                        texCoord[textIdx++] = atlasWallCoords[3].y;

                        //Triangles
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 4);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);

                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 2);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);
                    }
                    if (j > 0 && map[i][j - 1] < 2)
                    {

                        //Vertices
                        vertices[vertextIdx++] = position.x + j;
                        vertices[vertextIdx++] = position.y - i;
                        vertices[vertextIdx++] = position.z + 1.0f;

                        vertices[vertextIdx++] = position.x + j;
                        vertices[vertextIdx++] = position.y - i - 1.0f;
                        vertices[vertextIdx++] = position.z + 1.0f;

                        vertices[vertextIdx++] = position.x + j;
                        vertices[vertextIdx++] = position.y - i - 1.0f;
                        vertices[vertextIdx++] = position.z;

                        vertices[vertextIdx++] = position.x + j;
                        vertices[vertextIdx++] = position.y - i;
                        vertices[vertextIdx++] = position.z;

                        //Normals
                        normals[normIdx++] = -1f;
                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = -1f;
                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = -1f;
                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = -1f;
                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 0f;

                        //Texture coordinates
                        Vector2[] atlasWallCoords = Texture.getAtlasTexCoordinates(1, 0, 2);
                        texCoord[textIdx++] = atlasWallCoords[0].x;
                        texCoord[textIdx++] = atlasWallCoords[0].y;

                        texCoord[textIdx++] = atlasWallCoords[1].x;
                        texCoord[textIdx++] = atlasWallCoords[1].y;

                        texCoord[textIdx++] = atlasWallCoords[2].x;
                        texCoord[textIdx++] = atlasWallCoords[2].y;

                        texCoord[textIdx++] = atlasWallCoords[3].x;
                        texCoord[textIdx++] = atlasWallCoords[3].y;

                        //Triangles
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 4);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);

                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 2);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);
                    }
                    if (i < map.length - 1 && map[i + 1][j] < 2)
                    {
                        //Vertices
                        vertices[vertextIdx++] = position.x + j;
                        vertices[vertextIdx++] = position.y - i - 1.0f;
                        vertices[vertextIdx++] = position.z + 1.0f;

                        vertices[vertextIdx++] = position.x + j + 1.0f;
                        vertices[vertextIdx++] = position.y - i - 1.0f;
                        vertices[vertextIdx++] = position.z + 1.0f;

                        vertices[vertextIdx++] = position.x + j + 1.0f;
                        vertices[vertextIdx++] = position.y - i - 1.0f;
                        vertices[vertextIdx++] = position.z;

                        vertices[vertextIdx++] = position.x + j;
                        vertices[vertextIdx++] = position.y - i - 1.0f;
                        vertices[vertextIdx++] = position.z;

                        //Normals
                        normals[normIdx++] = 0f;
                        normals[normIdx++] = -1f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = 0f;
                        normals[normIdx++] = -1f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = 0f;
                        normals[normIdx++] = -1f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = 0f;
                        normals[normIdx++] = -1f;
                        normals[normIdx++] = 0f;

                        //Texture coordinates
                        Vector2[] atlasWallCoords = Texture.getAtlasTexCoordinates(1, 0, 2);
                        texCoord[textIdx++] = atlasWallCoords[0].x;
                        texCoord[textIdx++] = atlasWallCoords[0].y;

                        texCoord[textIdx++] = atlasWallCoords[1].x;
                        texCoord[textIdx++] = atlasWallCoords[1].y;

                        texCoord[textIdx++] = atlasWallCoords[2].x;
                        texCoord[textIdx++] = atlasWallCoords[2].y;

                        texCoord[textIdx++] = atlasWallCoords[3].x;
                        texCoord[textIdx++] = atlasWallCoords[3].y;

                        //Triangles
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 4);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);

                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 2);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);
                    }
                    if (j < map[0].length - 1 && map[i][j + 1] < 2)
                    {
                        //Vertices
                        vertices[vertextIdx++] = position.x + j + 1.0f;
                        vertices[vertextIdx++] = position.y - i - 1.0f;
                        vertices[vertextIdx++] = position.z + 1.0f;

                        vertices[vertextIdx++] = position.x + j + 1.0f;
                        vertices[vertextIdx++] = position.y - i;
                        vertices[vertextIdx++] = position.z + 1.0f;

                        vertices[vertextIdx++] = position.x + j + 1.0f;
                        vertices[vertextIdx++] = position.y - i;
                        vertices[vertextIdx++] = position.z;

                        vertices[vertextIdx++] = position.x + j + 1.0f;
                        vertices[vertextIdx++] = position.y - i - 1.0f;
                        vertices[vertextIdx++] = position.z;

                        //Normals
                        normals[normIdx++] = 1f;
                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = 1f;
                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = 1f;
                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 0f;

                        normals[normIdx++] = 1f;
                        normals[normIdx++] = 0f;
                        normals[normIdx++] = 0f;

                        //Texture coordinates
                        Vector2[] atlasWallCoords = Texture.getAtlasTexCoordinates(1, 0, 2);
                        texCoord[textIdx++] = atlasWallCoords[0].x;
                        texCoord[textIdx++] = atlasWallCoords[0].y;

                        texCoord[textIdx++] = atlasWallCoords[1].x;
                        texCoord[textIdx++] = atlasWallCoords[1].y;

                        texCoord[textIdx++] = atlasWallCoords[2].x;
                        texCoord[textIdx++] = atlasWallCoords[2].y;

                        texCoord[textIdx++] = atlasWallCoords[3].x;
                        texCoord[textIdx++] = atlasWallCoords[3].y;

                        //Triangles
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 4);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);

                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 3);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 2);
                        indices[indiceIdx++] = (short) (vertextIdx / 3 - 1);
                    }
                }
            }
        }
        
        //BUFFERS

        //Vertices
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        int[] buffer = new int[1];
        gl.glGenBuffers(1, buffer, 0);
        vbo = buffer[0];
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer, GL11.GL_STATIC_DRAW);

        //Tex coordinates
        byteBuf = ByteBuffer.allocateDirect(texCoord.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(texCoord);
        textureBuffer.position(0);

        buffer = new int[1];
        gl.glGenBuffers(1, buffer, 0);
        tbo = buffer[0];
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, tbo);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, texCoord.length * 4, textureBuffer, GL11.GL_STATIC_DRAW);

        //Normals
        byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        normalBuffer = byteBuf.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);

        buffer = new int[1];
        gl.glGenBuffers(1, buffer, 0);
        nbo = buffer[0];
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, nbo);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, normals.length * 4, normalBuffer, GL11.GL_STATIC_DRAW);

        byteBuf = ByteBuffer.allocateDirect(indices.length * 2);
        byteBuf.order(ByteOrder.nativeOrder());
        indexBuffer = byteBuf.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);

        //Indices
        buffer = new int[1];
        gl.glGenBuffers(1, buffer, 0);
        ibo = buffer[0];
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, ibo);
        gl.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indices.length * 2, indexBuffer, GL11.GL_STATIC_DRAW);

        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public boolean colidesWith(Ball ball)
    {
        //TODO Lepsi fyzika
        boolean colision = false;
        for (Rect rect : wallBoundingBoxes)
        {
            if (Rect.intersects(rect, ball.boundingBox))
            {
                Vector2 colCourse = getCollisionCourse(ball.boundingBox, rect);
                int axis = 0;
                if (Math.abs(colCourse.x) > Math.abs(colCourse.y))
                {
                    axis = 1;
                }
                ball.move(new Vector3(ball.position().x - colCourse.x, ball.position().y + colCourse.y, ball.position().z), axis);
                colision = true;
            }
        }
        return colision;
    }

    /**
     * Renders the map
     * @param gl            OPENGL context
     * @param camPosition   camera position
     */
    @Override
    public void render(GL11 gl, Vector3 camPosition)
    {
        gl.glPushMatrix();

        gl.glLoadIdentity();
        gl.glTranslatef(camPosition.x, camPosition.y, camPosition.z);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_ALPHA_TEST);
        gl.glAlphaFunc(GL10.GL_GREATER, 0.2f);

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
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, 0);

        gl.glDisable(GL10.GL_CULL_FACE);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_ALPHA_TEST);

        gl.glPopMatrix();
    }

    /**
     * Determines the direction of colision.
     * 
     * @param ball  Ball bounding box
     * @param wall  Wall bounding box
     * @return      Colision direction
     */
    private Vector2 getCollisionCourse(Rect ball, Rect wall)
    {
        Rect intersect = new Rect(Math.max(ball.left, wall.left), Math.max(ball.top, wall.top), Math.min(ball.right, wall.right), Math.min(ball.bottom, wall.bottom));
        Vector2 collision = new Vector2(0, 0);
        if (intersect.height() < 10 && intersect.width() < 10)
        {
            return collision;
        }
        if (intersect.height() > intersect.width())
        {
            collision.x = intersect.width();
            collision = collision.mul(Math.signum(ball.centerX() - wall.centerX()));
        }
        else
        {
            collision.y = intersect.height();
            collision = collision.mul(Math.signum(ball.centerY() - wall.centerY()));
        }
        return collision.mul(0.01f);
    }

    /**
     * Finds out if ball is falling in some hole
     * 
     * @param ball  Ball
     * @return      Hole if falls, null othewise
     */
    public Vector2 fallsInHole(Ball ball)
    {
        for (Vector2 hole : holePositions)
        {
            if (Vector2.vectorDistance(hole, new Vector2(-ball.position().x, ball.position().y)) < 0.5f)
            {
                return hole;
            }
        }
        return null;
    }

    /**
     * Returns 0. Has no use.
     * @param from  position from
     * @return      0
     */
    @Override
    public float distance(Vector3 from)
    {
        return 0;
    }

    /**
     * Returns zero Vector. Has no use.
     * @return zero vector
     */
    @Override
    public Vector3 position()
    {
        return new Vector3();
    }

    /**
     * Does nothing.
     * @param time time in ms
     */
    @Override
    public void update(long time)
    {
        //Nothing
    }

    /**
     * Return true, map is always displayed.
     * @return true
     */
    @Override
    public boolean displayed()
    {
        return true;
    }

    /**
     * Does nothing.
     * @param displayed nothing
     */
    @Override
    public void setDisplayed(boolean displayed)
    {
        //Nothing
    }

    /**
     * Clears up resources.
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
