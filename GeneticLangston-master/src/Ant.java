import javax.swing.*;
import java.awt.*;

/**
 * Created by Chris on 2/3/2017.
 */
public class Ant {
    private int x,y;
    private Direction direction;
    ImageIcon icon;

    public Ant(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        switch(direction){
            case NORTH:
                icon = new ImageIcon("img/langstonNorth.png");
                break;
            case SOUTH:
                icon = new ImageIcon("img/langstonSouth.png");
                break;
            case EAST:
                icon = new ImageIcon("img/langstonEast.png");
                break;
            case WEST:
                icon = new ImageIcon("img/langstonWest.png");
                break;
        }
    }

    public void move(){

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void resizeIcon(int x, int y){
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(x,y,Image.SCALE_SMOOTH);
        this.icon = new ImageIcon(newImage);
    }
}
