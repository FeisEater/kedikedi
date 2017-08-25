
import javax.swing.SwingUtilities;


public class Main {
	public static void main (String[] args) {
        GraphicsInterface gui = new GraphicsInterface();
        SwingUtilities.invokeLater(gui);
        Remote remote = new Remote();
        Input input = new Input(remote);
        while (!gui.componentsCreated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println("Piirtoalustaa ei ole vielä luotu.");
            }
        }
        remote.run();		
	}
}
