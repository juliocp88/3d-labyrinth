package mkz.labyrinth3D.game.objects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Heads Up Display with FPS counter and Remaining GEMS counter.
 * @author Hans
 */
public class Hud extends RelativeLayout
{
    /**TextField with FPS*/
    private TextView fpsTW;
    /**TextField with GEMS*/
    private TextView colectTW;

    /**
     * Creates new HUD.
     * @param context application context
     */
    public Hud(Context context)
    {
        super(context);
        fpsTW = new TextView(context);
        fpsTW.setText("FPS: 00");
        fpsTW.setTypeface(Typeface.MONOSPACE);
        fpsTW.setBackgroundColor(Color.argb(88, 0, 0, 0));
        fpsTW.setTextColor(Color.CYAN);
        fpsTW.setTextSize(20.0f);
        colectTW = new TextView(context);
        colectTW.setText("GEMS: 00");
        colectTW.setTypeface(Typeface.MONOSPACE);
        colectTW.setBackgroundColor(Color.argb(88, 0, 0, 0));
        colectTW.setTextColor(Color.CYAN);
        colectTW.setTextSize(20.0f);    
        this.addView(fpsTW);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        colectTW.setLayoutParams(params);
        this.addView(colectTW);
    }
    
    /**
     * Sets new FPS value.
     * @param fps value
     */
    public void setFPS(int fps)
    {
        if (fps < 0)
        {
            fpsTW.setText("ERROR");
        }
        else if (fps < 10)
        {
            fpsTW.setText("FPS: 0" + fps);
        }
        else
        {
            fpsTW.setText("FPS: " + fps);
        }
    }
    
    /**
     * Sets new GEM value.
     * @param gems value
     */
    public void setGEMS(int gems)
    {
        if (gems < 0)
        {
            colectTW.setText("ERROR");
        }
        else if (gems < 10)
        {
            colectTW.setText("GEMS: 0" + gems);
        }
        else
        {
            colectTW.setText("GEMS: " + gems);
        }
    }
}
