package mkz.labyrinth3D.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.KeyEvent;
import mkz.labyrinth3D.AccelerometerListener;
import mkz.labyrinth3D.AccelerometerManager;
import mkz.labyrinth3D.math.Vector3;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 *
 * @author Hans
 */
public class GLView extends GLSurfaceView implements GLSurfaceView.Renderer, AccelerometerListener
{

    private AccelerometerManager accelerometerManager;
    private Game curGame;
    private Context context;
    private Vector3 acceleration;
    private Vector3 camera;
    private boolean[] arows;
    private long oldRenderTime;
    private long oldCycleTime;
    private GL11 gl11;

    public GLView(Context context)
    {
        super(context);
        this.setRenderer(this);
        this.requestFocus();
        this.setFocusableInTouchMode(true);
        this.context = context;
        camera = new Vector3(0f, 0f, -7.0f);
        arows = new boolean[]
        {
            false, false, false, false
        };

        curGame = new Game(context);
        acceleration = new Vector3();

        if (AccelerometerManager.isSupported(context))
        {
            accelerometerManager = new AccelerometerManager(context);
        }
        oldRenderTime = System.currentTimeMillis();
        oldCycleTime = System.currentTimeMillis();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig cfg)
    {
        //Settings
        gl.glDisable(GL10.GL_DITHER);				//Disable dithering
        gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping
        gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); 	//Black Background
        gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do

        //Really Nice Perspective Calculations
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        curGame.loadResource(gl);
        if (AccelerometerManager.isSupported(context))
        {
            //accelerometerManager.startRunning(this);
        }

        gl11 = (GL11) gl;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        if (height == 0)
        {
            height = 1;
        }

        gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
        gl.glLoadIdentity(); 					//Reset The Projection Matrix

        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
        gl.glLoadIdentity();
    }

    public void onDrawFrame(GL10 gl)
    {
        long cycleTime = System.currentTimeMillis() - oldCycleTime;
        oldCycleTime = System.currentTimeMillis();

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glLoadIdentity();

        //Keys
        if (arows[0] == true)
        {
            acceleration.x -= 0.01f;
        }
        if (arows[1] == true)
        {
            acceleration.y += 0.01f;
        }
        if (arows[2] == true)
        {
            acceleration.x += 0.01f;
        }
        if (arows[3] == true)
        {
            acceleration.y -= 0.01f;
        }

        curGame.moveBall(acceleration, cycleTime);

        camera.x = curGame.getBall().position().x;
        camera.y = curGame.getBall().position().y;

        oldRenderTime = System.currentTimeMillis();
        curGame.render(gl, camera);
        long renderTime = System.currentTimeMillis() - oldRenderTime;
    }

    public void onAccelerationChanged(float x, float y, float z)
    {
        acceleration.x = x * 0.3f;
        acceleration.y = y * 0.3f;
        acceleration.z = z;
    }

    public void destroy()
    {
        this.queueEvent(new Runnable()
        {

            @Override
            public void run()
            {
                curGame.destroy(gl11);
            }
        });
        if (AccelerometerManager.isSupported(context))
        {
            accelerometerManager.stopRunning();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
        {
            arows[0] = true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
        {
            arows[1] = true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
        {
            arows[2] = true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
        {
            arows[3] = true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            destroy();
            System.gc();
            System.exit(0);
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
        {
            if (AccelerometerManager.isSupported(context))
            {
                if (accelerometerManager.isRunning())
                {
                    accelerometerManager.stopRunning();
                }
                else
                {
                    accelerometerManager.startRunning(this);
                }

            }

        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        super.onKeyUp(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
        {
            arows[0] = false;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
        {
            arows[1] = false;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
        {
            arows[2] = false;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
        {
            arows[3] = false;
        }

        return true;
    }
}
