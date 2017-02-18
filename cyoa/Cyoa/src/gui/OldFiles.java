package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class OldFiles extends Observable implements Observer {

	private List<File> myList;
	private File myFolder;
	
	public OldFiles() throws FileNotFoundException {
		myList = new ArrayList<File> ();
		myFolder = new File(new File ("").getAbsolutePath() + "\\src\\situations");
		checkFiles ();	
	}

	private void checkFiles() {
		myList.clear();

		for (final File file : myFolder.listFiles()) {
			myList.add(file);
		}
	}
	
	public List<File> getFileList () {
		return myList;
	}
	
	public void removeFile (final File theFile) {
		if (myList.contains(theFile)) {
			myList.remove(theFile);
			theFile.delete();
		} else {
			System.err.println("File not found");
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		checkFiles ();
	}

}
