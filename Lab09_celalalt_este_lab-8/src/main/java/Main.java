import org.model.Maze;
import org.gui.GameWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Increased maze size to 15x15
        Maze maze = new Maze(15);
        
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow(maze);
            window.setVisible(true);
        });

        // Increased to 6 robots and 3 bunnies for the larger map
        maze.startSimulation(6, 3);
    }
}