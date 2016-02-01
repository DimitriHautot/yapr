/*
 * Created on Jul 29, 2005
 */
package org.yapr.ui.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.yapr.filter.MovieFilter;

/**
 * @author dhautot
 */
public class MovieFilterUI extends FileFilter {

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		return MovieFilter.acceptFile(f);
	}

	public static boolean acceptFile(File f) {
		return MovieFilter.acceptFile(f);
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return "Movie files";
	}
}
