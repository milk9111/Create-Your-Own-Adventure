package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import backend.Enemy;
import backend.Fight;
import backend.GameController;
import backend.Player;
import backend.Situation;
import backend.TextWrap;

public class CyoaGUI extends Observable implements Observer, PropertyChangeListener {
	
	private static final Dimension MAX_SIZE = new Dimension(300, 500);
	
	private static final Dimension MIN_SIZE = new Dimension(300, 300);
	
	private static final Dimension BUTTON_PADDING = new Dimension(170, 30);

	private final JFrame myFrame;
	
	private final JPanel myPanel;
	
	private final JPanel myButtonPanel;
	
	private final JTextArea myDialog;
	
	private final JButton myAttack;
	
	private final GameController myGameController;
	
	private Situation mySituation;
	
	private boolean myCanAttack;
	
	private StringBuilder myPreviousMoves;
	
	private Player myPlayer;
	
	private JTextArea myPlayerHealthDisplay;
	
	private JTextArea myEnemyHealthDisplay;
	
	private int myPlayerHealth;
	
	private int myEnemyHealth;

	
	public CyoaGUI () {
		myFrame = new JFrame ("CYOA");
		myGameController = new GameController ();
		mySituation = myGameController.findSituationFile("Start");
		myPanel = new JPanel ();
		myButtonPanel = new JPanel ();
		myDialog = new JTextArea ();
		myAttack = new JButton ("Attack!");
		myCanAttack = true;
		myPreviousMoves = new StringBuilder ();
		myPlayer = new Player ("Connor", 5, 10, 10);
		myPlayerHealthDisplay = new JTextArea ();
		myEnemyHealthDisplay = new JTextArea ();
		myPlayerHealth = myPlayer.getHealthRemaining();
	}
	
	public void start () {
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLayout(new BorderLayout ());
		
		setFrame ();
		setObservers ();
		
		setPanel ();
		
		myPanel.setMaximumSize(MAX_SIZE);
		myPanel.setMinimumSize(MIN_SIZE);
		myFrame.add(myPanel);
		myFrame.pack();
	}
	
	private void setPanel () {
		myPanel.setLayout(new BorderLayout ());
		
		myDialog.setText(new TextWrap(mySituation.getDialog(), 50).toString());
		myDialog.setLineWrap(true);
		myDialog.setEditable(false);
		myPanel.add(myDialog, BorderLayout.CENTER);
		
		BoxLayout buttonLayout = new BoxLayout(myButtonPanel, BoxLayout.Y_AXIS);
		myButtonPanel.setLayout(buttonLayout);
		for (Component c : myButtonPanel.getComponents()) {
			myButtonPanel.remove(c);
		}
		
		if (mySituation.getState().equals("d")) {
			createDecisionState ();
		}
		else {
			createCombatState ();
		}
		
		myPanel.add(myButtonPanel, BorderLayout.SOUTH);
	}
	
	private void createDecisionState () {
		myButtonPanel.add(Box.createRigidArea(BUTTON_PADDING));
		for (int i = 0; i < mySituation.getButtonCount(); i++)
		{
			//we need this index to be final in order for the buttons to work individually.
			final int index = i;
			JButton button = new JButton(mySituation.getButtonKeys().get(index));
			button.setMinimumSize(BUTTON_PADDING);
			button.setMaximumSize(BUTTON_PADDING);
			button.addActionListener(new ActionListener () {

				@Override
				public void actionPerformed(ActionEvent e) {
					//System.out.println(mySituation.getButtonInfo().get(index));
					
					setChanged();
					notifyObservers(mySituation.getButtonInfo().get(index));
				}
				
			});
			myButtonPanel.add(button);
			myButtonPanel.add(Box.createRigidArea(BUTTON_PADDING));
		}
		myButtonPanel.setBackground(Color.BLACK);
	}
	
