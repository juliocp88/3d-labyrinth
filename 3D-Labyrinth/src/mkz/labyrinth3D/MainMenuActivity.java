package mkz.labyrinth3D;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 *
 * @author Hans
 */
public class MainMenuActivity extends Activity
{
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
    
    public void testLaunch(View v)
    {
        Intent i = new Intent(MainMenuActivity.this, GameActivity.class);
		startActivityForResult(i, 0x1234);
    }
}
