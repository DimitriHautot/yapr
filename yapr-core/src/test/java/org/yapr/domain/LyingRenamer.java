package org.yapr.domain;

import java.io.File;

/**
 * @author Dimitri
 */
public class LyingRenamer implements FileRenamer {

	/**
	 * Accept all {@link File}'s.
	 * @see org.yapr.domain.FileRenamer#accept(java.io.File)
	 */
	@Override
	public boolean accept(File file) {
		return true;
	}

	/**
	 * Pretend to rename any {@link File}, even if it is never done.
	 * @see org.yapr.domain.FileRenamer#rename(java.io.File, org.yapr.domain.Configuration)
	 */
	@Override
	public File rename(File file, Configuration configuration) {
		return file;
	}

}
