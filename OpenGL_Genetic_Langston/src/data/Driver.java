package data;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import Helpers.Artist;
import Helpers.Textures;
import static Helpers.Artist.*;

public class Driver {
	
	private ArrayList<Tile> tiles = new ArrayList<>();
	static final int TILE_SIZE = 20;
	static final int WINDOW_LENGTH = Artist.HEIGHT;	//Same in Artist
	static final int WINDOW_WIDTH = Artist.WIDTH;	//Same in Artist
	int halfOfBoardLength = ((WINDOW_LENGTH/TILE_SIZE)/2) * TILE_SIZE;
	int halfOfBoardWidth = ((WINDOW_WIDTH/TILE_SIZE)/2) * TILE_SIZE;
	final int OFFSET = 1;	//This is used to make the tiles and the ants look nicely dispersed.
	final int SPEED = 20;	//The amount of distance the ant goes each step. Basically the TILE_SIZE
	static int stepsTaken = 0;
	static Ant ant;
	String[] colorList = {"white","black","blue","green","yellow","red"};
	private ArrayList<Ant> ants = new ArrayList<>();
	private int generation = 0;
	private int lastGeneration = 0; //This is the last generation wanted - 1. EX: gen. 10 = 9
	static int numTiles = (WINDOW_LENGTH/TILE_SIZE)*(WINDOW_WIDTH/TILE_SIZE);
	static boolean[] visited = new boolean[numTiles];
	int numOfAntsNeeded = 10;
	int maxNumOfSteps = 100;
	float averageFitness = 0;
	static int visitedTiles = 1;	//Number of tiles touched so far.
	int sameTileCount = 0; 	//Counter to see if ant has made any progress.
	int noProgressCount = 0;
	
	final int AMOUNT_OF_ANT_ACTIONS = 5;
	String[] crossedColors = new String[AMOUNT_OF_ANT_ACTIONS + 1];
	String[] crossedMoves = new String[AMOUNT_OF_ANT_ACTIONS + 1];
	
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
		
		genTiles();
		genAnts();
				
		boolean isDone = false;
		
		int antIndex = 0;
		ant = ants.get(antIndex);
		visitCenterTile();
		
