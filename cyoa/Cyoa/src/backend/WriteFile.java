package backend;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

public class WriteFile extends Observable implements Observer {

	private List<Object> myObjectList;
	
	private String myPath;
	
	private boolean myAppendToFile;
	
	private boolean myAtEnd;
	
	
	//if I ever want to make changes to an already made file just open up the file and set
	//myAppendToFile to true
	public WriteFile(){
		myObjectList = new ArrayList<Object> (); 
		myPath = "\\situations\\";
		myAppendToFile = false;
		myAtEnd = false;
	}
	
	private void makeFile ()  throws IOException {
		File file = new File(new File("").getAbsolutePath () + "\\src\\situations\\" + (String) myObjectList.get(0));

		if (!file.exists()) {
			file.createNewFile();
		}
		
		file.setExecutable(true);
		file.setWritable(true);
		file.setReadable(true);
		
		PrintWriter printLine = new PrintWriter(file);
		System.out.println(myObjectList);
		if (((String) myObjectList.get(1)).equals("d")) {
			writeDecisionFile (printLine);
		} else {
			writeCombatFile (printLine);
		}
		
		setChanged ();
		notifyObservers (file);
		//write.close();
		printLine.close();
	}
	
	private void writeCombatFile(PrintWriter printLine) {
		printLine.println((String) myObjectList.get(1));
		printLine.println(((String) myObjectList.get(2)) + "*");
		printLine.println((String) myObjectList.get(6));
		printLine.println((Integer) myObjectList.get(7));
		printLine.println((Integer) myObjectList.get(8));
		printLine.println((Integer) myObjectList.get(9));
		printLine.println((String) myObjectList.get(10));
	}

	private void writeDecisionFile (final PrintWriter printLine) {
		//situation name
		printLine.println((String) myObjectList.get(1));
		//dialog
		printLine.println(((String) myObjectList.get(2)) + "*");
		//button count
		printLine.println(((Integer) myObjectList.get(3)).intValue());
	
		//button text
		writeButtonDetails (printLine, myObjectList.get(4));
		myAtEnd = true;
		//linked situation
		writeButtonDetails(printLine, myObjectList.get(5));
		myAtEnd = false;
	}

	private void writeButtonDetails(PrintWriter printLine, Object object) {
		List<String> list = (List<String>) object;
		
		for (String s : list) {
			printLine.print(s + "/");
		}
		
		if (!myAtEnd) {
			printLine.println();
		}
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof List<?>) {
			myObjectList = (List<Object>) arg1;
			try {
				makeFile ();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
