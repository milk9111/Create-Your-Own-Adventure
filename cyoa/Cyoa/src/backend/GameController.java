package backend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.JPanel;

public class GameController extends Observable implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1195908645558842416L;

	private static final String DIRECTORY = "src/situations";
	
	private final String myKeyWord;
	
	private Situation mySituation;
	
	private boolean myCanPass;
	
	public GameController () {
		myCanPass = false;
		myKeyWord = "Rat_attack";
	}
	
	public Situation findSituationFile (final String theKeyWord) {
		File dir = new File (DIRECTORY);
		Situation newSituation = new Situation();

		for (File file : dir.listFiles()) {
			if ((file.getName().equals(theKeyWord))) {
				try {
					newSituation = readSituationFile(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return newSituation;
		
	}
	
	private Situation readSituationFile(final File theFile) throws IOException {
		Scanner input;
		try {
			input = new Scanner (theFile);
		} catch (final IOException e) {
			input = new Scanner (getClass ().getResourceAsStream(theFile.getName()));
		}
		
		final String state = input.next();
		input.nextLine();
		
		final String dialog = readDialog (input);
		
		Situation newSituation = new Situation ();
		
		if (state.equals("d")) {
			newSituation = readDecisionState (input, state, dialog);
		} else {
			newSituation = readCombatState(input, state, dialog);
		}
		
		if (myCanPass) {
			setChanged();
			notifyObservers(newSituation);
		}
		
		input.close();
		return newSituation;
	}
	
	private Situation readCombatState (final Scanner theInput, final String theState, 
			final String theDialog) {
		
		final String name = theInput.nextLine();
		
		final int health = theInput.nextInt();
		theInput.nextLine();
		
		final int armorRating = theInput.nextInt();
		theInput.nextLine();
		
		final int attack = theInput.nextInt();
		theInput.nextLine();
		
		List<String> buttonInfo = new ArrayList<String> ();
		buttonInfo.add(theInput.nextLine());
		
		final Enemy enemy = new Enemy(name, armorRating, health, attack);
		return new Situation (theState, theDialog, enemy, buttonInfo);
	}
	
	private Situation readDecisionState (final Scanner theInput, final String theState, 
			final String theDialog) {
		
		final int buttonCount = theInput.nextInt();
		theInput.nextLine();
		
		final String infoString = theInput.nextLine();
		final String keyString = theInput.nextLine();
		
		List<String> buttonInfo = new ArrayList<String>();
		List<String> buttonKeys = new ArrayList<String>();

		readLine(infoString, buttonInfo);
		readLine(keyString, buttonKeys);
		
		return new Situation (theState, theDialog, buttonCount, buttonInfo, buttonKeys);
	}
	
	private String readDialog (final Scanner theInput) {
		boolean foundEnd = false;
		StringBuilder theDialog = new StringBuilder ();
		
		while (!foundEnd) {
			String nextLine = theInput.nextLine();
			for (int i = 0; i < nextLine.length(); i++) {
				if (nextLine.charAt(i) != '*') {
					theDialog.append(nextLine.charAt(i));
				} else {
					foundEnd = true;
				}
			}
		}
		
		return theDialog.toString();
		
	}

	private void readLine (final String theString, List<String> theList) {
		int lastPos = 0;

		for (int i = 0; i < theString.length(); i++) {
			if(theString.charAt(i) == '/') {
				theList.add(theString.substring(lastPos, i));
				lastPos = i + 1;
			}
		}
	}
	
	@Override
	public void update(final Observable arg0, final Object arg1) {
		if (arg1 instanceof Boolean) {
			myCanPass = ((Boolean) arg1).booleanValue();
		}
		if (arg1 instanceof String && myCanPass)
		{
			String arg = (String) arg1;
			findSituationFile(arg);
		}
	}

}
