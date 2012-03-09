package mkz.labyrinth3D.math;

/**
 *
 * @author Hans
 */
public class Vector3
{

    public float x;
    public float y;
    public float z;

    public Vector3()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(Vector3 v)
    {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    public Vector3 sub(Vector3 v)
    {
        return new Vector3(x - v.x, y - v.y, z - v.z);
    }

    public Vector3 mul(float d)
    {
        return new Vector3(x * d, y * d, z * d);
    }

    public float dot(Vector3 v)
    {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3 cross(Vector3 v)
    {
        return new Vector3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }

    public float size()
    {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 unit()
    {
        return new Vector3(x / size(), y / size(), z / size());
    }

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

    public void setSize(float size)
    {
        x = (x / size()) * size;
        y = (y / size()) * size;
        z = (z / size()) * size;
    }
    
    public Vector2 xy()
    {
        return new Vector2(x, y);
    }

    public static float vectorDistance(Vector3 a, Vector3 b)
    {
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2));
    }
}
