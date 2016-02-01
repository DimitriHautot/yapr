package org.yapr.renamer.strategies;

import java.io.File;

import org.yapr.filter.RawPictureFilter;

/**
 * @author Dimitri
 */
public class ExifThumbnailRawPictureRenamer extends ExifThumbnailMovieRenamer {

	/* (non-Javadoc)
	 * @see org.yapr.renamer.strategies.AbstractAssetRenamer#accept(java.io.File)
	 */
	@Override
	public boolean accept(File asset) {
		if ((asset == null) || (!asset.exists())) {
			return false;
		}

		boolean matchingRawPictureType = RawPictureFilter.acceptFile(asset);

		if (matchingRawPictureType) {
			String rawPictureName = splitFileName(asset)[0];
			File thumbnail = findThumbnail(rawPictureName);
			return thumbnail != null;
		}
		return false;
	}

}
