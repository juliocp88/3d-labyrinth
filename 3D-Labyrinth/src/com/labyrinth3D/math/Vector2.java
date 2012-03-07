package com.labyrinth3D.math;

public class Vector2
{
	public float x;
	public float y;
	
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2 add(Vector2 v)
	{
		return new Vector2(this.x + v.x, this.y + v.y);
	}
	
	public Vector2 sub(Vector2 v)
	{
		return new Vector2(this.x - v.x, this.y - v.y);
	}
	
	public float dot(Vector2 v)
	{
		return this.x * v.x + this.y * v.y;
	}
	
	public Vector2 mul(float d)
	{
		return new Vector2(d * this.x, d * this.y);
	}
	
	public float size()
	{
		return (float) Math.sqrt(x * x + y * y);
	}
	
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
	
	public void setSize(float size)
	{
		x = (x / size()) * size;
		y = (y / size()) * size;
	}
    
    public static float vectorDistance(Vector2 a, Vector2 b)
    {
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }
}
