package mkz.labyrinth3D;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerManager
{

    private Sensor sensor;
    private SensorManager sensorManager;
    private AccelerometerListener listener;
    private boolean running = false;
    private Context context;
    private SensorEventListener sensorEventListener;

    public AccelerometerManager(Context context)
    {
        this.context = context;
        sensorEventListener = new SensorEventListener()
        {
            public void onAccuracyChanged(Sensor arg0, int arg1)
            {
                // not used
            }

            public void onSensorChanged(SensorEvent arg0)
            {
                if (listener != null)
                {
                     listener.onAccelerationChanged(arg0.values[0], arg0.values[1], arg0.values[2]);
                }
            }
        };
    }

    public boolean isRunning()
    {
        return running;
    }

    public static boolean isSupported(Context context)
    {
        if (context != null)
        {
            SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (sensors.size() > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public void startRunning(AccelerometerListener accelerometerListener)
    {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0)
        {
            sensor = sensors.get(0);
            running = sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
            listener = accelerometerListener;
        }
    }

    public void stopRunning()
    {
        running = false;
        try
        {
            if (sensorManager != null && sensorEventListener != null)
            {
                sensorManager.unregisterListener(sensorEventListener);
                sensorManager = null;
                System.out.println("ACCELEROMETER CLEANED");
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR WHILE CLEANING ACCELEROMETER");
        }
    }
}
