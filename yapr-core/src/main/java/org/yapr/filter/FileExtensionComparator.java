package org.yapr.filter;

import java.io.File;
import java.util.Comparator;

/**
 * Sort files based on their extension: first movies, then pictures.
 * File name itself is not taken into account.
 * 
 * @author dhautot
 * @deprecated
 */
public class FileExtensionComparator implements Comparator<File> {

	private static final PictureFilter pictureFilter = new PictureFilter();
	private static final MovieFilter movieFilter = new MovieFilter();
	
	public int compare(File arg0, File arg1) {
		if ((pictureFilter.accept((File)arg0)) && (pictureFilter.accept((File)arg1))) {
			return 0;
		}
		if ((movieFilter.accept((File)arg0)) && (movieFilter.accept((File)arg1))) {
			return 0;
		}
		if (movieFilter.accept((File)arg0)) {
			return -1;
		}
		if (movieFilter.accept((File)arg1)) {
			return +1;
		}
		return 0;
	}
}
