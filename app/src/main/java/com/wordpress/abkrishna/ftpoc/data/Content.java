package com.wordpress.abkrishna.ftpoc.data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by balakrishna on 05-Dec-16.
 */

public class Content {

    /**
     * An array of File items.
     */
    public static final List<FileItem> FILE_ITEMS = new ArrayList<>();

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 2;

    public static List<String> getSdCardPaths(final Context context) {
        boolean includePrimaryExternalStorage = false;
        final File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
        if (externalCacheDirs == null || externalCacheDirs.length == 0)
            return null;
        if (externalCacheDirs.length == 1) {
            if (externalCacheDirs[0] == null)
                return null;
            final String storageState = EnvironmentCompat.getStorageState(externalCacheDirs[0]);
            if (!Environment.MEDIA_MOUNTED.equals(storageState))
                return null;
            if (!includePrimaryExternalStorage && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Environment.isExternalStorageEmulated())
                return null;
        }
        final List<String> result = new ArrayList<>();
        if (includePrimaryExternalStorage || externalCacheDirs.length == 1)
            result.add(getRootOfInnerSdCardFolder(externalCacheDirs[0]));
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
    //use this code
    //give includePrimaryExternalStorage = false; all the time
    public List<FileItem> initFTList(Context context) {
        if(FILE_ITEMS.size() == 0) {
            //List<FileItem> fileItems = new ArrayList<>();
            String path = Environment.getExternalStorageDirectory().toString();
            File rootDir = new File(path);
            addFileItem(createFileItem(rootDir));
            //FILE_ITEMS.add(createFileItem(rootDir));
            List<String> listOfPaths = getSdCardPaths(context);
            //path = System.getenv("SECONDARY_STORAGE");
            if (listOfPaths != null) {
                //boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                for (String st :
                        listOfPaths) {
                    addFileItem(createFileItem(new File(st)));
                }
            }
            addFileItem(new FileItem("My Photos", "My Photos", true));
            addFileItem(new FileItem("My Videos", "My Videos", true));
            addFileItem(new FileItem("My Music", "My Music", true));
        }
        return FILE_ITEMS;
    }
    /*static {
        String path = Environment.getExternalStorageDirectory().toString();
        Log.v("External ----  ", path);
        File rootDir = new File(path);
        //FILE_ITEMS.add(createFileItem(rootDir));
        //File[] files = rootDir.listFiles();
        List<File> filesList = new ArrayList<>();
        filesList.add(rootDir);
        //filesList.addAll(Arrays.asList(files));
        *//*if(files != null && files.length > 0)
        {
            for (File file : files) {
                filesList.add(file);
            }
        }*//*

        //files = rootDir.
        String secondaryPath = System.getenv("SECONDARY_STORAGE");
        if(secondaryPath != null) {
            //secondaryPath = System.getenv("SECONDARY_STORAGE");
            //files[files.length] = new File(secondaryPath);
            filesList.add(new File(secondaryPath));
        }
        for (File file : filesList) {
            addFileItem(createFileItem(file));
        }
    }*/

    private static void addFileItem(FileItem fileItem) {
        FILE_ITEMS.add(fileItem);
    }

    public static FileItem createFileItem(File file) {
        String filePath = file.getPath();
        String fileName = file.getName();
        boolean isDirectory = file.isDirectory();
        return new FileItem(filePath, fileName, isDirectory);
    }
}
