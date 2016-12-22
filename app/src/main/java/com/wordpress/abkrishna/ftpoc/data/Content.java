package com.wordpress.abkrishna.ftpoc.data;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by balakrishna on 05-Dec-16.
 */

public class Content {

    /**
     * An array of File items.
     */
    public static final ArrayList<FileItem> FILE_ITEMS = new ArrayList<>();

    private static List<String> getSdCardPaths(final Context context) {
        //boolean includePrimaryExternalStorage = false;
        final File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
        if (externalCacheDirs == null || externalCacheDirs.length == 0)
            return null;
        if (externalCacheDirs.length == 1) {
            if (externalCacheDirs[0] == null)
                return null;
            final String storageState = EnvironmentCompat.getStorageState(externalCacheDirs[0]);
            if (!Environment.MEDIA_MOUNTED.equals(storageState))
                return null;
            if (/*!includePrimaryExternalStorage &&*/ Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Environment.isExternalStorageEmulated())
                return null;
        }
        final List<String> result = new ArrayList<>();
        /*if (includePrimaryExternalStorage || externalCacheDirs.length == 1)
            result.add(getRootOfInnerSdCardFolder(externalCacheDirs[0]));*/
        for (int i = 1; i < externalCacheDirs.length; ++i) {
            final File file = externalCacheDirs[i];
            if (file == null)
                continue;
            final String storageState = EnvironmentCompat.getStorageState(file);
            if (Environment.MEDIA_MOUNTED.equals(storageState))
                result.add(getRootOfInnerSdCardFolder(externalCacheDirs[i]));
        }
        if (result.isEmpty())
            return null;
        return result;
    }

    /**
     * Given any file/folder inside an sd card, this will return the path of the sd card
     */
    private static String getRootOfInnerSdCardFolder(File file) {
        if (file == null)
            return null;
        final long totalSpace = file.getTotalSpace();
        while (true) {
            final File parentFile = file.getParentFile();
            Log.e("Size", parentFile.getTotalSpace() + "");
            if (parentFile == null || parentFile.getTotalSpace() != totalSpace)
                return file.getAbsolutePath();
            file = parentFile;
        }
    }

    public ArrayList<FileItem> initFTList(Context context) {
        if(FILE_ITEMS.size() == 0) {
            String path = Environment.getExternalStorageDirectory().toString();
            File rootDir = new File(path);
            addFileItem(createFileITem(rootDir, "Internal Storage"));
            List<String> listOfPaths = getSdCardPaths(context);
            if (listOfPaths != null) {
                for (String st : listOfPaths) {
                    addFileItem(createFileItem(new File(st)));
                }
            }
            File publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            addFileItem(createFileITem(publicDirectory, "My Music"));
            publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            addFileItem(createFileITem(publicDirectory, "My Pictures"));
            publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            addFileItem(createFileITem(publicDirectory, "My Photos"));
            publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            addFileItem(createFileITem(publicDirectory, "My Downloads"));
        }
        return FILE_ITEMS;
    }

    private static void addFileItem(FileItem fileItem) {
        FILE_ITEMS.add(fileItem);
    }

    public static FileItem createFileItem(File file) {
        String filePath = file.getPath();
        String fileName = file.getName();
        boolean isDirectory = file.isDirectory();
        return new FileItem(filePath, fileName, isDirectory);
    }

    public static FileItem createFileITem(File file, String fileName){
        String filePath = file.getPath();
        boolean isDirectory = file.isDirectory();
        return new FileItem(filePath, fileName, isDirectory);
    }
}
