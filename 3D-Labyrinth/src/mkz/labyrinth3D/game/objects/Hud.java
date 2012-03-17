package mkz.labyrinth3D.game.objects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *
 * @author Hans
 */
public class Hud extends RelativeLayout
{
    private Context context;
    TextView fpsTW;
    TextView colectTW;

    public Hud(Context context)
    {
        super(context);
        this.context = context;
        fpsTW = new TextView(context);
        fpsTW.setText("FPS: 17");
        fpsTW.setTypeface(Typeface.MONOSPACE);
        fpsTW.setBackgroundColor(Color.argb(88, 0, 0, 0));
        fpsTW.setTextColor(Color.CYAN);
        this.addView(fpsTW);
    }
    
    public void setFPS(int fps)
    {
        if (fps < 0)
        {
            fpsTW.setText("FPS: ER");
        }
        else if (fps < 10)
        {
            fpsTW.setText("FPS: 0" + fps);
        }
        else if(fps < 100)
        {
            fpsTW.setText("FPS: " + fps);
        }
        else if(fps < 1000)
        {
            fpsTW.setText("FPS:" + fps);
        }
        else
        {
            fpsTW.setText("FPS:999");
        }
    }
}
