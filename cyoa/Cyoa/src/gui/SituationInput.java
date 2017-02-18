package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import backend.WriteFile;

public class SituationInput extends Observable implements Observer {

	private WriteFile myFileWriter;
	
	private String myName;
	
	private String myState;
	
	private String myDialog;
	
	private String myEnemyName;
	
	private String myNextSituation;
	
	private JPanel myInput;
	
	private JPanel mySpecialInput;
	
	private JCheckBox myDialogCheck;
	
	private JCheckBox myCombatCheck;
	
	private List<String> myButtonText;
	
	private List<String> myButtonInfo;
	
	private List<JButton> myAddButtons;
	
	private int myButtonCount;
	
	private int myArmorRating;
	
	private int myHealth;
	
	private int myAttackDamage;
	
	private boolean myListCheck;
	
	private boolean myIsOpen;

	private JPanel myBottomButtons;
	
	
	public SituationInput () {
		this ("", "d", "", 1, new ArrayList<String> (), new ArrayList<String> ());
	}
	
	public SituationInput (final String theName, final String theState, final String theDialog, final String theEnemyName, 
			final int theArmorRating, final int theHealth, final int theAttackDamage, final String theNextSituation) {
		
		this (theName, theState, theDialog, 1, new ArrayList<String> (), new ArrayList<String> ());
		
		myEnemyName = theEnemyName;
		myArmorRating = theArmorRating;
		myHealth = theHealth;
		myAttackDamage = theAttackDamage;
		myNextSituation = theNextSituation;
	}
	
	public SituationInput (final String theName, final String theState, final String theDialog, final int theButtonCount,
			final List<String> theButtonText, final List<String> theButtonInfo) {
		
		myName = theName;
		myState = theState;
		myDialog = theDialog;
		myInput = new JPanel ();
		mySpecialInput = new JPanel ();
		myBottomButtons = new JPanel ();
		
		if (myState.equals("d")) {
			myDialogCheck = new JCheckBox ("Dialog", true);
			myCombatCheck = new JCheckBox ("Combat", false);
		} else {
			myDialogCheck = new JCheckBox ("Dialog", false);
			myCombatCheck = new JCheckBox ("Combat", true);
		}
		myButtonCount = theButtonCount;
		
		myButtonText = theButtonText;
		myButtonInfo = theButtonInfo;
		
		myAddButtons = new ArrayList<JButton> ();
		
		myListCheck = true;
	}
	
	public JPanel makePanel () {
		BoxLayout inputLayout = new BoxLayout(myInput, BoxLayout.Y_AXIS);
		myInput.setLayout(inputLayout);
		
		
		JPanel basicInput = new JPanel ();
		BoxLayout inputLayout2 = new BoxLayout(basicInput, BoxLayout.Y_AXIS);
		basicInput.setLayout(inputLayout2);
		createBasicInput (basicInput);
		
		basicInput.setMaximumSize(EngineGUI.MAX_SIZE);
		basicInput.setMinimumSize(EngineGUI.MIN_SIZE);
		
		myInput.add(basicInput);
		
		mySpecialInput = chooseSpecialInput  ();
		
		myInput.add(mySpecialInput);
		
		makeBottomButtons ();
		
		myInput.setMaximumSize(EngineGUI.MAX_SIZE);
		myInput.setMinimumSize(EngineGUI.MAX_SIZE);
		
		return myInput;
	}
	
	
	private boolean checkFields (final JPanel thePanel) {
		boolean allFull = true;
		
		/*for (Component c : thePanel.getComponents()) {
			System.out.println(c.toString());
		}*/
		System.out.println();
		for (Component c : thePanel.getComponents()) {
			if (c instanceof JTextField) {
				if (((JTextField) c).getText().equals("")) {
					System.out.println("Found a text field");
					allFull = false;
				}
			} else if (c instanceof JPanel) {
				System.out.println("going into another panel");
				allFull &= checkFields((JPanel) c);
			}
		}
		
		return allFull;
	}
	

