package mkz.labyrinth3D;

import mkz.labyrinth3D.game.GLView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import mkz.labyrinth3D.game.objects.Hud;

public class GameActivity extends Activity
{    
    private GLView gLView;
    private Hud hud;
    
    /**
     * Creates new activity
     * 
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        gLView = new GLView(this);
        hud = new Hud(this);
        FrameLayout fl = new FrameLayout(this);
        fl.addView(gLView);       
        fl.addView(hud);
        gLView.setGameActivity(this);
        setContentView(fl);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gLView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        gLView.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        gLView.destroy();
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
}
