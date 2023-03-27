package org.yapr.renamer.strategies;

import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mp4.Mp4Directory;
import org.yapr.filter.MovieFilter;
import org.yapr.filter.PictureFilter;

import java.io.File;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Dimitri
 */
public class ExifMovieRenamer extends AbstractAssetRenamer {

	/* (non-Javadoc)
	 * @see org.yapr.renamer.strategies.AssetRenamerStrategy#accepts(java.io.File)
	 */
	@Override
	public boolean accept(File asset) {
		if ((asset == null) || (!asset.exists())) {
			return false;
		}

		return MovieFilter.acceptFile(asset);
	}

	public Date getDateToUse(File file) {
		try {
            Metadata metadata = Mp4MetadataReader.readMetadata(file); // FIXME Find a better/safer way to read video metadata
			Mp4Directory directory = metadata.getFirstDirectoryOfType(Mp4Directory.class);
            if (directory.containsTag(Mp4Directory.TAG_CREATION_TIME)) {
				return directory.getDate(Mp4Directory.TAG_CREATION_TIME, TimeZone.getTimeZone("Europe/Brussels")); // FIXME
			}
		} catch (Exception e) {
			logger.error("An error occurred; message is: {}", e.getMessage());
		}
		return null;
	}

}
