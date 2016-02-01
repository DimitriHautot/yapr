package org.yapr.filter;

import java.io.File;
import java.io.FilenameFilter;

public class AllSupportedFilenamesFilter implements FilenameFilter {

	@Override
	public boolean accept(File folder, String filename) {
		File candidate = new File(folder, filename);

		return DirectoryFilter.acceptFile(candidate)
				|| MovieFilter.acceptFile(candidate)
				|| PictureFilter.acceptFile(candidate);
	}

}
