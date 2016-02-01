package org.yapr.renamer.strategies;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yapr.domain.Configuration;
import org.yapr.domain.FileRenamer;

/**
 * @author Dimitri
 *
 */
public abstract class AbstractAssetRenamer implements FileRenamer {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/* (non-Javadoc)
	 * @see org.yapr.renamer.strategies.FileRenamer#accept(java.io.File)
	 */
	public abstract boolean accept(File asset);

	public abstract Date getDateToUse(File file);

	/* (non-Javadoc)
	 * @see org.yapr.renamer.strategies.AssetRenamerStrategy#rename(java.io.File, org.yapr.domain.Configuration)
	 */
	@Override
	public File rename(File asset, final Configuration configuration) {
		File renamedAsset = null;
		if ((asset == null) || (!asset.exists())) {
			logger.warn("Invalid resource: {}", asset);
			return renamedAsset;
		}

		logger.info("Processing file: {}", asset);

		Date date = getDateToUse(asset);
		if (date == null) {
			return renamedAsset;
		}

		File newAsset = null;
		String name = null, extension = null, folder = null;

		// Get the absolute file name (without extension)
		String[] split = splitFileName(asset);
		name = split[0];
		extension = split[1];
		logger.debug("Resource file name: {}", name);

		// Get the absolute file directory (without name.extension)
		if (configuration.getTargetFolder() != null) {
			folder = configuration.getTargetFolder();
		} else {
			folder = asset.getParent();
		}
		logger.debug("Resource folder name: {}", folder);

		try {
			date.setTime(date.getTime() + (configuration.getHoursOffset()*1000*60*60));
			newAsset = newFile(folder, date, extension.toLowerCase(), configuration);
			boolean renamed = asset.renameTo(newAsset);
			if (renamed) {
				renamedAsset = newAsset;
			}
		} catch (Exception e) {
			logger.error("An error occured; message is: {}", e.getMessage());
		}
		return renamedAsset;
	}

	protected File newFile(String folder, Date date, String extension, Configuration configuration) {
		File newFile;
		if (folder == null) {
			folder = configuration.getTargetFolder();
		}
		if (! folder.endsWith(File.separator)) {
			folder += File.separator;
		}
		String newName = configuration.getDateFormat().format(date);
		newFile = new File(folder + newName + "." + extension);
		if (! newFile.exists()) {
			return newFile;
		}
		
		for (char c = 'a'; c <= 'z'; c++) {
			newFile = new File(folder + newName + c + "." + extension);
			if (! newFile.exists()) {
				return newFile;
			}
		}
		return null;
	}

	// Get the absolute file name (index=0) and extension (index=1)
	protected String[] splitFileName(File movie) throws StringIndexOutOfBoundsException {
		int index = movie.getAbsolutePath().lastIndexOf(".");
		String extension = movie.getAbsolutePath().substring(index + ".".length());
		String name = movie.getAbsolutePath().substring(0, index);
		logger.debug("Resource file name: {}", name);
		return new String [] {name, extension};
	}

}
