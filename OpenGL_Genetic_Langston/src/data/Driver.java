package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.lwjgl.Sys;
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
	private int lastGeneration = 99; //This is the last generation wanted - 1. EX: gen. 10 = 9
	static int numTiles = (WINDOW_LENGTH/TILE_SIZE)*(WINDOW_WIDTH/TILE_SIZE);
	static boolean[] visited = new boolean[numTiles];
	int numOfAntsNeeded = 20;	//Number of ants per generation.
	int maxNumOfSteps = 10000;	//Maximum amount of steps an ant can take before it dies.
	float averageFitness = 0;	//Total fitness throughout a generation.
	static int visitedTiles = 1;	//Number of tiles touched so far.
	int sameTileCount = 0; 	//Counter to see if ant has made any progress.
	int noProgressCount = 0;	//Used to see if an ant is making progress fast enough. If not, it dies.
	final int AMOUNT_OF_ANT_ACTIONS = colorList.length;
    private int displaySpeed = 500;  //Speed at which to loop through the program.
	Textures t = new Textures();
    Random rgen = new Random();
    Ant bestAnt = new Ant(0);  //The ant with the highest score.
    Ant genBestAnt = new Ant(0);  //The ant with the highest score per generation.

    int id = 0;
    Ant[] ancestors;


    public Driver() {
        BeginSession();
        t.load();

        //Creates board tiles and initial ants.
        genTiles();
        genAnts();
        //Ant oldAnt = new Ant(10000,halfOfBoardLength+OFFSET,halfOfBoardWidth+OFFSET,TILE_SIZE,TILE_SIZE,Textures.getTex("antNorth"),"north",SPEED,colorList,"265321","44a423");
        //ants.set(0,oldAnt);

        boolean isDone = false; //True when the program is done.

        int antIndex = 0;    //Which ant in the list of ants are we currently messing with.
        ant = ants.get(antIndex); //Selects the ant to simulate.

        visitCenterTile();    //Quick fix to make the starting tile "visited".

        System.out.println("GENERATION: 0");

        while (!Display.isCloseRequested() && isDone == false)        //Runs until simulation is terminated, or finished.
        {
            //Keeps images from staying on screen.
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);


            //Checks if ant has made significant progress, or if it should just be killed instead of finishing its run.
            if (sameTileCount != visitedTiles) {
                noProgressCount = 0;
                sameTileCount = visitedTiles;
            } else {
                noProgressCount++;
            }
            if (noProgressCount >= visitedTiles * 4)    //Number of moves they can make before needing to add a new tile.
            {
                stepsTaken = maxNumOfSteps - 1;
            }


            //Basically just updates the tiles and keeps them drawn.
            //Looks kinda funny if you comment out the update line.
            for (int i = 0; i < tiles.size(); i++) {
                tiles.get(i).update();
            }

            checkTile(ant); //calls the method that does all of the actions regarding the ant and tiles.

            ant.update();   //draws the ant and such...
            stepsTaken++;

            //Checks if this ants turn is done.
            if (stepsTaken == maxNumOfSteps || ant.isOffBoard() == true) {
                //Last ant in generation to check.
                if (antIndex == ants.size() - 1) {
                    //Last generation to check.
                    if (currGeneration == lastGeneration) {
                        isDone = true;
                    }
                    currGeneration++;
                    ant.setScore(getVisitedTiles());    //Gives ant its fitness score.
                    averageFitness += ant.getFitness();
                    System.out.println();
                    if (currGeneration != lastGeneration) {
                        System.out.println("GENERATION: " + currGeneration);
                    }
                    System.out.println("BEST FITNESS OF THIS GENERATION: " + genBestAnt.getFitness());
                    genBestAnt = new Ant(0);
                    //System.out.println("AVERGAE FITNESS: " + averageFitness / ants.size());

                    //Resets everything for next run.
                    generateNextAnts();
                    averageFitness = 0;
                    stepsTaken = 0;
                    restartTiles();
                    antIndex = 0;
                    ant = ants.get(antIndex);
                    ant.resetOrientation();
                    visitCenterTile();
                    //
                } else {
                    ant.setScore(getVisitedTiles());
                    if (ant.getFitness() >= genBestAnt.getFitness()) {
                        genBestAnt = ant;
                        }
                    if (ant.getFitness() >= bestAnt.getFitness()) {
                        bestAnt = ant;
                        System.out.println("======================================================");
                        System.out.println("NEWEST BEST FITNESS SCORE: " + bestAnt.getFitness());
                        bestAnt.getParentInfo();
                        bestAnt.printDNAString();
                        System.out.println("======================================================");
                    }
                    averageFitness += ant.getFitness();
                    antIndex++;
                    ant = ants.get(antIndex);
                    ant.resetOrientation();
                    stepsTaken = 0;
                    restartTiles();
                    visitCenterTile();
                }
                noProgressCount = 0;
                visitedTiles = 1;
                sameTileCount = 0;
            }

            Display.update();
            Display.sync(displaySpeed);  //Basically this is the speed of the simulation
        }
        System.out.println("======================================================");
        System.out.println("FINAL BEST FITNESS SCORE: " + bestAnt.getFitness());
        bestAnt.getParentInfo();
        bestAnt.getStringOfMoves();
        System.out.println("======================================================");
        Display.destroy();  //Destorys the GUI

        startTree();
 	}


 	private void startTree()
    {
        ancestors = new Ant[bestAnt.getID()];

        ancestors[bestAnt.getID()-1] = bestAnt;
        System.out.println("\n\n\n\n" + "BEST ANT ID: " + bestAnt.getID());
        getAncestors(bestAnt);

        for (int i = 0; i < ancestors.length; i++)
        {
            if (ancestors[i] == null)
            {
                //Do nothing.
            }
            else
            {
                if (ancestors[i].getParentA() == null)
                {
                    System.out.println(ancestors[i].getID());// + " // COLOR OUT LIST ==> " + ancestors[i].getStringOfColors()+ " // MOVE LIST ==> " + ancestors[i].getStringOfMoves());
                }
                else
                {
                    System.out.println(ancestors[i].getParentA().getID() + " + " + ancestors[i].getParentB().getID() + " = " + ancestors[i].getID());// + "    // COLOR OUT LIST  ==>    A: " + ancestors[i].getParentA().getStringOfColors() + "          B: " + ancestors[i].getParentB().getStringOfColors() + "          C: " + ancestors[i].getStringOfColors() + "    // MOVE LIST  ==>    A: " + ancestors[i].getParentA().getStringOfMoves() + "          B: " + ancestors[i].getParentB().getStringOfMoves() + "          C: " + ancestors[i].getStringOfMoves());
                }
            }
        }
    }

    private void getAncestors(Ant a)
    {
        if (a.getParentA() != null)
        {
            ancestors[a.getParentA().getID()] = a.getParentA();
            ancestors[a.getParentB().getID()] = a.getParentB();
            getAncestors(a.getParentA());
            getAncestors(a.getParentB());
        }
    }



	private void generateNextAnts()
	{
		//Selecting the best from the current generation.
		ArrayList<Ant> tmp = new ArrayList<>();
		averageFitness = averageFitness/ants.size();

		for (Ant a : ants)
		{
			a.setX(halfOfBoardLength + OFFSET);	//Sets the starting location back to the center.
			a.setY(halfOfBoardWidth + OFFSET);  //

            //Adds all the ants that are going to breed.
			if (a.getFitness() > averageFitness)
			{
				tmp.add(a);
			}
		}

		//If there is only one ant above average, we will add another random ant to the batch so
        //  it won't just breed with itself.
		int index = rgen.nextInt(ants.size());
		if (tmp.size() == 1)
        {
            tmp.add(ants.get(index));
        }

        //This will see if there is one ant, or an odd amount and it will add a mutated ant to it to fix odd counts.
		//This is for breeding purposes.
        if (tmp.size() == 1 || tmp.size() % 2 > 0)
        {
            System.out.println("MUTATION OCCURRED FOR            " + ant.getID());
            tmp.add(mutate(tmp.get(0)));    //Just mutate the first ant and add it to the bottom of the list.
        }
		
		numOfAntsNeeded = ants.size() - tmp.size();	//Figures out how many new ants we need to make.
		ants = tmp;

		crossOver();   //Breeds ant together.
        Collections.shuffle(ants);  //Shuffles the ants so it increases randomness.
		genAnts();  //Adding new ants to fill the places of the old failing ants.
	}

	
	/**
	 * At this point, the arrayList ants only contains the ants that are being crossed to make new ants.
	 */
	private void crossOver()
	{
		ArrayList<Ant> tmp = new ArrayList<>();
		int slicer;
        Ant mom, dad;

		boolean firstParent = true; //Determines whether to make parent B the next ant or previous ant.
		
		for (int i = 0; i < ants.size(); i++)
		{
            String[] crossedColors = new String[AMOUNT_OF_ANT_ACTIONS];	//Array of the ants formed from crossover.
            String[] crossedMoves = new String[AMOUNT_OF_ANT_ACTIONS];	//

            //This will make sure it passes good DNA which will speed up the process.
            //Could randomly generate this number to make it more random, but will slow down results.
            //slicer = ants.get(i).getSliceSpot();

            //Use this slicer to make it more of a random choice, which will have a chance of losing progress.
            slicer = rgen.nextInt(colorList.length);

            if (firstParent == true)
            {
                mom = ants.get(i);
                dad = ants.get(i+1);
                genMoveList(mom, dad,slicer, crossedMoves, crossedColors);
                firstParent = false;
            }
            else
            {
                mom = ants.get(i);
                dad = ants.get(i-1);
                genMoveList(mom, dad,slicer, crossedMoves, crossedColors);
                firstParent = true;
            }

            //New ant with crossover implemented.
            Ant a = new Ant(id++,halfOfBoardLength+OFFSET,halfOfBoardWidth+OFFSET,TILE_SIZE,TILE_SIZE,Textures.getTex("antNorth"),"north", SPEED, colorList, crossedMoves, crossedColors);
            a.setParents(mom,dad);

            //Adding mutation to ants here
            final int MUTATION_CHANCE = 10000;    //Change this to make mutation occur more or less.

            int shouldMutate = rgen.nextInt(MUTATION_CHANCE);

            if (shouldMutate == 1)	//0.01% chance
            {
                System.out.println("MUTATION OCCURRED FOR            " + ant.getID());
                a = mutate(a);
            }
            tmp.add(a);
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
        //Taking the parent DNAs and getting ready for crossover.
        String[] aMoves = a.getMoves(slicer, true);
		String[] aColors = a.getColors(slicer, true);
		String[] bMoves = b.getMoves(slicer, false);
		String[] bColors = b.getColors(slicer, false);

        //Adding the parent DNA into the array of the childs DNA.
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

	private Ant mutate(Ant a)
    {
        int index1 = 0;
        int index2 = 0;
        //Quick loop to make sure mutated indices don't pick the same DNA strand, which would basically do nothing, since
        //  we would be replacing the DNA with itself.
        while (index1 == index2)
        {
            index1 = rgen.nextInt(colorList.length);
            index2 = rgen.nextInt(colorList.length);
        }
        a.swap(index1,index2);
        return a;
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


	//Mostly used for tsting.
	private void getScore()
    {
        float highScore = 0;
        Ant bestAnt = null;
        for (int i = 0; i < ants.size(); i++)
        {
            if (ants.get(i).getFitness() > highScore)
            {
                highScore = ants.get(i).getFitness();
                bestAnt = ants.get(i);
            }
            else
            {
                /*System.out.println("LOSER ANT:");
                for (int j = 0; j < ants.get(i).moves.length; j++)
                {
                    System.out.print(ants.get(i).moves[j] + ", ");
                }
                System.out.println();*/
            }
        }
        System.out.println("BEST SCORE: " + highScore);
        System.out.println("BEST ANT MOVES: ");
        for (int i = 0; i < bestAnt.moves.length; i++)
        {
            System.out.print(bestAnt.moves[i] + ", ");
        }
        System.out.println();
    }


    /**
     * Not needed, but shows the moves and or colors of the current ant.
     */
	private static void getMoveList()
	{
		System.out.println();
		for (int i = 0; i < ant.colorIn.length; i ++)
		{
			System.out.print(ant.colorIn[i] + ", ");
		}
		System.out.println();
		for (int i = 0; i < ant.colorIn.length; i ++)
		{
			System.out.print(ant.moves[i] + ", ");
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
        moveList.add("turn clockwise");
        moveList.add("turn counter clockwise");
        moveList.add("turn counter clockwise");
        moveList.add("turn counter clockwise");
        moveList.add("turn around");
        moveList.add("move right");
        moveList.add("move right");
        moveList.add("move right");
        moveList.add("move backward");
        moveList.add("move left");
        moveList.add("move left");
        moveList.add("move forward");
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
            //System.out.println("NEW ANT MADE");
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
			
			Ant ant = new Ant(id++,halfOfBoardLength+OFFSET,halfOfBoardWidth+OFFSET,TILE_SIZE,TILE_SIZE,Textures.getTex("antNorth"),"north", SPEED,colorList, moves, colorOut);
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