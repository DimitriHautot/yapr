/*
 * Created on 14-mars-2005
 */
package org.yapr.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Dim 242
 */
public class DirectoryFilter implements FileFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		return f.isDirectory();
	}

	public static boolean acceptFile(File f) {
		return f.isDirectory();
	}

}
