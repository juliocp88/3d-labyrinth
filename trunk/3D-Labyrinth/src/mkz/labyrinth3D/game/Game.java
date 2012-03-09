package mkz.labyrinth3D.game;

import android.content.Context;
import mkz.labyrinth3D.R;
import mkz.labyrinth3D.game.objects.ObjectRenderer;
import mkz.labyrinth3D.game.objects.Texture;
import mkz.labyrinth3D.game.objects.Ball;
import mkz.labyrinth3D.math.Vector3;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 *
 * @author Hans
 */
public class Game
{    
    private ObjectRenderer renderer;
    private Level currentLVL;
    private Context context;
    private Ball ball;

    public Game(Context context)
    {
        this.context = context;
        currentLVL = new Level(context);
        renderer = new ObjectRenderer(ObjectRenderer.MAX_OBJECTS);
    }
    
    public  void loadResource(GL10 gl)
    {
        currentLVL.load("test", gl);
        Texture tex = new Texture(gl, context, R.drawable.chess);
        ball = new Ball(1, 17, 11, (GL11) gl, tex);
        renderer.addObject(currentLVL.map);
        renderer.addObject(ball);
    }
    
    public void moveBall(Vector3 acceleration, long time)
    {
        ball.move(acceleration, time);
        currentLVL.map.colidesWith(ball);
    }
    
    public void render(GL10 gl, Vector3 camPosition)
    {
        renderer.render(gl, camPosition);
    }

    public Ball getBall()
    {
        return ball;
    }
    
    public void destroy(GL11 gl)
    {
        renderer.destroy(gl);
    }
}
