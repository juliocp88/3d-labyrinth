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

public class GameActivity extends Activity
{
    private GLView gLView;
    private Hud hud;
    private FrameLayout frameLayout;
    private List<Boolean> colectedItems;
    private Vector3 ballPosition;

    /**
     * Creates new activity
     * 
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        System.out.println("******************* ON CREATE");
        hud = new Hud(this);
        frameLayout = new FrameLayout(this);
        setContentView(frameLayout);
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
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gLView = new GLView(this);
        gLView.setGameActivity(this);
        frameLayout.addView(gLView);
        frameLayout.addView(hud);
        gLView.onResume();
        System.out.println("******************* ON RESUME");
    }

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

    @Override
    protected void onPause()
    {
        super.onPause();
        gLView.onPause();
        System.out.println("******************* ON PAUSE");
        ballPosition = gLView.getGame().getBall().position();
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

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (gLView != null)
        {
            gLView.destroy();
        }
        System.out.println("******************* ON DESTROY");
    }

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

    @Override
    protected void onStart()
    {
        super.onStart();
        System.out.println("******************* ON START");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        System.out.println("******************* ON STOP");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        System.out.println("******************* ON RESTART");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("******************* ON RESTORE");
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
        }
    }
    
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
    }
}
