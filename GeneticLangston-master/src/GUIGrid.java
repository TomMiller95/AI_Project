import javax.swing.*;
import java.awt.*;

/**
 * Created by Chris on 2/3/2017.
 */
public class GUIGrid {
    final int WINDOW_SIZE = 600;
    int x, y;
    JLabel[][] grid;
    JFrame window;

    public void setup(int x, int y){
        this.x = x;
        this.y = y;
        grid = new JLabel[x][y];
        window = new JFrame("Ant");
        window.getContentPane().setBackground(Color.BLACK);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new GridLayout(x, y,1,1));
        window.setResizable(false);
        window.setSize(WINDOW_SIZE,WINDOW_SIZE);
        window.setVisible(true);

    }

    private void drawGrid(Ant a){
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                JLabel label = new JLabel();
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                if(i == a.getX() && j == a.getY()) {
                    a.resizeIcon(WINDOW_SIZE/x,WINDOW_SIZE/y);
                    label = new JLabel(a.icon);
                    label.setOpaque(true);
                    label.setBackground(Color.WHITE);
                    grid[i][j] = label;
                } else {
                    grid[i][j] = label;
                }
                window.add(label);
            }
        }
    }

    public void render(Ant a){
//        window.repaint();
        drawGrid(a);
        window.validate();
    }
}
