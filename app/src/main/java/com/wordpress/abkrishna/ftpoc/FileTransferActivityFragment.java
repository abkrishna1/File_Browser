package com.wordpress.abkrishna.ftpoc;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
public class FileTransferActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView mRecyclerView;

    static String currentDirectoryPath;
    static String parentDirectoryPath;
    static String parentDirectoryName;

    boolean isInitialState;


    private static final String TAG = FileTransferActivityFragment.class.getSimpleName();

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_LIST = "arg_list";

    public static final String ARG_LOADER_ID = "loader_id";

    // Identifies a particular Loader being used in this component
    public static final int AUDIO_LOADER_ID = 2;
    public static final int VIDEO_LOADER_ID = 3;
    public static final int IMAGE_LOADER_ID = 1;

    //Identifies a particular Uri for the loader
    private final Uri AUDIO_CONTENT_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    private final Uri VIDEO_CONTENT_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private final Uri IMAGE_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    //Identifies the required projection for the content
    private final String[] AUDIO_PROJECTION =
            {/*MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media._ID,*/
                    MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM/*,
                    "MAX(" + MediaStore.Audio.Media.DATE_MODIFIED + ") as maxDateTaken"*/};
    private final String[] VIDEO_PROJECTION =
            {MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE, MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.BUCKET_ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                    "MAX(" +MediaStore.Video.Media.DATE_MODIFIED + ") as maxDateTaken"};
    private final String[] IMAGES_PROJECTION =
            {MediaStore.Images.Media.DATA, MediaStore.Images.Media.TITLE, MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    "MAX(" +MediaStore.Images.Media.DATE_MODIFIED + ") as maxDateTaken"};



    List<FileItem> mFileItems;
    View mLayout;
    private final int LOADER_ID_INVALID = 999;
    private int mLoaderId = 0;
    private Bundle mArgs;
    Cursor mCursor;
    MyListCursorAdapter mListCursorAdapter;
    Context mContext;
    String mDirName;
    FileItem mParentFileItem;

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        mLoaderId = loaderID;
        mArgs = args;
        CursorLoader loader;
        Activity activity = getActivity();
        Uri uri = null;
        String[] mProjection = null;
        String mSelection = null;
        String mSortOder = null;
        /*
         * Takes action based on the ID of the Loader that's being created
         */
        switch (loaderID) {
            case AUDIO_LOADER_ID:
                uri = AUDIO_CONTENT_URI;
                mProjection = AUDIO_PROJECTION;/*
                mSelection = "1)GROUP BY (" + MediaStore.Audio.Albums.ALBUM_ID + "," + MediaStore.Audio.Albums.ALBUM_ART;
                mSortOder = "maxDateTaken desc";*/
                break;
            case VIDEO_LOADER_ID:
                uri = VIDEO_CONTENT_URI;
                mProjection = VIDEO_PROJECTION;
                mSelection = "1)GROUP BY (" + MediaStore.Video.Media.BUCKET_ID;
                mSortOder = "maxDateTaken desc";
                break;
            case IMAGE_LOADER_ID:
                uri = IMAGE_CONTENT_URI;
                mProjection = IMAGES_PROJECTION;
                mSelection = "1)GROUP BY (" + MediaStore.Images.Media.BUCKET_ID;
                mSortOder = "maxDateTaken desc";
                break;
        }
        // Returns a new CursorLoader
        loader = new CursorLoader(
                activity,        // Parent activity context
                uri,             // Table to query
                mProjection,     // Projection to return
                mSelection,      // No selection clause
                null,            // No selection arguments
                mSortOder        // Default sort order
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        /*
         * Moves the query results into the adapter, causing the
         * ListView fronting this adapter to re-display
         */

        mListCursorAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * Clears out the adapter's reference to the Cursor.
         * This prevents memory leaks.
         */
        mListCursorAdapter.changeCursor(null);
    }

    // Container Activity must implement this interface
    public interface OnDirectoryClickedListener {
        void onDirectoryClicked(FileItem clickedFileItem);

        boolean onBackPressed();
    }

    OnDirectoryClickedListener mListener = new OnDirectoryClickedListener() {
        @Override
        public void onDirectoryClicked(FileItem clickedFileItem) {
            isInitialState = false;
            File rootDir = new File(clickedFileItem.filePath);
            parentDirectoryPath = rootDir.getParent();
            parentDirectoryName = rootDir.getParentFile().getName();
            currentDirectoryPath = rootDir.getPath();
            boolean isInternal = rootDir.getName().equals("0");
            /*if(isPartOfInitialList(directoryPath) && !isInternal) {
                switch (directoryPath) {
                    case "My Photos" :
                        mLoaderId = IMAGE_LOADER_ID;
                        break;
                    case "My Videos" :
                        mLoaderId = VIDEO_LOADER_ID;
                        break;
                    case "My Music" :
                        mLoaderId = AUDIO_LOADER_ID;
                        break;
                }
                *//*
                 * Initializes the CursorLoader. The AUDIO_LOADER_ID value is eventually passed
                 * to onCreateLoader().
                 *//*
                getLoaderManager().initLoader(mLoaderId, null, FileTransferActivityFragment.this);

                mListCursorAdapter = new MyListCursorAdapter(mContext, mCursor, mLoaderId);
                mRecyclerView.setAdapter(mListCursorAdapter);
                return;
            }*/

            if (rootDir.isDirectory()) {
                ArrayList<FileItem> fileItems = getTheChildren(rootDir);
                String emulated = "emulated";

                AppCompatActivity activity = (AppCompatActivity) getActivity();
                ActionBar actionBar = null;
                if(activity.getSupportActionBar() != null)
                     actionBar = activity.getSupportActionBar();

                if(isPartOfInitialList(parentDirectoryName)) {
                    mDirName = getString(R.string.app_name);
                    if(actionBar != null)
                        actionBar.setDisplayHomeAsUpEnabled(false);
                }
                else {
                    mDirName = rootDir.getName();
                    if(actionBar != null)
                      actionBar.setDisplayHomeAsUpEnabled(true);
                }
                /*if(mDirName.equals(emulated)) {
                    mDirName = getString(R.string.app_name);
                    if(actionBar != null)
                        actionBar.setDisplayHomeAsUpEnabled(false);
                }*/
                if(mDirName.equals("0"))
                    mDirName = "Internal Storage";
                if (actionBar != null)
                    actionBar.setTitle(mDirName);
                setupRecyclerView(fileItems);
                mParentFileItem = clickedFileItem;
            }
        }

        ArrayList<FileItem> getTheChildren(File parentDirectory) {
            /*if(mParentFileItem != null && isPartOfInitialList(mParentFileItem.fileName))
                return Content.FILE_ITEMS;*/
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
            if(currentDirectoryPath != null && isPartOfInitialList(mParentFileItem.fileName)) {
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
                mParentFileItem = null;
                mParentFileItem = null;
                return true;
            }
            if(!isInitialState) {
                onDirectoryClicked(mParentFileItem);
                return true;
            }
            return false;
        }

        boolean isPartOfInitialList(String currentPath){
            boolean isIt = false;
            for (FileItem mainFile : Content.FILE_ITEMS) {
                if(isIt)
                    return isIt;
                isIt = mainFile.fileName.equals(currentPath);
            }
            return isIt;
        }

    };

    public FileTransferActivityFragment() {
        isInitialState = true;
        mLoaderId = LOADER_ID_INVALID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mLoaderId = getArguments().getInt(ARG_LOADER_ID);
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