	/*
	 * 	
	 */
	private void makeBottomButtons() {
		myBottomButtons.removeAll();
		final JButton sendInfo = new JButton ("Save Open Situation");
		sendInfo.addActionListener((theEvent) -> {
			if (checkFields (myInput)) {
				System.out.println("made it past the check");
				List<Object> list = new ArrayList<Object> ();
				list.add(myName);
				list.add(myState);
				list.add(myDialog);
				list.add(myButtonCount);
				list.add(myButtonText);
				list.add(myButtonInfo);
				list.add(myEnemyName);
				list.add(myArmorRating);
				list.add(myHealth);
				list.add(myAttackDamage);
				list.add(myNextSituation);
				
				setChanged ();
				notifyObservers (list);
			} else {
				JOptionPane.showMessageDialog(myInput, "Can't save! Empty text boxes!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		final JButton startGame = new JButton ("Start game");
		startGame.addActionListener((theEvent) -> {
			new CyoaGUI ().start();
		});
		
		myBottomButtons.add(sendInfo);
		myBottomButtons.add(startGame);
		myInput.add(myBottomButtons, BorderLayout.SOUTH);
	}

	private JPanel chooseSpecialInput () {
		myInput.remove(mySpecialInput);
		
		if (myState.equals("d")) {
			DecisionInput dInput = new DecisionInput(myButtonCount, myButtonText, myButtonInfo);
			dInput.addObserver(this);
			addObserver(dInput);
			
			mySpecialInput = dInput.makePanel();
			myInput.revalidate();
			myInput.repaint();
		} else if (myState.equals("c")) {
			CombatInput cInput = new CombatInput(myEnemyName, myArmorRating, myHealth, myAttackDamage, myNextSituation);
			cInput.addObserver(this);
			addObserver(cInput);
			
			mySpecialInput = cInput.makePanel();
			myInput.revalidate();
			myInput.repaint();
		}
		
		return mySpecialInput;
	}

	private void createBasicInput(final JPanel basicInput) {
		final JPanel nameInputP = new JPanel ();
		nameInputP.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel name = new JLabel ("Situation Name: ");
		JTextField nameInput = new JTextField (10);
		nameInput.setText(myName);
		JButton sendName = new JButton ("Add");
		sendName.addActionListener(new AddButtonListener(myDialog, nameInput, sendName));
		myAddButtons.add(sendName);
		
		//System.out.println(myAddButtons);
		
		nameInputP.add(name);
		nameInputP.add(nameInput);
		nameInputP.add(sendName);
		basicInput.add(nameInputP);
		
		final JPanel stateInputP = new JPanel ();
		stateInputP.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel state = new JLabel ("State: ");
		//add a listener here to send to FileWriter and to create bottom special panel
		myDialogCheck.addActionListener((theEvent) -> {
			myDialogCheck.setSelected(true);
			myCombatCheck.setSelected(false);
			
			myState = "d";
			myInput.remove(mySpecialInput);
			myInput.remove(myBottomButtons);
			mySpecialInput = chooseSpecialInput();
			myInput.add(mySpecialInput);
			makeBottomButtons();
			
			myInput.revalidate();
			myInput.repaint();
		});
		//add a listener here to send to FileWriter and to create bottom special panel
		//also link these check boxes together *done*
		myCombatCheck.addActionListener((theEvent) -> {
			myCombatCheck.setSelected(true);
			myDialogCheck.setSelected(false);
			
			myState = "c";
			myInput.remove(mySpecialInput);
			myInput.remove(myBottomButtons);
			mySpecialInput = chooseSpecialInput();
			myInput.add(mySpecialInput);
			makeBottomButtons();
			
			myInput.revalidate();
			myInput.repaint();
		});
		
		stateInputP.add(state);
		stateInputP.add(myDialogCheck);
		stateInputP.add(myCombatCheck);
		stateInputP.add(Box.createRigidArea(new Dimension (90, 10)));
		basicInput.add(stateInputP);
		
		final JPanel dialogInputP = new JPanel ();
		dialogInputP.setLayout(new FlowLayout(FlowLayout.LEFT));
		createDialogScroll (dialogInputP);
		
		basicInput.add(dialogInputP);
	}

	private void createDialogScroll(final JPanel dialogInputP) {
		JLabel dialog = new JLabel ("Dialog: ");
		JTextArea dialogInput = new JTextArea(5, 13);
		dialogInput.setText(myDialog);
		JButton sendDialog = new JButton ("Add");
		sendDialog.addActionListener(new AddButtonListener(myDialog, dialogInput, sendDialog));
		myAddButtons.add(sendDialog);
		
		//System.out.println(myAddButtons);
		
		//add a listener here to send to FileWriter
		dialogInput.setLineWrap(true);
		dialogInput.setWrapStyleWord(true);
		JScrollPane dialogPane = new JScrollPane (dialogInput);
		JScrollBar dialogScroll = new JScrollBar(JScrollBar.VERTICAL);
		
		dialogPane.add(dialogScroll);
		dialogInputP.add(dialog);
		dialogInputP.add(dialogPane);
		dialogInputP.add(sendDialog);
	}
	
	void clickButton () {
		//System.out.println("inside clickButton");
		//System.out.println(myAddButtons);
		for (final JButton button : myAddButtons) {
			button.doClick();
			//System.out.println("Button clicked");
		}
		//System.out.println("All buttons clicked");
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0.getClass().getSimpleName().equals("SituationInput") && arg1 instanceof String) {
			myInput.remove(mySpecialInput);
			mySpecialInput = chooseSpecialInput();
			myInput.add(mySpecialInput);
		} else if (arg0.getClass().getSimpleName().equals("DecisionInput")) {
			setChanged ();
			notifyObservers ("");
			if (arg1 instanceof Integer) {
				myButtonCount = (Integer) arg1;
			} else if (arg1 instanceof List<?> && myListCheck) {
				myButtonInfo = (List<String>) arg1;
				myListCheck = false;
			} else if (arg1 instanceof List<?> && !myListCheck) {
				myButtonText = (List<String>) arg1;
				myListCheck = true;
			} else if (arg1 instanceof Boolean) {
				myInput.remove(mySpecialInput);
				mySpecialInput = chooseSpecialInput ();
				myInput.add(mySpecialInput);
			}
		} else if (arg0.getClass().getSimpleName().equals("CombatInput")) {
			setChanged ();
			notifyObservers ("");
			if (arg1 instanceof List<?>) {
				List<Object> list = (List<Object>) arg1;
				if (((String) list.get(0)).equals("name")) {
					myEnemyName = (String) list.get(1);
				} else if (((String) list.get(0)).equals("armorRating")) {
					myArmorRating = (int) list.get(1);
				} else if (((String) list.get(0)).equals("health")) {
					myHealth = (int) list.get(1);
				} else if (((String) list.get(0)).equals("attackDamage")) {
					myAttackDamage = (int) list.get(1);
				} else if (((String) list.get(0)).equals("nextSituation")) {
					myNextSituation = (String) list.get(1);
				} else {
					System.err.println("SituationInput Update for CombatInput isn't working.");
				}
			}
		} else if (arg0.getClass().getSimpleName().equals("EngineGUI")) {
			if (arg1 instanceof Boolean) {
				clickButton();
			}
		}
	}
	
	class AddButtonListener implements ActionListener {
		
		private JTextComponent myField;
		
		private JButton myButton;
		
		private boolean myIsChanged;
		
		private String myString;
		
		public AddButtonListener (String theString, final JTextComponent theField, final JButton theButton) {
			myField = theField;
			myString = theString;
			
			myButton = theButton;
			myIsChanged = false;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//myString = myField.getText();
			
			if (myField.getClass().getSimpleName().equals("JTextArea")) {
				myDialog = myField.getText();
			} else {
				myName = myField.getText();
			}
			
			if (!myIsChanged && !myField.getText().equals("")) {		
				myField.setEditable(false);
				myButton.setText("Clear");
				myButton.setPreferredSize(new Dimension (65, myButton.getHeight()));
				myIsChanged = true;
			} else {
				myField.setEditable(true);
				myButton.setText("Add");
				myButton.setPreferredSize(new Dimension (60, myButton.getHeight()));
				myIsChanged = false;
			}
		}
	}
}
