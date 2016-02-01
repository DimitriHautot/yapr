/*
 * Created on Mar 11, 2005
 */
package org.yapr.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * This class allows to filter files having a movie file extension.
 *
 * @author dhautot
 */
public class MovieFilter implements FileFilter {
	private static MovieFilter instance = new MovieFilter();

	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File pathname) {
		String absolutePathUpperCase = pathname.getAbsolutePath().toUpperCase();
		return (((pathname.isFile() && (absolutePathUpperCase.endsWith(".MPG")
				|| absolutePathUpperCase.endsWith(".MPEG")
				|| absolutePathUpperCase.endsWith(".MP4")
				|| absolutePathUpperCase.endsWith(".MOV")
				|| absolutePathUpperCase.endsWith(".3GP")
				|| absolutePathUpperCase.endsWith(".AVI")))));
	}

	static public boolean acceptFile(File pathname) {
		return instance.accept(pathname);
	}
}
