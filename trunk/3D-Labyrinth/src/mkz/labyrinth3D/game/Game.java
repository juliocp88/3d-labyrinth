package mkz.labyrinth3D.game;

import android.content.Context;
import java.util.List;
import mkz.labyrinth3D.R;
import mkz.labyrinth3D.game.objects.ObjectRenderer;
import mkz.labyrinth3D.game.objects.Texture;
import mkz.labyrinth3D.game.objects.Ball;
import mkz.labyrinth3D.math.Vector3;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import mkz.labyrinth3D.game.objects.Item;
import mkz.labyrinth3D.math.Vector2;

/**
 *
 * @author Hans
 */
public class Game
{
    public static final long DEATH_ANIMATION_LENGTH = 1000;
    
    private ObjectRenderer renderer;
    private Level currentLVL;
    private Context context;
    private Ball ball;
    private boolean dead;
    private long deadTime;
    private boolean gameOver;

    public Game(Context context)
    {
        this.context = context;
        currentLVL = new Level(context);
        renderer = new ObjectRenderer(ObjectRenderer.MAX_OBJECTS);
        dead = false;
        deadTime = 0;
        gameOver = false;
    }

    public void loadResource(GL11 gl)
    {
        currentLVL.load("test", gl);
        Texture tex = new Texture(gl, context, R.drawable.ball);
        ball = new Ball(Ball.BALL_RADIUS, 17, 11, (GL11) gl, tex);
        ball.position().x = currentLVL.getBallPosition().x;
        ball.position().y = currentLVL.getBallPosition().y;
        ball.position().z = currentLVL.getBallPosition().z;
        renderer.addObject(currentLVL.getMap());
        renderer.addObject(currentLVL.getItemSet());
        renderer.addObject(ball);
    }

    public synchronized void moveBall(Vector3 acceleration, long time)
    {
        if (!dead)
        {
            ball.move(acceleration, time);
            currentLVL.getMap().colidesWith(ball);
            if (currentLVL.getItemSet().colidesWith(ball))
            {
                Item item = currentLVL.getItemSet().findColision(ball);
                if (item.displayed())
                {
                    item.setDisplayed(false);
                }
            }
            Vector2 hole = currentLVL.getMap().fallsInHole(ball);
            if (hole != null)
            {
                ball.position().z = 1;
                dead = true;
            }
        }
        else
        {
            deadTime += time;   
            Vector2 hole = currentLVL.getMap().fallsInHole(ball);
            if (hole != null)
            {
                Vector2 ballPos = ball.position().xy();
                ballPos.x =  - ballPos.x;
                
                Vector2 direction = hole.sub(ballPos);
                direction.setSize(direction.size() / 10);
                ballPos = ballPos.add(direction);
                
                ballPos.x =  - ballPos.x;
                Vector3 newPosition = new Vector3(ballPos.x, ballPos.y, ball.position().z);
                
                float duration = ((float) time) / 1000.0f;
                newPosition.z -= duration;
                newPosition.z = Math.max(0, newPosition.z);
                
                ball.move(newPosition, 2);
            }
            
            
            if (deadTime > DEATH_ANIMATION_LENGTH)
            {
                gameOver = true;
            }
        }
    }

    public synchronized void render(GL10 gl, Vector3 camPosition)
    {
        renderer.render(gl, camPosition);
    }

    public synchronized void update(long time)
    {
        renderer.update(time);
    }

    public synchronized Ball getBall()
    {
        return ball;
    }

    public void destroy(GL11 gl)
    {
        renderer.destroy(gl);
    }

    public int getRemainingGems()
    {
        return currentLVL.getItemSet().getRemaining();
    }

    public boolean isGameOver()
    {
        return gameOver;
    }
    
    public List<Item> getItems()
    {
        return currentLVL.getItemSet().getItems();
    }
}
