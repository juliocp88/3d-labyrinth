package mkz.labyrinth3D.math;

/**
 * Two dimensional vector.
 * @author Hans
 */
public class Vector2
{
    /**X*/
	public float x;
    /**Y*/
	public float y;
	
    /**
     * Creates new zero size vector.
     */
    public Vector2()
    {
        x = 0;
        y = 0;
    }
    
    /**
     * Creates new Vector.
     * @param x X
     * @param y Y
     */
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
    /**
     * Vector addition.
     * @param v added vector
     * @return  result vector
     */
	public Vector2 add(Vector2 v)
	{
		return new Vector2(this.x + v.x, this.y + v.y);
	}
	
    /**
     * Vector substraction.
     * @param v substracted vector
     * @return  result vector
     */
	public Vector2 sub(Vector2 v)
	{
		return new Vector2(this.x - v.x, this.y - v.y);
	}
	
    /**
     * Scalar product.
     * @param v second vector
     * @return  result scalar product
     */
	public float dot(Vector2 v)
	{
		return this.x * v.x + this.y * v.y;
	}
	
    /**
     * Vector multiplication (by scalar).
     * @param d scalar multipler
     * @return  result vector
     */
	public Vector2 mul(float d)
	{
		return new Vector2(d * this.x, d * this.y);
	}
	
    /**
     * Vector size.
     * @return size of vector
     */
	public float size()
	{
		return (float) Math.sqrt(x * x + y * y);
	}
	
    /**
     * Creates unit vector.
     * @return unit vector
     */
	public Vector2 unit()
	{
		float size = this.size();
		Vector2 unit = new Vector2(0, 0);
		try
		{
			unit.x = (x / size);
			unit.y = (y / size);
		} 
		catch (Exception e)
		{
			// Nothing to be done in case of zero division (0,0) will return
		}
		return unit;
	}
	
    /**
     * Sets vector size.
     * @param size new size
     */
	public void setSize(float size)
	{
		x = (x / size()) * size;
		y = (y / size()) * size;
	}
    
    /**
     * Distance of two vectors
     * @param a vector 1
     * @param b vector 2
     * @return distance
     */
    public static float vectorDistance(Vector2 a, Vector2 b)
    {
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }
}
