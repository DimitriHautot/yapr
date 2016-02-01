package org.yapr.renamer.strategies;

import java.io.File;
import java.util.Date;

public class LastModificationFileRenamer extends AbstractAssetRenamer {

	@Override
	public boolean accept(File asset) {
		return (asset != null && asset.lastModified() > 0L);
	}

	@Override
	public Date getDateToUse(File asset) {
		return new Date(asset.lastModified());
	}

}
