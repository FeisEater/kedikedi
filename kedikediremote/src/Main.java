
import javax.swing.SwingUtilities;


public class Main {
	public static void main (String[] args) {
        Remote remote = new Remote();
        Input input = new Input(remote);
		GraphicsInterface gui = new GraphicsInterface(remote);
        SwingUtilities.invokeLater(gui);
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
