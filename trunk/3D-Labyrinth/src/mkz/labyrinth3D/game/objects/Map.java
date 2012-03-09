/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mkz.labyrinth3D.game.objects;

import android.graphics.Rect;
import mkz.labyrinth3D.math.Vector2;
import mkz.labyrinth3D.math.Vector3;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 *
 * @author Hans
 */
public class Map extends Object3D
{
    private int[][] map;
    private int verticesCount;
    private int triangleCount;
    private int vbo;
    private int nbo;
    private int tbo;
    private int ibo;
    private Rect[] wallBoundingBoxes;

    public Map(int[][] map, Texture texture, GL11 gl)
    {
        this.map = map;
        this.texture = texture;
        verticesCount = 0;
        triangleCount = 0;
        int wallCount = 0;

        //Counts triangles and vertex count
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[0].length; j++)
            {
                if (map[i][j] == 0 || map[i][j] == 1)
                {
                    verticesCount += 4;
                    triangleCount += 2;
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

        int vertextIdx = 0;
        int textIdx = 0;
        int normIdx = 0;
        int indiceIdx = 0;
        wallCount = 0;

        Vector3 position = new Vector3();

        //Creates geometry
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[0].length; j++)
            {
                if (map[i][j] == 0) //HOLES
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
                    System.out.println("Bounding box: " + wallBoundingBoxes[wallCount - 1].toShortString());

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

    @Override
    public float distance(Vector3 from)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector3 position()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(long time)
    {
        //Nothing
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
        int[] buffers = new int[]
        {
            vbo, tbo, nbo, ibo
        };
        gl.glDeleteBuffers(4, buffers, 0);
    }
}
