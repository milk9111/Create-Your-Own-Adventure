package backend;

import java.util.ArrayList;
import java.util.List;

public class Situation {

	private final String myState;
	
	private final String myDialog;
	
	private final int myButtonCount;
	
	private final List<String> myButtonInfo;
	
	private final List<String> myButtonKeys;
	
	private Enemy myEnemy;
	
	
	public Situation () {
		this ("", "", 0, new ArrayList<String>(), new ArrayList<String>());
	}
	
	public Situation (final String theState, final String theDialog, final Enemy theEnemy, final List<String> theButtonInfo) {
		this (theState, theDialog, 0 , theButtonInfo, new ArrayList<String>());
		myEnemy = theEnemy;
	}
	
	public Situation (final String theState, final String theDialog, 
			final int theButtonCount, final List<String> theButtonInfo, final List<String> theButtonKeys) {
		
		myState = theState;
		myDialog = theDialog;
		myButtonCount = theButtonCount;
		myButtonInfo = theButtonInfo;
		myButtonKeys = theButtonKeys;
	}
	
	public Enemy getEnemy () {
		return myEnemy;
	}
	
	public String getState () {
		return myState;
	}
	
	public String getDialog () {
		return myDialog;
	}
	
	public int getButtonCount () {
		return myButtonCount;
	}
	
	public List<String> getButtonInfo () {
		
		List<String> clone = new ArrayList<String>();
		
		for (String s : myButtonInfo) {
			clone.add(s);
		}
		
		return clone;
	}
	
	public List<String> getButtonKeys () {
		
		List<String> clone = new ArrayList<String>();
		
		for (String s : myButtonKeys) {
			clone.add(s);
		}
		
		return clone;
	}
	
	public String toString () {
		return myState + "\n" + myDialog + "\n" + myButtonCount + "\n" + myButtonInfo + "\n" + myButtonKeys;
		
	}

}
