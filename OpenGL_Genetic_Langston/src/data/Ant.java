package data;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import Helpers.Artist;
import Helpers.Textures;

public class Ant {
	
	private float x,y,height, width, fitness;
	private Texture tex;
	private String direction;
	private int speed;
	public String[] colorIn;
	public String[] moves;
	public String[] colorOut;
	
	//Takes a color in from current tile, moves depending on color, changes that tile to new color.
	public Ant(float x, float y, float height, float width, Texture tex, String direction, int speed, String[] colorIn, String[] moves, String[] colorOut)
	{
		this.y = y;
		this.x = x;
		this.height = height;
		this.width = width;
		this.tex = tex;
		this.direction = direction;
		this.speed = speed;
		this.colorIn = colorIn;
		this.moves = moves;
		this.colorOut = colorOut;
	}

	private void draw()
	{
		Artist.DrawQuadTex(tex, x, y, height, width);
	}
	public void setX(float x)
	{
		this.x = x;
	}
	public void setY(float y)
	{
		this.y = y;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public void changeTexture(Texture newTexture)
	{
		tex = newTexture;
	}
	
	
	

	
	
	
	//This will output the index number of the given tile color.
	public int onTile(String color)
	{
		for (int i = 0; i < colorIn.length; i++)
		{
			if (colorIn[i].equals(color))
			{
				return i;
			}
		}
		return -1;
	}
		
	public String findAction(String c)
	{
		for (int i = 0; i < colorIn.length; i++)
		{
			if (colorIn[i].equals(c))
			{
				return moves[i];
			}
		}
		return "FAIL";
	}
	
	
	
	
	
	
	
	public String[] getMoves(int sliceSpot, boolean fromFront)
	{
		String[] newMoves;
		
		if (fromFront == true)
		{
			newMoves = new String[sliceSpot];
			for (int i = 0; i < sliceSpot; i++)
			{
				newMoves[i] = moves[i];
			}
		}
		else
		{
			newMoves = new String[moves.length - sliceSpot];
			int x = 0;
			for (int i = sliceSpot; i < moves.length; i++)
			{
				newMoves[x] = moves[i];
				x++;
			}
		}
		return newMoves;
	}
	public String[] getColors(int sliceSpot, boolean fromFront)
	{
		String[] newColors;
		
		if (fromFront == true)
		{
			newColors = new String[sliceSpot];
			for (int i = 0; i < sliceSpot; i++)
			{
				newColors[i] = colorOut[i];
			}
		}
		else
		{
			newColors = new String[colorIn.length - sliceSpot];
			int x = 0;
			for (int i = sliceSpot; i < colorOut.length; i++)
			{
				newColors[x] = colorOut[i];
				x++;
			}
		}
		return newColors;
	}
	
	
	
	
	
	
	
	
	public void doAction(String action)
	{
		if (action.equals("turn counter clockwise"))
		{
			rotateCounterClockwise();
		}
		else if (action.equals("turn clockwise"))
		{
			rotateClockwise();
		}
		else if (action.equals("move forward"))
		{
			move();
		}
		else if (action.equals("move backward"))
		{
			rotateClockwise();
			rotateClockwise();
			move();
		}
		else if (action.equals("move left"))
		{
			rotateClockwise();
			move();
		}
		else if (action.equals("move right"))
		{
			rotateCounterClockwise();
			move();
		}
	}
	
	//This outputs the color to change to from an index.
		public String getOutColor(int index)
		{
			return colorOut[index];
		}
	
	
	
	public void move()
	{
		if (direction.equals("north"))
		{
			moveUp();
		}
		else if (direction.equals("south"))
		{
			moveDown();
		}
		else if (direction.equals("west"))
		{
			moveLeft();
		}
		else if (direction.equals("east"))
		{
			moveRight();
		}
	}

	
	public void rotateClockwise()
	{
		if (direction.equals("north"))
		{
			tex = Textures.getTex("ant");	//east
			direction = "east";
		}
		else if (direction.equals("south"))
		{
			tex = Textures.getTex("ant");	//west
			direction = "west";
		}
		else if (direction.equals("west"))
		{
			tex = Textures.getTex("ant");	//north
			direction = "north";
		}
		else if (direction.equals("east"))
		{
			tex = Textures.getTex("ant");	//south
			direction = "south";
		}
	}
	
	public void resetOrientation()
	{
		if (direction.equals("south"))
		{
			rotateClockwise();
			rotateClockwise();
		}
		else if (direction.equals("east"))
		{
			rotateCounterClockwise();
		}
		else if (direction.equals("west"))
		{
			rotateClockwise();
		}
		direction = "north";
	}
	
	
	public void rotateCounterClockwise()
	{
		if (direction.equals("north"))
		{
			tex = Textures.getTex("ant");	//west
			direction = "west";
		}
		else if (direction.equals("south"))
		{
			tex = Textures.getTex("ant"); //east
			direction = "east";
		}
		else if (direction.equals("west"))
		{
			tex = Textures.getTex("ant");	//south
			direction = "south";
		}
		else if (direction.equals("east"))
		{
			tex = Textures.getTex("ant");	//north
			direction = "north";
		}
	}
	
	public void moveRight()
	{
		x += speed;
	}
	public void moveLeft()
	{
		x -= speed;
	}
	public void moveUp()
	{
		y -= speed;
	}
	public void moveDown()
	{
		y += speed;
	}
	
	public void setScore(float score)
	{
		fitness = score;
	}
	
	public float getFitness()
	{
		return fitness;
	}
	
	public boolean isOffBoard()
	{
		if (x < 0 || x > Driver.WINDOW_LENGTH || y < 0 || y > Driver.WINDOW_WIDTH)
		{
			return true;
		}
		return false;
	}
	
	public void update()
	{
		draw();
	}
}