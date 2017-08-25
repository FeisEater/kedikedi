import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GraphicsInterface extends JPanel implements Runnable {
	/** JFrame of the program. */
	    private JFrame frame;
	/** true if gui is created and is ready to be used. */
	    private boolean componentsCreated;
	/**
	 * Constructor.
	 * @param peli Pointer to the game logic object.
	 */
	    public GraphicsInterface()
	    {
	        componentsCreated = false;
	    }
	/**
	 * Creates components of the GUI, game layer and menu layer.
	 */
	    public void createComponents()
	    {
	        frame.add(this);
	    }
	/**
	 * Runs the window with GUI.
	 */
	    @Override
	    public void run()
	    {
	        frame = new JFrame("Robot remote");
	        frame.setPreferredSize(new Dimension(1024, 768));
	        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        createComponents();
	        frame.pack();
	        frame.setVisible(true);
	        componentsCreated = true;
	    }
	/**
	 * 
	 * @return true if gui is created and is ready to be used.
	 */
	    public boolean componentsCreated()
	    {
	        return componentsCreated;
	    }
	    
	    @Override
	    public boolean isOptimizedDrawingEnabled()
	    {
	        return false;
	    }
}
