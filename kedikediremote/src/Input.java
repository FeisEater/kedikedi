import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class Input implements KeyEventDispatcher {
	private Remote remote;
	public Input(Remote remote) {
		KeyboardFocusManager manager = 
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);
		this.remote = remote;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		switch (e.getID()) {
			case KeyEvent.KEY_PRESSED:
				switch (e.getKeyCode()) {
					case KeyEvent.VK_SPACE:
						remote.doSomething();
						break;
					case KeyEvent.VK_Q:
						remote.stop();
						break;
					default:
						break;
				}
				break;
			case KeyEvent.KEY_RELEASED:
				break;
			default:
				break;
		}
		return false;
	}

}
