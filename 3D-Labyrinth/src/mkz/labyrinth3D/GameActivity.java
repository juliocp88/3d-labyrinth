package mkz.labyrinth3D;

import mkz.labyrinth3D.game.GLView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import java.util.LinkedList;
import java.util.List;
import mkz.labyrinth3D.game.objects.Hud;
import mkz.labyrinth3D.game.objects.Item;
import mkz.labyrinth3D.math.Vector3;

/**
 * Game activity, contains HUD and OPENGL view. Handles saving and loading of application state.
 * @author Hans
 */
public class GameActivity extends Activity
{
    /**OPENGL view*/
    private GLView gLView;
    /**HUD*/
    private Hud hud;
    /**Activity layout*/
    private FrameLayout frameLayout;
    /**List of collected items for state save.*/
    private List<Boolean> colectedItems;
    /**Ball position for save state*/
    private Vector3 ballPosition;
    /**Currently played level ID*/
    private int levelID;

    /**
     * Creates new activity.
     * 
     * @param savedInstanceState saved activity state
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        hud = new Hud(this);
        frameLayout = new FrameLayout(this);
        setContentView(frameLayout);
        colectedItems = new LinkedList<Boolean>();
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("level"))
        {
            levelID = extras.getInt("level");
        }
        else
        {
            levelID = 0;
        }

        if (savedInstanceState != null)
        {
            if (savedInstanceState.containsKey("ballx"))
            {
                ballPosition = new Vector3(savedInstanceState.getFloat("ballx"), savedInstanceState.getFloat("bally"), savedInstanceState.getFloat("ballz"));
            }
            if (savedInstanceState.containsKey("items"))
            {
                boolean[] items = savedInstanceState.getBooleanArray("items");
                for (boolean item : items)
                {
                    colectedItems.add(item);
                }
            }

        }
    }

    /**
     * Resumes the activity after getting to foreground.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        gLView = new GLView(this);
        gLView.setGameActivity(this);
        gLView.getGame().setLevelID(levelID);
        frameLayout.addView(gLView);
        frameLayout.addView(hud);
        gLView.onResume();
    }

    /**
     * Restores the activity state.
     */
    public void restore()
    {
        if (ballPosition == null)
        {
            ballPosition = gLView.getGame().getBall().position();
        }
        else
        {
            gLView.getGame().getBall().position().x = ballPosition.x;
            gLView.getGame().getBall().position().y = ballPosition.y;
            gLView.getGame().getBall().position().z = ballPosition.z;
        }
        if (colectedItems.size() == gLView.getGame().getItems().size())
        {
            int idx = 0;
            for (Item item : gLView.getGame().getItems())
            {
                item.setDisplayed(colectedItems.get(idx++));
            }
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    gLView.pause();
                }
            });
        }
    }

    /**
     * Pauses the activity after leaving foreground.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        gLView.onPause();
        ballPosition = gLView.getGame().getBall().position();
        levelID = gLView.getGame().getLevelID();
        colectedItems.clear();
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                frameLayout.removeAllViews();
            }
        });

        for (Item item : gLView.getGame().getItems())
        {
            colectedItems.add(item.displayed());
        }
        gLView.destroy();
        gLView = null;
    }

    /**
     * Cleans up resources after finish().
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (gLView != null)
        {
            gLView.destroy();
        }
    }

    /**
     * Sets the current amount of FPS on HUD.
     * @param fps current amount of FPS
     */
    public void setFPS(final int fps)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                hud.setFPS(fps);
            }
        });
    }

    /**
     * Sets the current amount of GEMS on HUD.
     * @param gems current amount of GEMS
     */
    public void setGEMS(final int gems)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                hud.setGEMS(gems);
            }
        });
    }

    /**
     * Reloads the saved state of activity.
     * @param savedInstanceState saved state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        colectedItems = new LinkedList<Boolean>();
        if (savedInstanceState != null)
        {
            if (savedInstanceState.containsKey("ballx"))
            {
                ballPosition = new Vector3(savedInstanceState.getFloat("ballx"), savedInstanceState.getFloat("bally"), savedInstanceState.getFloat("ballz"));
            }
            if (savedInstanceState.containsKey("items"))
            {
                boolean[] items = savedInstanceState.getBooleanArray("items");
                for (boolean item : items)
                {
                    colectedItems.add(item);
                }
            }
            if (savedInstanceState.containsKey("level"))
            {
                levelID = savedInstanceState.getInt("level");
            }
            else
            {
                levelID = 0;
            }
        }
    }

    /**
     * Saves current state of activity.
     * @param outState saved state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        System.out.println("******************* ON SAVE");
        if (ballPosition != null)
        {
            outState.putFloat("ballx", ballPosition.x);
            outState.putFloat("bally", ballPosition.y);
            outState.putFloat("ballz", ballPosition.z);
        }
        if (colectedItems.size() > 0)
        {
            boolean[] items = new boolean[colectedItems.size()];
            for (int i = 0; i < items.length; i++)
            {
                items[i] = colectedItems.get(i);
            }
            outState.putBooleanArray("items", items);
        }
        outState.putInt("level", levelID);
    }
}
