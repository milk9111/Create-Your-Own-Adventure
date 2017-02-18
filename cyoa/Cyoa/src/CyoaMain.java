import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.CyoaGUI;
import gui.EngineGUI;

public class CyoaMain {
	
	private CyoaMain () {
		throw new IllegalStateException();
	}
	
	public static void main (final String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}
		catch (final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (final InstantiationException e) {
			e.printStackTrace();
		}
		catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable () {
			public void run () {
				new CyoaGUI().start ();
			}
		});
	}
}
