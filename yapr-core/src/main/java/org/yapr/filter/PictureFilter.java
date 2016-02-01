package org.yapr.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * @author dhautot
 */
public class PictureFilter implements FileFilter {
	private static final PictureFilter instance = new PictureFilter();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File pathname) {
		String absolutePathUpperCase = pathname.getAbsolutePath().toUpperCase();
		return (((pathname.isFile() &&
				(absolutePathUpperCase.endsWith(".JPG") ||
				absolutePathUpperCase.endsWith(".JPEG")))));
	}

	static public boolean acceptFile(File pathname) {
		return instance.accept(pathname);
	}
}
