package Helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public final class Textures {
	
	public static HashMap<String,Texture> texs = new HashMap<>();
	public static Texture ant;
	public static Texture whiteTile;
	public static Texture blackTile;
	public static Texture greenTile;
	public static Texture blueTile;
	public static Texture redTile;
	public static Texture yellowTile;
	
	public static void load() {
    	try {	
    		
    		ant = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/ant.png"));
    		whiteTile = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/whiteTile.png"));
    		blackTile = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/blackTile.png"));
    		greenTile = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/greenTile.png"));
    		blueTile = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/blueTile.png"));
    		redTile = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/redTile.png"));
    		yellowTile = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/yellowTile.png"));
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	texs.put("ant",ant);
    	texs.put("white",whiteTile);
    	texs.put("black",blackTile);
    	texs.put("green",greenTile);
    	texs.put("blue",blueTile);
    	texs.put("red",redTile);
    	texs.put("yellow",yellowTile);
    	texs.put("whiteTile",whiteTile);
	}
	
	public static Texture getTex(String desc)
	{
		return texs.get(desc);
	}
}
