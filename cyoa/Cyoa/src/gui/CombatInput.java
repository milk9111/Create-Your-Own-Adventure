package gui;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CombatInput extends SituationInput implements Observer {

	final JPanel myInput;
	
	String myName;
	
	int myArmorRating;
	
	int myHealth;
	
	int myAttackDamage;
	
	String myNextSituation;
	
	public CombatInput () {
		this ("", 0, 0, 0, "");
	}
	
	public CombatInput (final String theName, final int theArmorRating, final int theHealth, final int theAttackDamage,
			final String theNextSituation) {
		myInput = new JPanel ();
		myName = theName;
		myArmorRating = theArmorRating;
		myHealth = theHealth;
		myAttackDamage = theAttackDamage;
		myNextSituation = theNextSituation;
	}
	

	public JPanel makePanel () {
		myInput.setLayout(new FlowLayout (FlowLayout.LEFT));
		
		final JLabel enemyName = new JLabel ("Enemy Name:");
		final JTextField enemyNameField = new JTextField (10);
		final JButton enemyNameButton = new JButton ("Add");
		enemyNameField.setText(myName);
		enemyNameButton.addActionListener((theEvent) -> {
			if (!enemyNameButton.getText().equals("Clear")) {
				myName = enemyNameField.getText();
				List<Object> list = new ArrayList<Object>();
				list.add("name");
				list.add(myName);
				setChanged ();
				notifyObservers (list);
			}
			changeButtonAndField(enemyNameButton, enemyNameField);
			System.out.println(myName);
		});
		
		myInput.add(enemyName);
		myInput.add(enemyNameField);
		myInput.add(enemyNameButton);
		
		final JLabel enemyArmorRating = new JLabel ("Armor Rating:");
		final JTextField enemyArmorRatingField = new JTextField (10);
		final JButton enemyArmorRatingButton = new JButton ("Add");
		enemyArmorRatingField.setText("" + myArmorRating);
		enemyArmorRatingButton.addActionListener((theEvent) -> {
			if (!enemyArmorRatingButton.getText().equals("Clear")) {
				myArmorRating = checkForInt(enemyArmorRatingField.getText());
				List<Object> list = new ArrayList<Object>();
				list.add("armorRating");
				list.add(myArmorRating);
				setChanged ();
				notifyObservers (list);
			}
			changeButtonAndField(enemyArmorRatingButton, enemyArmorRatingField);
			System.out.println(myArmorRating);
		});
		
		myInput.add(enemyArmorRating);
		myInput.add(enemyArmorRatingField);
		myInput.add(enemyArmorRatingButton);
		
		final JLabel enemyHealth = new JLabel ("Health:");
		final JTextField enemyHealthField = new JTextField (10);
		final JButton enemyHealthButton = new JButton ("Add");
		enemyHealthField.setText("" + myHealth);
		enemyHealthButton.addActionListener((theEvent) -> {
			if (!enemyHealthButton.getText().equals("Clear")) {
				myHealth = checkForInt(enemyHealthField.getText());
				List<Object> list = new ArrayList<Object>();
				list.add("health");
				list.add(myHealth);
				setChanged ();
				notifyObservers (list);
			}
			changeButtonAndField(enemyHealthButton, enemyHealthField);
			System.out.println(myHealth);
		});
		
		myInput.add(enemyHealth);
		myInput.add(enemyHealthField);
		myInput.add(enemyHealthButton);
		
		final JLabel enemyAttackDamage = new JLabel ("Attack Damage:");
		final JTextField enemyAttackDamageField = new JTextField (10);
		final JButton enemyAttackDamageButton = new JButton ("Add");
		enemyAttackDamageField.setText("" + myAttackDamage);
		enemyAttackDamageButton.addActionListener((theEvent) -> {
			if (!enemyAttackDamageButton.getText().equals("Clear")) {
				myAttackDamage = checkForInt(enemyAttackDamageField.getText());
				List<Object> list = new ArrayList<Object>();
				list.add("attackDamage");
				list.add(myAttackDamage);
				setChanged ();
				notifyObservers (list);
			}
			
			changeButtonAndField(enemyAttackDamageButton, enemyAttackDamageField);
			System.out.println(myAttackDamage);
		});
		
		myInput.add(enemyAttackDamage);
		myInput.add(enemyAttackDamageField);
		myInput.add(enemyAttackDamageButton);
		
		final JLabel nextSituation = new JLabel ("Next Situation:");
		final JTextField nextSituationField = new JTextField (10);
		final JButton nextSituationButton = new JButton ("Add");
		nextSituationField.setText(myNextSituation);
		nextSituationButton.addActionListener((theEvent) -> {
			if (!nextSituationButton.getText().equals("Clear")) {
				myNextSituation = nextSituationField.getText();
				List<Object> list = new ArrayList<Object>();
				list.add("nextSituation");
				list.add(myNextSituation);
				setChanged ();
				notifyObservers (list);
			}
			changeButtonAndField(nextSituationButton, nextSituationField);
			System.out.println(myNextSituation);
		});
		
		myInput.add(nextSituation);
		myInput.add(nextSituationField);
		myInput.add(nextSituationButton);
		
		return myInput;
	}
	
	private int checkForInt (final String theString) {
		int check = 0;
		Scanner scan = new Scanner (theString);
		
		if (scan.hasNextInt()) {
			check = scan.nextInt();
		}
		else {
			System.out.println("not an int");
		}

		scan.close();
		return check;
	}
	
	private void changeButtonAndField (final JButton theButton, final JTextField theField) {
		if (theField.isEditable()) {
			theButton.setText("Clear");
			theField.setEditable(!theField.isEditable());
		} else {
			theButton.setText("Add");
			theField.setEditable(!theField.isEditable());
		}
	}

	@Override
	public void update (Observable o, Object arg) {
		// TODO Auto-generated method stub

	}
}
