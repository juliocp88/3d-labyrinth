package com.labyrinth3D.game.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
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
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);

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
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            bitmap.recycle();
            bitmap = scaledBitmap;
        }
    }
    
    public void destroy(GL10 gl)
    {
        gl.glDeleteTextures(1, texCoord, 0);
    }
}
