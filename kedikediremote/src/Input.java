import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Input implements KeyEventDispatcher {
	private Remote remote;
	public Input(Remote remote) {
		KeyboardFocusManager manager = 
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(this);
		this.remote = remote;
	}
	
	private Set<Integer> keys = new HashSet<>();
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		switch (e.getID()) {
			case KeyEvent.KEY_PRESSED:
				if (keys.contains(e.getKeyCode()))
					break;
				keys.add(e.getKeyCode());
				switch (e.getKeyCode()) {
					case KeyEvent.VK_SPACE:
						remote.sendMsg("grabber");
						break;
					case KeyEvent.VK_W:
						remote.sendMsg("start_forward");
						break;
					case KeyEvent.VK_S:
						remote.sendMsg("start_backward");
						break;
					case KeyEvent.VK_A:
						remote.sendMsg("start_left");
						break;
					case KeyEvent.VK_D:
						remote.sendMsg("start_right");
						break;
					//case KeyEvent.VK_ESCAPE:
					//	remote.stop();
					//	break;
					default:
						break;
				}
				break;
			case KeyEvent.KEY_RELEASED:
				keys.remove(e.getKeyCode());
				switch (e.getKeyCode()) {
					case KeyEvent.VK_W:
						remote.sendMsg("stop_forward");
						break;
					case KeyEvent.VK_S:
						remote.sendMsg("stop_backward");
						break;
					case KeyEvent.VK_A:
						remote.sendMsg("stop_left");
						break;
					case KeyEvent.VK_D:
						remote.sendMsg("stop_right");
						break;
					default:
						break;
				}
				break;
			default:
				break;
		}
		return false;
	}

}
