package data;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

import Helpers.Artist;
import Helpers.Textures;

public class Ant {
	
	private float x,y,height, width, fitness;
	private Texture tex;
	private String direction;
	private int id,speed;
	public String[] colorIn;
	public String[] moves;
	public String[] colorOut;
	private HashMap<String,Integer> colorsUsed = new HashMap<>();
    Ant parentA, parentB;

    //Constructor for bestAnt.
    public Ant(int score)
    {
        this.fitness = score;
    }

    public Ant(int id, float x, float y, float height, float width, Texture tex, String direction, int speed,String[] colorList, String colorsCode, String movesCode)
    {
        this.id = id;
        this.y = y;
        this.x = x;
        this.height = height;
        this.width = width;
        this.tex = tex;
        this.direction = direction;
        this.speed = speed;
        colorIn = colorList;
        String[] colorsChars = colorsCode.split("");
        String[] movesChars = movesCode.split("");

        colorOut = new String[colorsChars.length];
        moves = new String[movesChars.length];

        for (int i = 0; i < colorsChars.length; i++)
        {
            System.out.println(i);
            if (colorsChars[i].equals("1"))
            {
                colorOut[i] = "white";  //white black blue green yellow red
            }
            else if (colorsChars[i].equals("2"))
            {
                colorOut[i] = "black";
            }
            else if (colorsChars[i].equals("3"))
            {
                colorOut[i] = "blue";
            }
            else if (colorsChars[i].equals("4"))
            {
                colorOut[i] = "green";
            }
            else if (colorsChars[i].equals("5"))
            {
                colorOut[i] = "yellow";
            }
            else if (colorsChars[i].equals("6"))
            {
                colorOut[i] = "red";
            }
        }

        for (int i = 0; i < movesChars.length; i++) {
            System.out.println(i);
            if (movesChars[i].equals("1")) {
                moves[i] = "turn clockwise";  //white black blue green yellow red
            }
            else if (movesChars[i].equals("2")) {
                moves[i] = "turn counter clockwise";
            }
            else if (movesChars[i].equals("3")) {
                moves[i] = "turn around";
            }
            else if (movesChars[i].equals("4")) {
                moves[i] = "move right";
            }
            else if (movesChars[i].equals("5")) {
                moves[i] = "move backward";
            }
            else if (movesChars[i].equals("6")) {
                moves[i] = "move left";
            }
            else if (movesChars[i].equals("7")) {
                moves[i] = "move forward";
            }
            else if (movesChars[i].equals("8")) {
                moves[i] = "diaganol up right";
            }
            else if (movesChars[i].equals("9")) {
                moves[i] = "diaganol up left";
            }
            else if (movesChars[i].equals("a")) {
                moves[i] = "diaganol down right";
            }
            else if (movesChars[i].equals("b")) {
                moves[i] = "diaganol down left";
            }
            else if (movesChars[i].equals("c")) {
                moves[i] = "skip up";
            }
            else if (movesChars[i].equals("d")) {
                moves[i] = "skip down";
            }
            else if (movesChars[i].equals("e")) {
                moves[i] = "skip left";
            }
            else if (movesChars[i].equals("f")) {
                moves[i] = "skip right";
            }
            else if (movesChars[i].equals("g")) {
                moves[i] = "dont move";
            }
        }

        for (int i = 0; i < colorIn.length; i++)
        {
            colorsUsed.put(colorIn[i],0);
        }
    }

	//Takes a color in from current tile, moves depending on color, changes that tile to new color.
	public Ant(int id, float x, float y, float height, float width, Texture tex, String direction, int speed, String[] colorIn, String[] moves, String[] colorOut)
	{
        this.id = id;
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


        for (int i = 0; i < colorIn.length; i++)
        {
            colorsUsed.put(colorIn[i],0);
        }
	}

	public void printDNAString()
    {
        System.out.print("Color String ");
        for(int i = 0; i < colorOut.length; i++)
        {
            if (colorOut[i].equals("white"))
            {
                System.out.print("1");
            }
            else if (colorOut[i].equals("black"))
            {
                System.out.print("2");
            }
            else if (colorOut[i].equals("blue"))
            {
                System.out.print("3");
            }
            else if (colorOut[i].equals("green"))
            {
                System.out.print("4");
            }
            else if (colorOut[i].equals("yellow"))
            {
                System.out.print("5");
            }
            else if (colorOut[i].equals("red"))
            {
                System.out.print("6");
            }
            else
            {
                System.out.print("XXX");
            }
        }
        System.out.println();
        System.out.print("Move String ");
        for(int i = 0; i < moves.length; i++)
        {
            if (moves[i].equals("turn clockwise"))
            {
                System.out.print("1");
            }
            else if (moves[i].equals("turn counter clockwise"))
            {
                System.out.print("2");
            }
            else if (moves[i].equals("turn around"))
            {
                System.out.print("3");
            }
            else if (moves[i].equals("move right"))
            {
                System.out.print("4");
            }
            else if (moves[i].equals("move backward"))
            {
                System.out.print("5");
            }
            else if (moves[i].equals("move left"))
            {
                System.out.print("6");
            }
            else if (moves[i].equals("move forward"))
            {
                System.out.print("7");
            }
            else if (moves[i].equals("diaganol up right"))
            {
                System.out.print("8");
            }
            else if (moves[i].equals("diaganol up left"))
            {
                System.out.print("9");
            }
            else if (moves[i].equals("diaganol down right"))
            {
                System.out.print("a");
            }
            else if (moves[i].equals("diaganol down left"))
            {
                System.out.print("b");
            }
            else if (moves[i].equals("skip up"))
            {
                System.out.print("c");
            }
            else if (moves[i].equals("skip down"))
            {
                System.out.print("d");
            }
            else if (moves[i].equals("skip left"))
            {
                System.out.print("e");
            }
            else if (moves[i].equals("skip right"))
            {
                System.out.print("f");
            }
            else if (moves[i].equals("dont move"))
            {
                System.out.print("g");
            }
            else
            {
                System.out.print("XXX");
            }
        }
        System.out.println();
    }

