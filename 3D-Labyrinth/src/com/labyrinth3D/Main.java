package com.labyrinth3D;

import com.labyrinth3D.game.GLView;
import android.app.Activity;
import android.os.Bundle;

public class Main extends Activity
{    
    private GLView gLView;
    
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
        setContentView(gLView);
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
    
}
