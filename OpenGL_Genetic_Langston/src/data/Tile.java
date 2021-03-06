package data;

import org.newdawn.slick.opengl.Texture;

import Helpers.Artist;
import Helpers.Textures;

public class Tile {
	
	private float x,y,height, width;
	private Texture tex;
	private String color;
	private boolean visited;
	
	public Tile(float x, float y, float height, float width, Texture tex)
	{
		this.y = y;
		this.x = x;
		this.height = height;
		this.width = width;
		this.tex = tex;
		color = "white";
	}
	
	private void draw()
	{
		Artist.DrawQuadTex(tex, x, y, height, width);
	}
	
	public void unvist()
	{
		visited = false;
	}
	
	public void visit()
	{
		if (visited == false)
		{
			Driver.addOneTile();
		}
		visited = true;
	}
	
	public void changeColor(String color)
	{
		this.color = color;
		tex = Textures.getTex(color);
	}
	
	public String getColor()
	{
		return color;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void update()
	{
		draw();
	}
}
