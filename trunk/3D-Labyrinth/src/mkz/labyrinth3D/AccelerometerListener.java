package mkz.labyrinth3D;

/**
 * Interface for classes, which wants to be informed about accelerometer generated events.
 * @author Hans
 */
public interface AccelerometerListener
{
    /**
     * Called on acceleration change.
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
	public void onAccelerationChanged(float x, float y, float z);
}
