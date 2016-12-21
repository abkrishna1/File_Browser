package com.wordpress.abkrishna.ftpoc;

import android.os.Bundle;
import android.os.Environment;
import android.app.Fragment;
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

    static String currentDirectoryPath;
    static String parentDirectoryPath;

    boolean isInitialState;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_LIST = "arg_list";



    List<FileItem> mFileItems;
    View mLayout;

    // Container Activity must implement this interface
    public interface OnDirectoryClickedListener {
        void onDirectoryClicked(String directoryPath);

        boolean onBackPressed();
    }

    OnDirectoryClickedListener mListener = new OnDirectoryClickedListener() {
        @Override
        public void onDirectoryClicked(String directoryPath) {
            isInitialState = false;
            File rootDir = new File(directoryPath);
            parentDirectoryPath = rootDir.getParent();
            currentDirectoryPath = directoryPath;
            File[] files = rootDir.listFiles();
            ArrayList<FileItem> fileItems = new ArrayList<>();
            ArrayList<FileItem> dirItems = new ArrayList<>();
            if (files != null) {
                for (File file :
                        files) {
                    FileItem fileItem = Content.createFileItem(file);

                    if (file.isDirectory())
                        dirItems.add(fileItem);
                    else
                        fileItems.add(fileItem);
                }
                dirItems.addAll(fileItems);
                fileItems.clear();
            }
            if (rootDir.isDirectory()) {
                String dirName;
                String emulated = "emulated";

                AppCompatActivity activity = (AppCompatActivity) getActivity();
                ActionBar actionBar = null;
                if(activity.getSupportActionBar() != null)
                     actionBar = activity.getSupportActionBar();

                if(Content.FILE_ITEMS.contains(currentDirectoryPath)) {
                    dirName = getString(R.string.app_name);
                    if(actionBar != null)
                        actionBar.setDisplayHomeAsUpEnabled(false);
                }
                else {
                    dirName = rootDir.getName();
                    if(actionBar != null)
                      actionBar.setDisplayHomeAsUpEnabled(true);
                }
                if(dirName.equals(emulated)) {
                    dirName = getString(R.string.app_name);
                    if(actionBar != null)
                        actionBar.setDisplayHomeAsUpEnabled(false);
                }
                if(dirName.equals("0")) {
                    if(actionBar != null)
                        actionBar.setTitle("Internal Storage");
                }
                else {
                    if(actionBar != null)
                        actionBar.setTitle(dirName);
                }
                setupRecyclerView(dirItems);
            }
        }

        @Override
        public boolean onBackPressed() {
            if(currentDirectoryPath != null && isPartOfInitialList(currentDirectoryPath)) {
                setupRecyclerView(Content.FILE_ITEMS);
                ActionBar actionBar = null;
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                if(activity.getSupportActionBar() != null)
                    actionBar = activity.getSupportActionBar();
                if(actionBar != null) {
                    actionBar.setTitle(getString(R.string.app_name));
                    actionBar.setDisplayHomeAsUpEnabled(false);
                }
                isInitialState = true;
                currentDirectoryPath = null;
                return true;
            }
            if(!isInitialState) {
                onDirectoryClicked(parentDirectoryPath);
                return true;
            }
            return false;
        }

        boolean isPartOfInitialList(String currentPath){
            boolean isIt = false;
            for (FileItem mainFile :
                    Content.FILE_ITEMS) {
                if(isIt)
                    return isIt;
                isIt = mainFile.filePath.equals(currentPath);
            }
            return isIt;
        }

    };

    public FileTransferActivityFragment() {
        isInitialState = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayout = inflater.inflate(R.layout.fragment_file_transfer, container, false);
        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.directory_list);
        assert mRecyclerView != null;

        mFileItems = getArguments().getParcelableArrayList(ARG_LIST);

        //mFileItems = (new Content()).initFTList(getActivity());
        setupRecyclerView(mFileItems);

        return mLayout;
    }

    void setupRecyclerView(List<FileItem> fileItemList) {
        mRecyclerView.setAdapter(new DirectoryRecyclerViewAdapter(fileItemList, mListener));
    }
}
