import javax.swing.*;

/**
 * Created by Chris on 2/3/2017.
 */
public class Driver {
    static int x = 15, y =15;
    static GUIGrid grid;
    static Ant langston;
    public static void main(String[] args){
        grid = new GUIGrid();
        grid.setup(x, y);
        langston  = new Ant(x/2, y/2, Direction.NORTH);
        grid.grid[x/2][x/2] = new JLabel(langston.icon);

        //while(true){
            //langston.move();
            grid.render(langston);
        //}

    }
}
