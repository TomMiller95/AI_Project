package data;

import org.newdawn.slick.opengl.Texture;

/**
 * Created by Tom on 4/19/17.
 */
public class Node {

    private float x,y,height, width;
    private Texture tex;

    public Node(float x, float y, float height, float width, Texture tex)
    {
        this.y = y;
        this.x = x;
        this.height = height;
        this.width = width;
        this.tex = tex;
    }
}
