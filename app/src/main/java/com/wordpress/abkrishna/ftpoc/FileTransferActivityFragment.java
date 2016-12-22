package com.wordpress.abkrishna.ftpoc;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wordpress.abkrishna.ftpoc.data.Content;
import com.wordpress.abkrishna.ftpoc.data.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FileTransferActivityFragment extends Fragment {

    RecyclerView mRecyclerView;

    static String parentDirectoryPath;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_LIST = "arg_list";
    List<FileItem> mFileItems;
    View mLayout;
    Context mContext;
    String mDirName;
    FileItem mParentFileItem;
    static int depth = 0;

    // Container Activity must implement this interface
    public interface OnDirectoryClickedListener {
        void onDirectoryClicked(FileItem clickedFileItem);

        boolean onBackPressed();
    }

    OnDirectoryClickedListener mListener = new OnDirectoryClickedListener() {
        @Override
        public void onDirectoryClicked(FileItem clickedFileItem) {

            File rootDir = new File(clickedFileItem.filePath);
            parentDirectoryPath = rootDir.getParent();

            if (rootDir.isDirectory()) {
                ArrayList<FileItem> fileItems = getTheChildren(rootDir);

                AppCompatActivity activity = (AppCompatActivity) getActivity();
                ActionBar actionBar = null;
                if(activity.getSupportActionBar() != null)
                     actionBar = activity.getSupportActionBar();

                mDirName = rootDir.getName();

                if(mDirName.equals("0"))
                    mDirName = "Internal Storage";
                if(mDirName.equals("DCIM"))
                    mDirName = "My Photos";

                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setTitle(mDirName);
                }
                setupRecyclerView(fileItems);
                if(parentDirectoryPath != null)
                    mParentFileItem = Content.createFileItem(rootDir.getParentFile());
            }
        }

        ArrayList<FileItem> getTheChildren(File parentDirectory) {
            File[] files = parentDirectory.listFiles();
            ArrayList<FileItem> fileItems = new ArrayList<>();
            ArrayList<FileItem> dirItems = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    FileItem fileItem = Content.createFileItem(file);

                    if (file.isDirectory())
                        dirItems.add(fileItem);
                    else
                        fileItems.add(fileItem);
                }
                dirItems.addAll(fileItems);
            }
            return dirItems;
        }
        @Override
        public boolean onBackPressed() {
            depth--;
            Log.v("depth ---  ", " "+depth);
            if(depth == 0) {
                setupRecyclerView(Content.FILE_ITEMS);
                ActionBar actionBar = null;
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                if(activity.getSupportActionBar() != null)
                    actionBar = activity.getSupportActionBar();
                if(actionBar != null) {
                    actionBar.setTitle(getString(R.string.app_name));
                    actionBar.setDisplayHomeAsUpEnabled(false);
                }
                mParentFileItem = null;
                return true;
            }
            if(depth > 0) {
                onDirectoryClicked(mParentFileItem);
                return true;
            }
            return false;
        }
    };

    public FileTransferActivityFragment() {
        depth = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFileItems = getArguments().getParcelableArrayList(ARG_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayout = inflater.inflate(R.layout.fragment_file_transfer, container, false);
        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.directory_list);
        assert mRecyclerView != null;
        mContext = mLayout.getContext();
        setupRecyclerView(mFileItems);

        return mLayout;
    }

    void setupRecyclerView(List<FileItem> fileItemList) {
        mRecyclerView.setAdapter(new DirectoryRecyclerViewAdapter(fileItemList, mListener));
    }
}