	public int getID()
    {
        return id;
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


	public void setParents(Ant parA, Ant parB)
    {
        parentA = parA;
        parentB = parB;
    }

    public Ant getParentA()
    {
        return parentA;
    }
    public Ant getParentB()
    {
        return parentB;
    }

	public void getParentInfo()
    {
        if (parentA == null || parentB == null)
        {
            System.out.println("This ant has no parents.");
        }
        else
        {
            System.out.println("PARENT A ID: " + parentA.getID() + "  FITNESS SCORE: " + parentA.getFitness());
            System.out.println("PARENT B ID: " + parentB.getID() + "  FITNESS SCORE: " + parentB.getFitness());
        }
        System.out.println("CHILD ID: " + getID());
        System.out.println();
    }
	

	//For mutation, swaps random two
    public void swap(int in1, int in2)
    {
        String tmp;
        tmp = colorOut[in1];
        colorOut[in1] = colorOut[in2];
        colorOut[in2] = tmp;

        tmp = moves[in1];
        moves[in1] = moves[in2];
        moves[in2] = tmp;
    }
	
	//This will output the index number of the given tile color.
	public int onTile(String color)
	{
		for (int i = 0; i < colorIn.length; i++)
		{
			if (colorIn[i].equals(color))
			{
                colorsUsed.put(colorOut[i],colorsUsed.get(colorOut[i]) + 1);
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
			newColors = new String[colorOut.length - sliceSpot];
			int x = 0;
			for (int i = sliceSpot; i < colorOut.length; i++)
			{
				newColors[x] = colorOut[i];
				x++;
			}
		}
		return newColors;
	}


	public String getStringOfColors()
    {
        String output = colorOut[0];
        for (int i = 1; i < colorOut.length; i++)
        {
            output += ", " + colorOut[i];
        }
        return output;
    }

	public String getStringOfMoves()
	{
		String output = moves[0];
		for (int i = 1; i < moves.length; i++)
		{
			output += ", " + moves[i];
		}
		return output;
	}



	public int getSliceSpot()
    {
        int max = 0;
        int index = 0;
        for (int i = 0; i < colorIn.length; i++)
        {
            //System.out.println("COLOR USED: " + colorsUsed.get(colorIn[i]));
            if (colorsUsed.get(colorIn[i]) > max)
            {
                max = colorsUsed.get(colorIn[i]);
                index = i;
            }
        }
        return index;
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
		else if (action.equals("turn around"))
		{
			rotateClockwise();
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
		else if (action.equals("diaganol up right"))
		{
            x += speed;
            y -= speed;
		}
		else if (action.equals("diaganol down right"))
		{
            x += speed;
            y += speed;
		}
		else if (action.equals("diaganol up left"))
		{
            x -= speed;
            y -= speed;
		}
		else if (action.equals("diaganol down left"))
		{
            x -= speed;
            y += speed;
		}
		else if (action.equals("skip up"))
        {
            y -= speed * 2;
        }
        else if (action.equals("skip down"))
        {
            y += speed * 2;
        }
        else if (action.equals("skip right"))
        {
            x += speed * 2;
        }
        else if (action.equals("skip left"))
        {
            x -= speed * 2;
        }
        else if (action.equals("dont move"))
        {
            //do nothing.
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
			tex = Textures.getTex("antEast");	//east
			direction = "east";
		}
		else if (direction.equals("south"))
		{
			tex = Textures.getTex("antWest");	//west
			direction = "west";
		}
		else if (direction.equals("west"))
		{
			tex = Textures.getTex("antNorth");	//north
			direction = "north";
		}
		else if (direction.equals("east"))
		{
			tex = Textures.getTex("antSouth");	//south
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
			tex = Textures.getTex("antWest");	//west
			direction = "west";
		}
		else if (direction.equals("south"))
		{
			tex = Textures.getTex("antEast"); //east
			direction = "east";
		}
		else if (direction.equals("west"))
		{
			tex = Textures.getTex("antSouth");	//south
			direction = "south";
		}
		else if (direction.equals("east"))
		{
			tex = Textures.getTex("antNorth");	//north
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
		fitness = score;    //Scores off of just board completion.

        for (int i = 0; i < colorIn.length-1; i++)
        {
            if (colorsUsed.get(colorIn[i]) > 0)
            {
                fitness += 5;   //Scores off of colors used.
            }
        }
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