package org.yapr.renamer.strategies;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.yapr.filter.PictureFilter;
import sun.util.resources.cldr.be.TimeZoneNames_be;

import java.io.File;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Dimitri
 */
public class ExifPictureRenamer extends AbstractAssetRenamer {

	/* (non-Javadoc)
	 * @see org.yapr.renamer.strategies.AssetRenamerStrategy#accepts(java.io.File)
	 */
	@Override
	public boolean accept(File asset) {
		if ((asset == null) || (!asset.exists())) {
			return false;
		}

		return PictureFilter.acceptFile(asset);
	}

	public Date getDateToUse(File file) {
		try {
            Metadata metadata = JpegMetadataReader.readMetadata(file);
            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (exifDirectory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
				return exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getTimeZone("Europe/Brussels")); // FIXME
			}
		} catch (Exception e) {
			logger.error("An error occured; message is: {}", e.getMessage());
		}
		return null;
	}

}
