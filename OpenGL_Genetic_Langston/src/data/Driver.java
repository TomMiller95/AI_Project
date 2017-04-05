package data;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import Helpers.Artist;
import Helpers.Textures;
import static Helpers.Artist.*;

public class Driver {
	
	private ArrayList<Tile> tiles = new ArrayList<>();	//Holds all of the tiles.
	static final int TILE_SIZE = 20;	//Size of the tiles.
	static final int WINDOW_LENGTH = Artist.HEIGHT;	//Same in Artist
	static final int WINDOW_WIDTH = Artist.WIDTH;	//Same in Artist
	int halfOfBoardLength = ((WINDOW_LENGTH/TILE_SIZE)/2) * TILE_SIZE;
	int halfOfBoardWidth = ((WINDOW_WIDTH/TILE_SIZE)/2) * TILE_SIZE;
	final int OFFSET = 1;	//This is used to make the tiles and the ants look nicely dispersed.
	final int SPEED = 20;	//The amount of distance the ant goes each step. Basically the TILE_SIZE
	static int stepsTaken = 0;	//Count of amount of steps an ant takes in a round.
	static Ant ant;	//Represents the ant currently being active.
	String[] colorList = {"white","black","blue","green","yellow","red"};	//Basic list of colors for an ant to recieve.
	private ArrayList<Ant> ants = new ArrayList<>();	//Holds the ants of the current generation.
	private int currGeneration = 0;	//Current generation number.
	private int lastGeneration = 4; //This is the last generation wanted - 1. EX: gen. 10 = 9
	static int numTiles = (WINDOW_LENGTH/TILE_SIZE)*(WINDOW_WIDTH/TILE_SIZE);
	static boolean[] visited = new boolean[numTiles];
	int numOfAntsNeeded = 10;	//Number of ants per generation.
	int maxNumOfSteps = 3000;	//Maximum amount of steps an ant can take before it dies.
	float averageFitness = 0;	//Total fitness throughout a generation.
	static int visitedTiles = 1;	//Number of tiles touched so far.
	int sameTileCount = 0; 	//Counter to see if ant has made any progress.
	int noProgressCount = 0;	//Used to see if an ant is making progess fast enough. If not, it dies.
	final int AMOUNT_OF_ANT_ACTIONS = colorList.length;
	Textures t = new Textures();

