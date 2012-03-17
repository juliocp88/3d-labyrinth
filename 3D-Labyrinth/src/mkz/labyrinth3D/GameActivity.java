package mkz.labyrinth3D;

import mkz.labyrinth3D.game.GLView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GameActivity extends Activity
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
        FrameLayout fl = new FrameLayout(this);
        fl.addView(gLView);
        TextView tw = new TextView(this);
        tw.setText("Text");
        //tw.setBackgroundColor(Color.argb(127, 0, 0, 0));
        tw.setTextColor(Color.RED);
        fl.addView(tw);
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
    
}
