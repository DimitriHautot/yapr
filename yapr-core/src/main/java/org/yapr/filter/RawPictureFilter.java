package org.yapr.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * @author dhautot
 */
public class RawPictureFilter implements FileFilter {
	private static final RawPictureFilter instance = new RawPictureFilter();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File pathname) {
		String absolutePathUpperCase = pathname.getAbsolutePath().toUpperCase();
		return (((pathname.isFile() && (
				absolutePathUpperCase.endsWith(".3FR")
				|| absolutePathUpperCase.endsWith(".ARW")
				|| absolutePathUpperCase.endsWith(".CRW")
				|| absolutePathUpperCase.endsWith(".CR2")
				|| absolutePathUpperCase.endsWith(".DNG")
				|| absolutePathUpperCase.endsWith(".KDC")
				|| absolutePathUpperCase.endsWith(".MRW")
				|| absolutePathUpperCase.endsWith(".NEF")
				|| absolutePathUpperCase.endsWith(".NRW")
				|| absolutePathUpperCase.endsWith(".ORF")
				|| absolutePathUpperCase.endsWith(".PTX")
				|| absolutePathUpperCase.endsWith(".PEF")
				|| absolutePathUpperCase.endsWith(".RAF")
				|| absolutePathUpperCase.endsWith(".X3F")
				|| absolutePathUpperCase.endsWith(".RW2")))));
	}

	static public boolean acceptFile(File pathname) {
		return instance.accept(pathname);
	}
}