	public Driver()
	{
		BeginSession();
		t.load();
		
		/*try {
			PixelMachine p = new PixelMachine();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		//Creates board tiles and initial ants.
		genTiles();
		genAnts();
				
		boolean isDone = false;
		
		int antIndex = 0;	//Which ant in the list of ants are we currenlty messing with.
		ant = ants.get(antIndex);
		visitCenterTile();	//Quick fix to make the starting tile "visited"
		
		while (!Display.isCloseRequested() && isDone == false)		//Runs until simulation is terminated, or finished.
		{
			//Keeps images from staying on screen.
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);


            //Checks if ant has made significant progress, or if it should just be killed instead of finishing its run.
			if (sameTileCount != visitedTiles)
			{
				noProgressCount = 0;
				sameTileCount = visitedTiles;
			}
			else
			{
				noProgressCount++;
			}
			if (noProgressCount >= visitedTiles*4)	//Number of moves they can make before needing to add a new tile.
			{
				stepsTaken = maxNumOfSteps-1;
			}


			//Basically just updates the tiles and keeps them drawn.
            //Looks kinda funny if you comment out the update line.
			for (int i = 0; i < tiles.size(); i++)
			{
				tiles.get(i).update();
			}
			
			checkTile(ant); //calls the method that does all of the actions regarding the ant and tiles.
			
			ant.update();   //draws the ant and such...
			stepsTaken++;

            //Checks if this ants turn is done.
			if (stepsTaken == maxNumOfSteps || ant.isOffBoard() == true)
			{			
				//Last ant in generation to check.
				if (antIndex == ants.size()-1)
				{
                    //Last generation to check.
					if (currGeneration == lastGeneration)
					{
						isDone = true;
					}
                    currGeneration++;
					ant.setScore(getVisitedTiles());    //Gives ant its fitness score.
					averageFitness += ant.getFitness();
					System.out.println();
					System.out.println("GENERATION: " + currGeneration);
					System.out.println("AVERGAE FITNESS: " + averageFitness/ants.size());
                    getScore();

                    //Resets everything for next run.
                    generateNextAnts();
					averageFitness = 0;
					stepsTaken = 0;
					restartTiles();
					antIndex = 0;
					ant = ants.get(antIndex);
					ant.resetOrientation();
					visitCenterTile();
				}
				else
				{
					ant.setScore(getVisitedTiles());
					averageFitness += ant.getFitness();
					antIndex++;
					ant = ants.get(antIndex);
					ant.resetOrientation();
					stepsTaken = 0;
					restartTiles();
					visitCenterTile();
				}
				//getMoveList();
				noProgressCount = 0;
				visitedTiles = 1;
				sameTileCount = 0;
			}

			Display.update();
			Display.sync(100);  //Basically this is the speed of the simulation
		}
		
		Display.destroy();  //Destorys the GUI
 	}



	private void generateNextAnts()
	{
		//Selecting the best from the current generation.
		ArrayList<Ant> tmp = new ArrayList<>();
		averageFitness = averageFitness/ants.size();
        Ant tmpAnt = null; //used if only one ant passes the fitness eval.
		
		for (Ant a : ants)
		{
			a.setX(halfOfBoardLength + OFFSET);	//Sets the starting location back to the center.
			a.setY(halfOfBoardWidth + OFFSET);  //
			if (a.getFitness() > averageFitness)
			{
				tmp.add(a);
                tmpAnt = a;
			}
		}

        //This should probably change because its just creating the same ants.
		if (ants.size() == 1)
        {
            tmp.add(tmpAnt);
        }
		
		numOfAntsNeeded = ants.size() - tmp.size();	//Figures out how many new ants we need to make.
		ants = tmp;
		crossOver();
		
		//Adding new ants to fill the places of the old failing ants.
		genAnts();
	}
	
	
	/** 
	 * Called once in the beginning of each run to show that the ant visited the first tile.
	 */
	private void visitCenterTile()
	{
		for (int i = 0; i < tiles.size(); i++)
		{
			if ((tiles.get(i).getX() == halfOfBoardLength+OFFSET) && (tiles.get(i).getY() == halfOfBoardWidth+OFFSET))
			{
				tiles.get(i).visit();
			}
		}
	}
	
	
	/**
	 * At this point, the arrayList ants only contains the ants that are being crossed to make new ants.
	 */
	private void crossOver()
	{
		Random rgen = new Random();
		ArrayList<Ant> tmp = new ArrayList<>();
		int slicer = 0;
		
		int offset = ants.size() % 2; //Stops it from trying to cross with an odd amount of ants.

		boolean firstParent = true;
		
		for (int i = 0; i < ants.size(); i++)
		{
            String[] crossedColors = new String[AMOUNT_OF_ANT_ACTIONS];	//Array of the ants formed from crossover.
            String[] crossedMoves = new String[AMOUNT_OF_ANT_ACTIONS];	//
			//Last ant that passed the test. Odd amount so instead of crossover, he is just added in.
			if (offset > 0 && i == ants.size()-1)
			{
				tmp.add(ants.get(i));
			}
			//This will happen every time except possibly once at the end.
			else
			{
				slicer = rgen.nextInt(AMOUNT_OF_ANT_ACTIONS) + 1; //make the colors have a higher chance to be selected if they are used
				if (firstParent == true)
				{
					genMoveList(ants.get(i), ants.get(i+1),slicer, crossedMoves, crossedColors);
					firstParent = false;
				}
				else
				{
					genMoveList(ants.get(i), ants.get(i-1),slicer, crossedMoves, crossedColors);
					firstParent = true;
				}
				Ant a = new Ant(halfOfBoardLength+OFFSET,halfOfBoardWidth+OFFSET,TILE_SIZE,TILE_SIZE,Textures.getTex("ant"),"north", SPEED, colorList, crossedMoves, crossedColors);
				tmp.add(a);
			}
		}
		ants = tmp;
	}


    /**
     * Takes in two ants and a random number.
     * It will cut the ant's arrays of moves and colors up and mix them together to
     * do the crossover.
     */
	private void genMoveList(Ant a, Ant b, int slicer, String[] crossedMoves, String[] crossedColors)
	{
        System.out.println("CROSS OVER");
		String[] aMoves = a.getMoves(slicer, true);
		String[] aColors = a.getColors(slicer, true);
		String[] bMoves = b.getMoves(slicer, false);
		String[] bColors = b.getColors(slicer, false);
        System.out.println("ANT A MOVES");
        for (int i = 0; i < aMoves.length; i++)
        {
            System.out.print(aMoves[i] + ", ");
        }
        System.out.println();
        System.out.println("ANT B MOVES");
        for (int i = 0; i < bMoves.length; i++)
        {
            System.out.print(bMoves[i] + ", " );
        }
        System.out.println();


		int x = 0;
		for (int i = 0; i < aMoves.length; i++)
		{
			crossedMoves[i] = aMoves[i];
			x++;
		}
		for (int i = 0; i < bColors.length; i++)
		{
			crossedMoves[x] = bMoves[i];
			x++;
		}
		
		x = 0;
		for (int i = 0; i < aColors.length; i++)
		{
			crossedColors[i] = aColors[i];
			x++;
		}
		for (int i = 0; i < bColors.length; i++)
		{
			crossedColors[x] = bColors[i];
			x++;
		}

		System.out.println("CROSSED MOVES");
        for (int i = 0; i < crossedMoves.length; i++)
        {
            System.out.print(crossedMoves[i] + ", " );
        }
        System.out.println();
	}
	
	
	//Makes the tiles all white again.
	private void restartTiles()
	{
		visited = new boolean[numTiles];
		for (int i = 0; i < tiles.size(); i++)
		{
			tiles.get(i).changeColor("white");
			tiles.get(i).unvist();
		}
	}

    /**
     * Narrows down what tile the ant is on, checks the color of the tile,
     * sees what the ant should do when on that color,
     * does the action, changes the tile to the color specified by the ant.
     */
	private void checkTile(Ant ant)
	{
		for (int i = 0; i < tiles.size(); i++)
		{
			if (tiles.get(i).getX() == ant.getX() && tiles.get(i).getY() == ant.getY())
			{
				String color = tiles.get(i).getColor();
				int index = ant.onTile(color);
				String actionName = ant.findAction(color);
				ant.doAction(actionName);
				tiles.get(i).changeColor(ant.getOutColor(index));
				tiles.get(i).visit();
				visited[i] = true;
				break;
			}
		}
	}
	
	
	private static float getVisitedTiles()
	{
		int count = 0;
		for (int i = 0; i < visited.length; i++)
		{
			if (visited[i] == true)
			{
				count++;
			}
		}
		return((float)count/(float)numTiles * 100);
	}


	private void getScore()
    {
        float highScore = 0;
        for (int i = 0; i < ants.size(); i++)
        {
            if (ants.get(i).getFitness() > highScore)
            {
                highScore = ants.get(i).getFitness();
            }
        }
        System.out.println("BEST SCORE: " + highScore);
    }


    /**
     * Not needed, but shows the moves and or colors of all this ants in the generation.
     */
	private static void getMoveList()
	{
		System.out.println();
		for (int i = 0; i < ant.colorIn.length; i ++)
		{
			//System.out.print(ant.colorIn[i] + ", ");
		}
		System.out.println();
		for (int i = 0; i < ant.colorIn.length; i ++)
		{
			//System.out.print(ant.moves[i] + ", ");
		}
		System.out.println();
		for (int i = 0; i < ant.colorIn.length; i ++)
		{
			System.out.print(ant.colorOut[i] + ", ");
		}
		System.out.println();
	}
	
	
	
	
	
	
	
								//######## Startup Methods ########\\
	
	private void genAnts()
	{
		int antsToMake = numOfAntsNeeded;
        ArrayList<String> colorOutList = new ArrayList<>();
        colorOutList.add("white");
        colorOutList.add("black");
        colorOutList.add("blue");
        colorOutList.add("green");
        colorOutList.add("yellow");
        colorOutList.add("red");

        ArrayList<String> moveList = new ArrayList<>();
        moveList.add("turn clockwise");
        moveList.add("turn counter clockwise");
        moveList.add("move right");
        moveList.add("move backward");
        moveList.add("move left");
        moveList.add("move forward");
        moveList.add("diaganol up right");
        moveList.add("diaganol up left");
        moveList.add("diaganol down right");
        moveList.add("diaganol down left");
        moveList.add("skip up");
        moveList.add("skip down");
        moveList.add("skip left");
        moveList.add("skip right");
        moveList.add("dont move");


        while (antsToMake > 0)
		{
            System.out.println("NEW ANT MADE");
			String[] moves = new String[colorList.length];
			String[] colorOut = new String[colorList.length];
			
			Random rgen = new Random();
			
			int numOptions = colorList.length;
			while(numOptions > 0)
			{
				int randomOutColor = rgen.nextInt(colorOutList.size());
				int randomMove = rgen.nextInt(moveList.size());			
				
				moves[numOptions-1] = moveList.get(randomMove);
				colorOut[numOptions-1] = colorOutList.get(randomOutColor);
				
				numOptions--;
			}
			
			Ant ant = new Ant(halfOfBoardLength+OFFSET,halfOfBoardWidth+OFFSET,TILE_SIZE,TILE_SIZE,Textures.getTex("ant"),"north", SPEED,colorList, moves, colorOut);
			ants.add(ant);
			
			antsToMake--;
		}
	}

    /**
     * Called from another class to add 1 to the count of tiles that are visited.
     */
	public static void addOneTile()
	{
		visitedTiles++;
	}

	
	private void genTiles()
	{
		for (int x = 0; x < WINDOW_LENGTH/TILE_SIZE; x++)
		{
			for (int y= 0; y < WINDOW_WIDTH/TILE_SIZE; y++)
			{
				Tile t = new Tile(x*TILE_SIZE+OFFSET,y*TILE_SIZE+OFFSET,TILE_SIZE,TILE_SIZE,Textures.getTex("whiteTile"));
				tiles.add(t);
			}
		}
	}
	public static void main(String[] args)
	{
		new Driver();
	}
}