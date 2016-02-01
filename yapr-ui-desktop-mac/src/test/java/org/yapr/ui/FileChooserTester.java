package org.yapr.ui;

import java.awt.FileDialog;

import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

public class FileChooserTester {

	public static void main(String[] args) throws UnsupportedLookAndFeelException {
	    JFrame frame = new JFrame();
//	    System.setProperty("apple.awt.fileDialogForDirectories", "false");
	    FileDialog d = new FileDialog(frame, "Le titre", FileDialog.LOAD);
	    d.setVisible(true);
	    d.setVisible(false);
		System.out.println("directory = " + d.getDirectory());
		System.out.println("file = " + d.getFile());
	}
}
