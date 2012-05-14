package mkz.labyrinth3D;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import mkz.labyrinth3D.game.LevelMaps;

/**
 * Main menu class.
 * @author Hans
 */
public class MainMenuActivity extends Activity
{
    /**Selected level*/
    private int level = 0;
    
    /**
     * Called on activity creation.
     * 
     * @param savedInstanceState saved activity state
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    /**
     * Left button event handle.
     * @param v view (button)
     */
    public void left(View v)
    {
        level--;
        if (level < 0)
        {
            level = 0;
        }
        TextView levelView = (TextView) findViewById(R.id.levelview);
        levelView.setText("" + (level + 1));
    }
    
    /**
     * Right button event handle.
     * @param v view (button)
     */
    public void right(View v)
    {
        level++;
        if (level >= LevelMaps.getLevelCount())
        {
            level = LevelMaps.getLevelCount() - 1;
        }
        TextView levelView = (TextView) findViewById(R.id.levelview);
        levelView.setText("" + (level + 1));
    }
    
    /**
     * Launch button event handle. Starts the game activity.
     * @param v view (button)
     */
    public void testLaunch(View v)
    {
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        intent.putExtra("level", level);
		startActivityForResult(intent, 0x1234);
    }
}
