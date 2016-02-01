package org.yapr.ui.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.yapr.filter.DirectoryFilter;
import org.yapr.filter.MovieFilter;
import org.yapr.filter.PictureFilter;
import org.yapr.filter.RawPictureFilter;

/**
 * @author dhautot (created on Jul 29, 2005)
 */
public class AllSupportedFilesFilterUI extends FileFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		return DirectoryFilter.acceptFile(f)
				|| MovieFilter.acceptFile(f)
				|| PictureFilter.acceptFile(f)
				|| RawPictureFilter.acceptFile(f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return "All supported files";
	}

}
