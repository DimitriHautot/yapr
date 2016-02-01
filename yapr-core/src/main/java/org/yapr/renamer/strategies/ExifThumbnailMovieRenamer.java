package org.yapr.renamer.strategies;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

import org.yapr.domain.Configuration;
import org.yapr.filter.MovieFilter;

/**
 * @author Dimitri
 */
public class ExifThumbnailMovieRenamer extends ExifPictureRenamer {

	private final String[] supportedThumbnailExtensions;
	
	public ExifThumbnailMovieRenamer() {
		this.supportedThumbnailExtensions = new String[] {".THM", ".JPG", ".JPEG"};
	}

	protected String[] getSupportedThumbnailExtensions() {
		return this.supportedThumbnailExtensions;
	}

	/* (non-Javadoc)
	 * @see org.yapr.renamer.strategies.AbstractAssetRenamer#accept(java.io.File)
	 */
	@Override
	public boolean accept(File asset) {
		if ((asset == null) || (!asset.exists())) {
			return false;
		}

		boolean matchingMovieType = MovieFilter.acceptFile(asset);

		if (matchingMovieType) {
			String movieName = splitFileName(asset)[0];
			File thumbnail = findThumbnail(movieName);
			return thumbnail != null;
		}
		return false;
	}

	/**
	 * Rename the movie and its thumbnail.
	 * 
	 * @see org.yapr.renamer.strategies.AbstractAssetRenamer#rename(java.io.File, org.yapr.domain.Configuration)
	 */
	@Override
	public File rename(File asset, Configuration configuration) {
		if ((asset == null) || (!asset.exists())) {
			logger.warn("Invalid resource: {}", asset);
			return null;
		}

		logger.info("Processing asset {}", asset);

		File thumbnail = null, newThumbnail = null, newAsset = null, renamedAsset = null;
		String assetName = null, folder = null, assetExtension = null;

		// Get the absolute file name and the file extension
		String[] assetSplit = splitFileName(asset);
		assetName = assetSplit[0];
		assetExtension = assetSplit[1];

		// Get the absolute file directory (without name.extension)
		if (configuration.getTargetFolder() != null) {
			folder = configuration.getTargetFolder();
		} else {
			folder = asset.getParent();
		}
		logger.debug("Resource folder name: {}", folder);

		thumbnail = findThumbnail(assetName);
		if (null == thumbnail) {
			return null;
		}
		logger.info("thumbnail: {}", thumbnail);
		String[] thumbnailSplit = splitFileName(thumbnail);

		try {
			Date date = getDateToUse(thumbnail);
			
			if (date != null) {
				date.setTime(date.getTime() + (configuration.getHoursOffset()*1000*60*60));
	
				newThumbnail = newFile(folder, date, thumbnailSplit[1].toLowerCase(), configuration);
				boolean renamed = thumbnail.renameTo(newThumbnail);
				if (renamed) {
					newAsset = newFile(folder, date, assetExtension.toLowerCase(), configuration);
					renamed = asset.renameTo(newAsset);
				}
				if (renamed) {
					renamedAsset = newAsset;
				}
			}
		} catch (Exception e) {
			logger.error("An error occured; message is: {}", e.getMessage());
		}
		return renamedAsset;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected File findThumbnail(String name) {
		File thumbnail;
		String[] extensions = getSupportedThumbnailExtensions();
		for (int i = 0; i < extensions.length; i++) {
			thumbnail = new File(name + extensions[i]);
			if (thumbnail.exists()) {
				logger.debug("findThumbnail - found {}", thumbnail.getName());
				return thumbnail;
			}
			logger.debug("findThumbnail - not found {}", thumbnail.getName());
		}

		logger.warn("findThumbnail - no matching thumbnail ({}{}) file found!", name, Arrays.toString(extensions));
		return null;
	}

}
