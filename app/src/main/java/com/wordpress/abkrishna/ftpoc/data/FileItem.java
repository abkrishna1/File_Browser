package com.wordpress.abkrishna.ftpoc.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A File item representing an entry of directory
 * Created by balakrishna on 05-Dec-16.
 */

public class FileItem implements Parcelable {
    public final String filePath;
    public final String fileName;
    public final boolean isDirectory;

    FileItem(String filePath, String fileName, boolean isDirectory) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.isDirectory = isDirectory;
    }

    private FileItem(Parcel in) {
        filePath = in.readString();
        fileName = in.readString();
        isDirectory = in.readByte() != 0;
    }

    public static final Creator<FileItem> CREATOR = new Creator<FileItem>() {
        @Override
        public FileItem createFromParcel(Parcel in) {
            return new FileItem(in);
        }

        @Override
        public FileItem[] newArray(int size) {
            return new FileItem[size];
        }
    };

    @Override
    public String toString() {
        return fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(filePath);
        parcel.writeString(fileName);
        parcel.writeByte((byte) (isDirectory ? 1 : 0));
    }
}
