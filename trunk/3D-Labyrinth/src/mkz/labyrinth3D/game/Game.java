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
 * Game class, contins curently played game.
 * @author Hans
 */
public class Game
{
    /**Length of death animation in ms*/
    public static final long DEATH_ANIMATION_LENGTH = 1000;
    
    /**Scene rendering object*/
    private ObjectRenderer renderer;
    /**Curently played level*/
    private Level currentLVL;
    /**Application context*/
    private Context context;
    /**Player ball*/
    private Ball ball;
    /**Set to true when user starts to fall in hole*/
    private boolean dead;
    /**Time user is falling to hole*/
    private long deadTime;
    /**Set true after death animation ends*/
    private boolean gameOver;
    /**Played level ID*/
    private int levelID;

    /**
     * Creates new game.
     * @param context application context
     */
    public Game(Context context)
    {
        this.context = context;
        currentLVL = new Level(context);
        renderer = new ObjectRenderer(ObjectRenderer.MAX_OBJECTS);
        dead = false;
        deadTime = 0;
        gameOver = false;
        levelID = 0;
    }

    /**
     * Loads resources for scener.
     * @param gl OPENGL context
     */
    public void loadResource(GL11 gl)
    {
        
        currentLVL.load(levelID, gl);
        Texture tex = new Texture(gl, context, R.drawable.ball);
        ball = new Ball(Ball.BALL_RADIUS, 17, 11, (GL11) gl, tex);
        ball.position().x = currentLVL.getBallPosition().x;
        ball.position().y = currentLVL.getBallPosition().y;
        ball.position().z = currentLVL.getBallPosition().z;
        renderer.addObject(currentLVL.getMap());
        renderer.addObject(currentLVL.getItemSet());
        renderer.addObject(ball);
    }

    /**
     * Moves the ball. Based on the acceleration and time (ball rolling)
     * 
     * @param acceleration  acceleration vector
     * @param time          length of accleleration in ms
     */
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

    /**
     * Renders objects contained in the game.
     * @param gl            OPENGL context
     * @param camPosition   camera position
     */
    public synchronized void render(GL10 gl, Vector3 camPosition)
    {
        renderer.render(gl, camPosition);
    }

    /**
     * Updates scene objets.
     * @param time update length in ms
     */
    public synchronized void update(long time)
    {
        renderer.update(time);
    }

    /**
     * Returns player ball.
     * @return player ball
     */
    public synchronized Ball getBall()
    {
        return ball;
    }

    /**
     * Cleans up 3D objects.
     * @param gl OPENGL context
     */
    public void destroy(GL11 gl)
    {
        renderer.destroy(gl);
    }

    /**
     * Returns count of remaining gems.
     * @return count of remaining gems
     */
    public int getRemainingGems()
    {
        return currentLVL.getItemSet().getRemaining();
    }

    /**
     * Returns if game is over.
     * @return game over
     */
    public boolean isGameOver()
    {
        return gameOver;
    }
    
    /**
     * Return list of celtable items (gems).
     * @return gems
     */
    public List<Item> getItems()
    {
        return currentLVL.getItemSet().getItems();
    }

    /**
     * Returns current level ID.
     * @return current level ID
     */
    public int getLevelID()
    {
        return levelID;
    }

    /**
     * Sets current level ID.
     * @param levelID current level ID
     */
    public void setLevelID(int levelID)
    {
        this.levelID = levelID;
    }
}
