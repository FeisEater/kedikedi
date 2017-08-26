import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class GraphicsInterface extends JPanel implements Runnable {
	/** JFrame of the program. */
	    private JFrame frame;
	/** true if gui is created and is ready to be used. */
	    private boolean componentsCreated;
	    private JTextField fullSpeed = new JTextField("720", 4);
	    private JTextField turnSpeed = new JTextField("90", 4);
	    private JTextField staticTurnSpeed = new JTextField("360", 4);
	    private JButton speedButton = new JButton("Change speed");
	    private Remote remote;
    /**
	 * Constructor.
	 * @param peli Pointer to the game logic object.
	 */
	    public GraphicsInterface(Remote remote)
	    {
	        componentsCreated = false;
	        this.remote = remote;
	    }
	/**
	 * Creates components of the GUI, game layer and menu layer.
	 */
	    public void createComponents()
	    {
	        frame.add(this);
	        add(fullSpeed);
	        add(turnSpeed);
	        add(staticTurnSpeed);
	        add(speedButton);
	        speedButton.addActionListener(new SpeedButtonListener());
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
	    
	    private class SpeedButtonListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				remote.sendMsg("speeds " + fullSpeed.getText() + " " + turnSpeed.getText() + " " + staticTurnSpeed.getText());
			}
	    	
	    }
}
