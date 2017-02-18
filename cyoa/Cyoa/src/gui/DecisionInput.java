package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DecisionInput extends SituationInput implements Observer {

	private int  myButtonCount;
	
	private List<String> myButtonText;
	
	private List<String> myButtonInfo;
	
	private JPanel myDecisionInput;
	
	private JPanel myButtonTextInnerP;
	
	private JPanel myButtonInfoInnerP;
	
	private boolean myIsOpen;

	private ArrayList<JButton> myAddButtons;
	
	public DecisionInput () {
		this (1, new ArrayList<String> (), new ArrayList<String> ());
	}
	
	public DecisionInput (final int theButtonCount, final List<String> theButtonText, final List<String> theButtonInfo) {
		myButtonCount = theButtonCount;
		myButtonText = theButtonText;
		myButtonInfo = theButtonInfo;
		myAddButtons = new ArrayList<JButton> ();
		myDecisionInput = new JPanel ();
		myButtonTextInnerP = new JPanel ();
		myButtonInfoInnerP = new JPanel ();
		myIsOpen = false;
	}
	
	public JPanel makePanel() {
		final BoxLayout layout = new BoxLayout (myDecisionInput, BoxLayout.Y_AXIS); 
		myDecisionInput.setLayout(layout);
		
		final JPanel buttonCountP = new JPanel ();
		buttonCountP.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JSlider buttonCount = new JSlider(1, 4, myButtonCount);
		buttonCount.setPaintLabels(true);
		buttonCount.setMajorTickSpacing(1);
		buttonCount.setMinorTickSpacing(1);
		buttonCount.setSnapToTicks(true);
		//add action listener to this slider that will change the number of button texts
		//and linked situations inputs *done*, must also send info to SituationInput to send
		//to FileWriter *done*.
		final JLabel buttonCountL = new JLabel ("No. of Buttons:     ");
		final JButton send = new JButton ("Add");
		send.addActionListener((theEvent) -> {
			myButtonText = new ArrayList<String> ();
			myButtonInfo = new ArrayList<String> ();
			myButtonCount = buttonCount.getValue();
			calculateInputCount(myButtonTextInnerP, myButtonText);
			calculateInputCount(myButtonInfoInnerP, myButtonInfo);
			setChanged ();
			notifyObservers (new Integer (buttonCount.getValue()));
		});
		myAddButtons.add(send);
		
		buttonCountP.add(buttonCountL);
		buttonCountP.add(buttonCount);
		buttonCountP.add(send);
		myDecisionInput.add(buttonCountP);
		
		final JPanel buttonTextP = new JPanel ();
		buttonTextP.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel buttonTextL = new JLabel ("Button Text:          ");
		final BoxLayout layout2 = new BoxLayout (myButtonTextInnerP, BoxLayout.Y_AXIS);
		myButtonTextInnerP.setLayout(layout2);
		buttonTextP.add(buttonTextL);
		calculateInputCount (myButtonTextInnerP, myButtonInfo);
		buttonTextP.add(myButtonTextInnerP);
		myDecisionInput.add(buttonTextP);
		
		final JPanel buttonInfoP = new JPanel ();
		buttonInfoP.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel buttonInfoL = new JLabel ("Linked Situations:");
		final BoxLayout layout3 = new BoxLayout (myButtonInfoInnerP, BoxLayout.Y_AXIS);
		myButtonInfoInnerP.setLayout(layout3);
		buttonInfoP.add(buttonInfoL);
		calculateInputCount (myButtonInfoInnerP, myButtonText);
		buttonInfoP.add(myButtonInfoInnerP);
		myDecisionInput.add(buttonInfoP);
		
		return myDecisionInput;
	}

	private void calculateInputCount(final JPanel thePanel, final List<String> theList) {
		thePanel.removeAll();
		
		int pos = 0;
		thePanel.add(Box.createRigidArea(new Dimension(1, 10)));
		for (int i = 0; i < myButtonCount; i++) {
			JPanel panel = new JPanel ();
			JTextField input = new JTextField (10);
			JButton add = new JButton ("Add");
			add.setPreferredSize(new Dimension (60, 20));
			add.addActionListener(new AddButtonListener (theList, input, add, pos));
			
			if (!theList.isEmpty()) {
				input.setText(theList.get(i));
			}
			myAddButtons.add(add);

			panel.add(input);
			panel.add(add);
			
			thePanel.add(panel);
			pos++;
		}
		
		thePanel.validate();
		thePanel.repaint();
	}
	
	private void sendLists () {
		setChanged ();
		notifyObservers (myButtonText);
		setChanged ();
		notifyObservers (myButtonInfo);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o.getClass().getSimpleName().equals("SituationInput")) {
			clickButton();
		}

	}
	
	class AddButtonListener implements ActionListener {

		private List<String> myList;
		
		private JTextField myField;
		
		private JButton myButton;
		
		private boolean myIsChanged;
		
		private int myPos;
		
		public AddButtonListener (final List<String> theList, final JTextField theField, final JButton theButton, final int thePos) {
			myList = theList;
			myField = theField;
			myButton = theButton;
			myIsChanged = false;
			myPos = thePos;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!myIsChanged && !myField.getText().equals("")) {
				if (myList.contains(myField.getText())) {
					myList.remove(myField.getText());
				}
				
				if (!myList.isEmpty() && myList.size() > myPos) {
					myList.set(myPos, myField.getText());
				} else {
					if (myList.size() > myPos) {
						myList.add(myPos, myField.getText());
					} else {
						myList.add(myField.getText());
					}
				}
				myField.setEditable(false);
				myButton.setText("Clear");
				myButton.setPreferredSize(new Dimension (65, myButton.getHeight()));
				myIsChanged = true;
				//System.out.println("A button was set to clear");
				//System.out.println();
				sendLists ();
			} else {
				myList.remove(myField.getText());
				myField.setEditable(true);
				myButton.setText("Add");
				myButton.setPreferredSize(new Dimension (60, myButton.getHeight()));
				myIsChanged = false;
				//System.out.println("A button was set to add");
				//System.out.println();
				sendLists ();
			}
		}
		
	}
}
