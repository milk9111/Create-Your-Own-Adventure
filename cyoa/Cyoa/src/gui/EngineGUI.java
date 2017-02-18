package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import backend.GameController;
import backend.Situation;
import backend.WriteFile;

public class EngineGUI extends Observable implements Observer {

	public static final Dimension MAX_SIZE = new Dimension(475, 750);
	
	public static final Dimension MIN_SIZE = new Dimension(300, 60);
	
	private final JFrame myFrame;
	
	private final JPanel myInputPanel;
	
	private JPanel mySituationPanel;
	
	private JPanel myOldFilesPanel;
	
	private boolean myIsOpen;
	
	private SituationInput mySInput;
	
	private OldFiles myOldFiles;

	private WriteFile myFileWriter;
	
	public EngineGUI() {
		myFrame = new JFrame  ();
		myInputPanel = new JPanel ();
		mySituationPanel = new JPanel ();
		mySInput = new SituationInput ();
		try {
			myOldFiles = new OldFiles ();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		myFileWriter = new WriteFile ();
		myOldFilesPanel = new JPanel ();
		myIsOpen = false;
	}
	
	public void start () {
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLayout(new BorderLayout ());
		
		setFrame ();
		setObservers ();
		
		setInputPanel ();
		setOldFilesPanel ();
		
		myInputPanel.setPreferredSize(MAX_SIZE);
		myInputPanel.setMaximumSize(MAX_SIZE);
		myInputPanel.setMinimumSize(MIN_SIZE);
		myFrame.add(myOldFilesPanel, BorderLayout.EAST);
		myFrame.add(myInputPanel);
		myFrame.pack();
	}
	
	private void setOldFilesPanel() {
		myOldFilesPanel.setMaximumSize(new Dimension (175, 750));
		myOldFilesPanel.setPreferredSize(new Dimension (175, 750));
		myOldFilesPanel.setLayout(new GridLayout(1, 2));
		myOldFilesPanel.removeAll();

		try {
			myOldFiles = new OldFiles ();
	
			final JPanel labelPanel = new JPanel ();
			final JPanel editButtonPanel = new JPanel ();
			final JPanel deleteButtonPanel = new JPanel ();
			
			labelPanel.setLayout(new BoxLayout (labelPanel, BoxLayout.Y_AXIS));
			editButtonPanel.setLayout(new BoxLayout (editButtonPanel, BoxLayout.Y_AXIS));
			deleteButtonPanel.setLayout(new BoxLayout (deleteButtonPanel, BoxLayout.Y_AXIS));
			
			for (File file : myOldFiles.getFileList()) {	
				final JLabel label = new JLabel (file.getName());
				
				final JButton editButton = new JButton ("Edit");
				editButton.addActionListener((theEvent) -> {
					myIsOpen = true;
					mySituationPanel.removeAll();
					
					GameController gc = new GameController ();
					Situation s = gc.findSituationFile(file.getName());
					
					if (s.getState().equals("d")) {
						mySInput = new SituationInput(file.getName(), s.getState(), s.getDialog(), s.getButtonCount(), s.getButtonInfo(), s.getButtonKeys());
					} else {
						mySInput = new SituationInput(file.getName(), s.getState(), s.getDialog(), s.getEnemy().getName(), s.getEnemy().getArmorRating(), 
								s.getEnemy().getHealthRemaining(), s.getEnemy().getAttackDamage(), s.getButtonInfo().get(0));
					}
					
					mySInput.addObserver(this);
					mySInput.addObserver(myOldFiles);
					mySInput.addObserver(myFileWriter);
					
					
					setChanged();
					notifyObservers(myIsOpen);
					mySituationPanel = mySInput.makePanel();
					setChanged();
					notifyObservers(myIsOpen);
					/*
					 * This was my attempt at recursively searching through every component to 
					 * find the buttons and click them. It works, but only for the name field button.
					 * Not only that, but programmatically (button.doClick()) clicking the button makes it disappear.
					 */
					//findButtons (mySituationPanel.getComponents(), 0);
					
					myInputPanel.add(mySituationPanel);
					
					mySituationPanel.revalidate();
					mySituationPanel.repaint();
				});
				final JButton deleteButton = new JButton ("X");
				
				deleteButton.setBackground(Color.RED);
				deleteButton.setSize(new Dimension (editButton.getHeight(), editButton.getWidth() / 4));
				
				deleteButton.addActionListener((theEvent) -> {
					myOldFiles.removeFile(file);
					labelPanel.remove(label);
					editButtonPanel.remove(editButton);
					deleteButtonPanel.remove(deleteButton);
					
					myOldFilesPanel.revalidate();
					myOldFilesPanel.repaint();
					//System.out.println("deleted " + file.getName());
					//System.out.println(myOldFiles.getFileList());
				});
				
				labelPanel.add(label);
				labelPanel.add(Box.createRigidArea(new Dimension(1, 10)));
				
				editButtonPanel.add(editButton);
				
				deleteButtonPanel.add(deleteButton);
				
			}
			myOldFilesPanel.add(labelPanel);
			myOldFilesPanel.add(editButtonPanel);
			myOldFilesPanel.add(deleteButtonPanel);
						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void findButtons (Component[] theArray, int thePos) {
		if (thePos >= theArray.length) {
			return;
		} else if (theArray[thePos] instanceof JButton) {
			//System.out.println(theArray[thePos].getClass().getSimpleName());
			((JButton) theArray[thePos]).doClick();
			mySituationPanel.revalidate();
			mySituationPanel.repaint();
			thePos++;
			findButtons (theArray, thePos);
		} else if (theArray[thePos] instanceof JPanel) {
			findButtons (((JPanel) theArray[thePos]).getComponents(), 0);
		} else {
			thePos++;
			findButtons (theArray, thePos);
		}
	}

	private void setInputPanel () {
		JButton addSituation = new JButton ("Add a Situation");
		
		myInputPanel.setLayout(new BorderLayout ());
		myInputPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		myInputPanel.add(addSituation, BorderLayout.NORTH);
		
		addSituation.addActionListener((theEvent) -> {
			mySituationPanel.removeAll();
			
			myFileWriter = new WriteFile ();
			mySInput = new SituationInput ();
			mySInput.addObserver(this);
			mySInput.addObserver(myOldFiles);
			mySInput.addObserver(myFileWriter);
	
			addSituation.setText("Add another Situation");
			mySituationPanel = mySInput.makePanel();
			myInputPanel.add(mySituationPanel);
			mySituationPanel.revalidate();
			mySituationPanel.repaint();
		});
		
		
	}
	
	private void setObservers () {
		addObserver (mySInput);
		myFileWriter.addObserver(myOldFiles);
		
	}
	
	private void setFrame () {
		myFrame.setPreferredSize(MAX_SIZE);
		myFrame.setMaximumSize (MAX_SIZE);
		myFrame.setMinimumSize (MIN_SIZE);
		myFrame.setLocation(0, 0);
		myFrame.setResizable (false);
		myFrame.setVisible(true);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0.getClass().getSimpleName().equals("SituationInput")) {
			myFrame.pack();
			if (arg1 instanceof List<?>) {
				setOldFilesPanel();
				myOldFilesPanel.revalidate();
				myOldFilesPanel.repaint();
			}
		}
	}
}
