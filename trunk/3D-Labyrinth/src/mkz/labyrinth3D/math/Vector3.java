package mkz.labyrinth3D.math;

/**
 * Three imensional vector.
 * @author Hans
 */
public class Vector3
{
    /**X*/
    public float x;
    /**Y*/
    public float y;
    /**Z*/
    public float z;

    /**
     * Creates new zero size vector.
     */
    public Vector3()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Creates new Vector.
     * @param x X
     * @param y Y
     * @param z Z
     */
    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Vector addition.
     * @param v added vector
     * @return  result vector
     */
    public Vector3 add(Vector3 v)
    {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    /**
     * Vector substraction.
     * @param v substracted vector
     * @return  result vector
     */
    public Vector3 sub(Vector3 v)
    {
        return new Vector3(x - v.x, y - v.y, z - v.z);
    }

    /**
     * Vector multiplication (by scalar).
     * @param d scalar multipler
     * @return  result vector
     */
    public Vector3 mul(float d)
    {
        return new Vector3(x * d, y * d, z * d);
    }

    /**
     * Scalar product.
     * @param v second vector
     * @return  result scalar product
     */
    public float dot(Vector3 v)
    {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * Returns the cross product of two vectors (orthogonal vector).
     * @param v second vector
     * @return orthogonal vector
     */
    public Vector3 cross(Vector3 v)
    {
        return new Vector3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }

    /**
     * Vector size.
     * @return size of vector
     */
    public float size()
    {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Creates unit vector.
     * @return unit vector
     */
    public Vector3 unit()
    {
        return new Vector3(x / size(), y / size(), z / size());
    }

    /**
     * Vector division (by scalar).
     * @param d scalar divider
     * @return  result vector
     */
    public Vector3 div(float d)
    {
        if (d == 0)
        {
            d = Float.MAX_VALUE;
        }
        else
        {
            d = 1 / d;
        }
        return this.mul(d);
    }
    
    /**
     * Sets vector size.
     * @param size new size
     */
    public void setSize(float size)
    {
        x = (x / size()) * size;
        y = (y / size()) * size;
        z = (z / size()) * size;
    }
    
    /**
     * Returns Vector2 with only X and Y coordinate
     * @return XY Vector2
     */
    public Vector2 xy()
    {
        return new Vector2(x, y);
    }

    /**
     * Distance of two vectors
     * @param a vector 1
     * @param b vector 2
     * @return distance
     */
    public static float vectorDistance(Vector3 a, Vector3 b)
    {
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2));
    }
}
