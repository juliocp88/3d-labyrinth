package mkz.labyrinth3D;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import mkz.labyrinth3D.game.LevelMaps;

/**
 *
 * @author Hans
 */
public class MainMenuActivity extends Activity
{
    private int level = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    
    public void left(View v)
    {
        level--;
        if (level < 0)
        {
            level = 0;
        }
        TextView levelView = (TextView) findViewById(R.id.levelview);
        levelView.setText("" + level);
    }
    
    public void right(View v)
    {
        level++;
        if (level >= LevelMaps.getLevelCount())
        {
            level = LevelMaps.getLevelCount() - 1;
        }
        TextView levelView = (TextView) findViewById(R.id.levelview);
        levelView.setText("" + level);
    }
    
    public void testLaunch(View v)
    {
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        intent.putExtra("level", level);
		startActivityForResult(intent, 0x1234);
    }
}
