package backend;

import java.util.Observable;
import java.util.Observer;

import gui.CyoaGUI;

public class Fight extends Observable implements Observer {

	private Player myPlayer;
	
	private Enemy myEnemy;
	
	private boolean isDone;
	
	public Fight (final Player thePlayer, final Enemy theEnemy) {
		myPlayer = thePlayer;
		myEnemy = theEnemy;
	}
	
	private void enemyHit (final int theDamage) {
		myEnemy.receiveDamage(theDamage);
		
		if (myEnemy.getHealthRemaining() <= 0) {
			isDone = true;
		}
		
		String s = checkIfEnd(myEnemy.getHealthRemaining(), myPlayer.getName(), myEnemy.getName(), myEnemy.showDamageTaken(theDamage));
		changeAndNotify(true, s);
		
		if (!isDone) {
			playerHit (myEnemy.attack());
		}
	}
	
	private void playerHit (final int theDamage) {
		myPlayer.receiveDamage(theDamage);
		
		String s = checkIfEnd(myPlayer.getHealthRemaining(), myEnemy.getName(), myPlayer.getName(),  myPlayer.showDamageTaken(theDamage));
		changeAndNotify(true, s);
	}
	
	private void changeAndNotify (final boolean theValue, final String theString) {
		setChanged ();
		notifyObservers (new Boolean (theValue));
		setChanged ();
		notifyObservers (theString);
	}
	
	private String checkIfEnd (final int theHealth, final String theName1, final String theName2, final int theDamage) {
		String s = new String ();
		
		if (theHealth <= 0) {
			s = theName2 + " is dead!";
		} else {
			String plural = "points";
			if (theDamage == 1) {
				plural = "point";
			}
			s = theName1 + " hit " + theName2 + " for " + theDamage + " " + plural + " of damage.";
		}
		
		return s;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0.getClass().getSimpleName().equals("CyoaGUI") && arg1 instanceof Integer) {
			enemyHit((Integer) arg1);
		}
	}

}
