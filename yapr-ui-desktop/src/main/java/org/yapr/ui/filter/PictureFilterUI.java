package org.yapr.ui.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.yapr.filter.PictureFilter;

/**
 * @author dhautot
 */
public class PictureFilterUI extends FileFilter {

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		return PictureFilter.acceptFile(f);
	}

	public static boolean acceptFile(File f) {
		return PictureFilter.acceptFile(f);
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return "Picture files";
	}
}
