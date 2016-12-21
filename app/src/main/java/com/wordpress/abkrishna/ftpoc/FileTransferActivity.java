package com.wordpress.abkrishna.ftpoc;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wordpress.abkrishna.ftpoc.data.Content;
import com.wordpress.abkrishna.ftpoc.data.FileItem;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTransferActivity extends AppCompatActivity {
    private static final String TAG = FileTransferActivityFragment.class.getSimpleName();
    List<FileItem> mFileItems;

    private FileTransferActivityFragment.OnDirectoryClickedListener mDirectoryClickedListener;

    /**
     * Root of the layout of this Activity.
     */
    private View mLayout;
    Bundle mSavedInstanceState;
    Content content = new Content();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_transfer);
        mLayout = findViewById(R.id.activity_main_layout);

        Log.i(TAG, "Checking storage permission.");
        // BEGIN_INCLUDE(Storage_permission)
        // Check if the Storage permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Storage permission has not been granted.

            PermissionsUtility.requestExternalStoragePermission(this, mLayout);

        } else {

            // Storage permissions is already available, load the media.
            Log.i(TAG,
                    "Storage permission has already been granted.");
            mFileItems = content.initFTList(this);

        }
        // END_INCLUDE(storage_permission)

        if (savedInstanceState == null && mFileItems != null) {
            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            FileTransferActivityFragment fragment = new FileTransferActivityFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_transfer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mDirectoryClickedListener = DirectoryRecyclerViewAdapter.mDirectoryClickedListener;
        if (mDirectoryClickedListener != null && !mDirectoryClickedListener.onBackPressed()) {

            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtility.REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Snackbar.make(mLayout, R.string.permision_available_externalStorage,
                            Snackbar.LENGTH_SHORT)
                            .show();

                    //mFileItems = Content.FILE_ITEMS;
                    mFileItems = content.initFTList(this);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.i(TAG, "Contacts permissions were NOT granted.");
                    Snackbar.make(mLayout, R.string.permissions_not_granted,
                            Snackbar.LENGTH_SHORT)
                            .show();
                    mFileItems = new ArrayList<>();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
