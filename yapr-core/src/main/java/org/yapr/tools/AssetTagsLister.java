package org.yapr.tools;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDescriptor;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;

/**
 * Small utility class aimed at listing all available tags for a given asset.
 *
 * @author Dimitri Hautot
 */
public class AssetTagsLister {

    public static void main(String[] args) throws ImageProcessingException, IOException {
        File file1 = new File("/tmp/IMG_2337.mov");
        Metadata metadata = ImageMetadataReader.readMetadata(file1);

        for (Directory directory : metadata.getDirectories()) {
            System.out.format("%s\n", directory.getClass());

            for (Tag tag : directory.getTags()) {
                System.out.format("[%s] - %s = %s\n",
                        directory.getName(), tag.getTagName(), tag.getDescription());
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s\n", error);
                }
            }
        }
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                System.out.println(tag);
//            }
//        }
    }
}