	private void createCombatState () {
		JPanel attackPanel = new JPanel ();
		attackPanel.add(Box.createRigidArea(BUTTON_PADDING));		

		Fight fight = new Fight(myPlayer, mySituation.getEnemy());
		fight.addObserver(this);
		addObserver(fight);
		
		myAttack.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (myCanAttack) {
					setChanged ();
					notifyObservers (myPlayer.attack());
				}
			}
			
		});
		
		attackPanel.add(myAttack);
		attackPanel.add(Box.createRigidArea(BUTTON_PADDING));
		attackPanel.setBackground(Color.BLACK);
		
		myButtonPanel.add(attackPanel);
		
		JPanel healthDisplay = new JPanel ();
		myEnemyHealth = mySituation.getEnemy().getHealthRemaining();
		displayHealth (myPlayerHealth, myEnemyHealth);
		
		healthDisplay.add(myPlayerHealthDisplay);
		healthDisplay.add(myEnemyHealthDisplay);
		
		myButtonPanel.add(healthDisplay);
	}
	
	private void displayHealth (final int theNewPlayerHealth, final int theNewEnemyHealth) {
		myPlayerHealthDisplay.setText(myPlayer.getName() + "'s health: " + theNewPlayerHealth + " / " + myPlayerHealth);
		myEnemyHealthDisplay.setText(mySituation.getEnemy().getName() + "'s health: " + theNewEnemyHealth + " / " + myEnemyHealth);
		
		if (theNewEnemyHealth <= 0) {
			for (ActionListener action : myAttack.getActionListeners()) {
				
				myAttack.removeActionListener(action);
			}
			
			myAttack.setText("Continue");
			myAttack.addActionListener((theEvent) -> {
				setChanged ();
				notifyObservers (mySituation.getButtonInfo().get(0));
				//System.out.println("notified observers of " + mySituation.getButtonInfo().get(0));
			});
		}
		else if (theNewPlayerHealth <= 0) {
			myDialog.setText("GAME OVER...");
			myAttack.setEnabled(false);
		}
		
		myFrame.revalidate();
		myFrame.repaint();
	}
	
	private void setObservers () {
		addObserver(myGameController);
		
		myGameController.addObserver(this);
		setChanged();
		notifyObservers(new Boolean(true));
	}
	
	private void setFrame () {
		myFrame.setMaximumSize (MAX_SIZE);
		myFrame.setMinimumSize (MIN_SIZE);
		myFrame.setLocation(0, 0);
		myFrame.setResizable (false);
		myFrame.setVisible(true);
	}
	
	private void scanPreviousMoves () {
		String string = myPreviousMoves.toString();
		Scanner scan = new Scanner (string);
		
		int currentLines = 1;
		while (scan.hasNextLine()) {
			scan.nextLine();
			currentLines++;
		}
		scan.close();
		
		if (currentLines > 2) {
			myPreviousMoves = new StringBuilder("");
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0.getClass().getSimpleName().equals("Fight") && arg1 instanceof Boolean) {
			myCanAttack = (Boolean) arg1;
		}
		else if (arg0.getClass().getSimpleName().equals("Fight") && arg1 instanceof String) {
			scanPreviousMoves ();
			
			myPreviousMoves.append((String) arg1);
			myPreviousMoves.append("\n");
			
			int playerHealth = myPlayer.getHealthRemaining ();
			int enemyHealth = mySituation.getEnemy().getHealthRemaining ();
			
			playerHealth = checkHealth (playerHealth);
			enemyHealth = checkHealth (enemyHealth);
			
			displayHealth(playerHealth, enemyHealth);
			myDialog.setText(myPreviousMoves.toString());
		}
		
		if (arg1.getClass().getSimpleName().equals("Situation")) {
			mySituation = (Situation) arg1;
			setPanel();
		}
	}

	private int checkHealth(int theHealth) {
		if (theHealth < 0) {
			theHealth = 0;
		}
		return theHealth;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName().equals("situation")) {
			mySituation = (Situation) arg0.getNewValue();
			setPanel();
		}
	}

}
