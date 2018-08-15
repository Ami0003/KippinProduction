package com.kippin.utils.io;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gaganpreet.singh on 1/22/2016.
 */
public class FileWriter {
    /**
     * @param bitmap
     * @param folderName without extension
     *                   Is used to create png files in card.
     */
    public static boolean writeBitmap(Bitmap bitmap, String folderName, String fileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(createFileIfNotExists(createFolderIfNotExists(folderName).getAbsolutePath() + File.separator + fileName+".png"));
        return bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
    }


    public static File createFolderIfNotExists(String folder) {
        File folder_ = new File(folder);
        if (!folder_.exists()) {
            folder_.mkdirs();
        }
        return folder_;
    }


    public static File createFileIfNotExists(String file) throws IOException {
        File file_ = new File(file);
        if (!file_.exists()) {
            file_.createNewFile();
        }
        return file_;
    }

}