		while (!Display.isCloseRequested() && isDone == false)
		{
			//Keeps images from staying on screen.
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
			
			
			
			if (sameTileCount != visitedTiles)
			{
				noProgressCount = 0;
				sameTileCount = visitedTiles;
				//System.out.println("RESET!");
			}
			else
			{
				noProgressCount++;
			}
			if (noProgressCount >= visitedTiles*4)	//Number of moves they can make before needing to add a new tile.
			{
				stepsTaken = maxNumOfSteps-1;
				//System.out.println("KILLED FOR NO PROGRESS");
			}
			
			
			
			
			for (int i = 0; i < tiles.size(); i++)
			{
				tiles.get(i).update();
			}
			
			checkTile(ant);
			
			ant.update();
			stepsTaken++;
			
			if (stepsTaken == maxNumOfSteps || ant.isOffBoard() == true)
			{			
				//Last ant in batch to check.
				if (antIndex == ants.size()-1)
				{
					if (generation == lastGeneration)
					{
						isDone = true;
					}
					generation++;
					ant.setScore(getVisitedTiles());
					averageFitness += ant.getFitness();
					System.out.println();
					System.out.println("GENERATION: " + generation);
					System.out.println("AVERGAE FITNESS: " + averageFitness/ants.size());
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
			Display.sync(100);
		}
		
		Display.destroy();
 	}
	
	private void generateNextAnts()
	{
		System.out.println();
		System.out.println("OLD ANTS\n");
		for (int i = 0; i < ants.size(); i++)
		{
			System.out.println("FITNESS: " + ants.get(i).getFitness());
			for (int j = 0; j < ants.get(i).colorOut.length; j++)
			{
				System.out.print(ants.get(i).colorOut[j] + ", ");
			}
			System.out.println();
		}
		//Selecting the best from the current generation.
		ArrayList<Ant> tmp = new ArrayList<>();
		averageFitness = averageFitness/ants.size();
		
		for (Ant a : ants)
		{
			a.setX(halfOfBoardLength + OFFSET);	//Sets the starting location back to the center.
			a.setY(halfOfBoardWidth + OFFSET);  //
			if (a.getFitness() > averageFitness)
			{
				tmp.add(a);
			}
		}
		
		
		numOfAntsNeeded = ants.size() - tmp.size();	//Figures out how many new ants we need to make.
		ants = tmp;
		crossOver();
		
		//Adding new ants to fill the places of the old failing ants.
		genAnts();
		
		System.out.println("\nNEW ANTS\n");
		for (int i = 0; i < ants.size(); i++)
		{
			for (int j = 0; j < ants.get(i).colorOut.length; j++)
			{
				System.out.print("ANT #" + i + " " + ants.get(i).colorOut[j] + ", ");
			}
			System.out.println();
		}
		System.out.println("\n\n");
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
		System.out.println();
		System.out.println("DOING CROSSOVER");
		Random rgen = new Random();
		ArrayList<Ant> tmp = new ArrayList<>();
		int slicer = 0;
		
		int offset = ants.size() % 2; //Stops it from trying to cross with an odd amount of ants.
		
		boolean firstParent = true;
		
		for (int i = 0; i < ants.size(); i++)
		{
			//Last ant that passed the test. Odd amount so instead of crossover, he is just added in.
			if (offset > 0 && i == ants.size()-1)
			{
				System.out.println("ADDING OLD ANT");
				tmp.add(ants.get(i));
			}
			//This will happen every time except possibly once at the end.
			else
			{
				slicer = rgen.nextInt(AMOUNT_OF_ANT_ACTIONS) + 1;
				if (firstParent == true)
				{
					genMoveList(ants.get(i), ants.get(i+1),slicer);
					firstParent = false;
				}
				else
				{
					genMoveList(ants.get(i), ants.get(i-1),slicer);
					firstParent = true;
				}
				Ant a = new Ant(halfOfBoardLength+OFFSET,halfOfBoardWidth+OFFSET,TILE_SIZE,TILE_SIZE,Textures.getTex("ant"),"north", SPEED, colorList, crossedMoves, crossedColors);
				System.out.println("NEW BABY COLORS");
				for (int t = 0; t < a.colorOut.length; t++)
				{
					System.out.print(a.colorOut[t] + ", ");
				}
				System.out.println();
				tmp.add(a);
			}
		}
		ants = tmp;
		System.out.println("================");
		for (int i = 0; i < ants.size(); i++)
		{
			for (int j = 0; j < ants.get(i).colorOut.length; j++)
			{
				System.out.print("ANT #" + i + " " + ants.get(i).colorOut[j] + ", ");
			}
			System.out.println();
		}
		System.out.println("================");
	}
	
	
	private void genMoveList(Ant a, Ant b, int slicer)
	{
		String[] aMoves = a.getMoves(slicer, true);
		String[] aColors = a.getColors(slicer, true);
		String[] bMoves = b.getMoves(slicer, false);
		String[] bColors = b.getColors(slicer, false);
		
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
		/*System.out.println("========================");
		System.out.println(count + " tiles visited.");
		System.out.println(stepsTaken + " steps taken.");
		System.out.println((float)count/(float)numTiles * 100 + "% of board explored");
		getMoveList();
		System.out.println("========================");*/
		return((float)count/(float)numTiles * 100);
	}
	
	private static void getMoveList()
	{
		System.out.println();
		for (int i = 0; i < ant.moves.length; i ++)
		{
			System.out.print(ant.colorIn[i] + ", ");
		}
		System.out.println();
		for (int i = 0; i < ant.moves.length; i ++)
		{
			System.out.print(ant.moves[i] + ", ");
		}
		System.out.println();
		for (int i = 0; i < ant.moves.length; i ++)
		{
			System.out.print(ant.colorOut[i] + ", ");
		}
		System.out.println();
	}
	
	
	
	
	
	
	
								//######## Startup Methods ########\\
	
	private void genAnts()
	{
		int antsToMake = numOfAntsNeeded;
		
		while (antsToMake > 0)
		{
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
			
			String[] moves = new String[moveList.size()];
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
		
		//Black & Blue
//		String[] colorIn = {"white","black","blue","green","yellow","red"};
//		String[] moves= {"turn clockwise","turn counter clockwise","move right","move backward","move left","move forward"};
//		String[] colorOut = {"red","white","black","white","black","blue"};
		
		//Long Green Symmetry
//		String[] colorIn = {"red","blue","green","white","black","yellow"};
//		String[] moves= {"turn clockwise","turn counter clockwise","move right","move backward","move left","move backward"};
//		String[] colorOut = {"green","yellow","blue","green","white","green"};
		
		//Blue/Yellow Diamond
//    	String[] colorIn = {"red","blue","green","white","black","yellow"};
//		String[] moves= {"turn clockwise","turn counter clockwise","move left","move backward","move left","move forward"};
//		String[] colorOut = {"green","red","blue","yellow","white","green"};
		
		//Blue/Black Line
//		String[] colorIn = {"red","blue","green","white","black","yellow"};
//		String[] moves= {"turn counter clockwise","turn clockwise","move right","move backward","move left","move forward"};
//		String[] colorOut = {"red","green","blue","black","green","white"};
		
		//Nice Colors, but no real pattern
//		String[] colorIn = {"red","blue","green","white","black","yellow"};
//		String[] moves= {"turn counter clockwise","turn clockwise","move right","move backward","move left","move forward"};
//		String[] colorOut = {"black","yellow","red","blue","green","green"};

		//Square..?
//		String[] colorIn = {"red","green","yellow","black","blue","white"};
//		String[] moves= {"turn clockwise","turn counter clockwise","move backward","move forward","move left","move right"};
//		String[] colorOut = {"black","yellow","white","red","green","blue"};
	}
	
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