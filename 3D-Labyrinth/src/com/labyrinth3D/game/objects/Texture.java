package com.labyrinth3D.game.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLUtils;
import com.labyrinth3D.math.Vector2;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 * @author Hans
 */
public class Texture
{
    private int[] texCoord;
    public int texPointer;

    public Texture(GL10 gl, Context context, int resource)
    {
        texCoord = new int[1];
        loadTexture(gl, context, resource);
        texPointer = texCoord[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }

    private void loadTexture(GL10 gl, Context context, int resource)
    {
        Bitmap bitmap = null;
        InputStream is = context.getResources().openRawResource(resource);

        try
        {
            bitmap = BitmapFactory.decodeStream(is);
        }
        finally
        {
            try
            {
                is.close();
                is = null;
            }
            catch (IOException e)
            {
            }
        }

        gl.glGenTextures(1, texCoord, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texCoord[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_LINEAR);

        makeMipmap(bitmap);
        bitmap.recycle();
    }

    private void makeMipmap(Bitmap bitmap)
    {
        int level = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        while (true)
        {
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
            
            if (height == 1 || width == 1) 
                break;

            level++;

            height = height / 2;
            width = width / 2;
            //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            Bitmap scaledBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
            for (int i = 0; i < scaledBitmap.getHeight(); i++)
            {
                for (int j = 0; j < scaledBitmap.getWidth(); j++)
                {
                    int c00 = bitmap.getPixel(j * 2, i * 2);
                    int c01 = bitmap.getPixel(j * 2 + 1, i * 2);
                    int c10 = bitmap.getPixel(j * 2, i * 2 + 1);
                    int c11 = bitmap.getPixel(j * 2 + 1, i * 2 + 1);
                    
                    int red = (Color.red(c00) + Color.red(c01) + Color.red(c10) + Color.red(c11)) / 4;
                    int green = (Color.green(c00) + Color.green(c01) + Color.green(c10) + Color.green(c11)) / 4;
                    int blue = (Color.blue(c00) + Color.blue(c01) + Color.blue(c10) + Color.blue(c11)) / 4;
                    
                    scaledBitmap.setPixel(j, i, Color.rgb(red, green, blue));
                }
            }
            bitmap.recycle();
            bitmap = scaledBitmap;
        }
    }
    
    public static Vector2[] getAtlasTexCoordinates(int x, int y, int size)
    {
        float length = 1.0f / size;
        
        Vector2 topLeft = new Vector2(x * length + length / 4, y * length + length / 4);
        Vector2 topRight = new Vector2(x * length + 3 * length / 4, y * length + length / 4);
        Vector2 botRight = new Vector2(x * length + 3 * length / 4, y * length + 3 * length / 4); 
        Vector2 botLeft = new Vector2(x * length + length / 4, y * length + 3 * length / 4);
        
        return new Vector2[] 
        {
            topLeft,
            topRight,
            botRight,
            botLeft,
        };
    }
    
    public void destroy(GL10 gl)
    {
        gl.glDeleteTextures(1, texCoord, 0);
    }
}
